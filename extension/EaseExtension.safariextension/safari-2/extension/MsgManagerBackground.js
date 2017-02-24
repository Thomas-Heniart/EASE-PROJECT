var ContextManager = function () {
	var self = this;
	this.base = new base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}
	this.frames = {};
	this.addFrame = function (singleId, frame) {
		Object.keys(self.frames).forEach(function(attr, index) {
			if (self.frames[attr] == frame) {
				delete self.frames[attr];
			}
		});
		self.frames[singleId] = frame;
	}
	safari.application.addEventListener("message", function(event) {
		console.log("receive a message");
		if (event.message.magic === self.base.magic) {	//Verify if the message are not sended by an other API or script.
			if (event.name === "getContext") {
				console.log(event);
				var singleId = tool.generateSingleId();
				console.log(singleId);
				self.addFrame(singleId, event.target);
				event.target.page.dispatchMessage("setupContext",{'magic':self.base.magic, 'singleId':singleId});
			}
		}
	});
}

var contextManager = new ContextManager();

console.log("ContextManager injected.");