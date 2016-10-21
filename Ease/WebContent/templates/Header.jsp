<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.List"%>


<div class="header">
	<a href="index.jsp"><img class="logoImg"
		src="resources/images/logo.png"></a>

	<%
		if (session.getAttribute("User") != null) {
	%>
	<script src="js/logout.js"></script>
	<%@ include file="DropDownUserMenu.jsp"%>
	<%@ include file="SearchBar.jsp"%>
	<div class="logoutContainer">
		<a id="logoutButton"><i class="fa fa-power-off" aria-hidden="true"></i></a>
		<div class="logoutOptions">
			<a id="easeLogoutButton">Logout from Ease</a>
			<a id="allLogoutButton">Logout from all apps</a>
		</div>
		<!-- <a id="logoutButton"><span>Logout</span></a> -->
		<!-- <a id="allLogoutButton"><span>Global logout</span></a> -->
	</div>
	<%
		}
	%>

</div>