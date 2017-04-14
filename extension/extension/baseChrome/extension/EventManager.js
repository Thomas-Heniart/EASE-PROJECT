var EventListener = function (eventName, fCallback, count) {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	this.name = eventName;
	var fct = fCallback;
	this.maxCount = count;
	this.currCount = 0;

	this.exec = function(arguments) {
		fct(arguments, self.currCount);
	}
	this.isContinuous = function() {
		return this.maxCount == undefined;
	}
}

var EventManager = function () {
	var self = this;
	var base = new Base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}

	var listeners = {};

	this.addEventListener = function(eventName, fCallback, count) {
		var eventListener = undefined;
		
		if (count == undefined || count <= 0) {
			
			if (listeners[eventName] == undefined) {
				listeners[eventName] = [];
			}

			eventListener = new EventListener(eventName, fCallback, count);
			listeners[eventName].push(eventListener); 
		}

		return eventListener;
	}

	this.removeEventListener = function(eventListener) {
		if (listeners[eventListener.name]) {
			listeners[eventListener.name].splice(listeners[eventListener.name].indexOf(eventListener), 1);
		}
	}

	this.dispatchEvent = function(eventName, dispatcher) {
		if (listeners[eventName]) {
			listeners[eventName].forEach(function(eventListener) {

				eventListener.currCount++;
				eventListener.exec(dispatcher);
				
				if (!eventListener.isContinuous()) {
					if (eventListener.maxCount == eventListener.currCount) {
						self.removeEventListener(eventListener);
					}
				}

			});
		}
	}
}