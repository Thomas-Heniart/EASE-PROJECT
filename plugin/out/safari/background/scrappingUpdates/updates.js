var formSubmitted = {};

function onreloadCheckCo(senderTab) {
    extension.tabs.onReloaded.removeListener(onreloadCheckCo);
    checkSuccessfullConnexion(formSubmitted.hostUrl, senderTab, function () {
        encryptPassword(formSubmitted.update.password, function (passwordDatas) {
            sendUpdate({
                type: "classic",
                website: formSubmitted.hostUrl,
                username: formSubmitted.update.username,
                password: passwordDatas.password,
                keyDate: passwordDatas.keyDate
            });
        });
    });
}

extension.runtime.onMessage.addListener('newFormSubmitted', function (msg, senderTab, sendResponse) {
    msg.hostUrl = getHost(senderTab.url);
    formSubmitted = msg;
    rememberClassicConnection(msg.update.username, msg.hostUrl);
    extension.tabs.onReloaded.removeListener(onreloadCheckCo);
    extension.tabs.onReloaded.addListener(senderTab, onreloadCheckCo);
});

extension.runtime.onMessage.addListener('logWithButtonClicked', function (msg, senderTab, sendResponse) {
    msg.hostUrl = getHost(senderTab.url);

    function react(tabId, params, newTab) {
        if (newTab.url.indexOf("facebook") != -1) {
            extension.tabs.onUpdated.removeListener(react);
            extension.tabs.onReloaded.addListener(senderTab, function onreload() {
                extension.tabs.onReloaded.removeListener(onreload);
                checkSuccessfullConnexion(msg.hostUrl, senderTab, function () {
                    rememberLogWithConnection(msg.hostUrl, msg.hostLogWithWebsite);
                    if (storage.lastConnections.get(msg.hostLogWithWebsite)) {
                        sendUpdate({
                            type: "logwith",
                            website: msg.hostUrl,
                            username: storage.lastConnections.get(msg.hostLogWithWebsite).user,
                            logwith: msg.logWithWebsite
                        });
                    }

                });
            });
        }
    }
    extension.tabs.onUpdated.addListener(null, react);
    setTimeout(function () {
        extension.tabs.onUpdated.removeListener(react);
    }, 5000);
});

function checkSuccessfullConnexion(hostUrl, tab, callback) {
    var checkAlreadyLogged;
    getCheckAlreadyLoggedCondition(hostUrl, function (cAL) {
        checkAlreadyLogged = JSON.parse(cAL);
        var bigStep = {
            "website": {
                "checkAlreadyLogged": checkAlreadyLogged
            }
        };
        console.log(bigStep);
        generateSteps("checkAlreadyLogged", false, bigStep, function (steps) {
            console.log(steps);
            executeSteps(tab, steps, function () {
                console.log("connection success");
                callback();
            }, function () {
                console.log("connection fail");
            });
        });
    });
}

function getCheckAlreadyLoggedCondition(host, callback) {
    $.post("https://ease.space/GetCheckAlreadyLogged", {
            "host": host
        },
        function (resp) {
            var res = resp.split(" ");
            if (res[0] == "200") {
                callback(resp.substring(4, resp.length));
            }
        });
}

function sendUpdate(update) {
    var sId = storage.sessionId.value;
    if (sId != "") {
        $.post("https://ease.space/CreateUpdate", {
                "sessionId": sId,
                "update": JSON.stringify(update)
            },
            function (resp) {
                var res = resp.split(" ");
                if (res[0] == "200") {
                    if (res[1] == "1") {
                        storeUpdate(update);
                    } else {
                        removeUpdate(update);
                    }
                }
            });
    } else {
        storeUpdate(update);
    }

}
