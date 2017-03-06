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

	this.exec = function(event, sender, fResponse) {
		fct(event.message.msg, sender, fResponse, self.currCount);
	}
	this.isContinuous = function() {
		return this.maxCount == undefined;
	}
}

var MsgManager = function(tab) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	var msgListeners = {};
	var respListeners = {};

	this.addMsgListener = function(msgName, fCallback, count) {
		var msgListener = undefined;
		
		if (count == undefined || count > 0) {
			
			if (msgListeners[msgName] == undefined) {
				msgListeners[msgName] = [];
			}

			msgListener = new MsgListener(msgName, fCallback, count);
			msgListeners[msgName].push(msgListener); 
		}

		return msgListener;
	}

	this.removeMsgListener = function(msgListener) {
		if (msgListeners[msgListener.name]) {
			msgListeners[msgListener.name].splice(msgListeners[msgListener.name].indexOf(msgListener), 1);
		}
	}

	this.dispatchMsg = function(event, sender) {
		if (msgListeners[event.message.name]) {
			if (sender == undefined) {
				sender = tab;
			}
			msgListeners[event.message.name].forEach(function(msgListener) {

				msgListener.currCount++;
				var replyed = false;
				msgListener.exec(event, sender, function(response) {
					sender.target.page.dispatchMessage("response", {'magic': base.magic, 'singleId': sender.singleId, 'responseCode': event.message.responseCode, 'msg':response});
					replyed = true;
				});
				
				if (replyed == false) {
					sender.target.page.dispatchMessage("response", {'magic': base.magic, 'singleId': sender.singleId, 'responseCode': event.message.responseCode, 'msg':undefined});
				}
				
				if (!msgListener.isContinuous()) {
					if (msgListener.maxCount == msgListener.currCount) {
						self.removeMsgListener(msgListener);
					}
				}

			});
		}
	}

	this.sendMessage = function(msgName, msg, fResponse) {
		var responseCode = tool.generateSingleId();
		respListeners[responseCode] = fResponse;
		tab.target.page.dispatchMessage("message", {'name': msgName, 'magic': base.magic, 'singleId': tab.singleId, 'responseCode': responseCode, 'msg':msg});
	}

	this.dispatchResponse = function(event) {
		if (respListeners[event.message.responseCode]) {
			respListeners[event.message.responseCode](event.message.msg);
			delete respListeners[event.message.responseCode];
		}
	}

}