if(window.location.href.indexOf("https://ease.space")==0 || window.location.href.indexOf("http://51.254.207.91")==0){

var user = "anonymous";

var updatesToDelete = {};
var directUpdateToDelete = {};

document.addEventListener("NewEaseUser", function(event){
    user = event.detail;
    if(user=="anonymous"){
        extension.runtime.sendMessage("Disconnected",{}, function(){});
    }
    extension.runtime.sendMessage("ChangeEaseUser",{user:event.detail},function(res){});
}, false);


document.addEventListener("GetUpdates", function(event){
    var updatesToSend = [];
    extension.storage.get("allConnections", function(res){
        updatesToDelete["anonymous"]=[];
        updatesToDelete[user]=[];
        event.detail.forEach(function (email) {
            if(res["anonymous"][email]){
                for(var website in res["anonymous"][email]){
                    if(res["anonymous"][email][website].logWith)
                        var toSend = {user:email, website:website, logWith:res[email][website].logWith};
                    else  var toSend = {user:email, website:website, password:res[email][website].password};
                    updatesToSend.push(toSend);
                    updatesToDelete["anonymous"].push(toSend);
                }
            }
        });
        for(var email in res[user]){
            for(var website in res[user][email]){
                if(res[user][email][website].logWith)
                    var toSend = {user:email, website:website, logWith:res[email][website].logWith};
                else  var toSend = {user:email, website:website, password:res[email][website].password};
                updatesToSend.push(toSend);
                updatesToDelete[user].push(toSend);
            }
        }
        document.dispatchEvent(new CustomEvent("Updates", {"detail":updatesToSend}));
    });
});

document.addEventListener("DeleteUpdates", function(event){
    extension.storage.get("allConnections", function(res){
        if(updatesToDelete["anonymous"]){
            for(var update in updatesToDelete["anonymous"]){
                if(res["anonymous"][update.user] && res["anonymous"][update.user][update.website]){
                    delete res["anonymous"][update.user][update.website];
                    if(jQuery.isEmptyObject(res["anonymous"][update.user])){
                        delete res["anonymous"][update.user];
                    }
                }
            }
        }
        if(user != "anonymous" && updatesToDelete[user]){
            for(var update in updatesToDelete[user]){
                if(res[user][update.user] && res[user][update.user][update.website]){
                    delete res[user][update.user][update.website];
                    if(jQuery.isEmptyObject(res[user][update.user])){
                        delete res[user][update.user];
                    }
                }
            }
        }
        extension.storage.set("allConnections", res, function(){});
    });
});


extension.runtime.onMessage("SendUpdate", function logoutHandler(message, sendResponse){
    directUpdateToDelete = message;
    document.dispatchEvent(new CustomEvent("UserUpdate", {"detail":message}));
});

document.addEventListener("DeleteSingleUpdate", function(event){
    extension.storage.get("allConnections", function(res){
        if(user != "anonymous" && directUpdateToDelete[user]){
            for(var update in directUpdateToDelete[user]){
                if(res[user][update.user] && res[user][update.user][update.website]){
                    delete res[user][update.user][update.website];
                    if(jQuery.isEmptyObject(res[user][update.user])){
                        delete res[user][update.user];
                    }
                }
            }
        }
        extension.storage.set("allConnections", res, function(){});
    });
});

}
