var MsgListener = function(msgName, fCallback, count) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.name = msgName;
	var fct = fCallback;
	this.maxCount = count;
	this.currCount = 0;

	this.exec = function(event, fResponse) {
		fct(event.request.msg, fResponse, self.currCount);
	}
	this.isContinuous = function() {
		return this.maxCount == undefined;
	}
}

var MsgManager = function(contextSingleId) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.msgListeners = {};
	this.respListeners = {};

	this.addMsgListener = function(msgName, fCallback, count) {
		var msgListener = undefined;
		if (count == undefined || count > 0) {
			
			if (self.msgListeners[msgName] == undefined) {
				self.msgListeners[msgName] = [];
			}

			msgListener = new MsgListener(msgName, fCallback, count);
			self.msgListeners[msgName].push(msgListener);
		}

		return msgListener;
	}

	this.removeMsgListener = function(msgListener) {
		if (self.msgListeners[msgListener.name]) {
			self.msgListeners[msgListener.name].splice(self.msgListeners[msgListener.name].indexOf(msgListener), 1);
		}
	}

	this.dispatchMsg = function(event) {
		if (self.msgListeners[event.request.name]) {
			self.msgListeners[event.request.name].forEach(function(msgListener) {

				msgListener.currCount++;
				var replyed = false;
				msgListener.exec(event, function(response) {
					/*safari.self.tab.dispatchMessage("response", {'magic': base.magic, 'singleId': contextSingleId, 'responseCode': event.message.responseCode, 'msg':response});*/
					chrome.runtime.sendMessage({'type':"response", 'magic': base.magic, 'singleId': contextSingleId, 'responseCode': event.message.responseCode, 'msg':response});
					replyed = true;
				});
				if (replyed == false) {
					/*safari.self.tab.dispatchMessage("response", {'magic': base.magic, 'singleId': contextSingleId, 'responseCode': event.message.responseCode, 'msg':undefined});*/
					chrome.runtime.sendMessage({'type':"response", 'magic': base.magic, 'singleId': contextSingleId, 'responseCode': event.message.responseCode, 'msg':undefined});
				}
				
				if (!msgListener.isContinuous()) {
					if (msgListener.maxCount == msgListener.currCount) {
						self.removeMsgListener(msgListener);
					}
				}

			});
		}
	}

	this.sendMessageToBackground = function(msgName, msg, fResponse) {
		var responseCode = tool.generateSingleId();
		self.respListeners[responseCode] = fResponse;
		/*safari.self.tab.dispatchMessage("message", {'name': msgName, 'magic': base.magic, 'singleId': contextSingleId, 'responseCode': responseCode, 'msg':msg});*/
		chrome.runtime.sendMessage({'type':"message", 'name': msgName, 'magic': base.magic, 'singleId': contextSingleId, 'responseCode': responseCode, 'msg':msg});
	}

	this.dispatchResponse = function(event) {
		if (self.respListeners[event.request.responseCode]) {
			self.respListeners[event.request.responseCode](event.message.msg);
			delete self.respListeners[event.request.responseCode];
		}
	}

}