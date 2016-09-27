function rememberConnection(username, website){
    console.log("Connection for email " + username + " on website " + website + " recieved by Ease");
    extension.storage.get('allConnections', function(res){
        if(!res) res = {};
        res[website] = username;
        console.log(res);
        extension.storage.set('allConnections', res, function(){});
    });
}

extension.runtime.onMessage('newConnectionToRandomWebsite', function(msg, sendResponse){
    rememberConnection(msg.username, msg.website);
});

var lastNavigatedWebsiteUrl = null;

chrome.tabs.onActivated.addListener(function lastNavigation(infos){
    chrome.tabs.get(infos.tabId, function(activatedTab){
        if(activatedTab.url){
            if(!matchFacebookConnectUrl(activatedTab.url)) lastNavigatedWebsiteUrl = activatedTab.url;
        } else {
            chrome.tabs.onUpdated.addListener(function tabIsReady(tabId, params, tab){
                if(tabId == infos.tabId){
                    chrome.tabs.onUpdated.removeListener(tabIsReady);
                     if(!matchFacebookConnectUrl(tab.url)) lastNavigatedWebsiteUrl = tab.url;
                }
            });
        }
    });       
});

chrome.tabs.onUpdated.addListener(function randomTabIsReady(tabId, params, tab){
    if(tab.active){
        if(!matchFacebookConnectUrl(tab.url)) lastNavigatedWebsiteUrl = tab.url;
    }
});

chrome.tabs.onUpdated.addListener(function facebookTab(tabId, params, tab){
    if(matchFacebookConnectUrl(tab.url)){
        rememberConnection("logWithFB", getHost(lastNavigatedWebsiteUrl));
    }                   
});

function matchFacebookConnectUrl(url){
    if(url.substr(0,25)=="https://www.facebook.com/" && url.indexOf("oauth") !== -1) return true;
    if(url.indexOf("facebook")!==-1 && url.substr(12,8) != "facebook") return true;
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