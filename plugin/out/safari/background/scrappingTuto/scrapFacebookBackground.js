extension.runtime.onMessage.addListener("ScrapFacebook", function (msg, senderTab, sendResponse) {
    startScrapFacebook(msg.login, msg.password, function (success, response) {
        facebookScrap.user = {};
        if (success && response.length == 0) {
            success = false;
            response = "You did not connect to any website with this Facebook account. Try it with another account."
        }
        extension.tabs.focus(senderTab, function () {});
        sendResponse({
            "success": success,
            "msg": response
        });
    });
});

var facebookScrap = {
    "user": {

    },
    "website": {
        "home": "https://www.facebook.com",
        "checkAlreadyLogged": {
            "todo": [
                {
                    "action": "check",
                    "type": "absentElement",
                    "search": "input[type='password']"
                }
            ]
        },
        "connect": {
            "todo": [
                {
                    "action": "fill",
                    "what": "login",
                    "search": "#email",
                    "grave": true
                },
                {
                    "action": "fill",
                    "what": "password",
                    "search": "#pass",
                    "grave": true
                },
                {
                    "action": "click",
                    "search": "#loginbutton",
                    "grave": true
                }
			]
        },
        "logout": {
            "todo": [
                {
                    "action": "erasecookies",
                    "name": "c_user"
                },
                {
                    "action": "goto",
                    "url": "http://www.facebook.com"
                }
			]
        }
    }
}

function startScrapFacebook(login, password, finalCallback) {
    facebookScrap.user.login = login;
    facebookScrap.user.password = password;
    extension.windows.getCurrent(function (window) {
        extension.tabs.create(window, "https://www.facebook.com", false, function (tab) {


            function onclose() {
                finalCallback(false, "It seems that you closed the tab. Please try again.");
                extension.tabs.onClosed.removeListener(onclose);
                extension.tabs.onReloaded.removeListener(checkIfConnected);
                extension.tabs.onReloaded.removeListener(checkIfConnected2);
                extension.tabs.onReloaded.removeListener(scrapFb);
            }

            function checkIfConnected(tab) {
                extension.tabs.onReloaded.removeListener(checkIfConnected);
                generateSteps("checkAlreadyLogged", "scrapFacebook", facebookScrap, function (stepsCheckLogged) {
                    executeSteps(tab, stepsCheckLogged, function (tab, response) {
                        var actionSteps = [];
                        generateSteps("logout", "scrapFacebook", facebookScrap, function (addedSteps) {
                            actionSteps = actionSteps.concat(addedSteps);
                            doConnect(tab, actionSteps);
                        });
                    }, function (tab, response) {
                        doConnect(tab, [])
                    });
                });
            }

            function doConnect(tab, actionSteps) {
                generateSteps("connect", "scrapFacebook", facebookScrap, function (addedSteps) {
                    actionSteps = actionSteps.concat(addedSteps);
                    executeSteps(tab, actionSteps, function (tab, response) {
                        extension.tabs.onReloaded.addListener(tab, checkIfConnected2);
                    }, function (tab, response) {
                        finalCallback(false, "Error. Please try again.");
                        extension.tabs.onClosed.removeListener(onclose);
                        setTimeout(function () {
                            //extension.tabs.close(tab);
                        }, 500);
                    });
                });
            }

            function checkIfConnected2(tab) {
                extension.tabs.onReloaded.removeListener(checkIfConnected2);
                generateSteps("checkAlreadyLogged", "scrapFacebook", facebookScrap, function (stepsCheckLogged) {
                    executeSteps(tab, stepsCheckLogged, function (tab, response) {
                        extension.tabs.update(tab, "https://www.facebook.com/settings?tab=applications", function (tab) {
                            extension.tabs.onReloaded.addListener(tab, scrapFb);
                        });
                    }, function (tab, response) {
                        finalCallback(false, "Wrong login or password. Please try again.");
                        extension.tabs.onClosed.removeListener(onclose);
                        setTimeout(function () {
                            //extension.tabs.close(tab);
                        }, 500);
                    });
                });
            }

            function scrapFb(tab) {
                extension.tabs.onReloaded.removeListener(scrapFb);
                extension.tabs.sendMessage(tab, "scrapFb", {}, function (response) {
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
