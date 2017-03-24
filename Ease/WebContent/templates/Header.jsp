<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.List"%>


<a href="index.jsp"><img class="logoImg"
	src="resources/images/logo.png"></a>

	<script src="js/logout.js"></script>
	<%@ include file="DropDownUserMenu.jsp"%>
	<div class="logoutContainer">
		<a id="logoutButton"><i class="fa fa-power-off" aria-hidden="true"></i></a>
		<div class="logoutOptions">
			<a id="easeLogoutButton">Logout from Ease</a>
			<a id="allLogoutButton">Logout from all apps</a>
		</div>
	</div>
