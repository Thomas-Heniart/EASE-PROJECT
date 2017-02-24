msgManager.onMessage("toto", function(msg, fResponse, count) {
	console.log("message recu: " + msg + " count = " + count);
	fResponse("Message recu wazzaaaa");
});