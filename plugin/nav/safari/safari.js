var tab_id_increment = 0;
var listenersUpdates = [];
var listenersMessages = [];

var extension = {
	storage:{
		get:function(key, callback){
			localStorage.getItem("key", "value");
		},
		set:function(key, value, callback){
			localStorage.setItem("key", "value");
		}
	},
    currentWindow:function(callback){
        callback(safari.application.activeBrowserWindow);
    },
	addShortCut:function(fct){
	},
	onWindowCreated:function(fct){
		safari.application.activeBrowserWindow.addEventListener("open", fct, false);
	},
	onWindowRemoved:function(fct){
		safari.application.activeBrowserWindow.addEventListener("close", fct, false);
	},
	runtime:{
		sendMessage:function(name, msg, callback){
            var completeMessage = {"content":msg, "sender":safari.self.tab}
			safari.application.dispatchMessage(name, completeMessage);
			safari.self.addEventListener("message", function waitResponse(event){
                if(event.name==name+" response"){
                    tab.removeEventListener(waitResponse);
                    callback(event.message);
                }
            });
		},
		onMessage:function(name, fct){
			safari.self.addEventListener("message", function(event){
				if(event.name==name){
                    function sendResponse(response){
                        safari.self.dispatchMessage(event.name+" response", response);
                    }
					fct(event.message, sendResponse);
				}
			}, false);
		}        
	},
	tabs:{
		update:function(tab, url, callback){
			tab.url = url;
			callback(tab);
		},
		create:function(window, url, active, callback){
			for (var t in window.tabs){
				if (window.tabs[t] == tab){
					tab.id = tab_id_increment;
					tab_id_increment++;
				}
			}
			tab.url = url;
			callback(tab);
		},
		close:function(tabId, callback){
   			tab.close();
   			callback();
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
		sendMessage:function(tab, name, msg, callback){
			tab.dispatchMessage(name, msg);
            tab.addEventListener("message", function waitResponse(event){
                if(event.name==name+" response"){
                    tab.removeEventListener(waitResponse);
                    callback(event.message);
                }
            });
		},
        onMessage:function(tab, name, fct){
        	listenersMessages[tab.id] = function (event){
				if(event.name==name && event.message.sender == tab){
                    function sendResponse(response){
                        safari.self.dispatchMessage(event.name+" response", response);
                    }
					fct(event.message.content, sendResponse);
				}
			}
			safari.self.addEventListener("message", listenersMessages[tab.id], false);
		},
        onMessageRemoveListener:function(tab){
            safari.self.removeEventListener("message", listenersMessages[tab.id], false);  
        },
		injectScript:function(tab, fileName, callback){
			callback();
		}
	}
}