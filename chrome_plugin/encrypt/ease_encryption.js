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

/* Initialize RSA */
var RSAEncryption = new JSEncrypt({default_key_size: 1024});
var publicKey = RSAEncryption.getKey().getPublicKey();
publicKey = publicKey.substring(27, (publicKey.length - 25)).replace("\n", "");
var serverPublicKey = "";
var serverRSAEncryption = new JSEncrypt({default_key_size: 1024});

/* Max size of a string : 117 characters */

function cipher(aString) {
    return serverRSAEncryption.encrypt(aString);
}

function decipher(aString) {
    return RSAEncryption.decrypt(aString);
}

function cipherObject(object) {
    map(object, cipher);
}

function decipherObject(object) {
    map(object, decipher);
}

function setServerPublicKey(key) {
    serverPublicKey = "-----BEGIN PUBLIC KEY-----\n" + key;
    serverPublicKey = serverPublicKey.substring(0, 91) + "\n" + serverPublicKey.substring(91, 91 + 64) + "\n" + serverPublicKey.substring(91 + 64, 91 + 64 + 64) + "\n" + serverPublicKey.substring(91 + 64 + 64, serverPublicKey.length) + "\n-----END PUBLIC KEY-----";
    serverRSAEncryption.setKey(serverPublicKey);
}

extension.runtime.bckgrndOnMessage("GetPublicKey", function (data, senderTab, sendResponse) {
    sendResponse(publicKey);
});

extension.runtime.bckgrndOnMessage("SetServerPublicKey", function (data, senderTab, sendReponse) {
    setServerPublicKey(data);
});

extension.runtime.bckgrndOnMessage("Cipher", function (data, senderTab, sendResponse) {
    cipherObject(data);
    sendResponse(data);
});

extension.runtime.bckgrndOnMessage("Decipher", function (data, senderTab, sendResponse) {
    decipherObject(data);
    sendResponse(data);
});
