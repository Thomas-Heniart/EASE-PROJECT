I.BrowserManager = function () {
	var parentIn = I;
	var I = {};
	var self = this;
	this.print = function() {
		console.log(self);
	}
	I.WindowManager = function (singleId, browserWindow, parentIn) {
		var parentIn = parentIn;
		var I = {};
		var self = this;
		this.print = function() {
			console.log(self);
		}
		I.TabManager = function (singleId, browserTab, parentIn) {
			var parentIn = parentIn;
			var I = {};
			var self = this;
			this.print = function() {
				console.log(self);
			}
			
			I.IframeManager = function (singleId, parentIn, actions) {
				var parentIn = parentIn;
				var I = {};
				I.IframeManager = parentIn.IframeManager;
				I.PopupManager = parentIn.PopupManager;
				I.MessageListener = parentIn.MessageListener;
				var self = this;
				this.print = function() {
					console.log(self);
				}
//==IframeManager==//

				I.singleId = singleId;
				
				I.iFrames = {};
				I.popups = {};
				I.add = parentIn.add;
				I.remove = parentIn.remove;

				I.addContent = parentIn.addContent;
				I.removeContent = parentIn.addContent;

				I.actions = actions;

				this.getIframes = function() {

				}
				this.getIframeById = function(singleId) {

				}
				this.getPopups = function() {

				}
				this.getPopupById = function (singleId) {

				}
				this.sendMessage = function(msgName, msg, onResponse) {

				}
				this.addMessageListener = function(msgName, msg, sender, sendResponse) {

				}
				this.removeMessageListener = function(listener) {

				}
				this.getMessageListeners = function() {

				}
				this.silence = function() {

				}

				parentIn.iframes[singleId] = {"iframes": this, "addContent":I.addContent, "removeContent":I.removeContent};

//=FIN=IframeManager==//
			}
			I.PopupManager = function (singleId, parentIn, actions) {
				var parentIn = parentIn;
				var I = {};
				I.IframeManager = parentIn.IframeManager;
				I.PopupManager = parentIn.PopupManager;
				I.MessageListener = parentIn.MessageListener;
				var self = this;
				this.print = function() {
					console.log(self);
				}
//==PopupManager==//

				I.singleId = singleId;

				I.iFrames = {};
				I.popups = {};
				I.add = parentIn.add;
				I.remove = parentIn.remove;

				I.addContent = parentIn.addContent;
				I.removeContent = parentIn.removeContent;

				I.actions = actions;

				this.getIframes = function() {

				}
				this.getIframeById = function(singleId) {

				}
				this.getPopups = function() {

				}
				this.getPopupById = function (singleId) {

				}
				this.sendMessage = function(msgName, msg, onResponse) {

				}
				this.addMessageListener = function(msgName, msg, sender, sendResponse) {

				}
				this.removeMessageListener = function(listener) {

				}
				this.getMessageListeners = function() {
			
				}
				this.silence = function() {
					
				}

				parentIn.popups[singleId] = {"popup": this, "addContent":I.addContent, "removeContent":I.removeContent};
				
//=FIN=PopupManager==//
			}

			I.ActionManager = function (name, code, tabIn, tab) {
				var I = {};
				var self = {};
				this.print = function() {
					console.log(self);
				}
//==ActionManager==//

				I.tab = tab;
				I.actionName = name;

				I.onMessage = function(callback) {
					this.onMessage = callback;
				}

				I.sendMessage = function(msgName, msg, onResponse) {
					chrome.tabs.sendMessage(tabIn.browserTab.id, {'magic': IDENTITY.getMagic(), 'type':"message", 'actionName':name, 'msgName':msgName, 'msg':msg}, onResponse);
				}

				eval(code);
				self = new Background();

				this.destroy = function() {
					self.destroy();
					delete self;
				}
//=FIN=ActionManager==//
			}

//==TabManager==//
			
			I.singleId = singleId;
			I.browserTab = browserTab;

			I.iframes = {};
			I.popups = {};
			I.actions = {};
			I.add = {
				iframe : function(singleId, actions) {
					return new I.IframeManager(singleId, I, actions);
				},
				popup : function(singleId, actions) {
					return new I.PopupManager(singleId, I, actions);
				}
			}
			I.remove = {
				iframe : function(singleId) {
					if (iframes[singleId])
						delete iframes[singleId];
				},
				popup : function(singleId) {
					if (popups[singleId])
						delete popups[singleId];
				}
			}
			I.addAction = function(actionName) {
				chrome.storage.local.get("updater", function(item) {
					I.actions[actionName] = new I.ActionManager(actionName, item.updater.actions.background[actionName], I, self);
				});
			}
			I.removeAction = function(actionName) {
				if (I.actions[actionName]) {
					I.actions[actionName].destroy();
					delete I.actions[actionName];
				}
			}

			I.closeCallback = undefined;

			I.addContent = function(pathTab) {
				if (pathTab.length > 0) {
					var content = pathTab[0];
					console.log(content.type);
					pathTab.shift();
					if (I[content.type + "s"][content.singleId]) {
						I[content.type + "s"][content.singleId].addContent(pathTab);
					} else {
						I.add[content.type](content.singleId, I.actions);
						I[content.type + "s"][content.singleId].addContent(pathTab);
					}
				} else {
					console.log("======");
				}
			}
			I.removeContent = function(pathTab) {
				var content = pathTab[0];
				if (I[content.type + "s"][content.singleId]) {
					if (pathTab.length == 1) {
						I.remove[content.type](content.singleId);
					} else {
						pathTab.shift();
						I[content.type + "s"][content.singleId].removeContent(pathTab);
					}
				}
			}

			I.update = function() {
				chrome.tabs.sendMessage(I.browserTab.id, {'magic': IDENTITY.getMagic(), 'type':"update"})
			}

			I.message = function(msg, sendResponse) {
				if (actions[msg.actionName]) {
					if (actions[msg.actionName].onMessage)
						actions[msg.actionName].onMessage(msg.msgName, msg.msg, sendResponse);
				}
			}

			I.actionFinished = function(actionName) {
				I.removeAction(actionName);
			}

			this.getIframes = function() {

			}
			this.getIframeById = function(singleId) {

			}
			this.getPopups = function() {

			}
			this.getPopupById = function (singleId) {

			}
			this.launchAction = function(actionName, params) {

			}
			this.getPosition = function() {

			}
			this.close = function() {

			}
			this.onClosed = function(callback) {

			}
			this.silence = function() {
					
			}

			this.launchAction = function(actionName) {
				chrome.tabs.sendMessage(I.browserTab.id, {'magic': IDENTITY.getMagic(), 'type':"launchAction", 'actionName':actionName});
				I.addAction(actionName);
			}


			parentIn.tabsSingleId[singleId] = {"tab": this, "addContent":I.addContent, "removeContent":I.removeContent, "browserTabId":I.browserTab.id, 'update':I.update, "actionFinished": I.actionFinished, "message":I.message};
			parentIn.tabsBrowserId[browserTab.id] = {"tab": this, "addContent":I.addContent, "removeContent":I.removeContent, "singleId":I.singleId, 'update':I.update, "actionFinished": I.actionFinished, "message":I.message};

//=FIN=TabManager==//
		}



//==WindowManager==//		

		I.singleId = singleId;
		I.browserWindow = browserWindow;

		I.tabsSingleId = {};
		I.tabsBrowserId = {};
		I.openTabCallback = undefined;
		I.closeTabCallback = undefined;
		I.userOpenTab = {};
		I.addTab = function(singleId, browserTab) {
			var tab = new I.TabManager(singleId, browserTab, I);
			if (I.userOpenTab[browserTab.id]) {
				I.userOpenTab[browserTab.id](tab);
				delete I.userOpenTab[browserTab.id];
			}
			return tab;
		}
		I.removeTab = function(singleId) {
			if (I.tabsSingleId[singleId]) {
				var browserTabId = I.tabsSingleId[singleId].browserTabId;
				I.tabsSingleId[singleId].tab.silence();
				delete I.tabsSingleId[singleId];
				delete I.tabsBrowserId[browserTabId];
			}
		}

		I.closeCallback = undefined;

		I.addContent = function(pathTab, browserTab) {
			console.log(pathTab);
			if (pathTab.length > 0) {
				var content = pathTab[0];
				pathTab.shift();
				if (I.tabsSingleId[content.singleId]) {
					I.tabsSingleId[content.singleId].addContent(pathTab);
				} else {
					var tab = I.addTab(content.singleId, browserTab);
					I.tabsSingleId[content.singleId].addContent(pathTab);
					if (I.openTabCallback)
						openTabCallback(tab);
				}
			}
		}
		I.removeContent = function(pathTab) {
			var content = pathTab[0];
				if (I.tabsSingleId[content.singleId]) {
					if (pathTab.length == 1) {
						I.removeTab(content.singleId);
					} else {
						pathTab.shift();
						I.tabsSingleId[content.singleId].removeContent(pathTab);
					}
				}
		}

		I.update = function() {
			Object.keys(I.tabsSingleId).forEach(function(attr) {
				I.tabsSingleId[attr].update();
			});
		}

		I.actionFinished = function(browserTabId, actionName) {
			if (I.tabsBrowserId[browserTabId]) {
				I.tabsBrowserId[browserTabId].actionFinished(actionName);
			}
		}

		I.message = function(browserTabId, msg, sendResponse) {
			if (I.tabsBrowserId[browserTabId]) {
				I.tabsBrowserId[browserTabId].message(msg, sendResponse);
			}
		}

		this.getTabs = function () {
			var tabs = [];
			Object.keys(I.tabsSingleId).forEach(function(attr, index) {
				tabs.push(I.tabsSingleId[attr].tab);
			});
			return tabs;
		}
		this.getTabById = function(singleId) {
			return tabsSingleId[singleId].tab;
		}

		this.getPosition = function() {
			return I.browserTab.index;
		}

		this.openTab = function(url, position, callback) {
			chrome.tabs.create({'index':position, 'url':url}, function(browserTab) {
				I.userOpenTab[browserTab.id] = callback;
			});
		}
		this.onTabOpened = function(callback) {
			I.openTabCallback = callback;
		}
		this.onTabClosed = function(callback) {
			I.closeTabCallback = callback;
		}
		this.getCurrentTab = function(callback) {
			chrome.tabs.getCurrent(function(browserTab) {
				if (I.tabs[browserTab.id]) {
					callback(I.tabs[browserTab.id].tab);
				} else {
					callback(undefined);
				}
			});
		}
		this.close = function() {
			chrome.windows.remove(I.browserWindow.id);
		}
		this.onClosed = function(callback) {
			parentIn.windowsSingleId[singleId].onClosed = callback;
			parentIn.windowsBrowserId[browserWindow.id].onClosed = callback;
		}
		this.silence = function() {
					
		}

		parentIn.windowsSingleId[singleId] = {"window": this, "addContent":I.addContent, "removeContent":I.removeContent, "browserWindowId":I.browserWindow.id, "update":I.update, "actionFinished": I.actionFinished, "message":I.message};
		parentIn.windowsBrowserId[browserWindow.id] = {"window": this, "addContent":I.addContent, "removeContent":I.removeContent, "singleId":I.singleId, "update":I.update, "actionFinished": I.actionFinished, "message":I.message};

//=FIN=WindowManager==//
	}
	I.BackgroundManager = function (BackgroundString) {
		var parentIn = {};
		var I = {};
		var self = {};
		this.print = function() {
			console.log(self);
		}
//==BackgroundManager==//
		
		eval(BackgroundString);
		self = new Background();
		Background = undefined;

		this.update = function(BackgroundString) {
			self.destroy();
			delete self;	
			eval(BackgroundString);
    		self = new Background();
    		Background = undefined;
		}

//=FIN=BackgroundManager==//
	}

//==BrowserManager==//
	
	I.windowsSingleId = {};
	I.windowsBrowserId = {};
	I.openWindowCallback = undefined;
	I.closeWindowCallback = undefined;
	I.userOpenWindow = {};
	I.addWindow = function(browserWindow) {
		var singleId = tool.generateSingleId();
		var window = new I.WindowManager(singleId, browserWindow, I);
		return window;
	}
	I.removeWindow = function(browserWindowId) {
		var singleId = I.windowsBrowserId[browserWindowId].singleId;
		I.windowsSingleId[singleId].window.silence();
		if (I.windowsSingleId[singleId].onClosed)
			I.windowsSingleId[singleId].onClosed();
		delete I.windowsSingleId[singleId];
		delete I.windowsBrowserId[browserWindowId];
	}

	I.singleId;
	I.websocket = null;
	I.interval = null;
	I.serverUrl = "localhost:8080/";


	I.connectionState = "unconnected";
	I.connectionStateChangedCallback = {"extension":undefined, "background":undefined};
	I.setConnectionState = function(connectionState) {
		if (I.connectionState !== connectionState) {
			I.connectionState = connectionState;
			if (I.connectionStateChangedCallback.extension)
				I.connectionStateChangedCallback.extension(connectionState);
			if (I.connectionStateChangedCallback.background)
				I.connectionStateChangedCallback.background(connectionState);
		}
	}


	I.sources = {	"background":undefined, 
					"actions":{"background":{"default":undefined}, "content":{"default":undefined}},
					"version":undefined
				};
	I.isActive = false;
	I.inactiveCallback = {"update":undefined};

	I.background = undefined;

	I.identityMsgHandler = {
    	welcome : function(data) {
    		I.websocket.send(JSON.stringify({'context':"identity", 'order':"lastSingleId", 'data': {'lastSingleId': I.singleId}}));
    		I.singleId = data.singleId;
    		chrome.storage.local.set({"identity": {"lastSingleId":I.singleId}});
    	}
    }

    I.updaterMsgHandler = {
    	update : function(data) {
    		if (data.version !== I.sources.version) {
    			I.websocket.send(JSON.stringify({'context':"updater", 'order':"getSources", 'data': {}}));
    		}
    	},
    	sources : function(data) {
    		function launchUpdate() {
    			chrome.storage.local.set({"updater":data}, function() {
    				I.sources.version = data.version;
    				I.sources.background = data.background;
    				I.sources.actions.background.default = data.actions.background.default;
    				I.sources.actions.content.default = data.actions.content.default;
    				
    				I.background.update(I.sources.background);
    				Object.keys(I.windowsSingleId).forEach(function(attr) {
    					I.windowsSingleId[attr].update();
    				});
    			});
    		}
    		if (!I.isActive) {
				launchUpdate();
			} else {
				I.inactiveCallback.update = launchUpdate;
			}
    	}
    }

	I.openSocket = function() {
		var xmlHttp = new XMLHttpRequest();
    	xmlHttp.open( "GET", "http://" + I.serverUrl + "extensionOpenSession", false ); // false for synchronous request
    	xmlHttp.send( null );
    	
    	I.websocket = new WebSocket("ws://" + I.serverUrl + "extensionEndpoint");

    	I.websocket.onmessage = function(message) {
			var msg = JSON.parse(message.data);
			if (msg.context === "identity") {
    			if (I.identityMsgHandler[msg.order] != undefined)
    				I.identityMsgHandler[msg.order](msg.data);
    		} else if (msg.context === "updater") {
    			if (I.updaterMsgHandler[msg.order] != undefined)
    				I.updaterMsgHandler[msg.order](msg.data);
    		}
    	}
    	I.websocket.onopen = function() {
    		console.log("open");
    		I.setConnectionState("connected");
    	}
    	I.websocket.onclose = function(evt) {
    		console.log("close: " + evt.code);
 			I.setConnectionState("unconnected");
    	}
    	I.websocket.onerror = function(evt) {
    		console.log("error: " + evt.type);
    	}
    }

    chrome.storage.local.get("updater", function(item) {
		if (item.updater == undefined) {
			item.updater = {"background":"var Background = function () {var self = this;this.destroy = function () {delete self;}}",
							"content":"var Content = function () {var self = this;this.destroy = function () {delete self;}}",
							"actions": {"background":{}, "content":{}},
							"files":{},
							"magic":IDENTITY.getMagic(),
							"version":"0.0.0"};
			chrome.storage.local.set({"updater":item.updater});
		}
		I.sources.version = item.updater.version;
		I.sources.background = item.updater.background;
		I.sources.actions.background.default = item.updater.actions.background.default;
    	I.sources.actions.content.default = item.updater.actions.content.default;
		
		
		I.background = new I.BackgroundManager(I.sources.background);

		I.interval = setInterval(function() {
			if (I.websocket.readyState === I.websocket.CLOSED) {
				console.log("Connection lost, try to reconnect...");
				I.openSocket();
			}
		}, 5000);
		I.openSocket();

		I.messageHandler = {
			newContent : function(event) {
				if (event.sender.tab && event.sender.tab.windowId) {
					if (I.windowsBrowserId[event.sender.tab.windowId]) {
						I.windowsBrowserId[event.sender.tab.windowId].addContent(event.msg.pathTab, event.sender.tab);
					} else {
						chrome.windows.get(event.sender.tab.windowId, function(browserWindow) {
							if (browserWindow) {
								var window = I.addWindow(browserWindow);
								I.windowsBrowserId[event.sender.tab.windowId].addContent(event.msg.pathTab, event.sender.tab);
								if (I.openWindowcallback)
									openWindowcallback(window);
							}
						});
					}
				}
			},
			closeContent : function(event) {
				if (event.sender.tab) {
					if (I.windowsBrowserId[event.sender.tab.windowId]) {
						I.windowsBrowserId[event.sender.tab.windowId].removeContent(event.msg.pathTab);
					}
				}
			},
			actionFinished : function(event) {
				if (I.windowsBrowserId[event.sender.tab.windowId]) {
					I.windowsBrowserId[event.sender.tab.windowId].actionFinished(event.sender.tab.id, event.msg.actionName);
				}
			},
			message : function(event, sendResponse) {
				if (I.windowsBrowserId[event.sender.tab.windowId]) {
					I.windowsBrowserId[event.sender.tab.windowId].message(event.sender.tab.id, event.msg, sendResponse);
				}
			}
		}

		chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
			if (request.magic === IDENTITY.getMagic()) {
				if (I.messageHandler[request.type]) {
					I.messageHandler[request.type]({'msg':request, 'sender':sender}, sendResponse);
				}
			}
		});

		chrome.windows.onCreated.addListener(function(browserWindow) {
			if (I.windowsBrowserId[event.sender.tab.windowId] == undefined) {
				var window = I.addWindow(browserWindow);
				if (I.openWindowcallback)
					openWindowcallback(window);
				if (I.userOpenWindow[browserWindow.id]) {
					I.userOpenWindow[browserWindow.id](window);
					delete(I.userOpenWindow[browserWindow.id]);
				}
			}
		});

		chrome.windows.onRemoved.addListener(function(browserWindowId) {
			if (windowsBrowserId[browserWindowId]) {
				I.removeWindow(browserWindowId);
				if (I.closeWindowCallback) {
					I.closeWindowCallback();
				}
			}
		});

	});

	this.getWindows = function() {
		var windows = [];
		Object.keys(I.windowsSingleId).forEach(function(attr, index) {
			windows.push(I.windowsSingleId[attr].window);
		});
		return windows;
	}

	this.getWindowById = function(singleId) {
		if (windowsSingleId[singleId]) {
			return windowsSingleId[singleId].window;
		}
		return undefined;
	}

	this.getCurrentWindow = function(callback) {
		chrome.windows.getCurrent(function(browserWindow) {
			if (I.windowsBrowserId[browserWindow.id]) {
				callback(I.windowsBrowserId[browserWindow.id].window);
			}
			else {
				callback(undefined);
			}
		});
	}

	this.openWindow = function(url, callback) {
		chrome.windows.create({'url':url}, function (browserWindow) {
			I.userOpenWindow[browserWindow.id] = callback;
		});
	}

	this.onWindowOpened = function(callback) {
		I.openWindowCallback = callback;
	}
	this.onWindowClosed = function(callback) {
		I.closeWindowCallback = callback;
	}
	
	this.onConnectionStateChanged = function(callback) {
		I.connectionStateChangedCallback = callback;
	}

	chrome.windows.getAll({populate:true}, function(browserWindows) {
		browserWindows.forEach(function(browserWindow) {
			browserWindow.tabs.forEach(function(tab) {
				if (tab.url.substring(0, 9) !== "chrome://")
					chrome.tabs.reload(tab.id);
			});
		});
	});
//=FIN=BrowserManager==//
}

var browserManager = new I.BrowserManager();