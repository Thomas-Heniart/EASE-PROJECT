extension.runtime.bckgrndOnMessage("TestConnections", function(msg, senderTab, sendResponse){
    extension.storage.set("WebsitesToTest", [] ,function(){
        var j = 0;
        for(var i in msg){
            var coInfos = {detail:msg.detail[i]};
            console.log(coInfos.detail[coInfos.detail.length-1].website.name + " : Initialize test");
            connectTo(coInfos);
        }
    });
});

function connectTo(msg){
    msg.todo = "checkAlreadyLogged";
    msg.bigStep = 0;
    msg.actionStep = 0;
    msg.waitreload= false;
    extension.currentWindow(function(currentWindow) {
        extension.tabs.create(currentWindow, msg.detail[0].website.home, false, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                extension.tabs.inject(tab, ["tools/extensionLight.js","overlay/overlay.css", "overlay/injectOverlay.js"], function(){});
            });
            extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
                extension.tabs.inject(tab, ["tools/extension.js","jquery-3.1.0.js","contentScripts/actions.js"], function(){
                    extension.tabs.sendMessage(tab, "goooo", msg, function(response){
                        if (response){
                            if (response.type == "completed") {
                                msg.waitreload = response.waitreload;
                                msg.todo = response.todo;
                                msg.bigStep = response.bigStep;
                                msg.actionStep = response.actionStep;
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
                                    } else if (msg.todo=="nextBigStep"){
                                        msg.todo = "checkAlreadyLogged";
                                        extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function(){});
                                    } else {
                                        msg.actionStep = 0;
                                        msg.bigStep++;
                                        if (msg.bigStep < msg.detail.length){
                                            if(msg.waitreload){
                                                msg.todo="nextBigStep";
                                            } else {
                                                msg.todo = "checkAlreadyLogged";
                                                extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function(){});
                                            }
                                        } else {
                                            msg.todo = "checkAlreadyLogged";
                                            msg.result = "Success";
                                            setTimeout(function(){
                                                logoutFrom(msg, tab);
                                            },2000);
                                            extension.tabs.onUpdatedRemoveListener(tab);
                                            extension.tabs.onMessageRemoveListener(tab);
                                        }
                                    }
                                }
                            } else if (response != undefined){
                                printConsole(false, "connection", msg);
                                msg.result = "Fail";
                                extension.tabs.close(tab, function(){});
                                extension.tabs.onUpdatedRemoveListener(tab);
                                extension.tabs.onMessageRemoveListener(tab);
                            }
                        }
                    });
                });
            });
        });
    });   
}

function logoutFrom(msg, oldTab) {
    msg.todo = "checkAlreadyLogged";
    msg.bigStep = msg.detail.length-1;
    msg.actionStep = 0;
    msg.waitreload= false;
    extension.currentWindow(function(currentWindow) {
         extension.tabs.update(oldTab, msg.detail[msg.bigStep].website.home, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                extension.tabs.inject(tab, ["tools/extensionLight.js","overlay/overlay.css", "overlay/injectOverlay.js"], function(){});
            });
            extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
                    extension.tabs.inject(tab, ["tools/extension.js","jquery-3.1.0.js","contentScripts/actions.js"], function(){
                            extension.tabs.sendMessage(tab, "logout", msg, function(response){
                                if(response){
                                    if(response.type == "completed"){
                                        msg.actionStep = response.actionStep;
                                        if (msg.actionStep < msg.detail[msg.bigStep].website.logout.todo.length){
                                            //do nothing
                                        } else {
                                            extension.tabs.onUpdatedRemoveListener(tab);
                                            extension.tabs.onMessageRemoveListener(tab);
                                            reconnect(msg, tab);
                                        }
                                    } else if(response.type == "connection fail") {
                                        printConsole(false, "connection", msg);
                                        extension.tabs.onMessageRemoveListener(tab);
                                        extension.tabs.onUpdatedRemoveListener(tab);
                                        extension.tabs.close(tab, function() {});
                                    } else if (response != undefined){
                                        printConsole(false, "logout", msg);
                                        extension.tabs.onMessageRemoveListener(tab);
                                        extension.tabs.onUpdatedRemoveListener(tab);
                                        extension.tabs.close(tab, function() {});
                                    }
                                }
                            });
                        });
                });
            });
        });   
}

