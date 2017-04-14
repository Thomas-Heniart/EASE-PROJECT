
var tabActions = {};
var notActiveCallback = [];

var Content = function(singleId, type, target, win, parent) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.singleId = singleId;
	this.type = type;
	this.target = target;
	this.window = win;
	this.parent = parent;
	this.actions = {};

	var eventManager = new EventManager();
	var msgManager = new MsgManager(self);
	
	this.iFrames = {};
	this.popups = {};

	//
	///	Using EventManager
	//

	this.addEventListener = function(eventName, fCallback, count) {
		return eventManager.addEventListener(eventName, fCallback, count);
	}

	this.on = function(eventName, fCallback, count) {
		return self.addEventListener(eventName, fCallback, count);
	}

	this.removeEventListener = function(eventListener) {
		return eventManager.removeEventListener(eventListener);
	}

	this.dispatchEvent = function(eventName) {
		return eventManager.dispatchEvent(eventName, self);
	}

	//
	///	Using MsgManager
	//

	this.onMessage = function(name, fCallback, count) {
		return msgManager.addMsgListener(name, fCallback, count);
	}

	this.addMessageListener = function(name, fCallback, count) {
		return self.onMessage(name, fCallback, count);
	}

	this.removeMessageListener = function(msgListener) {
		return msgManager.removeMsgListener(msgListener);
	}

	this.sendMessage = function(name, msg, fResponse) {
		return msgManager.sendMessage(name, msg, fResponse);
	}

	this.dispatchMessage = function(event, sender) {
		msgManager.dispatchMsg(event, sender);
	}

	this.dispatchResponse = function(event) {
		msgManager.dispatchResponse(event);
	}

	if (self.type === "tab") {
		self.close = function() {
			chrome.tabs.remove(self.target.id);
		}
	}

	this.update = function() {
		if (self.target && self.target.id) {
			chrome.tabs.sendMessage(self.target.id, {'type':"update", 'magic': base.magic});
		}
	}

	this.launchAction = function(action) {
		if (tabActions[self.target.id] == undefined) {
			tabActions[self.target.id] = {};	
		}
		tabActions[self.target.id][action] = {};
		console.log("active");
		self.actions[action] = {};
		chrome.tabs.sendMessage(self.target.id, {'type':"launchAction", 'magic': base.magic, 'action': action});
	}
}

var createTabCallback = {};

var Window = function(target) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.target = target;
	var eventManager = new EventManager();
	this.tabs = {};

	this.close = function() {
		chrome.windows.remove(self.target.id);
	}
	this.openTab = function (url, fCallback) {
		chrome.tabs.create({'windowId':self.target.id, 'url':url}, function(browserTab) {
			createTabCallback[browserTab.id] = fCallback;
		});
	}

	this.addEventListener = function(eventName, fCallback, count) {
		return eventManager.addEventListener(eventName, fCallback, count);
	}

	this.on = function(eventName, fCallback, count) {
		return self.addEventListener(eventName, fCallback, count);
	}

	this.removeEventListener = function(eventListener) {
		return eventManager.removeEventListener(eventListener);
	}

	this.dispatchEvent = function(eventName) {
		return eventManager.dispatchEvent(eventName, self);
	}

}

