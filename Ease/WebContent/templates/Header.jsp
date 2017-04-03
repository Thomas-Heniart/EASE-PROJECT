<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>


<a href="/"><img class="logoImg"
	src="resources/images/logo.svg"></a>

	<%@ include file="DropDownUserMenu.jsp"%>
	<div class="logoutContainer">
		<a id="logoutButton"><i class="fa fa-power-off" aria-hidden="true"></i></a>
		<div class="logoutOptions">
			<a id="easeLogoutButton">Logout from Ease</a>
			<a id="allLogoutButton">Logout from all apps</a>
		</div>
	</div>
