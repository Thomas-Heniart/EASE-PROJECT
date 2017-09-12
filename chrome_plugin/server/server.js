var stringConstructor = "test".constructor;
var arrayConstructor = [].constructor;
var objectConstructor = {}.constructor;

function typeOf(object) {
    if (object === null) {
        return "null";
    }
    else if (object === undefined) {
        return "undefined";
    }
    else if (object.constructor === stringConstructor) {
        return "String";
    }
    else if (object.constructor === arrayConstructor) {
        return "Array";
    }
    else if (object.constructor === objectConstructor) {
        return "Object";
    }
    else {
        return "don't know";
    }
}

function map(obj, mapFunction) {
    if (typeOf(obj) === "String")
        return mapFunction(obj);
    else if (typeOf(obj) === "Object") {
        for (var key in obj) {
            if (obj.hasOwnProperty(key))
                obj[key] = map(obj[key], mapFunction);
        }
        return obj;
    }
    else if (typeOf(obj) === "Array") {
        for (var i = 0; i < obj.length; i++)
            obj[i] = map(obj[i], mapFunction);
        return obj;
    }
    else
        return obj;
}

/* Plugin key */
var publicKey = null;

/* Initialize RSA */
var RSAEncryption = new JSEncrypt({default_key_size: 1024});

var serverPublicKey = "";
var serverRSAEncryption = new JSEncrypt({default_key_size: 1024});

/* Max size of a string : 117 characters */

var server = {
    serverUrl: "https://localhost:8443",
    cipher: function (aString) {
        return serverRSAEncryption.encrypt(aString);
    },
    decipher: function (aString) {
        return RSAEncryption.decrypt(aString);
    },
    post: function (url, json, successCallback, errorCallback, async) {
        if (serverPublicKey !== "")
            map(json, this.cipher);
        var self = this;
        $.ajax({
            type: "POST",
            url: this.serverUrl + url,
            data: JSON.stringify(json),
            contentType: "application/JSON; charset=utf-8",
            dataType: "json",
            async: ((async === null) ? true : async),
            success: function (data) {
                if (serverPublicKey !== "")
                    map(data, self.decipher);
                successCallback(data);
            },
            error: errorCallback
        });
    },
    initialize: function () {
        publicKey = RSAEncryption.getKey().getPublicKey();
        publicKey = publicKey.substring(27, (publicKey.length - 25)).replace("\n", "");
        this.post("/SendSessionPublicKey", {publicKey: publicKey}, function (data) {
            serverPublicKey = data.publicKey;
            serverPublicKey = "-----BEGIN PUBLIC KEY-----\n" + serverPublicKey;
            serverPublicKey = serverPublicKey.substring(0, 91) + "\n" + serverPublicKey.substring(91, 91 + 64) + "\n" + serverPublicKey.substring(91 + 64, 91 + 64 + 64) + "\n" + serverPublicKey.substring(91 + 64 + 64, serverPublicKey.length) + "\n-----END PUBLIC KEY-----";
            serverRSAEncryption.setKey(serverPublicKey);
        }, null, false);
    }

}