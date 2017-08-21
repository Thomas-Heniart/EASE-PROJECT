var AesUtil = function (salt, passphrase) {
    this.keySize = 128 / 32;
    this.iterationCount = 65536;
    this.salt = salt;
    this.passphrase = passphrase;
    this.key = this.generateKey(this.salt, this.passphrase);
};

AesUtil.prototype.generateKey = function (salt, passphrase) {
    return CryptoJS.PBKDF2(
        passphrase,
        CryptoJS.enc.Hex.parse(salt),
        {keySize: this.keySize, iterations: this.iterationCount});
};

AesUtil.prototype.encrypt = function (plainText, iv) {
    var encrypted = CryptoJS.AES.encrypt(
        plainText,
        this.key,
        {iv: CryptoJS.enc.Hex.parse(iv)});
    return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
}

AesUtil.prototype.decrypt = function (iv, cipherText) {
    var key = this.generateKey(this.salt, this.passphrase);
    var cipherParams = CryptoJS.lib.CipherParams.create({
        ciphertext: CryptoJS.enc.Base64.parse(cipherText)
    });
    var decrypted = CryptoJS.AES.decrypt(
        cipherParams,
        key,
        {iv: CryptoJS.enc.Hex.parse(iv)});
    return decrypted.toString(CryptoJS.enc.Utf8);
};

AesUtil.prototype.generateIv = function () {
    return CryptoJS.lib.WordArray.random(128 / 8).toString(CryptoJS.enc.Hex);
};

var aesUtils;

/* Initialize RSA */
var RSAEncryption = new JSEncrypt({default_key_size: 1024});

$(document).ready(function () {
    $.get('/api/v1/common/GetServerKey', {
        public_key: RSAEncryption.getPublicKeyB64()
    }, function (data) {
        aesUtils = new AesUtil(RSAEncryption.decrypt(data.salt), RSAEncryption.decrypt(data.passphrase));
        var iv = aesUtils.generateIv();
        alert(iv);
        var cipheredText = aesUtils.encrypt("bite", iv);
        $.ajax({
            type: "POST",
            url: "/TestEncryption",
            data: JSON.stringify({
                iv: iv,
                text: cipheredText
            }),
            contentType: "application/JSON; charset=utf-8",
            dataType: "json",
            success: function (data) {
                alert(aesUtils.decrypt(data.iv, data.msg));
            },
        });
    });
});