var extension = {
    storage: {
        get: function (key, callback) {
            chrome.storage.local.get(key, function (res) {
                callback(res[key]);
            });
        },
        set: function (key, value, callback) {
            var obj = {};
            obj[key] = value;
            chrome.storage.local.set(obj, callback);
        }
    },
    runtime: {
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
        }
    }
}
