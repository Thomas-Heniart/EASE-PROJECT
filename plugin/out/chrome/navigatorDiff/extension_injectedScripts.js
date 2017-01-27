var extension = {
    sendMessage: function (name, msg, callback) {
        chrome.runtime.sendMessage({
            "name": name,
            "message": msg
        }, callback);
    },
    onMessage: {
        addListener: function (messageName, fct) {
            var f = function (event, sender, sendResponse) {
                if (event.name == messageName) {
                    fct(event.message, sendResponse);
                    return true;
                }
            };
            listenerFactory.addListener(this, chrome.runtime.onMessage, fct, f);
        },
        removeListener: function (fct) {
            listenerFactory.removeListener(this, chrome.runtime.onMessage, fct);
        }
    },
    getStorage: function (name, callback) {
        extension.sendMessage("getStorage", name, function (res) {
            callback(res);
        });
    },
    setStorage: function (name, val, callback) {
        extension.sendMessage("setStorage", {
            name: name,
            val: val
        }, function (res) {});
    },
    getRessource: function(url){
        return chrome.extension.getURL(url);
    }
}

if (window.top === window) {
    var frames = {
        sendMessage: function (frameUrl, name, msg, callback) {
            extension.sendMessage("sendBackToFrame", {
                "name": name,
                "message": msg,
                "frame": frameUrl
            }, callback)
        },
        onMessage: {
            addListener: function (messageName, fct) {
                var f = function (event, sender, sendResponse) {
                    if (event.name == "toParentFrame" && event.message.name == messageName) {
                        fct(event.message.message, sendResponse);
                        return true;
                    }
                };
                listenerFactory.addListener(this, chrome.runtime.onMessage, fct, f);
            },
            removeListener: function (fct) {
                listenerFactory.removeListener(this, chrome.runtime.onMessage, fct);
            }
        }
    }

} else {
    var parentframe = {
        sendMessage: function (name, msg, callback) {
            extension.sendMessage("sendBackToParent", {
                "name": name,
                "message": msg
            }, callback)
        },
        onMessage: {
            addListener: function (messageName, fct) {
                var f = function (event, sender, sendResponse) {
                    if (event.name == "toChildFrame" && event.message.name == messageName && window.location.href == event.message.frame) {
                        fct(event.message.message, sendResponse);
                        return true;
                    }
                };
                listenerFactory.addListener(this, chrome.runtime.onMessage, fct, f);
            },
            removeListener: function (fct) {
                listenerFactory.removeListener(this, chrome.runtime.onMessage, fct);
            }
        }
    }
}


var listenerFactory = {
    addListener: function (easeFct, chromeFct, userFct, createdFct) {
        if (!easeFct.listeners) {
            easeFct.listeners = [];
        }
        var list = easeFct.listeners;
        var store = {
            "userFct": userFct,
            "createdFct": createdFct,
        }
        list.push(store);
        chromeFct.addListener(createdFct);
    },
    removeListener: function (easeFct, chromeFct, userFct) {
        if (easeFct.listeners) {
            var list = easeFct.listeners;
            if (!userFct) {
                for (var i = 0; i < list.length; i++) {
                    chromeFct.removeListener(list[i].createdFct);
                    list.splice(i, 1);
                    i--;
                }
            } else {
                for (var i in list) {
                    if (list[i].userFct == userFct) {
                        chromeFct.removeListener(list[i].createdFct);
                        list.splice(i, 1);
                        break;
                    }
                }
            }
        }

    }
};

