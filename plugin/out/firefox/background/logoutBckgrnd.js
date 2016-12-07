extension.runtime.bckgrndOnMessage("Logout", function (msg, easeTab, sendResponse) {
    console.log("-- Global logout --");
    extension.storage.get("visitedWebsites", function(visitedWebsites) {
        extension.runtime.tempBckgrndOnMessage("Disconnected", function (msg, easeTab2, sendResponse){
            easeTab = easeTab2;
            extension.tabs.sendMessage(easeTab, "logoutFrom", visitedWebsites, function(){});
        });
        for(var i in visitedWebsites){
            var website = visitedWebsites[i];
            if(website.siteId == undefined || website.siteSrc == undefined) website.siteId = "-1";
            logOutFrom(website, easeTab);
        }
        extension.storage.set("visitedWebsites", [], function(){});       
    });
});

function logOutFrom(website, easeTab){
            var msg = {};
            msg.detail = [];
            msg.detail.push({});
            msg.detail[0].website = website;
            msg.todo = "logout";
            msg.bigStep = 0;
            msg.actionStep = 0;
            extension.currentWindow(function(currentWindow) {
                if(msg.detail[0].website.logout.url){
                    msg.detail[0].website.home = msg.detail[0].website.logout.url;
                }
                extension.tabs.create(currentWindow, msg.detail[0].website.home, false, function(tab){
                    extension.tabs.onUpdated(tab, function (newTab) {
                        tab = newTab;
                        extension.tabs.inject(tab, ["tools/extensionLight.js","overlay/injectOverlay.js", "overlay/overlay.css"], function() {});
                    });
                    extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
                        console.log("-- Page reloaded --");
                        extension.tabs.inject(tab, ["tools/extension.js","jquery-3.1.0.js","contentScripts/actions.js", "contentScripts/logout.js"], function() {
                                    extension.tabs.sendMessage(tab, "logout", msg, function(response){
                                        console.log("-- Status : "+response.type+" --");
                                        if(response){
                                            if(response.type == "completed"){
                                                msg.actionStep = response.actionStep;
                                                if (msg.actionStep < msg.detail[0].website.logout.todo.length){
                                                    //do nothing
                                                } else {
                                                    console.log("-- Log out done for website "+ website.name +" --");
                                                    extension.tabs.onUpdatedRemoveListener(tab);
                                                    extension.tabs.onMessageRemoveListener(tab);
                                                    extension.tabs.sendMessage(easeTab, "logoutDone", {"siteId":website.siteId, "siteSrc":website.siteSrc, "status":"success" }, function (){});
                                                    setTimeout(function(){extension.tabs.close(tab, function() {});},1000);
                                                }
                                            } else {
                                                console.log("-- Log out fail --");
                                                extension.tabs.onMessageRemoveListener(tab);
                                                extension.tabs.onUpdatedRemoveListener(tab);
                                                extension.tabs.close(tab, function() {});
                                                extension.tabs.sendMessage(easeTab, "logoutDone", {"siteId":website.siteId, "siteSrc":website.siteSrc, "status":"fail" }, function (){});
                                            }
                                        }
                                    });
                                
                            });
                    });
                });
            });
}