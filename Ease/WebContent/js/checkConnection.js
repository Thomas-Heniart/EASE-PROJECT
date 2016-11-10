function refresh() {
	console.log($("#userEmail").data("content"));
	postHandler.post('checkConnection',
			{"email":$("#userEmail").data("content")},
			function(){},
			function(retMsg) {}, 
			function(retMsg) {
				window.location.replace("index.jsp");
			},
			'text'
		);
	setTimeout(refresh, 15*1000);
 /*$.ajax({
    url: 'index.jsp'
  }).success(function() {
    setTimeout(refresh, 20*60*1000);
  });*/
}

$(document).ready(function() {
  refresh();
})
