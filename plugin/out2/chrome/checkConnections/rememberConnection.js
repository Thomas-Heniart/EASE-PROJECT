cleanStoredUpdates();
var senderTabs = [];

extension.runtime.bckgrndOnMessage('newFormSubmitted', function (msg, senderTab, sendResponse) {
    msg.hostUrl = getHost(senderTab.url);
    for (var i in senderTabs) {
        if (senderTabs[i] == senderTab.id) {
            console.log("tab already sent something");
            extension.tabs.onMessageRemoveListener(senderTab);
            senderTabs.splice(i, 1);
            break;
        }
    }
    senderTabs.push(senderTab.id);
    rememberConnection(msg.update.username, msg.hostUrl);
    extension.tabs.onMessage(senderTab, "reloadDone", function () {
        extension.tabs.onMessageRemoveListener(senderTab);
        checkSuccessfullConnexion(msg.hostUrl, senderTab, function () {
            encryptPassword(msg.update.password, function (passwordDatas) {
                console.log("send update " + msg.hostUrl + " " + msg.update.username);
                sendUpdate({
                    type: "classic",
                    website: msg.hostUrl,
                    username: msg.update.username,
                    password: passwordDatas.password,
                    keyDate: passwordDatas.keyDate
                });
            });
        });
    });
});

extension.runtime.bckgrndOnMessage('logWithButtonClicked', function (msg, senderTab, sendResponse) {
    msg.hostUrl = getHost(senderTab.url);

    function react(tabId, params, newTab) {
        if (newTab.url.indexOf("facebook") != -1) {
            chrome.tabs.onUpdated.removeListener(react);
            extension.tabs.onMessage(senderTab, "reloadDone", function () {
                extension.tabs.onMessageRemoveListener(senderTab);
                checkSuccessfullConnexion(msg.hostUrl, senderTab, function () {
                    rememberLogWithConnection(msg.hostUrl, msg.logWithWebsite, msg.hostLogWithWebsite);
                    extension.storage.get('lastConnections', function (res) {
                        if (!res) res = {};
                        if (res[msg.hostLogWithWebsite]) {

                            sendUpdate({
                                type: "logwith",
                                website: msg.hostUrl,
                                username: res[msg.hostLogWithWebsite].user,
                                logwith: msg.logWithWebsite
                            });
                        }
                    });

                });
            });
        }
    }
    chrome.tabs.onUpdated.addListener(react);
    setTimeout(function () {
        chrome.tabs.onUpdated.removeListener(react);
    }, 3000);
});

function rememberConnection(username, website) {
    extension.storage.get('lastConnections', function (res) {
        if (!res) res = {};
        res[website] = {
            "user": username
        };
        console.log("-- Connection for email " + username + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function () {});
    });
}

function rememberDirectLogWithConnection(website, logWithDatas) {
    extension.storage.get('lastConnections', function (res) {
        if (!res) res = {};
        res[website] = logWithDatas;
        console.log("-- Connection with " + logWithDatas + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function () {});
        logWithDatas.website = website;
    });
}

function rememberLogWithConnection(website, logWithWebsite, hostLogWithWebsite) {
    extension.storage.get('lastConnections', function (res) {
        if (!res) res = {};
        if (res[hostLogWithWebsite]) {
            res[website] = {
                "user": res[hostLogWithWebsite].user,
                "logWith": logWithWebsite
            }
        }
        console.log("-- Connection with " + logWithWebsite + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function () {});
    });
}

function checkSuccessfullConnexion(hostUrl, tab, callback) {
    var checkAlreadyLogged;
    getCheckAlreadyLoggedCondition(hostUrl, function (xhrRes) {
        checkAlreadyLogged = JSON.parse(xhrRes);
        extension.tabs.sendMessage(tab, "checkCo", {
            elem: checkAlreadyLogged
        }, function (res) {
            if (res) {
                callback();
            }
        });
    });
}

function getDOM(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function (aEvt) {
        if (xhr.readyState == 4) {
            callback(xhr.response);
        }
    };

    xhr.send(null);
}

