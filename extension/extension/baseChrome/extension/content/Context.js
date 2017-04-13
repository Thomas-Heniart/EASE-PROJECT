var Context = function () {
	var self = this;
	var base = new Base();
	this.print = function() {
		console.log(self);
	}

	var eventManager = new EventManager();
	var msgManager = new MsgManager;

	var type = undefined;
	var parent = undefined;
	if (window.self !== window.top) {
		type = "iFrame";
		parent = window.parent;
	} else if (window.opener != undefined) {
		type = "popup";
		parent = window.opener;
	} else {
		type = "tab";
	}

	this.getType = function() {
		return type;
	}
	this.singleId = tool.generateSingleId();

	this.version = "0.0.0";
	this.sources = {"background":"", "content":"", "magic":"4577LaMenuiserieMec!", "version":self.version};

	this.content = null;

	this.actions = [];
	
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

	this.onMessage = function(eventName, fCallback, count) {
		return msgManager.addMsgListener(eventName, fCallback, count);
	}

	this.addMessageListener = function(eventName, fCallback, count) {
		return self.onMessage(eventName, fCallback, count);
	}

	this.removeMessageListener = function(msgListener) {
		return msgManager.removeMsgListener(msgListener);
	}

	this.sendMessageToBackground = function(name, msg, fResponse) {
		return msgManager.sendMessageToBackground(name, msg, fResponse);
	}

	var initContentToBackground = function(pathTab) {
		pathTab.unshift({"type":type, "singleId":self.singleId});
		if (type === "tab") {
			chrome.runtime.sendMessage({'magic': base.magic, 'type':"newContent", 'pathTab':pathTab});
		} else {
			parent.postMessage({'magic': base.magic, 'name': "childInit", 'pathTab':pathTab}, "*");
		}
	}
	initContentToBackground([]);

	var windowMessageHandler = {
		childInit : function(msg) {
			initContentToBackground(msg.pathTab);
		}
	}

	window.addEventListener("message", function(event) {
		var msg = event.data;
		if (msg.magic === base.magic) {
			windowMessageHandler[msg.name](msg);
		}
	});

	var backgroundMessageHandler = {
		getContents : function(event) {
			initContentToBackground([]);
		},
		message : function(event) {
			if (self.singleId === event.request.singleId)
				msgManager.dispatchMsg(event);
		},
		reponse : function(event) {
			if (self.singleId === event.request.singleId)
				msgManager.dispatchResponse(event);
		},
		update : function(event) {
			chrome.storage.local.get("updater", function(item) {
				self.version = item.updater.version;
				self.sources = item.updater.sources;
				if (self.content) {
					self.content.destroy();
					delete self.content;
				}
				eval(self.sources.content);
				self.content = new Content();
				if (self.content.onAction["default"]) {
					self.content.onAction["default"]();
				}
			});
		},
		launchAction : function(event) {
			if (type === "tab") {
				if (self.content) {
					
				} else {
					self.actions.push(event.request.action);
				}
			}
		}
	}

	chrome.runtime.onMessage.addListener(function(request, sender) {
		if (request.magic === base.magic) {
			if (backgroundMessageHandler[request.type]) {
				backgroundMessageHandler[request.type]({'request':request, 'sender':sender});
			}
		}
	});

	/*window.onbeforeunload = function(event) {
		chrome.runtime.sendMessage({'magic': base.magic, 'type':"closeContent", 'singleId':self.singleId});
	};*/

	chrome.storage.local.get("updater", function(item) {
		self.version = item.updater.version;
		self.sources = item.updater.sources;
		eval(self.sources.content);
		self.content = new Content();
		self.actions.forEach(function(elem) {
			
		});
	});

	this.actionDone = function (action) {
		chrome.runtime.sendMessage({'magic': base.magic, 'type':"actionDone", "action":action});
	}
}

var context = new Context();