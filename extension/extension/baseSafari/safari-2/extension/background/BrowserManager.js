
var Content = function(singleId, type, target) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.singleId = singleId;
	this.type = type;
	this.target = target;

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

		}
	}

	this.update = function() {
		self.target.page.dispatchMessage("update", {'magic': base.magic});
	}
}

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
		self.target.close();
	}
	this.newTab = function () {
		self.target.openTab();
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
		console.log(self);
	}

	var eventManager = new EventManager();
	var msgManager = new MsgManager();
	this.windows = {};
	var contentsAndParents = {};


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

	safari.application.addEventListener("close", function (e) {
		if (e.target instanceof SafariBrowserWindow) {
			if (e.target.singleId !== undefined) {
				delete self.windows[e.target.singleId];
			}
		} else if (e.target instanceof SafariBrowserTab){
			console.log("close");
			closeContent(e.target.singleId);
			if (e.target.browserWindow.singleId) {
				if (self.windows[e.target.browserWindow.singleId].tabs.length == 0) {
					delete self.windows[e.target.browserWindow.singleId];
				}
			}
		}
	}, true);

	var newContent = function(parent, pathTab, target) {
		var ret = {};
		pathTab.forEach(function(elem) {
			if (parent[elem.type + "s"][elem.singleId] == undefined) {
				parent[elem.type + "s"][elem.singleId] = new Content(elem.singleId, elem.type, undefined);
			}
			ret.parent = parent;
			parent = parent[elem.type + "s"][elem.singleId];
		});
		parent.target = target;
		parent.target.singleId = parent.singleId;
		ret.content = parent;
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
			if (event.target.page) {
				var contentAndParent = undefined;
				if (event.target.browserWindow.singleId == undefined) {
					event.target.browserWindow.singleId = tool.generateSingleId();
					self.windows[event.target.browserWindow.singleId] = new Window(event.target.browserWindow);
				}
				var win = self.windows[event.target.browserWindow.singleId];
				contentAndParent = newContent(win, event.message.pathTab, event.target);
				contentsAndParents[event.message.pathTab[event.message.pathTab.length - 1].singleId] = contentAndParent;
			}
		},
		closeContent : function(event) {
			closeContent(event.message.singleId);
		},
		message : function(event) {
			var win = self.windows[event.target.browserWindow.singleId]
			msgManager.dispatchMsg(event, win.tabs[event.message.singleId]);
			if (self.windows[win.tabs[event.message.singleId]]) {
				self.windows[win.tabs[event.message.singleId]].dispatchMessage(event, win.tabs[event.message.singleId]);
			}
		},
		response : function(event) {
			msgManager.dispatchResponse(event);
			var win = self.windows[event.target.browserWindow.singleId]
			if (win.tabs[event.message.singleId]) {
				win.tabs[event.message.singleId].dispatchResponse(event);
			}
		}

	}

	safari.application.addEventListener("message", function(event) {
		if (event.message.magic === base.magic) {
			if (messageHandler[event.name]) {
				messageHandler[event.name](event);
			}
		}
	});

	safari.application.browserWindows.forEach(function(win) {
		win.singleId = tool.generateSingleId();
		self.windows[win.singleId] = new Window(win);
		win.tabs.forEach(function(tab) {
			if (tab.page) {
				tab.page.dispatchMessage("getContents", {'magic':base.magic});
			}
		});
	});
}

var browserManager = new BrowserManager();