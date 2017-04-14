var Background = function() {
	console.log("addWebsiteAction");

	this.destroy = function() {
		console.log("destroy addWebsiteAction");
	}

	I.onMessage("unconnectedDom", function(msg) {
		console.log("unconnectedDom");
		I.sendMessageToServer("unconnectedDom", {'dom': msg});
	});
	I.onMessage("connectedDom", function(msg) {
		console.log("connectedDom");
		I.sendMessageToServer("connectedDom", {'dom': msg});
	});
	I.onMessage("loggedOutDom", function(msg) {
		console.log("loggedOutDom");
		I.sendMessageToServer("loggedOutDom", {'dom': msg});
	});

	
}