extension.runtime.onMessage("Logout", function (msg, sendResponse) {
    extension.storage.get("visitedWebsites", function(visitedWebsites) {
        visitedWebsites = visitedWebsites["visitedWebsites"];
        for(var i in visitedWebsites){logOutFrom(visitedWebsites[i], sendResponse);}
            extension.storage.set("visitedWebsites", []);
                                            
    });
});

function logOutFrom(website, sendResponse){
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
                        extension.tabs.injectScript(tab, "checkForReload.js", function() {});
                    });
                    extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
                        extension.tabs.injectScript(tab, "jquery-3.1.0.js", function() {
                            extension.tabs.injectScript(tab, "extension.js", function() {
                                extension.tabs.injectScript(tab, "actions.js", function() {
                                    extension.tabs.injectScript(tab, "logout.js", function(){
                                    extension.tabs.sendMessage(tab, "logout", msg, function(response){
                                        console.log(response);
                                        if(response){
                                            if(response.type == "completed"){
                                                msg.actionStep = response.actionStep;
                                                if (msg.actionStep < msg.detail[0].website.logout.todo.length){
                                                    //do nothing
                                                } else {
                                                    console.log("Logout done");
                                                    extension.tabs.onUpdatedRemoveListener(tab);
                                                    extension.tabs.onMessageRemoveListener(tab);
                                                    setTimeout(function() {
                                                        extension.tabs.close(tab, function() {});
                                                    }, 1000);
                                                    sendResponse("Good");
                                                }
                                            } else {
                                                extension.tabs.onMessageRemoveListener(tab);
                                                extension.tabs.onUpdatedRemoveListener(tab);
                                                setTimeout(function() {
                                                    extension.tabs.close(tab, function() {});
                                                }, 1000);
                                                sendResponse(response);
                                            }
                                        }
                                    });
                                    });
                                });
                            });
                        });
                    });
                });
            });
}