extension.runtime.onMessage("checkChromeCo", function (msg, sendResponse) {
    if ($("div#password input[name='password'], #identifierId").length != 0) {
        sendResponse(false);
    } else {
        sendResponse(true);
    }
});

extension.runtime.onMessage("typePasswordChrome", function (msg, sendResponse) {
    $("div#password input[name='password']").val(msg.pass);
    $("#passwordNext").click();
});

extension.runtime.onMessage("connectToChrome", function (msg, sendResponse) {
    $("#identifierId").val(msg.login);
    $("#identifierNext").click();

    function waitForPass() {
        if ($("#identifierId[aria-invalid='true']").length > 0) {
            sendResponse(false);
        } else if ($("div#password input[name='password']").length != 0) {
            $("div#password input[name='password']").val(msg.pass);
            $("#passwordNext").click();
            sendResponse(true);
        } else {
            setTimeout(waitForPass, 100);
        }
    }

    waitForPass();
});

extension.runtime.onMessage("scrapChrome", function (msg, sendResponse) {
    function waitload(callback) {
        $(document).ready(function () {
            if ($("div[jscontroller='VXdfxd']").length !== 0) {
                console.log("start scrap");
                callback();
            } else if ($(".gga").length !== 0) {
                console.log("no pass");
                sendResponse([]);
            } else {
                console.log("wait pass");
                setTimeout(function () {
                    waitload(callback);
                }, 100);
            }
        });
    }

    waitload(function () {
        var loadingString = "";
        var waitingTime = 20;
        var passwordEyes = $(".mUbCce.fKz7Od.cXmCRb[role='button'][jsaction*='click:cOuCgd']");
        var nbOfPass = passwordEyes.length;
        var results = [];

        function getPass(index) {
            var eyeElem = $(passwordEyes[index]);
            var eyeElemParent = eyeElem.parent().parent();
            var field = $("input", eyeElemParent);
            var entireRow = eyeElemParent.parent().parent().parent().parent();
            var website = $("div[role='rowheader'] div", entireRow).text();
            var login = $("div[role='gridcell']:nth-child(2) div", entireRow).text();
            var total_time = 0;
            var interval = setInterval(function () {
                if (field.val() !== "••••••••" && field.val() !== loadingString) {
                    if (waitingTime === 20) {
                        loadingString = field.val();
                        waitingTime = 50;
                        total_time += waitingTime;
                    } else {
                        if (login !== "")
                            results.push({website: website, login: login, pass: field.val()});
                        clearInterval(interval);
                        if (index + 1 < nbOfPass)
                            getPass(index + 1);
                        else
                            sendResponse(results);
                    }
                } else {
                    if (field.val() === "••••••••")
                        eyeElem[0].click();
                    total_time += waitingTime;
                    if (total_time > 1000) {
                        clearInterval(interval);
                        sendResponse(results);
                    }
                }
            }, waitingTime);
        }

        getPass(0);
    });
});
