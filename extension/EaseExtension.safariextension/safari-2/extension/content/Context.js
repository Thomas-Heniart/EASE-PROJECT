var Context = function () {
	var self = this;
	var base = new Base();
	this.print = function() {
		console.log(self);
	}

	var eventManager = new EventManager();
	var msgManager;

	this.singleId;
	var isReady = false;
	var readyListener = [];

	this.onReady = function(fCallback) {
		if (isReady == true) {
			fCallback();
		} else {
			readyListener.push(fCallback);
		}
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

	//
	///	Init
	//

	function ready() {
		msgManager = new MsgManager(self.singleId);
		isReady = true;
		readyListener.forEach(function(element) {
			element();
		});
		readyListener = [];
	}

	if (window.self == window.top && window.opener == undefined) { // Not in an iframe or a popup
		
		function init() {
			function initResponse(event) {
				if (event.message.magic === base.magic) {	//Verify if the message are not sended by an other API or script.
					if (event.name === "initResponse") {
						safari.self.removeEventListener("message", initResponse, false);
						self.singleId = event.message.singleId;
						ready();
					}
				}
			}
			safari.self.addEventListener("message", initResponse);
		
			safari.self.tab.dispatchMessage("tabInit", {'magic': base.magic});
		}

		init();

		safari.self.addEventListener("message", function(event) {
			if (event.message.magic === base.magic) {
				if (event.name === "checkExistingTabs") {
					if (isReady == true) {
						safari.self.tab.dispatchMessage("existingTab", {'magic': base.magic, 'singleId': self.singleId});
					}
				} else if (self.singleId === event.message.singleId) {
					if (event.name === "message") {
						msgManager.dispatchMsg(event);
					} else if (event.name === "response") {
						msgManager.dispatchResponse(event);
					}
				}
			}
		});

		/*safari.self.addEventListener("close", function (e) {
			if (e.target instanceof SafariBrowserTab) {
				safari.self.tab.dispatchMessage("tabClosed", {'magic': base.magic, 'singleId': self.singleId});	
			}
		}, true);*/
	}
}

var context = new Context();


