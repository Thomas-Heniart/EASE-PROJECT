function rememberWebsite(website){
    if (website.lastLogin == "" || !website.lastLogin)
        return;
    extension.storage.get("visitedWebsites", function(visitedWebsites) {
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
        if (typeof visitedWebsites === "undefined" || visitedWebsites == null || visitedWebsites == undefined || visitedWebsites.length == 0 || visitedWebsites == {})
            visitedWebsites = [];
        visitedWebsites.push(website);
        extension.storage.set("visitedWebsites", visitedWebsites);
        
    });
    if(!website.loginUrl) website.loginUrl = website.home;
    if(website.lastLogin.logWith){
        rememberDirectLogWithConnection(getHost(website.loginUrl), website.lastLogin);
    } else {
        rememberConnection(website.lastLogin.user, website.lastLogin.password, getHost(website.loginUrl));
    }  
}

function endConnection(currentWindow, tab, msg, sendResponse){
    extension.tabs.sendMessage(tab, "rmOverlay", msg, function(response){});
    /*if(msg.result == "Success"){
        extension.notifications.print("Connection to "+ msg.detail[msg.detail.length-1].website.name +" : DONE ;)", msg.detail[msg.detail.length-1].website.folder, 2000);
    } else {
        extension.notifications.print("Connection to "+ msg.detail[msg.detail.length-1].website.name+" : FAIL :(", msg.detail[msg.detail.length-1].website.folder, 2000);
    }*/
}

extension.runtime.bckgrndOnMessage("NewConnection", function (msg, senderTab, sendResponse) {
    msg.todo = "checkAlreadyLogged";
    msg.bigStep = 0;
    msg.actionStep = 0;
    msg.waitreload= false;
    extension.currentWindow(function(currentWindow) {
        extension.tabs.createOrUpdate(currentWindow, senderTab, msg.detail[0].website.home, msg.highlight, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                extension.tabs.inject(tab, ["tools/extensionLight.js","overlay/overlay.css", "overlay/injectOverlay.js"], function(){});
            });
            extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
                console.log("-- Page reloaded --");
                    extension.tabs.inject(tab, ["tools/extension.js","jquery-3.1.0.js","contentScripts/actions.js", "contentScripts/connect.js"], function(){
                        extension.storage.get("visitedWebsites", function(visitedWebsites) {
                            extension.storage.get("allConnections", function(allConnections) {
                            msg.visitedWebsites = visitedWebsites;
                            msg.allConnections = allConnections;
                            extension.tabs.sendMessage(tab, "goooo", msg, function(response){
                              if (response){
                                console.log("-- Status : "+response.type+" --");
                                if (response.type == "completed") {
                                    msg.waitreload = response.waitreload;
                                    msg.todo = response.todo;
                                    msg.bigStep = response.bigStep;
                                    msg.actionStep = response.actionStep;
                                    msg.detail[msg.bigStep].website.lastLogin = response.detail[msg.bigStep].website.lastLogin;
                                    if (msg.todo != "end" && msg.todo!="nextBigStep" && msg.actionStep < msg.detail[msg.bigStep].website[msg.todo].todo.length){
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
                                        } else if (msg.todo=="nextBigStep"){
                                            msg.todo = "checkAlreadyLogged";
                                            extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function() {});
                                        } else {
                                            rememberWebsite(msg.detail[msg.bigStep].website);
                                            msg.actionStep = 0;
                                            msg.bigStep++;
                                            if (msg.bigStep < msg.detail.length){
                                                if(msg.waitreload){
                                                    msg.todo="nextBigStep";
                                                    console.log("-- Wait for nextBigStep --");
                                                } else {
                                                    msg.todo = "checkAlreadyLogged";
                                                    extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function() {});
                                                }
                                            } else {
                                                msg.todo = "checkAlreadyLogged";
                                                msg.result = "Success";
                                                endConnection(currentWindow, tab, msg, sendResponse);
                                                extension.tabs.onUpdatedRemoveListener(tab);
                                                extension.tabs.onMessageRemoveListener(tab);
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


