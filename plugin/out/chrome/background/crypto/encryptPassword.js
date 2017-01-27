function encryptPassword(password, callback){
    var encrypt = new JSEncrypt();
    getLastKey(function(keyDatas){
        encrypt.setPublicKey(keyDatas.key);
        callback({password:encrypt.encrypt(password), keyDate:keyDatas.date})
    });
}

function encryptAllPasswords(accounts, callback){
    var l = accounts.length;
    function encrypt(i, callback){
        encryptPassword(accounts[i].pass, function(res){
            accounts[i].pass = res.password;
            accounts[i].keyDate = res.keyDate;
            i=i+1;
            if(i<l){
                encrypt(i, callback);
            } else {
                callback(accounts);
            }
        });
    }
    if(l>0){
        encrypt(0, callback);
    } else {
        callback([]);
    }
}

function getLastKey(callback){
    $.get('https://ease.space/resources/publicEncryptionKeys.txt', function(res){
        var keys = res.split("\n");
        var lastKey = keys[keys.length-1];
        callback({date:lastKey.substring(0,6), key:lastKey.substring(7,lastKey.length)});
    });
}