function getCheckAlreadyLoggedCondition(host, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/GetCheckAlreadyLogged", true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function (aEvt) {
        if (xhr.readyState == 4) {
            if (xhr.response.indexOf("200 ") == 0) {
                callback(xhr.response.substring(4, xhr.response.length));
            }
        }
    };
    var params = "host=" + host;
    xhr.send(params);
}

function isConnected(url, user) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function (aEvt) {
        if (xhr.readyState == 4) {
            console.log(xhr);
            console.log("has password : " + xhr.response.indexOf("type=\"password\""));
            console.log("has " + user + " : " + xhr.response.indexOf(user));
            console.log("has logout : " + xhr.response.indexOf("logout"));
        }
    };

    xhr.send(null);
}

function sendUpdate(update) {
    extension.storage.get("sessionId", function (sId) {
        if (sId != "") {
            console.log("sessionId:");
            console.log(sId);
            $.post("http://localhost:8080/CreateUpdate", {
                    "sessionId": sId,
                    "update": JSON.stringify(update)
                },
                function (resp) {
                    var res = resp.split(" ");
                    if (res[0] == "200") {
                        if (res[1] == "1") {
                            storeUpdate(update);
                        } else {
                            removeUpdate(update, function () {});
                        }
                    }
                });
        } else {
            storeUpdate(update);
        }
    });
}

function storeUpdate(update) {
    removeUpdate(update, function () {
        extension.storage.get("storedUpdates", function (storedUpdates) {
            if (storedUpdates == undefined) {
                storedUpdates = [];
            }
            var nDate = new Date();
            update.expiration = nDate.getTime() + (2 * 604800000); //expiration en 2 semaines
            storedUpdates.push(update);
            extension.storage.set("storedUpdates", storedUpdates, function () {});
        });
    });

}

function removeUpdate(update, callback) {
    extension.storage.get("storedUpdates", function (storedUpdates) {
        var toDelete = -1;
        if (storedUpdates != undefined) {
            for (var i = 0; i < storedUpdates.length; i++) {
                var oldUpdate = storedUpdates[i];
                if (oldUpdate.type == update.type && oldUpdate.website == update.website) {
                    if (update.type == "logwith" && oldUpdate.logwith == update.logwith && oldUpdate.user == update.user) {
                        toDelete = i;
                        break;
                    }
                    if (update.type == "classic" && oldUpdate.username == update.username) {
                        toDelete = i;
                        break;
                    }
                }
            }
        }
        if (toDelete > -1) {
            storedUpdates.splice(toDelete, 1);
        }
        extension.storage.set("storedUpdates", storedUpdates, callback);
    });
}

function cleanStoredUpdates() {
    extension.storage.get("storedUpdates", function (storedUpdates) {
        if (storedUpdates == undefined) {
            storedUpdates = [];
        }
        for (var i = 0; i < storedUpdates.length; i++) {
            if (storedUpdates[i].expiration < (new Date()).getTime()) {
                storedUpdates.splice(i, 1);
                i--;
            }
        }
        extension.storage.set("storedUpdates", storedUpdates, function () {});
    });
    setTimeout(cleanStoredUpdates, 1000 * 60 * 60 * 4);
}

extension.runtime.bckgrndOnMessage("fbDisconnected", function () {
    rememberConnection("disconnected", "www.facebook.com");
});

function getHost(url) {
    var getLocation = function (href) {
        var l = document.createElement("a");
        l.href = href;
        return l;
    };
    return getLocation(url).hostname;
}

function equalArrays(array1, array2) {
    // compare lengths - can save a lot of time 
    if (array2.length != array1.length)
        return false;

    for (var i = 0, l = array2.length; i < l; i++) {
        // Check if we have nested arrays
        if (array2[i] instanceof Array && array1[i] instanceof Array) {
            // recurse into the nested arrays
            if (!array2[i].equals(array1[i]))
                return false;
        } else if (array2[i] != array1[i]) {
            // Warning - two different object instances will never be equal: {x:20} != {x:20}
            return false;
        }
    }
    return true;
}

function printLastConnections() {
    extension.storage.get('lastConnections', function (res) {
        console.log(res);
    });
}

function printStoredUpdates() {
    extension.storage.get('storedUpdates', function (res) {
        console.log(res);
    });
}
