
var Tab = function (singleId, target) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.singleId = singleId;
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
}

var BrowserManager = function () {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.tabs = {};
	var eventManager = new EventManager();
	var msgManager = new MsgManager();

	var tabToRemove = {};

	this.openTab = function() {
		safari.application.activeBrowserWindow.openTab();
	}
	this.closeTab = function(tab) {
		tab.target.close();
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
		self.tabs.forEach(function(tab) {
			tab.sendMessage(name, msg, fResponse);
		});
	}

	//
	///	Init
	//

	//
	//	Add a tab
	//

	function addTab(target) {
		var singleId = undefined;
		Object.keys(self.tabs).forEach(function(attr, index) {
			if (self.tabs[attr].target == target) {
				singleId = attr;
				self.tabs[attr].dispatchEvent("reload");
				self.tabs[attr].iFrames = {};
				self.dispatchEvent("tabReload", self.tabs[attr]);
			}
		});
		if (singleId == undefined) {
			singleId = tool.generateSingleId();
			self.tabs[singleId] = new Tab(singleId, target);
			self.tabs[singleId].dispatchEvent("open");
			self.dispatchEvent("tabOpen", self.tabs[singleId]);
		}
		return singleId;
	}
	
	function addIFrame(singleId, parentIds, target) {
		function addIframeRec(currentTab, singleId, parentIds, target, i) {
			if (parentIds.length > i) {
				addIframeRec(currentTab.iFrames[parentIds[i]], singleId, parentIds, target, i + 1);
			} else {
				currentTab.iFrames[singleId] = new Tab(singleId, target);
			}
		}
		addIframeRec(self.tabs[parentIds[0]], singleId, parentIds, target, 1);
	}
	function iFrameGetParent(parentIds) {
		if (parentIds.length == 0) {
			return undefined;
		}
		function iFrameGetParentRec(currentTab, parentIds, i) {
			if (parentIds.length > i) {
				return iFrameGetParentRec(currentTab.iFrames[parentIds[i]], parentIds, i + 1);
			} else {
				return currentTab;
			}
		}
		return iFrameGetParentRec(self.tabs[parentIds[0]], parentIds, 1);
	}

	safari.application.addEventListener("message", function(event) {
		if (event.message.magic === base.magic) {
			if (event.name === "tabInit") {
				if (event.target.page) {
					var singleId = addTab(event.target);
					event.target.page.dispatchMessage("tabInit",{'magic':base.magic, 'singleId':singleId});
				}
			} else if (event.name === "iFrameInit") {
				if (event.target.page) {
					event.target.page.dispatchMessage("checkChildIFrame", {'magic':base.magic, 'singleId':event.message.singleId});
				}
			} else if (event.name === "iFrameFinded") {
				if (event.target.page) {
					var parent = iFrameGetParent(event.message.parentIds);
					if (tabToRemove[event.message.singleId] == undefined) {
						parent.iFrames[event.message.singleId] = new Tab(event.message.singleId, event.target);
					} else {
						delete tabToRemove[event.message.singleId];
					}
				}
			} else if (event.name === "closeIFrame") {
				if (event.target.page) {
					var parent = iFrameGetParent(event.message.parentIds);
					if (parent && parent.iFrames[event.message.singleId]) {
						delete parent.iFrames[event.message.singleId];
					} else {
						tabToRemove[event.message.singleId] = "iframe";
					}
				}
			} else if (event.name === "existingTab") {
				if (event.target.page) {
					if (self.tabs[event.message.singleId] == undefined) {
						self.tabs[event.message.singleId] = new Tab(event.message.singleId, event.target);
					}
				}
			} else if (event.name === "message") {
				msgManager.dispatchMsg(event, self.tabs[event.message.singleId]);
				if (self.tabs[event.message.singleId]) {
					self.tabs[event.message.singleId].dispatchMessage(event, self.tabs[event.message.singleId]);
				}
			} else if (event.name === "response") {
				msgManager.dispatchResponse(event);
				if (self.tabs[event.message.singleId]) {
					self.tabs[event.message.singleId].dispatchResponse(event);
				}
			}
		}
	});

	//
	///	Close a tab
	//

	safari.application.addEventListener("close", function (e) {
		if (e.target instanceof SafariBrowserTab) {
			Object.keys(self.tabs).forEach(function(attr, index) {
				if (self.tabs[attr].target == e.target) {
					self.tabs[attr].dispatchEvent("close");
					self.dispatchEvent("tabClose", self.tabs[attr]);
					delete self.tabs[attr];
				}
			});
		}
	}, true);

	//
	///	Init all existing tabs
	//

	safari.application.browserWindows.forEach(function(wins) {
		wins.tabs.forEach(function(tab) {
			if (tab.page) {
				tab.page.dispatchMessage("checkExistingTabs", {'magic':base.magic});
			}
		});
	});
}

var browserManager = new BrowserManager();


