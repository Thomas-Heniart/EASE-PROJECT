var storage = {
    sessionId: {},
    extensionId: {},
    websitesToLogout: {},
    storedUpdates: {},
    settings: {},
    lastConnections: {}
}

createDatasValue("sessionId", function (res) {
    storage.sessionId = res;
});

createDatasValue("extensionId", function (res) {
    storage.extensionId = res;
    if (storage.extensionId.value == "") {
        id = generateId(32);
        storage.extensionId.set(id);
    }
});

function generateId(len) {
    var text = "";
    var alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
    for (var i = 0; i < len; i++)
        text += alphabet.charAt(Math.floor(Math.random() * alphabet.length));
    return text;
}

createDatasArray("websitesToLogout", function (res) {
    storage.websitesToLogout = res;
});

createDatasArray("storedUpdates", function (res) {
    storage.storedUpdates = res;
    cleanStoredUpdates();
});

createDatasObject("settings", function (res) {
    if (!('homepage' in res.values)){
        res.set("homepage", true);
    }
    storage.settings = res;
});

createDatasObject("lastConnections", function (res) {
    storage.lastConnections = res;
});


function createDatasValue(name, callback) {
    extension.local_storage.get(name, function (res) {
        if (!res) {
            res = "";
        }
        var datas = {
            type: "value",
            name: name,
            value: res,
            get: function () {
                return this.value;
            },
            set: function (value) {
                this.value = value;
                extension.local_storage.set(this.name, this.value, function () {});
            },
            getDatas: function () {
                return this.value;
            },
            setDatas: function (val) {
                this.value = val;
                extension.local_storage.set(this.name, this.value, function () {});
            }
        };
        callback(datas);
    });
}

function createDatasArray(name, callback) {
    extension.local_storage.get(name, function (res) {
        if (!res) {
            res = [];
        }
        var datas = {
            type: "array",
            name: name,
            list: res,
            length: res.length,
            get: function (index) {
                return this.list[index];
            },
            push: function (value) {
                this.list.push(value);
                this.length = this.list.length;
                extension.local_storage.set(this.name, this.list, function () {});
            },
            remove: function (index) {
                this.list.slice(index, 1);
                this.length = this.list.length;
                extension.local_storage.set(this.name, this.list, function () {});
            },
            clear: function () {
                this.list = [];
                this.length = this.list.length;
                extension.local_storage.set(this.name, [], function () {});
            },
            getDatas: function () {
                return this.list;
            },
            setDatas: function (val) {
                this.list = val;
                this.length = this.list.length;
                extension.local_storage.set(this.name, this.list, function () {});
            }
        };
        callback(datas);
    });
}

function createDatasObject(name, callback) {
    extension.local_storage.get(name, function (res) {
        if (!res) {
            res = {};
        }
        var datas = {
            type: "object",
            name: name,
            values: res,
            get: function (key) {
                if (this.values[key]) {
                    return this.values[key];
                } else {
                    return false;
                }
            },
            set: function (key, value) {
                this.values[key] = value;
                extension.local_storage.set(this.name, this.values, function () {});
            },
            remove: function (key) {
                delete this.values[key];
                extension.local_storage.set(this.name, this.values, function () {});
            },
            clear: function () {
                this.values = {};
                extension.local_storage.set(this.name, {}, function () {});
            },
            getDatas: function () {
                return this.values;
            },
            setDatas: function (val) {
                this.values = val;
                extension.local_storage.set(this.name, this.values, function () {});
            }
        };
        callback(datas);
    });
}

extension.runtime.onMessage.addListener("getStorage", function (msg, senderTab, sendResponse) {
    for (var k in storage) {
        if (storage[k].name == msg) {
            sendResponse(storage[k].getDatas());
            return;
        }
    }
    sendResponse(false);
});

extension.runtime.onMessage.addListener("setStorage", function (msg, senderTab, sendResponse) {
    for (var k in storage) {
        if (storage[k].name == msg.name) {
            storage[k].setDatas(msg.val);
            return;
        }
    }
});
