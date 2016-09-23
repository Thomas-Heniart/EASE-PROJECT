var tab_id_increment = 0;
var listenersUpdates = [];
var listenersMessages = [];

var extension = {
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
			safari.self.tab.dispatchMessage(name, msg);
			safari.self.addEventListener("message", function waitResponse(event){
                if(event.name==name+" response"){
                    safari.self.removeEventListener(waitResponse);
                    callback(event.message);
                }
            }, false);
		},
		onMessage:function(name, fct){
			safari.self.addEventListener("message", function(event){
				if(event.name==name){
					console.log("message recieved");
                    function sendResponse(response){
                        safari.self.tab.dispatchMessage(event.name+" response from tab "+event.message.tab, response);
                        console.log("response sent");
                    }
					fct(event.message.msg, sendResponse);
				}
			}, false);
		},
		bckgrndOnMessage:function(name, fct){
			safari.application.addEventListener("message", function(event){
				if(event.name==name){
                    function sendResponse(response){
                        event.target.page.dispatchMessage(event.name+" response", response);
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
			tab = window.openTab();
			tab.id = tab_id_increment;
			tab_id_increment++;
			tab.url = url;
			console.log(tab);
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
			var fullMessage = {"msg":msg, "tab":tab.id};
			tab.page.dispatchMessage(name, fullMessage);
			console.log("message : " + name + " has been dispatched");
            safari.application.addEventListener("message", function waitResponse(event){
                if(event.name==name+" response from tab "+tab.id){
                	console.log("response from tab "+tab.id);
                    safari.application.removeEventListener(waitResponse);
                    callback(event.message);
                }
            }, false);
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
		injectScript:function(tab, fileName, callback){
			callback();
		}
	}
}