msgManager.onMessage("toto", function(msg, fResponse, count) {
	console.log("message recu: " + msg + " count = " + count);
	fResponse("Message recu wazzaaaa");
});


var websitesVisited = {};
var globalCount = 0;

msgManager.onMessage("websiteVisited", function(msg, fResponse, c) {
	globalCount++;
	var stringCount = websitesVisited[msg.url];
	if (stringCount == undefined)
		websitesVisited[msg.url] = "1";
	else {
		var count = parseInt(stringCount);
		websitesVisited[msg.url] = (count + 1).toString();
	}
	fResponse({"websitesVisited": websitesVisited, "globalCount": globalCount});
});

msgManager.onMessage("resetWebsitesVisited", function(msg, fResponse, count) {
	globalCount = 0;
	websitesVisited = {};
	fResponse();
})