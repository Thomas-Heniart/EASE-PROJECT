if (window.top === window && (window.location.href.indexOf("https://accounts.google.com") == 0 || window.location.href.indexOf("https://myaccount.google.com") == 0 || window.location.href.indexOf("https://passwords.google.com") == 0)) {

    extension.runtime.onMessage("checkChromeCo", function (msg, sendResponse) {
        if ($("#Passwd, #password input[type='password']").length != 0) {
            sendResponse(false);
        } else {
            sendResponse(true);
        }
    });

    extension.runtime.onMessage("typePasswordChrome", function (msg, sendResponse) {
        $("#Passwd, #password input[type='password']").val(msg.pass);
        $("#signIn, #passwordNext").click();
    });

    extension.runtime.onMessage("connectToChrome", function (msg, sendResponse) {
        $("#Email, #identifierId").val(msg.login);
        $("#next, #identifierNext").click();

        function waitForPass() {
            if ($(".form-error, #identifierId[aria-invalid='true']").length > 0) {
                sendResponse(false);
            } else if ($("#Passwd, #password input[type='password']").length != 0) {
                $("#Passwd, #password input[type='password']").val(msg.pass);
                $("#signIn, #passwordNext").click();
                sendResponse(true);
            } else {
                setTimeout(waitForPass, 100);
            }
        }

        waitForPass();
    });

    extension.runtime.onMessage("scrapChrome", function (msg, sendResponse) {
        function waitload(callback) {
            if ($(".z-Of.cba.z-op").length != 0) {
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

        waitload(function () {
            var loadingString = "";
            var waitingTime = 5;
            var nbOfPass = $(".z-Of.cba.z-op, .N5v1Jd.cba.HSMSQd").length;
            var results = [];

            function getPass(index) {
                var element = $(".z-Of.cba.z-op, .N5v1Jd.cba.HSMSQd").get(index);
                var field = $(element).find(".bba.EW.a-Fa");

                function waitPass() {
                    if ($(field).val() != "••••••••" && $(field).val() != loadingString) {
                        if (waitingTime == 5) { //Get the string "Chargement en cours" whatever language
                            loadingString = $(field).val();
                            waitingTime = 50;
                            setTimeout(waitPass, waitingTime);
                        } else {
                            if ($(element).find(".CW").text() != "") {
                                var tmp = {
                                    website: $(element).find(".Zaa").text(),
                                    login: $(element).find(".CW").text(),
                                    pass: $(field).val()
                                };
                                //Really not good but slack will work
                                var slackTeam = $(".Tw6cHd .qga", element);
                                if (slackTeam.length > 0 && slackTeam.val().endsWith(".slack.com/")) {
                                    tmp.team = tmpslackTeam.val().split("/")[2].split(".")[0];
                                }
                                results.push(tmp);
                            }
                            if (index + 1 < nbOfPass)
                                getPass(index + 1);
                            else {
                                $(element).find(".Vaa.AW.aga").click();
                                sendResponse(results);
                            }
                        }
                    } else {
                        setTimeout(waitPass, waitingTime);
                    }
                }

                if ($(element).find(".Vaa.AW.BW").length != 0) {
                    $(element).find(".Vaa.AW.BW").click();
                    waitPass();
                } else {
                    if (index + 1 < nbOfPass)
                        getPass(index + 1);
                    else {
                        $(element).find(".Vaa.AW.aga").click();
                        sendResponse(results);
                    }
                }

            }

            getPass(0);
        });
    });

}
