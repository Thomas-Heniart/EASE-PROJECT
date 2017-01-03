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
			{},
			function(){},
			function(retMsg){
				easeTracker.trackEvent('EaseLogout');
				location.href = "index.jsp";
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
				location.href = "index.jsp";
			},
			function(retMsg){
				console.log(retMsg);
			},
			'text'
			);
	});
});
