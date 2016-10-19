extension.runtime.bckgrndOnMessage('newConnectionToRandomWebsite', function(msg, senderTab, sendResponse){
    rememberConnection(msg.username, msg.password, msg.website, false);
});

var lastNavigatedWebsite = "";

function printConnections(){
    extension.storage.get('allConnections', function(res){
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
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        res[website] = {"user":username,"password":password};
        console.log("-- Connection for email " + username + " and password " + password +" on website " + website + " remembered --");
        extension.storage.set('allConnections', res, function(){
            console.log(res);
        });
    });    
}


function rememberDirectLogWithConnection(website, logWithDatas){
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        res[website] =logWithDatas;
        console.log("-- Connection with " +logWithDatas + " on website " + website + " remembered --");
        extension.storage.set('allConnections', res, function(){
        });
    });    
}

function rememberLogWithConnection(website, logWithWebsite){
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        if(res[logWithWebsite]){
            if(res[logWithWebsite].user)
                res[website] = {"user":res[logWithWebsite].user, "logWith":logWithWebsite};
            else 
                res[website] = {"user":res[logWithWebsite], "logWith":logWithWebsite};
        }
        console.log("-- Connection with " +logWithWebsite + " on website " + website + " remembered --");
        extension.storage.set('allConnections', res, function(){
        });
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