easeMsgManager.sendMessage("websiteVisited", {"url": window.location.hostname}, function(response) {
	console.log(response);
	if (response.globalCount >= 20) {
		var websitesVisited = JSON.stringify(response.websitesVisited);
		$.ajax({
    		type: 'POST',
    		url: 'http://localhost:8080/WebsitesVisited',
    		crossDomain: true,
    		data: websitesVisited,
    		dataType: 'text',
    		success: function(responseData, textStatus, jqXHR) {
        		console.log("success");
        		easeMsgManager.sendMessage("resetWebsitesVisited", null, function(response) {});
    		}
		});
	}
});