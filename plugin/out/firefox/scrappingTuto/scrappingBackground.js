extension.runtime.bckgrndOnMessage("GetChromeUser", function (msg, senderTab, sendResponse) {
    chrome.identity.getProfileUserInfo(function(userInfo){
        sendResponse(userInfo.email);
    });
})

function testOverlay(){
    extension.currentWindow(function(window){
        extension.tabs.create(window, "http://www.linkedin.com", true, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                extension.tabs.sendMessage(tab, "scrapOverlay", "Facebook", function(){});
            });
        });
    });
}


extension.runtime.bckgrndOnMessage("ScrapFacebook", function (msg, senderTab, sendResponse) {
    console.log(msg);
    startScrapFacebook(msg.login, msg.password, function(success, response){
        if(success && response.length==0){
            success=false;
            response = "You did not connect to any website with this Facebook account. Try it with another account."
        }
        extension.tabs.focus(senderTab, function(){});
        sendResponse({"success":success, "msg":response});
    });
});

extension.runtime.bckgrndOnMessage("ScrapLinkedin", function (msg, senderTab, sendResponse) {
    startScrapLinkedin(msg.login, msg.password, function(success, response){
        if(success && response.length==0){
            success=false;
            response = "You did not connect to any website with this Linkedin account. Try it with another account."
        }
        extension.tabs.focus(senderTab, function(){});
        sendResponse({"success":success, "msg":response});
    });
});

extension.runtime.bckgrndOnMessage("ScrapChrome", function (msg, senderTab, sendResponse) {
    startScrapChrome(msg.login, msg.password, function(success, response){
        if(success && response.length==0){
            success=false;
            response = "There is no password saved on this chrome account. Try it with another account."
        }
        extension.tabs.focus(senderTab, function(){});
        sendResponse({"success":success, "msg":response});
    });
});


function startScrapFacebook(login, password, finalCallback){
    extension.currentWindow(function(window){
        extension.tabs.create(window, "https://www.facebook.com", false, function(tab){
            extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                extension.tabs.sendMessage(tab, "scrapOverlay", "Facebook", function(){});
            });
            extension.tabs.onClosed(tab, function () {
                finalCallback(false,"It seems that you closed the tab. Please try again.");
                extension.tabs.onClosedRemoveListener(tab);
                extension.tabs.onMessageRemoveListener(tab);
                extension.tabs.onUpdatedRemoveListener(tab);
            });
            extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                extension.tabs.onMessageRemoveListener(tab);
                extension.tabs.sendMessage(tab, "checkFbCo", {}, function(response){
                    if(response==true){
                        logoutFromFb(tab, function(tab){
                            connectToFb(tab,login, password, finalCallback);
                        });
                    } else {
                        connectToFb(tab, login, password,finalCallback);
                    }
                });
            });
        });
    });
}

function logoutFromFb(tab, callback){
    extension.tabs.sendMessage(tab, "logoutFromFb", {}, function(response2){
        extension.tabs.update(tab, "https://www.facebook.com", function(tab){
            extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                extension.tabs.onMessageRemoveListener(tab);
                callback(tab);
            });
        });
    });
}

function connectToFb(tab, login, pass, callback){
    extension.tabs.sendMessage(tab, "connectToFb", {login:login, pass:pass}, function(response){
        extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
            extension.tabs.onMessageRemoveListener(tab);
            extension.tabs.sendMessage(tab, "checkFbCo", {}, function(response){
                if(response == false){
                    callback(false,"Wrong login or password. Please try again.");
                    extension.tabs.onClosedRemoveListener(tab);
                    extension.tabs.onUpdatedRemoveListener(tab);
                    setTimeout(function(){
                        extension.tabs.close(tab);
                    }, 500);
                } else {
                    scrapFb(tab, callback);
                }
            });
        });
    });
}

function scrapFb(tab, callback){
    extension.tabs.update(tab, "https://www.facebook.com/settings?tab=applications", function(tab){
        extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
            extension.tabs.onMessageRemoveListener(tab);
            extension.tabs.sendMessage(tab, "scrapFb", {}, function(response){
                callback(true,response);
                extension.tabs.onClosedRemoveListener(tab);
                extension.tabs.onUpdatedRemoveListener(tab);
                setTimeout(function(){
                    extension.tabs.close(tab);
                }, 500);
            });
        });
    });
}

////////////////////////////////////////////////////////////////////////////////////////////////////

