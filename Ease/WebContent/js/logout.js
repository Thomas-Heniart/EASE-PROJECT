$(document).ready(function(){
	$('#logoutButton').click(function(){
		postHandler.post(
			'logout',
			{},
			function(){},
			function(retMsg){window.location.replace("index.jsp?logout");},
			function(){},
			'text'
		);
	});
	$('#allLogoutButton').click(function(){
		var event = new CustomEvent("Logout");
		document.dispatchEvent(event);
	});
});