var BrowserManager = function() {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self.windows);
	}

	var eventManager = new EventManager();
	var msgManager = new MsgManager();
	this.windows = {};
	var contentsAndParents = {};
	var singleIdWindows = {};
	var callbackCreateWindows = {};
	var singleIdTabs = {};

	this.openWindow = function(url, fCallback) {
		chrome.windows.create({'url':url}, function (browserWindow) {
			callbackCreateWindows[browserWindow.id] = fCallback;
		});
	}

	//
	///	Using EventManager
	//

	this.addEventListener = function(eventName, fCallback, count) {
		return eventManager.addEventListener(eventName, fCallback, count);
	}

	this.on = function(eventName, fCallback, count) {
		return self.addEventListener(eventName, fCallback, count);
	}

	this.removeEventListener = function(eventListener) {
		return eventManager.removeEventListener(eventListener);
	}

	this.dispatchEvent = function(eventName, dispatcher) {
		return eventManager.dispatchEvent(eventName, dispatcher);
	}

	//
	///	Using MsgManager
	//

	this.onMessage = function(name, fCallback, count) {
		return msgManager.addMsgListener(name, fCallback, count);
	}

	this.addMessageListener = function(name, fCallback, count) {
		return self.onMessage(name, fCallback, count);
	}

	this.removeMessageListener = function(msgListener) {
		return msgListener.removeMsgListener(msgListener);
	}

	this.sendMessageToAllTabs = function(name, msg, fResponse) {
		Object.keys(self.windows).forEach(function(winId, index) {
			var currWin = self.windows[winId];
			Object.keys(currWin.tabs).forEach(function(tabId, index) {
				currWin.tabs[tabId].sendMessage(name, msg, fResponse);
			});
		});
	}

	this.updateAllTabs = function() {
		Object.keys(self.windows).forEach(function(winId, index) {
			var currWin = self.windows[winId];
			Object.keys(currWin.tabs).forEach(function(tabId, index) {
				currWin.tabs[tabId].update();
			});
		});
	}

	chrome.tabs.onRemoved.addListener(function(tabId) {
		if (singleIdTabs[tabId]) {
			closeContent(singleIdTabs[tabId].singleId);
			if (self.windows[singleIdWindows[singleIdTabs[tabId].windowId]]) {
				if (jQuery.isEmptyObject(self.windows[singleIdWindows[singleIdTabs[tabId].windowId]].tabs) == true) {
					delete self.windows[singleIdWindows[singleIdTabs[tabId].windowId]];
					delete singleIdWindows[singleIdTabs[tabId].windowId];
				}
			}
			delete singleIdTabs[tabId];
		}
		if (tabActions[tabId]) {
			delete tabActions[tabId];
			if (jQuery.isEmptyObject(tabActions)) {
				console.log("inactive");
				notActiveCallback.forEach(function(elem) {
					elem();
					notActiveCallback.splice(0, 1);
				});
			}
		}
	});

	var newContent = function(parent, pathTab, target) {
		var ret = {};
		ret.contentAndParent={};
		var win = parent;
		pathTab.forEach(function(elem) {
			if (parent[elem.type + "s"][elem.singleId] == undefined) {
				parent[elem.type + "s"][elem.singleId] = new Content(elem.singleId, elem.type, undefined, win, ((win == parent) ? undefined : parent));
				if (elem.type === "tab") {
					singleIdTabs[target.id] = {'singleId':elem.singleId, 'windowId':target.windowId};
				}
			}
			ret.contentAndParent.parent = parent;
			parent = parent[elem.type + "s"][elem.singleId];
		});
		if (pathTab.length == 1) {
			ret.tabCreated = parent;
		}
		parent.target = target;
		parent.target.singleId = parent.singleId;
		ret.contentAndParent.content = parent;
		if (parent.type === "tab") {
			if (tabActions[target.id] == undefined) {
				tabActions[target.id] = {};
			}
			
			tabActions[target.id]["default"] = {};
			parent.actions["default"] = {};
			chrome.tabs.sendMessage(target.id, {'type':"launchAction", 'magic': base.magic, 'action': "default"});
			
			Object.keys(tabActions[target.id]).forEach(function(action, index) {
				if (action !== "default") {
					chrome.tabs.sendMessage(target.id, {'type':"launchAction", 'magic': base.magic, 'action': action});
				}
			});
			
			console.log("action");
		}
		return ret;
	}

	var closeChildContents = function(content) {
		Object.keys(content.iFrames).forEach(function(attr, index) {
			closeChildContents(content.iFrames[attr]);
			delete content.iFrames[attr];
			delete contentsAndParents[attr];
		});
		Object.keys(content.popups).forEach(function(attr, index) {
			closeChildContents(content.popups[attr]);
			delete content.popups[attr];
			delete contentsAndParents[attr];
		});
	}

	var closeContent = function(singleId) {
		var contentAndParent = contentsAndParents[singleId];
		if (contentAndParent) {
			closeChildContents(contentAndParent.content);
			delete contentAndParent.parent[contentAndParent.content.type + "s"][singleId];
			delete contentsAndParents[singleId];
		}
	}

	var messageHandler = {
		newContent : function(event) {
			if (event.sender.tab) {
				var contentAndParent = undefined;
				chrome.windows.get(event.sender.tab.windowId, function(browserWindow) {
					var haveNewWindow = false;
					if (singleIdWindows[browserWindow.id] == undefined) {
						singleIdWindows[browserWindow.id] = tool.generateSingleId();
						self.windows[singleIdWindows[browserWindow.id]] = new Window(browserWindow);
						haveNewWindow = true;
					}
					var win = self.windows[singleIdWindows[browserWindow.id]];
					var ret = newContent(win, event.request.pathTab, event.sender.tab);
					contentAndParent = ret.contentAndParent;
					contentsAndParents[event.request.pathTab[event.request.pathTab.length - 1].singleId] = contentAndParent;
					if (haveNewWindow == true && callbackCreateWindows[browserWindow.id]) {
						callbackCreateWindows[browserWindow.id](win);
						delete callbackCreateWindows[browserWindow.id];
					}
					if (ret.tabCreated && createTabCallback[ret.tabCreated.target.id]) {
						createTabCallback[ret.tabCreated.target.id](ret.tabCreated);
						delete createTabCallback[ret.tabCreated.target.id];
					}
				});
			}
		},
		closeContent : function(event) {
			closeContent(event.request.singleId);
		},
		message : function(event) {
			chrome.windows.get(event.sender.tab.windowId, function(browserWindow) {
				var win = self.windows[singleIdWindows[browserWindow.id]];
				msgManager.dispatchMsg(event, win.tabs[event.request.singleId]);
				if (self.windows[win.tabs[event.request.singleId]]) {
					self.windows[win.tabs[event.request.singleId]].dispatchMessage(event, win.tabs[event.request.singleId]);
				}
			});
		},
		response : function(event) {
			chrome.windows.get(event.sender.tab.windowId, function(browserWindow) {
				msgManager.dispatchResponse(event);
				var win = self.windows[singleIdWindows[browserWindow.id]];
				if (win.tabs[event.request.singleId]) {
					win.tabs[event.request.singleId].dispatchResponse(event);
				}
			});
		},
		actionDone : function(event) {
			chrome.windows.get(event.sender.tab.windowId, function(browserWindow) {
				var tab = self.windows[singleIdWindows[browserWindow.id]].tabs[singleIdTabs[event.sender.tab.id].singleId];
				var action = event.request.action;
				if (tab.actions[action] != undefined) {
					delete tab.actions[action];
				}
				if (tabActions[tab.target.id] && tabActions[tab.target.id][action]) {
					delete tabActions[tab.target.id][action];
					console.log("action done");
					if (jQuery.isEmptyObject(tabActions[tab.target.id])) {
						delete tabActions[tab.target.id];
						if (jQuery.isEmptyObject(tabActions)) {
							console.log("inactive");
							notActiveCallback.forEach(function(elem) {
								elem();
								notActiveCallback.splice(0, 1);
							});
						}
					}
				}
			});
		}

	}

	chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
		if (request.magic === base.magic) {
			if (messageHandler[request.type]) {
				messageHandler[request.type]({'request':request, 'sender':sender});
			}
		}
	});

	chrome.windows.getAll({populate:true}, function(browserWindows) {
		browserWindows.forEach(function(browserWindow) {
			singleIdWindows[browserWindow.id] = tool.generateSingleId();
			self.windows[singleIdWindows[browserWindow.id]] = new Window(browserWindow);
			browserWindow.tabs.forEach(function(tab) {
				if (tab.url.substring(0, 9) !== "chrome://")
					chrome.tabs.reload(tab.id);
			});
		});
	});
}

var browserManager = new BrowserManager();