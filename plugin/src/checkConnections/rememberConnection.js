extension.runtime.bckgrndOnMessage('newConnectionToRandomWebsite', function(msg, senderTab, sendResponse){
    rememberConnection(msg.username, msg.website, false);
});

var lastNavigatedWebsite = "";

function printConnections(){
    extension.storage.get('allConnections', function(res){
        console.log(res);
    });
}

extension.tabs.onNavigation(function(url){
    if(matchFacebookConnectUrl(url) && !matchFacebookConnectUrl(lastNavigatedWebsite)){
        rememberLogWithConnection(lastNavigatedWebsite, "www.facebook.com");
    } else if(matchLinkedinConnectUrl(url) && !matchLinkedinConnectUrl(lastNavigatedWebsite)){
        rememberLogWithConnection(lastNavigatedWebsite, "www.linkedin.com");
    }
    lastNavigatedWebsite = getHost(url);
    console.log("-- Last navigated website : "+lastNavigatedWebsite+" --");
});

function rememberConnection(username, website){
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        res[website] = username;
        console.log("-- Connection for email " + username + " on website " + website + " remembered --");
        extension.storage.set('allConnections', res, function(){});
    });    
}


function rememberDirectLogWithConnection(website, logWithDatas){
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        res[website] =logWithDatas;
        console.log("-- Connection with " +logWithDatas + " on website " + website + " remembered --");
        extension.storage.set('allConnections', res, function(){});
    });    
}

function rememberLogWithConnection(website, logWithWebsite){
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        if(res[logWithWebsite]){
            res[website] = {"user":res[logWithWebsite], "logWith":logWithWebsite};
        }
        console.log("-- Connection with " +logWithWebsite + " on website " + website + " remembered --");
        extension.storage.set('allConnections', res, function(){});
    });    
}

function matchFacebookConnectUrl(url){
    console.log(url);
    if(url.indexOf("facebook")!=-1 && (url.indexOf("www.facebook.com")>10 || url.indexOf("www.facebook.com")<0)) {
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