function startScrapChrome(login, password, finalCallback){
    extension.currentWindow(function(window){
        extension.tabs.create(window, "https://accounts.google.com/Logout", false, function(tab){
             extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                 extension.tabs.sendMessage(tab, "scrapOverlay", "Chrome", function(){});
            });
            extension.tabs.onClosed(tab, function () {
                finalCallback(false,"It seems that you closed the tab. Please try again.");
                extension.tabs.onClosedRemoveListener(tab);
                extension.tabs.onMessageRemoveListener(tab);
                extension.tabs.onUpdatedRemoveListener(tab);
            });
            extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                extension.tabs.onMessageRemoveListener(tab);
                extension.tabs.update(tab, "https://accounts.google.com/ServiceLogin?sacu=1#identifier", function(tab){
                    extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                        extension.tabs.onMessageRemoveListener(tab);
                        extension.tabs.sendMessage(tab, "connectToChrome", {login:login, pass:password}, function(response){
                            if(response==false){
                                finalCallback(false,"Wrong login or password. Please try again");
                                extension.tabs.onClosedRemoveListener(tab);
                                extension.tabs.onUpdatedRemoveListener(tab);
                                setTimeout(function(){
                                    extension.tabs.close(tab);
                                }, 500);
                            } else {
                                extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                                    extension.tabs.onMessageRemoveListener(tab);
                                    extension.tabs.sendMessage(tab, "checkChromeCo", {}, function(isConnected){
                                        if(!isConnected){
                                            finalCallback(false,"Wrong login or password. Please try again");
                                            extension.tabs.onClosedRemoveListener(tab);
                                            extension.tabs.onUpdatedRemoveListener(tab);
                                            setTimeout(function(){
                                                extension.tabs.close(tab);
                                            }, 500);
                                        } else {
                                            extension.tabs.update(tab, "https://passwords.google.com/", function(tab){
                                                extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                                                    extension.tabs.onMessageRemoveListener(tab);
                                                    if(tab.url.indexOf("https://myaccount.google.com/")!=-1){
                                                        extension.tabs.update(tab, "https://passwords.google.com/", function(tab){
                                                            extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                                                                extension.tabs.onMessageRemoveListener(tab);
                                                                next();
                                                            });
                                                        });
                                                    } else {
                                                        next();
                                                    }
                                                    function next(){
                                                        extension.tabs.sendMessage(tab, "typePasswordChrome", {pass:password}, function(response){
                                                            extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                                                            extension.tabs.onMessageRemoveListener(tab);
                                                                extension.tabs.sendMessage(tab, "scrapChrome", {}, function(response){
                                                                    encryptAllPasswords(response, function(finalRes){
                                                                        extension.tabs.onClosedRemoveListener(tab);
                                                                        extension.tabs.onUpdatedRemoveListener(tab);
                                                                        setTimeout(function(){
                                                                            extension.tabs.close(tab);
                                                                        }, 500);
                                                                        finalCallback(true,finalRes);
                                                                    });
                                                                });
                                                            });
                                                        });
                                                    }
                                                });
                                            });
                                        }
                                    });
                                });
                            }
                        });
                    });
                });
            });
        });
    });
}

////////////////////////////////////////////////////////////////////////////////

function startScrapLinkedin(login, password, finalCallback){
    extension.currentWindow(function(window){
        extension.tabs.create(window, "https://www.linkedin.com/psettings/third-party-applications", false, function(tab){
             extension.tabs.onUpdated(tab, function (newTab) {
                tab = newTab;
                extension.tabs.sendMessage(tab, "scrapOverlay", "Linkedin", function(){});
            });
            extension.tabs.onClosed(tab, function () {
                finalCallback(false,"It seems that you closed the tab. Please try again.");
                extension.tabs.onClosedRemoveListener(tab);
                extension.tabs.onMessageRemoveListener(tab);
                extension.tabs.onUpdatedRemoveListener(tab);
            });
            extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                extension.tabs.onMessageRemoveListener(tab);
                extension.tabs.sendMessage(tab, "checkLnkdnCo", {}, function(response){
                    if(response==true){
                        logoutFromLnkdn(tab, function(tab){
                            connectToLnkdn(tab,login, password, finalCallback);
                        });
                    } else {
                        connectToLnkdn(tab, login, password,finalCallback);
                    }
                });
            });
        });
    });
}

function logoutFromLnkdn(tab, callback){
    extension.tabs.sendMessage(tab, "logoutFromLnkdn", {}, function(response2){
        extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
            extension.tabs.onMessageRemoveListener(tab);
            extension.tabs.update(tab, "https://www.linkedin.com/psettings/third-party-applications", function(tab){
                extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
                    extension.tabs.onMessageRemoveListener(tab);
                        callback(tab);
                });
            });
        });
    });
}

function connectToLnkdn(tab, login, pass, callback){
    extension.tabs.sendMessage(tab, "connectToLnkdn", {login:login, pass:pass}, function(response){
        extension.tabs.onMessage(tab, "scrapReloaded", function (event, sendResponse1) {
            extension.tabs.onMessageRemoveListener(tab);
            extension.tabs.sendMessage(tab, "checkLnkdnCo", {}, function(response){
                if(response == false){
                    callback(false,"Wrong login or password. Please try again");
                    extension.tabs.onClosedRemoveListener(tab);
                    extension.tabs.onUpdatedRemoveListener(tab);
                    setTimeout(function(){
                        extension.tabs.close(tab);
                    }, 500);
                } else {
                    scrapLnkdn(tab, callback);
                }
            });
        });
    });
}

function scrapLnkdn(tab, callback){
    extension.tabs.sendMessage(tab, "scrapLnkdn", {}, function(response){
        extension.tabs.onClosedRemoveListener(tab);
        extension.tabs.onUpdatedRemoveListener(tab);
        callback(true,response);
        setTimeout(function(){
            extension.tabs.close(tab);
        }, 500);
    });
}