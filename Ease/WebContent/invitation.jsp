<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Ease registration</title>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <link rel="chrome-webstore-item" href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm">
    
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
    <link rel="stylesheet" href="css/default_style.css" />
    <link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro' rel='stylesheet' type='textcss' />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/owl.carousel.css" />
    <link rel="stylesheet" href="css/owl.theme.css" />
    <link rel="stylesheet" href="css/owl.transitions.css" />


		

    <link rel="stylesheet" href="css/lib/vicons-font/vicons-font.css">
    <link rel="stylesheet" href="css/lib/vicons-font/buttons.css">
	<link rel="stylesheet" href="css/lib/textInputs/set1.css">
	<link rel="stylesheet" href="css/lib/borderLoading/component.css">
	<link rel="stylesheet" href="css/lib/niftyPopupWindow/component.css">
	<link rel="stylesheet" href="css/lib/ColorSelect/cs-select.css">
	<link rel="stylesheet" href="css/lib/ColorSelect/cs-skin-boxes.css">
	<link rel="manifest" href="manifest.json">
	
	<script src="js/classie.js"></script>
   	<script src="js/owl.carousel.js"></script>
    <script src="js/basic-utils.js" ></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"> </script>
    <script src="js/jquery.mousewheel.min.js"></script>
    
		<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
		<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
		<script src="js/snap.svg-min.js"></script>
    	<script src="js/modalEffects.js"></script>
    	<script src="js/selectFx.js"></script>
		<link rel="stylesheet" type="text/css" href="component.css" />
</head>
<body>
	<%
	String invitationCode = request.getParameter("code");
	String emailAddress = request.getParameter("email");
	%>
	<p><%= invitationCode %></p>
	<p><%= emailAddress %></p>
</body>
</html>