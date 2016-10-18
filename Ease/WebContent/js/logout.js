$(document).ready(function(){
	
	$("#arrowLogout").hover(function(){
		$("#arrowLogout").css("border-bottom-left-radius","0px");
		$("#logoutButton").css("border-bottom-right-radius","0px");
		$(".logoutContainer").css("max-height","61px");
	}, function(){
		$(".logoutContainer").css("max-height","30px");
		$("#arrowLogout").css("border-bottom-left-radius","3px");
		$("#logoutButton").css("border-bottom-right-radius","3px");
	});
	
	$("#allLogoutButton").hover(function(){
		$("#arrowLogout").css("border-bottom-left-radius","0px");
		$("#logoutButton").css("border-bottom-right-radius","0px");
		$(".logoutContainer").css("max-height","61px");
	}, function(){
		$(".logoutContainer").css("max-height","30px");
		$("#arrowLogout").css("border-bottom-left-radius","3px");
		$("#logoutButton").css("border-bottom-right-radius","3px");
	});
	
	$('#logoutButton').click(function(){
		postHandler.post(
			'logout',
			{},
			function(){},
			function(retMsg){window.location.replace("index.jsp");},
			function(){},
			'text'
		);
	});
	
	$('#allLogoutButton').click(function(){
		var event = new CustomEvent("Logout");
		document.dispatchEvent(event);
		postHandler.post(
			'logout',
			{},
			function(){},
			function(retMsg){window.location.replace("index.jsp");},
			function(){},
			'text'
		);
	});
});