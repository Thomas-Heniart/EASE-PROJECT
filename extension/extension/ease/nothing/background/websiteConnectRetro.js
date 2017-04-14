var Background = function() {
	console.log("connectWebsiteRetroAction");

	this.destroy = function() {
		console.log("destroy connectWebsiteRetroAction");
	}
	console.log(I.memory);

	var actionInFrame = false;
	var actionInFrameData;

	I.onMessage("enterFrame", function(msg, sendResponse) {
		console.log("enterFrame");
		I.sendMessage("inFrame", msg);
		actionInFrame = true;
		actionInFrameData = msg;
	});

	I.onMessage("exitFrame", function(msg, sendResponse) {
		actionInFrame = false;
		actionInFrameData = undefined;
	});

	I.onMessage("openFrame", function(msg, sendResponse) {
		console.log("openFrame");
		if (actionInFrame == true) {
			console.log("send InFrame");
			I.sendMessage("inFrame", actionInFrameData);
		}
	});
}