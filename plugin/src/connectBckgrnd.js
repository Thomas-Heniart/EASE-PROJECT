function endConnection(currentWindow, tab, msg, sendResponse){
    console.log(msg.result);
    extension.tabs.sendMessage(tab, "rmOverlay", msg, function(response){});
    /*if(msg.result == "Success"){
        extension.notifications.print("Connection to "+ msg.detail[msg.detail.length-1].website.name +" : DONE ;)", msg.detail[msg.detail.length-1].website.folder, 2000);
    } else {
        extension.notifications.print("Connection to "+ msg.detail[msg.detail.length-1].website.name+" : FAIL :(", msg.detail[msg.detail.length-1].website.folder, 2000);
    }*/
}

extension.runtime.bckgrndOnMessage("NewConnection", function (msg, sendResponse) {
    msg.todo = "checkAlreadyLogged";
    msg.bigStep = 0;
    msg.actionStep = 0;
    extension.currentWindow(function(currentWindow) {
        extension.tabs.create(currentWindow, msg.detail[0].website.home, msg.highlight, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                console.log("updated");
                extension.tabs.inject(tab, ["overlay.css", "checkForReload.js"], function(){});
            });
            extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
                console.log("reloaded");
                    extension.tabs.inject(tab, ["extension.js","jquery-3.1.0.js","actions.js", "connect.js"], function(){
                          extension.storage.get("visitedWebsites", function(visitedWebsites) {
                            extension.storage.get("allConnections", function(allConnections) {
                            msg.visitedWebsites = visitedWebsites;
                            msg.allConnections = allConnections;
                                console.log(allConnections);
                            extension.tabs.sendMessage(tab, "goooo", msg, function(response){
                                console.log(response);
                              if (response){
                                if (response.type == "completed") {
                                    msg.todo = response.todo;
                                    msg.bigStep = response.bigStep;
                                    msg.actionStep = response.actionStep;
                                    msg.detail[msg.bigStep].website.lastLogin = response.detail[msg.bigStep].website.lastLogin;
                                    if (msg.todo != "end" && msg.actionStep < msg.detail[msg.bigStep].website[msg.todo].todo.length){
                                        //do nothing
                                    } else {
                                        if (msg.todo == "logout"){
                                            if (typeof msg.detail[msg.bigStep].logWith === "undefined") {
                                                msg.todo = "connect";
                                            } else {
                                                msg.todo = msg.detail[msg.bigStep].logWith;
                                            }
                                            msg.actionStep = 0;
                                        /*} else if(msg.todo == "end"){
                                            rememberWebsite(msg.detail[msg.bigStep].website);
                                            msg.todo = "checkAlreadyLogged";
                                            msg.actionStep = 0;
                                            msg.bigStep++;
                                            if (msg.bigStep < msg.detail.length){
                                                setTimeout(function () {
                                                    extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function() {});
                                                }, 1000);
                                            } else {
                                                msg.result = "Success";
                                                extension.tabs.onUpdatedRemoveListener(tab);
                                                extension.tabs.onMessageRemoveListener(tab);
                                                endConnection(currentWindow, tab, msg, sendResponse);
                                            }
                                        } else {
                                            msg.todo = "end";
                                            console.log("Check already log to end connection");
                                        }*/
                                        } else {
                                            rememberWebsite(msg.detail[msg.bigStep].website);
                                            msg.todo = "checkAlreadyLogged";
                                            msg.actionStep = 0;
                                            msg.bigStep++;
                                            if (msg.bigStep < msg.detail.length){
                                                setTimeout(function () {
                                                    extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function() {});
                                                }, 1000);
                                            } else {
                                                msg.result = "Success";
                                                endConnection(currentWindow, tab, msg, sendResponse);
                                                extension.tabs.onUpdatedRemoveListener(tab);
                                                extension.tabs.onMessageRemoveListener(tab);
                                                endConnection(currentWindow, tab, msg, sendResponse);
                                            }
                                        }
                                    }
                                } else if (response != undefined){
                                    msg.result = "Fail";
                                    endConnection(currentWindow, tab, msg, sendResponse);
                                    extension.tabs.onUpdatedRemoveListener(tab);
                                    extension.tabs.onMessageRemoveListener(tab);
                                    
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


