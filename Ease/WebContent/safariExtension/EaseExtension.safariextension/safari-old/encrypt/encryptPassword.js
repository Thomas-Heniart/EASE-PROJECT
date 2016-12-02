var encrypt = new JSEncrypt();
encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDA/XTdrGCk70Xh2qqXVJpahP4mnvDl/41P1oFNtnbuHAwxZClnNrfw9DUg6l1sGKBmxbXp8MqMruBrtlsoMMrK8gM//6ayBGQYTpyD/QQbU7I6Ji/t3raHiLn9neNq+FnQjwZsnyOmQLtrUkz5hoAN/hgzIT4zluEneqwbXuTNwIDAQAB");

function encryptPassword(password, callback){
    return encrypt.encrypt(password);
}