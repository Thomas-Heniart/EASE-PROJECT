$(document).ready(function(){
	$("#logoutButton").click(function(e) {
		e.preventDefault();
		$('.logoutOptions').toggleClass("show");
	});
	$(document).click(function(e) {
		if(!$(e.target).closest(".logoutOptions, #logoutButton").length)
			$(".logoutOptions").removeClass("show");
	});
	$('#easeLogoutButton').click(function(e){
		e.preventDefault();
		postHandler.post(
			'Logout',
			{},
			function(){},
			function(retMsg){
				easeTracker.trackEvent('EaseLogout');
				easeTracker.logout();
				window.location = "/";
			},
			function(retMsg){
				console.log(retMsg);
			},
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
				easeTracker.trackEvent('AllAppsLogout');
				easeTracker.logout();
				window.location = "/";
			},
			function(retMsg){
				console.log(retMsg);
			},
			'text'
			);
	});
});
