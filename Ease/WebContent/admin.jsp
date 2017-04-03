<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.Ease.Dashboard.User.User" %>	
<%@ page import="java.util.Base64"%>
<%@ page import="java.util.Base64.Encoder"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
<% 	User user = (User) (session.getAttribute("user")); %>
<%
	if (session.getAttribute("user") == null || !((User)session.getAttribute("user")).isAdmin()) {
%>
<script>
		window.location.replace("index.jsp");
</script>
<% } %>
<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
<title>EASE.space</title>
<link rel="chrome-webstore-item"
	href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm">

<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
<link rel="stylesheet" href="css/default_style.css" />
<link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro'
	rel='stylesheet' type='textcss' />
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link rel="stylesheet" href="css/lib/niftyPopupWindow/component.css">
<link rel="manifest" href="manifest.json">

<script src="js/classie.js"></script>
<script src="js/owl.carousel.js"></script>
<script src="js/basic-utils.js"></script>
<script src="js/postHandler.js"></script>
<script src="js/form/form.js"></script>
<script src="js/form/errorMsg.js"></script>
<script src="js/form/input.js"></script>
<script src="js/form/popup.js"></script>
<script src="js/form/emailSuggestions.js"></script>
<script src="js/admin.js"></script>
<script src="js/statistics.js"></script>
<script src="js/websocket.js"></script>

<script src="js/jquery.mousewheel.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css"
	href="css/lib/dropDownMenu/dropdown.css" />
<script src="js/snap.svg-min.js"></script>
<script src="js/modalEffects.js"></script>
<script src="js/selectFx.js"></script>
<link rel="stylesheet" type="text/css" href="component.css" />
<link rel="stylesheet" href="css/hover.css">
<script src="js/backOffice/websitesVisited.js"></script>
<script src="js/backOffice/tags.js"></script>
<script src="js/backOffice/unregisteredEmails.js"></script>
<script src="js/backOffice/websitesRequests.js"></script>
</head>


<body role="document" class="mainBody">
	<c:set var="groupManager"	scope="session" value='${servletContext.getAttribute("groupManager")}' />
	<c:set var="websitesVisitedManager" scope="session" value='${servletContext.getAttribute("websitesVisitedManager")}' />
	<div id="loggedBody">
		<div class="col-left show" style="width: 100%; float: left">
			<%@ include file="templates/AdminView.jsp"%>
		</div>
		<div class="md-overlay"></div>
	</div>
	<%@ include file="templates/Footer.html"%>
	<div class="la-anim-10" id="loading"></div>
	<%@ include file="templates/ChatButton.jsp" %>
</body>
</html>
