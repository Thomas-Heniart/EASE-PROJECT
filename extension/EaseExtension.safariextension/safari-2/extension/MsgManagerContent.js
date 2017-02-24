var Context = function () {
	var self = this;
	this.base = new base(); //config.js ---> This is to define API identity
	this.print = function() {
		console.log(self);
	}
	this.singleId;
	this.isFrame = window.self !== window.top; //Check if we are in an iframe
	this.isPopup = window.opener !== undefined; //Check if we are in a popup
	var getTabIdEvent = function(event) {
		if (event.magic === self.base.magic) {	//Verify if the message are not sended by an other API or script.
			if (event.name === "setupContext") {
				safari.self.removeEventListener("message", getTabIdEvent, false);
				self.singleId = event.msg.tabId;
			}
		}
	}
	safari.self.addEventListener("message", getTabIdEvent);
	safari.self.tab.dispatchMessage("getContext", {'magic':self.base.magic});
}

var context = new Context();

console.log("Context injected.");