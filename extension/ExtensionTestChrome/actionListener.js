msgManager.onMessage("toto", function(msg, fResponse, count) {
	console.log("message recu: " + msg + " count = " + count);
	fResponse("Message recu wazzaaaa");
});


var websitesVisited = {};
var globalCount = 0;

msgManager.onMessage("websiteVisited", function(msg, fResponse, c) {
	globalCount++;
	var count = parseInt(websitesVisited[msg.url]);
	if (count == undefined)
		websitesVisited[msg.url] = "1";
	else
		websitesVisited[msg.url] = (count + 1).toString();
	fResponse({"websitesVisites": websitesVisited, "globalCount": globalCount});
});

msgManager.onMessage("resetWebsitesVisited", function(msg, fResponse, count) {
	globalCount = 0;
	websitesVisited = {};
	fResponse();
})