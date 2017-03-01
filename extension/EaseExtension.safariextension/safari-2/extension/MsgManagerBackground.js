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
	this.exec = function (msg, sender, fResponse) {
		self.callbacks.forEach(function(elem) {
			if (elem.currCount !== undefined){
				elem.currCount++;
				elem.fct(msg, sender, fResponse, elem.currCount);
				if (elem.currCount == elem.maxCount) {
					self.removeCallback(elem);
				}
			} else {
				elem.fct(msg, sender, fResponse);				
			}
		});
	}
	this.execEvent = function(target) {
		self.callbacks.forEach(function(elem) {
			if (elem.currCount !== undefined){
				elem.currCount++;
				elem.fct(target);
				if (elem.currCount == elem.maxCount) {
					self.removeCallback(elem);
				}
			} else {
				elem.fct(target);				
			}
		});	
	}
}

var Tab = function(singleId, target) {
	var self = this;
	this.base = new base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}
	this.singleId = singleId;
	this.target = target;
	this.msgListeners = {};
	this.responseListeners = {};

	this.onEvent = function (event) {
		if (event.message.singleId === self.singleId) {
			if (event.name === "message") {
				if (self.msgListeners[event.message.name]) {
					var replyed = false;
					self.msgListeners[event.message.name].exec(event.message.msg, self, function (msg) { 
						self.target.page.dispatchMessage("response", {'magic': self.base.magic, 'singleId': self.singleId, 'responseCode': event.message.responseCode, 'msg':msg});
						replyed = true;
					});
					if (replyed == false) {
						self.target.page.dispatchMessage("response", {'magic': self.base.magic, 'singleId': self.singleId, 'responseCode': event.message.responseCode, 'msg':undefined});
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
	this.sendMessage = function(name, msg, fResponse) {
		var responseCode = tool.generateSingleId();
		self.responseListeners[responseCode] = fResponse; 
		self.target.page.dispatchMessage("message", {'name': name,'magic': self.base.magic, 'singleId': self.singleId, 'responseCode': responseCode, 'msg':msg});
	}


	this.stateListeners = {};
	this.on = function (name, fCallback, count) {
		var listener = self.stateListeners[name];
		if (count == undefined || count <= 0) {
			if (listener) {
				listener.addCallback(fCallback, count);
			} else {
				self.stateListeners[name] = new Listener(name, fCallback, count);
			}
		}
	}
	this.onReloaded = function(fCallback, count) {
		self.on("reloaded", fCallback, count);
	}
	this.onClosed = function(fCallback, count) {
		self.on("reloaded", fCallback, count);
	}
	this.dispatchEvent = function(name) {
		this.stateListeners[name].execEvent(self);
	}

}

var ContextManager = function () {
	var self = this;
	this.base = new base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}
	this.tabs = {};
	this.addTab = function (target) {
		var singleId;
		Object.keys(self.tabs).forEach(function(attr, index) {
			if (self.tabs[attr].target == target) {
				singleId = attr;
				self.tabs[attr].dispatchEvent("reloaded");
			}
		});
		if (singleId == undefined) {
			singleId = tool.generateSingleId();
			self.tabs[singleId] = new Tab(singleId, target);
		}
		return singleId;
	}

	//When a tab is closed
	safari.application.addEventListener("close", function (e) {
		console.log("tab closed");
		if (e.target instanceof SafariBrowserTab) {
			Object.keys(self.tabs).forEach(function(attr, index) {
				if (self.tabs[attr].target == e.target) {
					delete self.tabs[attr];
				}
			});
		}
	}, true);

	safari.application.addEventListener("message", function(event) {
		if (event.message.magic === self.base.magic) {	//Verify if the message are not sended by an other API or script.
			if (event.name === "getContext") {			//Sended on tab load
				var singleId = self.addTab(event.target);
				event.target.page.dispatchMessage("setupContext",{'magic':self.base.magic, 'singleId':singleId});
			} else {
				self.tabs[event.message.singleId].onEvent(event);
				if (event.name === "message") {			//Sended on content sendMessage
					if (self.msgListeners[event.message.name]) {
						var replyed = false;
						self.msgListeners[event.message.name].exec(event.message.msg, self.tabs[event.message.singleId], function(msg) {
							replyed = true;
							self.tabs[event.message.singleId].target.page.dispatchMessage("response", {'magic': self.base.magic, 'singleId': event.message.singleId, 'responseCode': event.message.responseCode, 'msg':msg});
						});
						if (replyed == false) {			//If no response sended, send it with undefined msg
							self.tabs[event.message.singleId].target.page.dispatchMessage("response", {'magic': self.base.magic, 'singleId': event.message.singleId, 'responseCode': event.message.responseCode, 'msg':undefined});
						}
					}
				}
			}
		}
	});

	this.msgListeners = {};
	this.responseListeners = {};
	this.sendMessage = function(tabSingleId, name, msg, fResponse) {
		self.tabs[tabSingleId].sendMessageToBackground(name, msg, fResponse);
	}
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
}

var contextManager = new ContextManager();