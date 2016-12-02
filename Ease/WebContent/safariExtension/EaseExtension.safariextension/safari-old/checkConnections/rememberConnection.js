cleanEveryConnections();
var lastEaseUser = "";
var lastEaseTab = null;

extension.runtime.bckgrndOnMessage("ChangeEaseUser", function(message, sender, sendResponse){
    lastEaseUser = message.user;
    lastEaseTab = sender;
});

extension.runtime.bckgrndOnMessage("GetEaseUser", function(message, sender, sendReponse){
    sendResponse(getCurrentUser());
});

function getCurrentUser(){
        if(extension.nbOfEaseTabs()==0){
            return "anonymous";
        } else {
            return lastEaseUser;
        }
}

extension.runtime.bckgrndOnMessage('newConnectionToRandomWebsite', function(msg, senderTab, sendResponse){
    rememberConnection(msg.username, msg.password, msg.website, false);
});

function printLastConnections(){
    extension.storage.get('lastConnections', function(res){
        console.log(res);
    });
}

function printAllConnections(){
    extension.storage.get('allConnections', function(res){
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

function rememberConnection(username, password, website, fromEase){
    extension.storage.get('lastConnections', function(res){
        if(!res) res = {};
        res[website] = {"user":username};
        console.log("-- Connection for email " + username +" on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function(){});
        if(!fromEase) rememberEveryConnections({user:username, password:password, website:website});
    });    
}

function rememberDirectLogWithConnection(website, logWithDatas){
    extension.storage.get('lastConnections', function(res){
        if(!res) res = {};
        res[website] =logWithDatas;
        console.log("-- Connection with " +logWithDatas + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function(){});
        logWithDatas.website = website;
        //rememberEveryConnections(logWithDatas);
    });    
}

function rememberLogWithConnection(website, logWithWebsite){
    extension.storage.get('lastConnections', function(res){
        if(!res) res = {};
        if(res[logWithWebsite]){
            if(res[logWithWebsite].user)
                res[website] = {"user":res[logWithWebsite].user, "logWith":logWithWebsite};
            else 
                res[website] = {"user":res[logWithWebsite], "logWith":logWithWebsite};
        }
        console.log("-- Connection with " +logWithWebsite + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function(){});
        rememberEveryConnections({user:res[website].user, logWith:logWithWebsite, website:website});
    });    
}

function rememberEveryConnections(connectionDatas){
    var creation = new Date();
    var easeUser = getCurrentUser();
    connectionDatas.expiration = creation.getTime()+604800000; //expiration en 1 semaine
    extension.storage.get("allConnections", function(res){
        if(res[easeUser] == undefined) 
            res[easeUser] = {};
        if(res[easeUser][connectionDatas.user] == undefined) 
            res[easeUser][connectionDatas.user] = {};
        if(connectionDatas.logWith) 
            res[easeUser][connectionDatas.user][connectionDatas.website] = {logWith:connectionDatas.logWith, expiration:connectionDatas.expiration};
        else 
            res[easeUser][connectionDatas.user][connectionDatas.website] = {password:encryptPassword(connectionDatas.password), expiration:connectionDatas.expiration};
        extension.storage.set('allConnections', res, function(){
            if(easeUser!="anonymous" && lastEaseTab != null){
                extension.tabs.sendMessage(lastEaseTab, "SendUpdate", connectionDatas, function(){});
            }
        });        
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
}

extension.runtime.bckgrndOnMessage("fbDisconnected", function(){
     rememberConnection("disconnected", "", "www.facebook.com", true);
});

function matchFacebookUrl(url){
    if(url.indexOf("www.facebook.com")!=-1 && url.indexOf("www.facebook.com")<10){
        return true;
    }
    return false;
}

function matchFacebookConnectUrl(url){
    if(url.indexOf("facebook")!=-1 && !matchFacebookUrl(url)) {
        return true; 
    }
    return false;
}

function matchLinkedinUrl(url){
    if(url.indexOf("www.linkedin.com")!=-1 && url.indexOf("www.linkedin.com")<10){
        return true;
    }
    return false;
}

function matchLinkedinConnectUrl(url){
    if(url.indexOf("linkedin")!=-1 && (url.indexOf("www.linkedin.com")>10 || url.indexOf("www.linkedin.com")<0)) {
        return true;
    }
    return false;
}

function getHost(url){
    var getLocation = function(href) {
        var l = document.createElement("a");
        l.href = href;
        return l;
    };
    return getLocation(url).hostname;
}

function equalArrays (array1, array2) {
    // compare lengths - can save a lot of time 
    if (array2.length != array1.length)
        return false;

    for (var i = 0, l=array2.length; i < l; i++) {
        // Check if we have nested arrays
        if (array2[i] instanceof Array && array1[i] instanceof Array) {
            // recurse into the nested arrays
            if (!array2[i].equals(array1[i]))
                return false;       
        }           
        else if (array2[i] != array1[i]) { 
            // Warning - two different object instances will never be equal: {x:20} != {x:20}
            return false;   
        }           
    }       
    return true;
}