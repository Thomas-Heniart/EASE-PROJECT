extension.runtime.onMessage.addListener("ScrapLinkedin", function (msg, senderTab, sendResponse) {
    startScrapLinkedin(msg.login, msg.password, function (success, response) {
        linkedinScrap.user = {};
        if (success && response.length == 0) {
            success = false;
            response = "You did not connect to any website with this Linkedin account. Try it with another account."
        }
        extension.tabs.focus(senderTab, function () {});
        sendResponse({
            "success": success,
            "msg": response
        });
    });
});

var linkedinScrap = {
    "user": {},
    "website": {
        "home": "https://www.linkedin.com/",
        "checkAlreadyLogged": {
            "todo": [
                {
                    "action": "check",
                    "type": "hasElement",
                    "search": "input[type='password']"
                }
            ]
        },
        "connect": {
            "todo": [
                {
                    "action": "goto",
                    "url": "https://www.linkedin.com/uas/login"
                },
                {
                    "action": "check",
                    "type": "hasElement",
                    "search": "#session_key-login]"
                },
                {
                    "action": "fill",
                    "what": "login",
                    "search": "#session_key-login"
                },
                {
                    "action": "fill",
                    "what": "password",
                    "search": "#session_password-login"
                },
                {
                    "action": "click",
                    "search": "#btn-primary"
                }
            ]
        },
        "logout": {
            "todo": [
                {
                    "action": "aclick",
                    "search": "a[href*='https://www.linkedin.com/uas/logout?session_full_logout=']"
                }
            ]
        }
    }
}

function startScrapLinkedin(login, password, finalCallback) {
    linkedinScrap.user.login = login;
    linkedinScrap.user.password = password;
    extension.windows.getCurrent(function (window) {
        extension.tabs.create(window, "https://www.linkedin.com", false, function (tab) {
            function onclose() {
                finalCallback(false, "It seems that you closed the tab. Please try again.");
                extension.tabs.onClosed.removeListener(onclose);
                extension.tabs.onReloaded.removeListener(checkIfConnected);
                extension.tabs.onReloaded.removeListener(checkIfConnected2);
                extension.tabs.onReloaded.removeListener(scrapLnkdn);
            }

            function checkIfConnected(tab) {
                extension.tabs.onReloaded.removeListener(checkIfConnected);
                generateSteps("checkAlreadyLogged", "scrapLinkedin", linkedinScrap, function (stepsCheckLogged) {
                    executeSteps(tab, stepsCheckLogged, function (tab, response) {
                        var actionSteps = [];
                        generateSteps("logout", "scrapLinkedin", linkedinScrap, function (addedSteps) {
                            actionSteps = actionSteps.concat(addedSteps);
                            doConnect(tab, actionSteps);
                        });
                    }, function (tab, response) {
                        doConnect(tab, [])
                    });
                });
            }

            function doConnect(tab, actionSteps) {
                generateSteps("connect", "scrapLinkedin", linkedinScrap, function (addedSteps) {
                    actionSteps = actionSteps.concat(addedSteps);
                    executeSteps(tab, actionSteps, function (tab, response) {
                        setTimeout(function () {
                            if (!alreadyChecked) {
                                checkIfConnected2(tab);
                            }
                        }, 10000);
                        extension.tabs.onReloaded.addListener(tab, checkIfConnected2);
                    }, function (tab, response) {
                        finalCallback(false, "Error. Please try again.");
                    });
                });
            }

            var alreadyChecked = false;

            function checkIfConnected2(tab) {
                alreadyChecked = true;
                extension.tabs.onReloaded.removeListener(checkIfConnected2);
                generateSteps("checkAlreadyLogged", "scrapLinkedin", linkedinScrap, function (stepsCheckLogged) {
                    executeSteps(tab, stepsCheckLogged, function (tab, response) {
                        extension.tabs.update(tab, "https://www.linkedin.com/psettings/third-party-applications", function (tab) {
                            extension.tabs.onReloaded.addListener(tab, scrapLnkdn);
                        });
                    }, function (tab, response) {
                        finalCallback(false, "Wrong login or password. Please try again.");
                    });
                });
            }

            function scrapLnkdn(tab) {
                extension.tabs.onReloaded.removeListener(scrapFb);
                extension.tabs.sendMessage(tab, "scrapLnkdn", {}, function (response) {
                    finalCallback(true, response);
                    extension.tabs.onClosed.removeListener(onclose);
                    setTimeout(function () {
                        extension.tabs.close(tab);
                    }, 500);
                });
            }

            extension.tabs.onClosed.addListener(tab, onclose);
            extension.tabs.onReloaded.addListener(tab, checkIfConnected);
        });
    });
}
