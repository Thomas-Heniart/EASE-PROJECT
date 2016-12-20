function encryptPassword(password, callback){
    var encrypt = new JSEncrypt();
    getLastKey(function(keyDatas){
        encrypt.setPublicKey(keyDatas.key);
        callback({password:encrypt.encrypt(password), keyDate:keyDatas.date})
    });
}

function encryptAllPassword(accounts, callback){
    var l = accounts.length;
    var return = accounts;
    function encrypt(i, callback){
        encryptPassword(accounts[i].password, function(res){
            accounts[i].password = res.password;
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
    }
}

function getLastKey(callback){
    var req = new XMLHttpRequest();
    req.open('GET', /*'http://localhost:8080/HelloWorld/resources/publicEncryptionKeys.txt'  */ 'https://ease.space/resources/publicEncryptionKeys.txt', false);
    req.send(null);
    if(req.status == 200){
        var keys = req.responseText.split("\n");
        var lastKey = keys[keys.length-1];
        callback({date:lastKey.substring(0,6), key:lastKey.substring(7,lastKey.length)});
    }
}