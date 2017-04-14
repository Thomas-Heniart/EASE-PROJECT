var Background = function() {
	console.log("connectWebsiteAction");

	this.destroy = function() {
		console.log("destroy connectWebsiteAction");
	}
	console.log(I.memory);

	var actionInFrame = [];

	I.onMessage("inFrame", function(msg) {
		
		I.sendMessage("inFrame", msg);
		actionInFrame.push(msg);
		
	});

	I.onMessage("openFrame", function(msg) {
		actionInFrame.forEach(function(elem) {
			I.sendMessage("inFrame", elem);
		});
	});
}