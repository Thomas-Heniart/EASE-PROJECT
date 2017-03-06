var Context = function () {
	var self = this;
	var base = new Base();
	this.print = function() {
		console.log(self);
	}

	var eventManager = new EventManager();
	var msgManager;

	this.singleId;
	this.parentTabIds = [];
	var isReady = false;
	var readyListener = [];
	this.isIFrame = window.self !== window.top;
	this.isPopup = window.opener != undefined;

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


	if (self.isIFrame == true) { // Is a iframe
		
		window.addEventListener("message", function(event) {
			var msg = event.data;
			if (msg.magic === base.magic) {
				if (msg.name === "checkChildIFrame") {
					if (msg.singleId === self.singleId) {
						self.parentTabIds = msg.parentIds;
						safari.self.tab.dispatchMessage("iFrameFinded", {'magic': base.magic, 'singleId':self.singleId, 'parentIds':msg.parentIds});
						ready();
					} else {
						var parentIds = msg.parentIds;
						parentIds.push(self.singleId);
						Array.prototype.slice.call(document.getElementsByTagName("iframe")).forEach(function(iframe) {
							iframe.contentWindow.postMessage({'magic': base.magic, 'name': "checkChildIFrame", 'parentIds':parentIds, 'singleId':msg.singleId}, "*");
						});
					}
				}
			}
		}, false);

		var singleId = tool.generateSingleId();
		self.singleId = singleId;
		safari.self.tab.dispatchMessage("iFrameInit", {'magic': base.magic, 'singleId':singleId});
		window.onbeforeunload = function(event) {
			if (safari.self.tab) {
				safari.self.tab.dispatchMessage("closeIFrame", {'magic': base.magic, 'parentIds': self.parentTabIds, 'singleId':self.singleId});
			}
		}
	} else if (self.isPopup == true) { // Is a popup

	} else { // Not in an iframe or a popup	
		function tabInit(event) {
			if (event.message.magic === base.magic) {	//Verify if the message are not sended by an other API or script.
				if (event.name === "tabInit") {
					safari.self.removeEventListener("message", tabInit, false);
					self.singleId = event.message.singleId;
					ready();
				}
			}
		}
		safari.self.addEventListener("message", tabInit);
		safari.self.tab.dispatchMessage("tabInit", {'magic': base.magic});
	}


	safari.self.addEventListener("message", function(event) {
		if (event.message.magic === base.magic) {
			if (self.isIFrame == true) { // Is a iframe

			} else if (self.isPopup == true) { // Is a popup

			} else { // Not in an iframe or a popup
				if (event.name === "checkExistingTabs") {
					if (isReady == true) {
						safari.self.tab.dispatchMessage("existingTab", {'magic': base.magic, 'singleId': self.singleId});
					}
				} else if (event.name === "checkChildIFrame") {
					var parentIds = [];
					parentIds.push(self.singleId);
					Array.prototype.slice.call(document.getElementsByTagName("iframe")).forEach(function(iframe) {
						iframe.contentWindow.postMessage({'magic': base.magic, 'name': "checkChildIFrame" ,'parentIds':parentIds, 'singleId':event.message.singleId}, "*");
					});
				}
			}
			if (self.singleId === event.message.singleId) {
				if (event.name === "message") {	
					msgManager.dispatchMsg(event);
				} else if (event.name === "response") {
					msgManager.dispatchResponse(event);
				}
			}
		}
	});
}

context = new Context();


