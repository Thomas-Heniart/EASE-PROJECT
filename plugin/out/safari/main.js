
function rememberWebsite(visitedWebsites, website){
    console.log(visitedWebsites);
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
                extension.tabs.injectScript(tab, "jquery-3.1.0.js", function() {
                    extension.tabs.injectScript(tab, "extension.js", function() {
                        extension.tabs.injectScript(tab, "autofill.js", function() {
                          extension.storage.get("visitedWebsites", function(visitedWebsites) {
                            msg.visitedWebsites = visitedWebsites["visitedWebsites"];
                            extension.tabs.sendMessage(tab, "goooo", msg, function(response){
                              console.log(response);
                              if (response){
                                if (response.type == "completed") {
                                    msg.todo = response.todo;
                                    msg.bigStep = response.bigStep;
                                    msg.actionStep = response.actionStep;
                                    msg.detail[msg.bigStep].website.lastLogin = response.detail[msg.bigStep].website.lastLogin;
                                    if (msg.actionStep < msg.detail[msg.bigStep].website[msg.todo].todo.length){
                                         //do nothing
                                    } else {
                                        if (msg.todo == "logout"){
                                            msg.todo = "connect";
                                            msg.actionStep = 0;
                                        } else if (msg.todo == "connect"){
                                            msg.todo = "checkAlreadyLogged";
                                            msg.actionStep = 0;
                                            rememberWebsite(msg.visitedWebsites, msg.detail[msg.bigStep].website);
                                            msg.bigStep++;
                                            if (msg.bigStep < msg.detail.length){
                                                extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function() {});
                                            } else {
                                                console.log("Connecting done");
                                                extension.tabs.onUpdatedRemoveListener(tab);
                                                extension.tabs.onMessageRemoveListener(tab);
                                                sendResponse("Good");
                                            }
                                        }
                                    }
                                } else {
                                    extension.tabs.onMessageRemoveListener(tab);
                                    extension.tabs.onUpdatedRemoveListener(tab);
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
});