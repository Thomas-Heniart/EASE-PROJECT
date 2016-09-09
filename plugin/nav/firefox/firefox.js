var listenersUpdates = [];
var listenersMessages = [];

var extension = {
	storage:{
		get:function(key, callback){
			chrome.storage.local.get(key, callback);
		},
		set:function(key, value, callback){
			var obj = {};
			obj[key] = value;
			chrome.storage.local.set(obj, callback);
		}
	},
    currentWindow:function(callback){
        chrome.windows.getCurrent({}, callback)
    },
	addShortCut:function(fct){
		chrome.commands.onCommand.addListener(fct);
	},
	onWindowCreated:function(fct){
		chrome.windows.onCreated.addListener(fct);
	},
	onWindowRemoved:function(fct){
		chrome.windows.onRemoved.addListener(fct);
	}, 
	runtime:{
		sendMessage:function(name, msg, callback){
			chrome.runtime.sendMessage({"name":name, "message":msg}, callback);
		},
		onMessage:function(name, fct){
			chrome.runtime.onMessage.addListener(function(event, sender, sendResponse){
				if(event.name == name){
					fct(event.message, sendResponse);
                    return true;
				}
			});
		}
	},
	tabs:{
		update:function(tab, url, callback){
			chrome.tabs.update(tab.id, {"url":url}, callback);
		},
		create:function(window, url, active, callback){
			chrome.tabs.create({"windowId":window.id, "url":url, "active":active}, callback);
		},
		close:function(tab, callback){
   			chrome.tabs.remove(tab.id, callback);
		},
		onUpdated:function(tab, fct){
			listenersUpdates[tab.id] = function (tabId, params, newTab){
				if(tab.id==tabId){
				    fct(newTab);
				}
			}
			chrome.tabs.onUpdated.addListener(listenersUpdates[tab.id]);
		},
        onUpdatedRemoveListener:function(tab){
            chrome.tabs.onUpdated.removeListener(listenersUpdates[tab.id]);
        },
		sendMessage:function(tab, name, msg, callback){
			chrome.tabs.sendMessage(tab.id, {"name":name, "message":msg}, callback);
		},
        onMessage:function(tab, name, fct){
        	listenersMessages[tab.id] = function (event, sender, sendResponse){
				if(event.name == name && sender.tab.id == tab.id){
					fct(event.message, sendResponse);
				}
			}
            chrome.runtime.onMessage.addListener(listenersMessages[tab.id]);
        },
        onMessageRemoveListener:function(tab){
            chrome.runtime.onMessage.removeListener(listenersMessages[tab.id]);  
        },
		injectScript:function(tab, fileName, callback){
			chrome.tabs.executeScript(tab.id, {"file":fileName}, callback);
		}
	}
}