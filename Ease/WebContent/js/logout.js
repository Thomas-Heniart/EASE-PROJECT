$(document).ready(function(){
	$('#logoutButton').click(function(){
		var event = new CustomEvent("Logout");
		document.dispatchEvent(event);
		postHandler.post(
			'logout',
			{},
			function(){},
			function(retMsg){window.location.replace("logout.jsp");},
			function(){},
			'text'
		);
	});
});