easeMsgManager.sendMessage("websiteVisited", {"url": window.location.hostname}, function(response) {
	if (response.globalCount >= 20) {
		var websitesVisited = JSON.stringify(response.websitesVisited);
		$.post(
    		'http://localhost:8080/WebsitesVisited',
    		{
    			websitesVisited: websitesVisited
    		},
    		function(data) {
    			easeMsgManager.sendMessage("resetWebsitesVisited", null, function(response) {});
    		}
		);
	}
});