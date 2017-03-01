var Listener = function(name, fCallback, count) {
	var self = this;
	this.name = name;
	this.callbacks = [];
	this.addCallback = function (fCallback, count) {
		self.callbacks.push({"fct":fCallback, "maxCount":count, "currCount":0});
	}
	self.addCallback(fCallback, count);
	this.removeCallback = function (callback) {
		self.callbacks.splice(self.callbacks.indexOf(callback), 1);
	}
	this.exec = function (msg, fResponse) {
		self.callbacks.forEach(function(elem) {
			if (elem.currCount !== undefined){
				elem.currCount++;
				elem.fct(msg, fResponse, elem.currCount);
				if (elem.currCount == elem.maxCount) {
					self.removeCallback(elem);
				}
			} else {
				elem.fct(msg, fResponse);				
			}
		});
	}
}

var Context = function () {
	var self = this;
	this.base = new base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}
	this.singleId;
	var getTabIdEvent = function(event) {
		if (event.message.magic === self.base.magic) {	//Verify if the message are not sended by an other API or script.
			if (event.name === "setupContext") {
				safari.self.removeEventListener("message", getTabIdEvent, false);
				self.singleId = event.message.singleId;
				
			}
		}
	}

	if (window.self == window.top && window.opener == undefined) { // Not in an iframe or popup
		console.log("Is new tab");
		safari.self.addEventListener("message", getTabIdEvent);
		safari.self.tab.dispatchMessage("getContext", {'magic': self.base.magic});


		safari.application.addEventListener("close", function (e) {
			console.log("tab closed");
			if (e.target instanceof SafariBrowserTab) {
				safari.self.tab.dispatchMessage("removeContext", {'magic': self.base.magic, 'singleId': self.singleId});	
			}
		}, true);
	}
	this.msgListeners = {};
	this.responseListeners = {};
	this.onEvent = function (event) {
		if (event.message.magic === self.base.magic) {
			if (event.message.singleId === self.singleId && event.name === "message") {
				if (self.msgListeners[event.message.name]) {
					var replyed = false;
					self.msgListeners[event.message.name].exec(event.message.msg, function (msg) { 
						safari.self.tab.dispatchMessage("response", {'magic': self.base.magic, 'singleId': self.singleId, 'responseCode': event.message.responseCode, 'msg':msg});
						replyed = true;
					});
					if (replyed == false) {
						safari.self.tab.dispatchMessage("response", {'magic': self.base.magic, 'singleId': self.singleId, 'responseCode': event.message.responseCode, 'msg':undefined});
					}
				}
			} else if (event.message.singleId === self.singleId && event.name === "response") {
				if (self.responseListeners[event.message.responseCode]) {
					self.responseListeners[event.message.responseCode](event.message.msg);
					delete self.responseListeners[event.message.responseCode];
				}
			}
		}
	}
	safari.self.addEventListener("message", self.onEvent);
	this.onMessage = function(name, fCallback, count) {
		var listener = self.msgListeners[name];
		if (count == undefined || count <= 0) {
			if (listener) {
				listener.addCallback(fCallback, count);
			} else {
				self.msgListeners[name] = new Listener(name, fCallback, count);
			}
		}
	}
	this.sendMessageToBackground = function(name, msg, fResponse) {
		var responseCode = tool.generateSingleId();
		self.responseListeners[responseCode] = fResponse; 
		safari.self.tab.dispatchMessage("message", {'name': name,'magic': self.base.magic, 'singleId': self.singleId, 'responseCode': responseCode, 'msg':msg});
	}	
}

var context = new Context();
