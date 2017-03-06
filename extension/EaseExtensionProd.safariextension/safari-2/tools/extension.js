var tab_id_increment = 0;
var listenersUpdates = [];
var listenersMessages = [];
var listenersClose = [];
var currentUser = "anonymous";

var extension = {
	nbOfEaseTabs:function(){
        var nb=0;
        for(var w in safari.application.browserWindows){
            var window = safari.application.browserWindows[w];
            for(var i in window.tabs) {
                if(window.tabs[i].url && window.tabs[i].url.indexOf("ease.space")!=-1){
                    nb++;
                }
            }
        }
        return nb;
    },
    reloadEaseTabs:function(){
        for(var w in safari.application.browserWindows){
            var window = safari.application.browserWindows[w];
            for(var i in window.tabs) {
                if(window.tabs[i].url && window.tabs[i].url.indexOf("https://ease.space")==0){
                    window.tabs[i].url = window.tabs[i].url;
                }
            }
        }
    },
	storage:{
		get:function(key, callback){
			var json;
			try {json = JSON.parse(localStorage.getItem(key));}
			catch(e){json="";}
			callback(json);
		},
		set:function(key, value){
			localStorage.setItem(key, JSON.stringify(value));
		}
	},
    currentWindow:function(callback){
        callback(safari.application.activeBrowserWindow);
    },
    hasMultipleEaseTabs:function(window){
        var nb=0;
        for(var i in window.tabs) {
            if(window.tabs[i].url.indexOf("ease.space")!=-1){
                nb++;
            }
        }
        return nb>1;
    },
	addShortCut:function(fct){},//empty
	onWindowCreated:function(fct){
		safari.application.activeBrowserWindow.addEventListener("open", fct, true);
	},
	onWindowRemoved:function(fct){
		safari.application.activeBrowserWindow.addEventListener("close", fct, true);
	},
    onNewWindow:function(fct){
    	safari.application.addEventListener("open", function(e){
    		if(e.target!=undefined && e.target.browserWindow!=undefined && e.target.browserWindow.tabs.length < 2)
    			fct(e.target.browserWindow);
    	}, true);
    },
	runtime:{
		sendMessage:function(name, msg, callback){
			safari.self.tab.dispatchMessage(name, msg);
			safari.self.addEventListener("message", function waitResponse(event){
                if(event.name==name+" response"){
                    safari.self.removeEventListener("message", waitResponse, false);
                    callback(event.message);
                }
            }, false);
		},
		onMessage:function(name, fct){
			safari.self.addEventListener("message", function(event){
				if(event.name==name){
                    function sendResponse(response){
                        safari.self.tab.dispatchMessage(event.name+" response from tab "+event.message.tab, response);
                    }
					fct(event.message.msg, sendResponse);
				}
			}, false);
		},
		bckgrndOnMessage:function(name, fct){
            var ret = function(event) {
                if(event.name==name){
                    function sendResponse(response){
                        event.target.page.dispatchMessage(event.name + " response", response);
                    }
                    fct(event.message, event.target, sendResponse);
                }
            }
            safari.application.addEventListener("message", ret, false);
            return ret;



			/*safari.application.addEventListener("message", function(event){
				if(event.name==name){
                    function sendResponse(response){
                        event.target.page.dispatchMessage(event.name+" response", response);
                    }
					fct(event.message, event.target, sendResponse);
				}
			}, false);*/
		},
        tempBckgrndOnMessage:function(name, fct){
            safari.application.addEventListener("message", function temp(event){
				if(event.name==name){
                    safari.application.removeEventListener("message",temp,false);
                    function sendResponse(response){
                        event.target.page.dispatchMessage(event.name+" response", response);
                    }
					fct(event.message, event.target, sendResponse);
				}
			}, false);
        },
        removeListener:function(fct) {
            safari.application.removeEventListener("message", fct, false);
        }
	},
	tabs:{
        stopLoad:function(tab, callback){callback();},//empty
		update:function(tab, url, callback){
			tab.url = url;
			callback(tab);
		},
		create:function(window, url, active, callback){
			if(active){
				tab = window.openTab();
			} else {
				tab = window.openTab("background");
			}
			tab.id = tab_id_increment;
			tab_id_increment++;
			tab.url = url;
			callback(tab);
		},
        createOrUpdate:function(window, tab, url, active, callback){
        	if(active){
        		if(extension.hasMultipleEaseTabs(window)){
                	this.update(tab, url, callback);
            	} else {
            	    this.create(window, url, active, callback);
            	}
        	} else {
        		this.create(window, url, active, callback);
        	}
        },
        focus:function(tab, callback){
            tab.activate();
            callback();
        },
		close:function(tab){
   			tab.close();
		},
		onUpdated:function(tab, fct){
			listenersUpdates[tab.id] = function (){
				fct(tab);
			}
			tab.addEventListener("navigate", listenersUpdates[tab.id], false);
		},
        onUpdatedRemoveListener:function(tab){
            tab.removeEventListener("navigate", listenersUpdates[tab.id], false);
        },
        onNewTab:function(fct){
        	var noNavigation = true;
        	function handleOpen(e) {
    			if (e.target instanceof SafariBrowserTab) {
        			e.target.addEventListener('beforeNavigate', handleBeforeNavigate, false);
        			setTimeout(function () {
            			if(noNavigation) {
            				e.target.removeEventListener('beforeNavigate', handleBeforeNavigate, false);
            				fct(e.target);
            			} else {
            				noNavigation = true;
            			}
        			}, 500);
    			}
			}
			function handleBeforeNavigate(e) {
				noNavigation = false;
 				e.target.removeEventListener('beforeNavigate', handleBeforeNavigate, false);
    			if (e.url === null || e.url == undefined || e.url == "") {
        			fct(e.target);
    			}
			}
        	safari.application.addEventListener("open", handleOpen, true);
        },
		sendMessage:function(tab, name, msg, callback){
			var fullMessage = {"msg":msg, "tab":tab.id};
			tab.page.dispatchMessage(name, fullMessage);
            safari.application.addEventListener("message", function waitResponse(event){
                if(event.name==name+" response from tab "+tab.id){
                    safari.application.removeEventListener("message", waitResponse, false);
                    callback(event.message);
                }
            }, false);
		},
        onNavigation:function(fct){
            extension.runtime.bckgrndOnMessage("newFocus", function(message, sendResponse){
                fct(message.url);
            });
        },
        onMessage:function(tab, name, fct){
        	listenersMessages[tab.id] = function (event){
				if(event.name==name && event.target.id == tab.id){
                    function sendResponse(response){
                        safari.self.dispatchMessage(event.name+" response", response);
                    }
					fct(event.message, sendResponse);
				}
			}
			safari.application.addEventListener("message", listenersMessages[tab.id], false);
		},
        onMessageRemoveListener:function(tab){
            safari.application.removeEventListener("message", listenersMessages[tab.id], false);  
        },
        onClosed:function(tab, fct){
            listenersClose[tab.id] = function (){
                fct(tab);
            }
			tab.addEventListener("close", listenersClose[tab.id], false);
        },
        onClosedRemoveListener:function(tab){
            tab.removeEventListener("close", listenersClose[tab.id], false);
        },
		injectScript:function(tab, fileName, callback){
			if (fileName.indexOf("injectOverlay.js") != -1) {
				extension.tabs.sendMessage(tab, "strtOverlay", {}, function(){});
			}
			callback();
		},
		injectCSS:function(tab, fileName, callback){
			callback();
		},
		inject:function(tab, files, callback){
			for(var i in files){
				if (files[i].indexOf("injectOverlay.js") != -1) {
					extension.tabs.sendMessage(tab, "strtOverlay", {}, function(){});
				}
			}
			callback();
		}
	}
}