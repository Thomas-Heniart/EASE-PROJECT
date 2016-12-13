$(document).ready(function(){
	$("#logoutButton").click(function(e) {
		e.preventDefault();
		$('.logoutOptions').toggleClass("show");
	});
	$(document).click(function(e) {
		if(!$(e.target).closest(".logoutOptions, #logoutButton").length)
			$(".logoutOptions").removeClass("show");
	});
	$('#easeLogoutButton').click(function(){
		postHandler.post(
			'Logout',
			{
				socketId: socketId
			},
			function(){},
			function(retMsg){
				easeTracker.trackEvent('Normal logout');
				window.location.replace("index.jsp");
			},
			function(){},
			'text'
			);
	});
	$('#allLogoutButton').click(function(){
		var event = new CustomEvent("Logout");
		document.dispatchEvent(event);
		postHandler.post(
			'Logout',
			{},
			function(){},
			function(retMsg){
				easeTracker.trackEvent('General logout');
				window.location.replace("index.jsp");
			},
			function(){},
			'text'
			);
	});
});