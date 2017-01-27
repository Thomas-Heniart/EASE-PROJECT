extension.runtime.onMessage.addListener("ScrapChrome", function (msg, senderTab, sendResponse) {
    startScrapChrome(msg.login, msg.password, function (success, response) {
        chromeScrap.user = {};
        if (success && response.length == 0) {
            success = false;
            response = "There is no password saved on this chrome account. Try it with another account."
        }
        extension.tabs.focus(senderTab, function () {});
        sendResponse({
            "success": success,
            "msg": response
        });
    });
});

var chromeScrap = {
    "user": {
    },
    "website": {
        "home": "https://accounts.google.com/Logout",
        "connect": {
            "todo": [
                {
                    "action": "waitfor",
                    "search": ".omTHz"
                },
                {
                    "action": "goto",
                    "url": "https://accounts.google.com/ServiceLogin?sacu=1#identifier"
                },
                {
                    "action": "fill",
                    "what": "login",
                    "search": "#Email",
                    "grave": true
                },
                {
                    "action": "click",
                    "search": "#next"
                },
                {
                    "action": "check",
                    "type": "absentElement",
                    "search": ".form-error"
                },
                {
                    "action": "waitfor",
                    "search": "#Passwd"
                },
                {
                    "action": "fill",
                    "what": "password",
                    "search": "#Passwd",
                    "grave": true
                },
                {
                    "action": "click",
                    "search": "#signIn",
                    "grave": true
                },
                {
                    "action": "waitload"
                },
                {
                    "action": "check",
                    "type": "absentElement",
                    "search": "#Passwd"
                },
                {
                    "action": "waitfor",
                    "search": ".vmZ0T"
                },
                {
                    "action": "goto",
                    "url": "https://passwords.google.com/"
                },
                {
                    "action":"waitfor",
                    "search":"#Passwd"
                },
                {
                    "action": "fill",
                    "what": "password",
                    "search": "#Passwd",
                    "grave": true
                },
                {
                    "action": "click",
                    "search": "#signIn",
                    "grave": true
                },
			]
        }
    }
}

function startScrapChrome(login, password, finalCallback) {
    chromeScrap.user.login = login;
    chromeScrap.user.password = password;
    extension.windows.getCurrent(function (window) {
        extension.tabs.create(window, "https://accounts.google.com/Logout", false, function (tab) {
            function onclose() {
                finalCallback(false, "It seems that you closed the tab. Please try again.");
                extension.tabs.onClosed.removeListener(onclose);
                extension.tabs.onReloaded.removeListener(doConnect);
                extension.tabs.onReloaded.removeListener(scrapChrome);
            }

            function doConnect(tab) {
                extension.tabs.onReloaded.removeListener(doConnect);
                generateSteps("connect", "scrapChrome", chromeScrap, function (actionSteps) {
                    executeSteps(tab, actionSteps, function (tab, response) {
                        extension.tabs.onReloaded.addListener(tab, scrapChrome);
                    }, function (tab, response) {
                        extension.tabs.onClosed.removeListener(onclose);
                        setTimeout(function () {
                            //extension.tabs.close(tab);
                        }, 500);
                        finalCallback(false, "Wrong login or password. Please try again.");
                    });
                });
            }

            function scrapChrome(tab) {
                extension.tabs.onReloaded.removeListener(scrapChrome);
                extension.tabs.sendMessage(tab, "scrapChrome", {}, function (response) {
                    encryptAllPasswords(response, function (finalRes) {
                        extension.tabs.onClosed.removeListener(onclose);
                        setTimeout(function () {
                            //extension.tabs.close(tab);
                        }, 500);
                        finalCallback(true, finalRes);
                    });
                });
            }

            extension.tabs.onClosed.addListener(tab, onclose);
            extension.tabs.onReloaded.addListener(tab, doConnect);
        });
    });
}
