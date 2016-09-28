function rememberConnection(username, website){
    
    console.log("Connection for email " + username + " on website " + website + " recieved by Ease");
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        res[website] = username;
        checkForNewLogWith(website, res, function(newRes){
            console.log(newRes);
            extension.storage.set('allConnections', newRes, function(){});
        });
    });    
    
}

function rememberLogWithConnection(website, logWithWebsite){
    
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        if(res[logWithWebsite]){
            res[website] = {"user":res[logWithWebsite], "logWith":logWithWebsite};
        }
        console.log("Connection with " +logWithWebsite + " on website " + website + " recieved by Ease.");
        checkForNewLogWith(website, res, function(newRes){
            console.log(newRes);
            extension.storage.set('allConnections', newRes, function(){});
        });
    });    
}

function checkForNewLogWith(website, res, callback){
    for(var site in res){
        if(res[site].logWith && !res[site].user && res[site].logWith == website){
            res[site].user = res[website];
        }
    }
    callback(res);
}

extension.runtime.onMessage('newConnectionToRandomWebsite', function(msg, sendResponse){
    rememberConnection(msg.username, msg.website, false);
});

var lastNavigatedWebsite = null;

chrome.windows.onFocusChanged.addListener(function (windowId){
    if(windowId != -1) chrome.windows.get(windowId, {"populate":true}, function(window){
        for(var i in window.tabs){
            if(window.tabs[i].active && window.tabs[i].url){
                lastNavigatedWebsite = getHost(window.tabs[i].url);
                console.log("Last website ::: " + lastNavigatedWebsite);
            }
        }
    });
});

chrome.tabs.onActivated.addListener(function lastNavigation(infos){
    chrome.tabs.get(infos.tabId, function(activatedTab){
        if(activatedTab.url){
            lastNavigatedWebsite = getHost(activatedTab.url);
            console.log("Last website ::: " + lastNavigatedWebsite);
        } else {
            chrome.tabs.onUpdated.addListener(function tabIsReady(tabId, params, tab){
                if(tabId == infos.tabId){
                    chrome.tabs.onUpdated.removeListener(tabIsReady);
                    lastNavigatedWebsite = getHost(tab.url);
                    console.log("Last website ::: " + lastNavigatedWebsite);
                }
            });
        }
    });       
});

chrome.tabs.onUpdated.addListener(function (tabId, params, tab){
    if(matchFacebookConnectUrl(tab.url) && !matchFacebookConnectUrl(lastNavigatedWebsite) && getHost(tab.url)==lastNavigatedWebsite){
        console.log("Connexion with url " + tab.url);
        rememberLogWithConnection(lastNavigatedWebsite, "www.facebook.com");
    } else if(matchLinkedinConnectUrl(tab.url) && !matchLinkedinConnectUrl(lastNavigatedWebsite) && getHost(tab.url)==lastNavigatedWebsite){
        console.log("Connexion with url " + tab.url);
        rememberLogWithConnection(lastNavigatedWebsite, "www.linkedin.com");
    } else if(tab.active) {
        lastNavigatedWebsite = getHost(tab.url);
        console.log("Last website ::: " + lastNavigatedWebsite);
    }                  
});

function matchFacebookConnectUrl(url){
    if(url.indexOf("facebook")!==-1 && url.substr(12,8) != "facebook") {
        return "true";
    }
    return false;
}

function matchLinkedinConnectUrl(url){
    if(url.indexOf("linkedin")!==-1 && url.substr(12,8) != "linkedin") {
        return "true";
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