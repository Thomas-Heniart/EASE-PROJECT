<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.List" %>

<script>
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


</script>
<div class="header">
	<a href="index.jsp"><img class="logoImg" src="resources/images/logo.png" ></a>



<% if (session.getAttribute("User") != null){ %>
		<%@ include file="DropDownUserMenu.jsp"%>
		<%@ include file="SearchBar.jsp"%>
	<a id="logoutButton"><i class="fa fa-fw fa-sign-out"></i><span>Logout</span></a>	
<%}%>

</div>