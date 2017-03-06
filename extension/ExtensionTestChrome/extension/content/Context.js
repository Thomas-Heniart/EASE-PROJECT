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
			safari.self.tab.dispatchMessage("newContent", {'magic': base.magic, 'pathTab':pathTab});
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
			if (self.singleId === event.message.singleId)
				msgManager.dispatchMsg(event);
		},
		reponse : function(event) {
			if (self.singleId === event.message.singleId)
				msgManager.dispatchResponse(event);
		}
	}

	safari.self.addEventListener("message", function(event) {
		if (event.message.magic === base.magic) {
			backgroundMessageHandler[event.name](event);
		}
	});

	window.onbeforeunload = function(event) {
		if (safari.self.tab) {
			safari.self.tab.dispatchMessage("closeContent", {'magic': base.magic, 'singleId':self.singleId});
		}
	};

}

var context = new Context();

console.log(context.singleId);

