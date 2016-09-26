<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
$(document).ready(function(){
	$('#logoutButton').click(function(){
		var event = new CustomEvent("Logout");
		document.dispatchEvent(event);
	});
});


</script>
<div class="header">
	<a href="index.jsp"><img class="logoImg" src="resources/images/logo.png" ></a>



<% if (session.getAttribute("User") != null){ %>
		<%@ include file="DropDownUserMenu.jsp"%>
	<a href="logout" id="logoutButton"><i class="fa fa-fw fa-sign-out"></i><span>Logout</span></a>	
<%}%>

</div>

