function refresh() {
	postHandler.post('CheckConnection',
			{"email":$("#userEmail").data("content")},
			function(){},
			function(retMsg) {
				//console.log(retMsg);
			}, 
			function(retMsg) {
				window.location.replace("index.jsp");
			},
			'text'
		);
	setTimeout(refresh, 15*1000);
}

$(document).ready(function() {
  refresh();
})
