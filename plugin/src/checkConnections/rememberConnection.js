extension.runtime.bckgrndOnMessage('newConnectionToRandomWebsite', function(msg, senderTab, sendResponse){
    rememberConnection(msg.username, msg.password, msg.website, false);
});

var lastNavigatedWebsite = "";

function printConnections(){
    extension.storage.get('lastConnections', function(res){
        console.log(res);
    });
}

extension.tabs.onNavigation(function(url){
    if(matchFacebookConnectUrl(url) && !matchFacebookUrl(lastNavigatedWebsite)){
        rememberLogWithConnection(lastNavigatedWebsite, "www.facebook.com");
    } else if(matchLinkedinConnectUrl(url) && !matchLinkedinUrl(lastNavigatedWebsite)){
        rememberLogWithConnection(lastNavigatedWebsite, "www.linkedin.com");
    }
    //if(!matchFacebookUrl(url)){
        lastNavigatedWebsite = getHost(url);
    //}
});

function rememberConnection(username, password, website){
    extension.storage.get('lastConnections', function(res){
        if(!res) res = {};
        res[website] = {"user":username,"password":password};
        console.log("-- Connection for email " + username + " and password " + password +" on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function(){});
        rememberEveryConnections({user:username, password:password, website:website});
    });    
}


function rememberDirectLogWithConnection(website, logWithDatas){
    extension.storage.get('lastConnections', function(res){
        if(!res) res = {};
        res[website] =logWithDatas;
        console.log("-- Connection with " +logWithDatas + " on website " + website + " remembered --");
        extension.storage.set('lastConnections', res, function(){});
        logWithDatas.website = website;
        rememberEveryConnections(logWithDatas);
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
    connectionDatas.expiration = creation.getTime()+604800000; //expiration en 1 semaine
    extension.storage.get("allConnections", function(res){
        if(JSON.stringify(res)[0]=="{") res = [];
        for(var i in res){
            if(res[i].website && res[i].website == connectionDatas.website){
                if (res[i].user && res[i].user == connectionDatas.user){
                    res.splice(i, 1);
                    break;
                }
            }
        }
        console.log(connectionDatas);
        res.push(connectionDatas);
        console.log(res);
        extension.storage.set('allConnections', res, function(){});
    });
}

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