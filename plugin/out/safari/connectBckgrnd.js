
function rememberWebsite(website){
    extension.storage.get("visitedWebsites", function(visitedWebsites) {
        visitedWebsites = visitedWebsites["visitedWebsites"];
        for (var i in visitedWebsites){
            if (visitedWebsites[i].name == website.name){
                if (visitedWebsites[i].lastLogin == website.lastLogin){
                    return ;
                }
                else {
                    visitedWebsites.splice(i, 1);
                    break;
                }
            }
        }
        if (typeof visitedWebsites === "undefined")
            visitedWebsites = [];
        visitedWebsites.push(website);
        extension.storage.set("visitedWebsites", visitedWebsites);
        
    });
  
}

extension.runtime.onMessage("NewConnection", function (msg, sendResponse) {
    msg.todo = "checkAlreadyLogged";
    msg.bigStep = 0;
    msg.actionStep = 0;
    extension.currentWindow(function(currentWindow) {
        extension.tabs.create(currentWindow, msg.detail[0].website.home, true, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                extension.tabs.injectScript(tab, "checkForReload.js", function() {
                
                });
            });
            extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
                console.log("reloaded");
                extension.tabs.injectScript(tab, "jquery-3.1.0.js", function() {
                    extension.tabs.injectScript(tab, "extension.js", function() {
                        extension.tabs.injectScript(tab, "actions.js", function() {
                        extension.tabs.injectScript(tab, "connect.js", function() {
                          extension.storage.get("visitedWebsites", function(visitedWebsites) {
                            msg.visitedWebsites = visitedWebsites["visitedWebsites"];
                            extension.tabs.sendMessage(tab, "goooo", msg, function(response){
                                
                              if (response){
                                  console.log(response.type);
                                if (response.type == "completed") {
                                    msg.todo = response.todo;
                                    msg.bigStep = response.bigStep;
                                    msg.actionStep = response.actionStep;
                                    msg.detail[msg.bigStep].website.lastLogin = response.detail[msg.bigStep].website.lastLogin;
                                    console.log("action step number : " + msg.actionStep + " / length of todo : "+  msg.detail[msg.bigStep].website[msg.todo].todo.length);
                                    if (msg.actionStep < msg.detail[msg.bigStep].website[msg.todo].todo.length){
                                         console.log("wait");
                                    } else {
                                        console.log(msg.todo)
                                        if (msg.todo == "logout"){
                                            if (typeof msg.detail[msg.bigStep].logWith === "undefined") {
                                                msg.todo = "connect";
                                            } else {
                                                msg.todo = msg.detail[msg.bigStep].logWith;
                                            }
                                            msg.actionStep = 0;
                                        } else {
                                            if (msg.todo == "connect")
                                                rememberWebsite(msg.detail[msg.bigStep].website);
                                            msg.todo = "checkAlreadyLogged";
                                            msg.actionStep = 0;
                                            msg.bigStep++;
                                            if (msg.bigStep < msg.detail.length){
                                                setTimeout(function () {
                                                    extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function() {});
                                                }, 1000);
                                            } else {
                                                extension.tabs.onUpdatedRemoveListener(tab);
                                                extension.tabs.onMessageRemoveListener(tab);
                                                sendResponse("Good");
                                            }
                                        }
                                    }
                                }
                            } else {console.log("no response");}
                              });
                            });
                        });
                        });
                    });
	              });
            });
        });   
    });
});