function reconnect(msg, tab){
    msg.todo = "checkAlreadyLogged";
    msg.bigStep = 0;
    msg.actionStep = 0;
    msg.waitreload= false;
    extension.tabs.onUpdated(tab, function (newTab) {
        tab = newTab;
        extension.tabs.inject(tab, ["tools/extensionLight.js","overlay/overlay.css", "overlay/injectOverlay.js"], function(){});
    });
    extension.tabs.onMessage(tab, "reloaded", function (event, sendResponse1) {
        extension.tabs.inject(tab, ["tools/extension.js","jquery-3.1.0.js","contentScripts/actions.js"], function(){
                extension.tabs.sendMessage(tab, "reconnect", msg, function(response){
                        if (response){
                            if (response.type == "completed") {
                                msg.waitreload = response.waitreload;
                                msg.todo = response.todo;
                                msg.bigStep = response.bigStep;
                                msg.actionStep = response.actionStep;
                                if (msg.todo != "end" && msg.todo!="nextBigStep" && msg.actionStep < msg.detail[msg.bigStep].website[msg.todo].todo.length){
                                    //do nothing
                                } else {
                                    if (msg.todo=="nextBigStep"){
                                        msg.todo = "checkAlreadyLogged";
                                        extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function(){});
                                    } else {
                                        msg.actionStep = 0;
                                        msg.bigStep++;
                                        if (msg.bigStep < msg.detail.length){
                                            if(msg.waitreload){
                                                msg.todo="nextBigStep";
                                            } else {
                                                msg.todo = "checkAlreadyLogged";
                                                extension.tabs.update(tab, msg.detail[msg.bigStep].website.home, function(){});
                                            }
                                        } else {
                                            msg.todo = "checkAlreadyLogged";
                                            msg.result = "Success";
                                           setTimeout(function(){
                                                lastcheck(msg, tab);
                                            }, 2000);
                                            extension.tabs.onUpdatedRemoveListener(tab);
                                            extension.tabs.onMessageRemoveListener(tab);
                                        }
                                    }
                                }
                            } else if(response.type == "logout fail") {
                                        printConsole(false, "logout", msg);
                                        extension.tabs.onMessageRemoveListener(tab);
                                        extension.tabs.onUpdatedRemoveListener(tab);
                                        extension.tabs.close(tab, function() {});
                            } else if (response != undefined){
                                        printConsole(false, "reconnection", msg);
                                        extension.tabs.onMessageRemoveListener(tab);
                                        extension.tabs.onUpdatedRemoveListener(tab);
                                        extension.tabs.close(tab, function() {});
                            }
                        } 
                            });
                        });
                });  
}

function lastCheck(msg, tab){
    msg.todo = "checkAlreadyLogged";
    msg.bigStep = 0;
    msg.actionStep = 0;
    msg.waitreload= false;
    extension.tabs.inject(tab, ["tools/extensionLight.js","overlay/overlay.css", "overlay/injectOverlay.js", "tools/extension.js","jquery-3.1.0.js","contentScripts/actions.js"], function(){
        extension.tabs.sendMessage(tab, "lastcheck", msg, function(response){
                if(response){
                    if(response.type == "completed"){
                        extension.tabs.onUpdatedRemoveListener(tab);
                        extension.tabs.onMessageRemoveListener(tab);
                        printConsole(true, "", msg);
                        extension.tabs.close(tab, function() {});
                    } else if(response.type == "reconnection fail") {
                        printConsole(false, "reconnection", msg);
                        extension.tabs.onMessageRemoveListener(tab);
                        extension.tabs.onUpdatedRemoveListener(tab);
                        extension.tabs.close(tab, function() {});
                    } else if (response != undefined){
                        printConsole(false, "unknown error", msg);
                        extension.tabs.onMessageRemoveListener(tab);
                        extension.tabs.onUpdatedRemoveListener(tab);
                        extension.tabs.close(tab, function() {});
                    }
                }
        });
    });
  
}

function printConsole(success, type, msg){
    var website = msg.detail[msg.detail.length-1].website.name;
    if (typeof msg.detail[msg.detail.length-1].logWith === "undefined") {
        var connectionType = "classic connection";
    } else {
        var connectionType = "connection with website "+ msg.detail[msg.detail.length-1].logWith;
    }
    if(success){
        console.log(website + " : SUCCESS connection, logout and reconnection for "+connectionType);
    } else {
        if(type != "logout")
            console.log(website + " : FAIL "+type+" for "+connectionType);
        else
            console.log(website + " : FAIL "+type);
    }
}
