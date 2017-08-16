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
        console.log($("div#password input[name='password']"));
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
        if ($("div[jscontroller='VXdfxd']").length != 0) {
            console.log("start scrap");
            callback();
        } else if ($(".gga").length != 0) {
            console.log("no pass");
            sendResponse([]);
        } else {
            console.log("wait pass");
            setTimeout(function () {
                waitload(callback);
            }, 100);
        }
    }

    /* it works */
    /* @TODO Handle loadingString */
    /* function clickAndGetPassword(index) {
        var eyeElem = $(passwordEyes[index]);
        eyeElem.click();
        var eyeElemParent = eyeElem.parent().parent();
        var field = $("input", eyeElemParent);
        var entireRow = eyeElemParent.parent().parent().parent().parent();
        var website = $("div[role='rowheader'] div", entireRow).text();
        var login = $("div[role='gridcell']:nth-child(2) div", entireRow).text();
        setTimeout(function () {
            console.log("Password: " + field.val());
            console.log("login: " + login);
            console.log("website: " + website);
        }, 200);
        if (index < passwordEyes.length) {
            setTimeout(function () {
                clickAndGetPassword(index + 1);
            }, 1000);
        }
    } */

    waitload(function () {
        var loadingString = "";
        var waitingTime = 5;
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

            //var field = $(element).find(".bba.EW.a-Fa");
            function waitPass() {
                console.log(entireRow);
                console.log(loadingString + " and field: " + field.val() + " and login: " + login + " and website: " + website + " and index: " + index);
                if (field.val() != "••••••••" && field.val() != loadingString) {
                    if (waitingTime == 5) {//Get the string "Chargement en cours" whatever language
                        loadingString = field.val();
                        waitingTime = 50;
                        setTimeout(waitPass, waitingTime);
                    } else {
                        if (login != "")
                            results.push({website: website, login: login, pass: field.val()});
                        console.log(results);
                        console.log(index);
                        if (index + 1 < nbOfPass)
                            getPass(index + 1);
                        else
                            sendResponse(results);
                    }
                } else {
                    if (field.val() == "••••••••")
                        eyeElem.click();
                    setTimeout(waitPass, waitingTime);
                }
            }
            console.log(eyeElem);
            eyeElem.click();
            waitPass();
            /* } else {
                if (index + 1 < nbOfPass)
                    getPass(index + 1);
                else {
                    $(element).find(".Vaa.AW.aga").click();
                    sendResponse(results);
                }
            } */

        }

        getPass(0);
    });
});
