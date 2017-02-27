integrityConstraints = [
	"porn"
]

function checkUrlIntegrity(url) {
	return (url.replace(/[^a-z]/gi,'').match(new RegExp("("+$.map(integrityConstraints,function(e){
			return e.replace(/[^a-z]/gi,'');
		}).join('|')+")"))!=null);
}

if (!checkUrlIntegrity(window.location.href))
	easeMsgManager.sendMessage("websiteVisited", {"url": window.location.hostname}, function(response) {
		if (response.globalCount >= 2) {
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