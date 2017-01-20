cleanStoredUpdates();
//var lastEaseUser = "";
//var lastEaseUserNotAnonymous = "";
//var lastEaseTab = null;

/*extension.runtime.bckgrndOnMessage("ChangeEaseUser", function(message, sender, sendResponse){
    console.log("-- Current ease user : "+message.user +" --");
    lastEaseUser = message.user;
    if(lastEaseUser != "anonymous" && lastEaseUser != lastEaseUserNotAnonymous){
        extension.storage.set("visitedWebsites", [], function(){});
        lastEaseUserNotAnonymous = lastEaseUser;
        console.log("-- Current ease user : "+ lastEaseUserNotAnonymous +" --");
    } else if (lastEaseUser == "anonymous"){
        console.log("-- Disconnected from Ease --");
    }
    lastEaseTab = sender;
});

extension.runtime.bckgrndOnMessage("GetEaseUser", function(message, sender, sendReponse){
    sendResponse(getCurrentUser());
});

extension.runtime.bckgrndOnMessage("GetLastEaseUser", function(message, sender, sendReponse){
    sendResponse(lastEaseUserNotAnonymous);
});*/

/*function getCurrentUser(){
    if(extension.nbOfEaseTabs()==0){
        return "anonymous";
    } else {
        return lastEaseUser;
    }    
}*/

extension.runtime.bckgrndOnMessage('newConnectionToRandomWebsite', function (msg, senderTab, sendResponse) {
    rememberConnection(msg.username, msg.password, msg.website, false)
});

//msg = {'website':siteHost, 'username':username, 'password':password}

/*extension.runtime.bckgrndOnMessage('newFacebookUpdates', function(msg, senderTab, sendResponse){
    sendUpdate(msg);
}); //TODO

extension.runtime.bckgrndOnMessage('newLinkedinUpdates', function(msg, senderTab, sendResponse){
    sendUpdate(msg);
});*/ //TODO

function sendUpdate(update) {
    extension.storage.get("sessionId", function (sId) {
        if (sId != "") {
            extension.storage.get("extensionId", function (eId) {
                var xhr = new XMLHttpRequest();
                xhr.open("POST", "https://ease.space/CreateUpdate", false);
                xhr.onreadystatechange = function (aEvt) {
                    if (xhr.readyState == 4) {
                        var res = xhr.response.split(" ");
                        if (res[0] == "200") {
                            if (res[1] == "1") {
                                storeClassicUpdate(toSend);
                            } else {
                                removeClassicUpdate(toSend);
                            }
                        }
                    }
                };
                xhr.send("sessionId=" + sId + "&extensionId=" + eId + "&updates=" + JSON.stringify(toSend));
            });
        }
    });
}

function printLastConnections() {
    extension.storage.get('lastConnections', function (res) {
        console.log(res);
    });
}

function printAllConnections() {
    extension.storage.get('allConnections', function (res) {
        console.log(res);
    });
}

//SCRAPPEUR LOG WITH. NON FONCTIONNEL
/*var lastNavigatedWebsite = "";

extension.tabs.onNavigation(function(url){
    if(matchFacebookConnectUrl(url) && !matchFacebookUrl(lastNavigatedWebsite)){
        rememberLogWithConnection(lastNavigatedWebsite, "www.facebook.com");
    } else if(matchLinkedinConnectUrl(url) && !matchLinkedinUrl(lastNavigatedWebsite)){
        rememberLogWithConnection(lastNavigatedWebsite, "www.linkedin.com");
    }
    //if(!matchFacebookUrl(url)){
        lastNavigatedWebsite = getHost(url);
    //}
});*/

function rememberConnection(username, password, website, fromEase) {
    extension.storage.get('lastConnections', function (res) {
        if (!res) res = {};
        res[website] = {
            "user": username
        };
        console.log("-- Connection for email " + username + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function () {});
        if (!fromEase) {
            encryptPassword(password, function (passwordDatas) {
                sendUpdate({
                    type: "classic",
                    website: website,
                    username: username,
                    password: passwordDatas.password,
                    keyDate: passwordDatas.keyDate
                });
            });
        }
    });
}

function rememberDirectLogWithConnection(website, logWithDatas) {
    extension.storage.get('lastConnections', function (res) {
        if (!res) res = {};
        res[website] = logWithDatas;
        console.log("-- Connection with " + logWithDatas + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function () {});
        logWithDatas.website = website;
        //rememberEveryConnections(logWithDatas);
    });
}

function rememberLogWithConnection(website, logWithWebsite) {
    extension.storage.get('lastConnections', function (res) {
        if (!res) res = {};
        if (res[logWithWebsite]) {
            if (res[logWithWebsite].user)
                res[website] = {
                    "user": res[logWithWebsite].user,
                    "logWith": logWithWebsite
                };
            else
                res[website] = {
                    "user": res[logWithWebsite],
                    "logWith": logWithWebsite
                };
        }
        console.log("-- Connection with " + logWithWebsite + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function () {});
        //rememberEveryConnections({user:res[website].user, logWith:logWithWebsite, website:website});
    });
}

