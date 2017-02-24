
var EaseListener = function(name, fCallback, count) {
	var self = this;
	this.name = name;
	this.callbacks = [];
	this.callbacks.add({"fct":fCallback, "maxCount":count, "currCount":0});
	this.addCallback = function (fCallback, count) {
		self.callbacks.add({"fct":fCallback, "maxCount":count, "currCount":0});
	}
	this.removeCallback = function (callback) {
		self.callbacks = self.callbacks.splice(self.callbacks.indexOf(callback), 1);
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

var EaseMsgManager = function () {
	var self = this;
	this.listeners = {};
	this.sendMessage = function(name, msg, fResponse) {
		easeExtension.background.sendMessage({"name":name, "msg":msg}, fResponse);
	}
	this.onMessage = function(name, fCallback, count) {
		var listener = self.listeners[name];
		if (listener) {
			listener.addCallback(fCallback, count);
		} else {
			self.listeners[name] = new EaseListener(name, fCallback, count);
		}
	}
}

var easeMsgManager = new EaseMsgManager();