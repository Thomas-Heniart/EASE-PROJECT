var RSAEncryption = new JSEncrypt({default_key_size: 1024});
var serverRSAEncryption = new JSEncrypt({default_key_size: 1024});

function setServerPublicKey(serverPublicKey) {
    serverPublicKey = "-----BEGIN PUBLIC KEY-----\n" + serverPublicKey;
    serverPublicKey = serverPublicKey.substring(0, 91) + "\n" + serverPublicKey.substring(91, 91 + 64) + "\n" + serverPublicKey.substring(91 + 64, 91 + 64 + 64) + "\n" + serverPublicKey.substring(91 + 64 + 64, serverPublicKey.length) + "\n-----END PUBLIC KEY-----";
    serverRSAEncryption.setKey(serverPublicKey);
}

function decipher(msg) {
    return RSAEncryption.decrypt(msg);
}

function cipher(msg) {
    return serverRSAEncryption.encrypt(msg);
}

window.addEventListener("load", function () {
    $.get("/api/v1/common/GetServerKey", {
        public_key: RSAEncryption.getPublicKeyB64()
    }, function (data) {
        setServerPublicKey(data.server_public_key);
    });
    /*ajaxHandler.get("/api/v1/common/GetServerKey", {
        public_key: RSAEncryption.getPublicKeyB64()
    }, function () {

    }, function (data) {
        setServerPublicKey(data.server_public_key);
    }, function () {

    }, false);*/
});