function storeUpdate(update) {
    removeUpdate(update);
    extension.storage.get("storedUpdates", function (storedUpdates) {
        if (storedUpdates == undefined) {
            storedUpdates = [];
        }
        var nDate = new Date();
        update.expiration = nDate.getTime() + (2 * 604800000); //expiration en 2 semaines
        storedUpdates.push(update);
        extension.storage.set("storedUpdates", storedUpdate, function () {});
    });
}

function removeUpdate(update) {
    extension.storage.get("storedUpdates", function (storedUpdates) {
        var toDelete = -1;
        if (storedUpdates != undefined) {
            for (var i = 0; i < storedUpdates.length; i++) {
                var oldUpdate = storedUpdates[i];
                if (oldUpdate.type == update.type && oldUpdate.website == update.website) {
                    if (update.type == "logwith" && oldUpdate.logwith == update.logwith && oldUpdate.user == update.user) {
                        toDelete = i;
                        break;
                    }
                    if (update.type == "classic" && oldUpdate.username == update.username) {
                        toDelete = i;
                        break;
                    }
                }
            }
        }
        if (toDelete > -1) {
            storedUpdates = storedUpdate.splice(toDelete, 1);
        }
        extension.storage.set("storedUpdates", storedUpdate, function () {});
    });
}

function cleanStoredUpdates() {
    extension.storage.get("storedUpdates", function (storedUpdates) {
        if (storedUpdates == undefined) {
            storedUpdates = [];
        }
        for (var i = 0; i > storedUpdates.length; i++) {
            if (storedUpdates[i].expiration < (new Date()).getTime()) {
                storedUpdates = storedUpdate.splice(i, 1);
                i--;
            }
        }
        extension.storage.set("storedUpdates", storedUpdates, function () {});
    });
    setTimeout(cleanStoredUpdates, 1000 * 60 * 60 * 4);
}

/*function rememberEveryConnections(connectionDatas){
    var creation = new Date();
    var easeUser = getCurrentUser();
    connectionDatas.expiration = creation.getTime()+604800000; //expiration en 1 semaine
    extension.storage.get("allConnections", function(res){
        if(res[easeUser] == undefined) 
            res[easeUser] = {};
        if(res[easeUser][connectionDatas.user] == undefined) 
            res[easeUser][connectionDatas.user] = {};
        
        if(connectionDatas.logWith) {
            res[easeUser][connectionDatas.user][connectionDatas.website] = {logWith:connectionDatas.logWith, expiration:connectionDatas.expiration};
            extension.storage.set('allConnections', res, function(){
                if(easeUser!="anonymous" && lastEaseTab != null){
                    extension.tabs.sendMessage(lastEaseTab, "SendUpdate", connectionDatas, function(){});
                }
            });        
        }
            
        else {
            encryptPassword(connectionDatas.password, function(passwordDatas){
                res[easeUser][connectionDatas.user][connectionDatas.website] = {password:passwordDatas.password, expiration:connectionDatas.expiration, keyDate:passwordDatas.keyDate};
                extension.storage.set('allConnections', res, function(){
                    if(easeUser!="anonymous" && lastEaseTab != null){
                        extension.tabs.sendMessage(lastEaseTab, "SendUpdate", connectionDatas, function(){});
                    }
                });        
            });
        }
        
    });
    
}

function cleanEveryConnections(){
    extension.storage.get("allConnections", function(response){
        if((response==undefined || !response.validator) && !response.newValidator) 
            response = {validator:"ok"};
        if(!response.newValidator){
            var res = {newValidator:"ok"};
            res["anonymous"]=response;
        } else {
            var res = response;
        }
        for (var easeUser in res){
            for(var user in res[easeUser]){
                for (var website in res[easeUser][user]){
                    if(res[easeUser][user][website].expiration < (new Date()).getTime()){
                        delete res[easeUser][user][website];
                    }
                }
                if(jQuery.isEmptyObject(res[easeUser][user]))
                    delete res[easeUser][user];
            }
            if(jQuery.isEmptyObject(res[easeUser]))
                delete res[easeUser];
        }
        
        extension.storage.set("allConnections", res, function(){});
    });
    setTimeout(cleanEveryConnections, 1000*60*60*4);
}*/

extension.runtime.bckgrndOnMessage("fbDisconnected", function () {
    rememberConnection("disconnected", "", "www.facebook.com", true);
});

function getHost(url) {
    var getLocation = function (href) {
        var l = document.createElement("a");
        l.href = href;
        return l;
    };
    return getLocation(url).hostname;
}

function equalArrays(array1, array2) {
    // compare lengths - can save a lot of time 
    if (array2.length != array1.length)
        return false;

    for (var i = 0, l = array2.length; i < l; i++) {
        // Check if we have nested arrays
        if (array2[i] instanceof Array && array1[i] instanceof Array) {
            // recurse into the nested arrays
            if (!array2[i].equals(array1[i]))
                return false;
        } else if (array2[i] != array1[i]) {
            // Warning - two different object instances will never be equal: {x:20} != {x:20}
            return false;
        }
    }
    return true;
}