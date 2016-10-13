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
	<script src="js/registration.js"></script>
	<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
	<script src="js/snap.svg-min.js"></script>
	<script src="js/modalEffects.js"></script>
	<script src="js/selectFx.js"></script>
	<link rel="stylesheet" type="text/css" href="component.css" />
	<script src="js/postHandler.js"></script>
</head>
<body id="invitationBody">
	<%
	String invitationCode = request.getParameter("code");
	String emailAddress = request.getParameter("email");
	if (invitationCode == null || emailAddress == null) 
		request.getRequestDispatcher("index.jsp").forward(request, response);
	%>
	<div class="logo">
		<img src="resources/images/Ease_Logo.png"/>
	</div>
	<div id="helpInformations" email="<%= emailAddress %>" code="<%= invitationCode%>" style="display: none;"></div>
	<div class="registrationBlock">
		<div class="title">
			<p>Set up your account !</p>
		</div>
		<form action="registerInv" id="registrationForm">
			<div class="custom-info">
				<p>What's your name ?</p>
			</div>
			<div class="doubleInput">
				<span class="input input--minoru">
					<input class="input__field input__field--minoru" name="fname" type="text" id="input-8" placeholder="First name"/>
					<label class="input__label input__label--minoru" for="input-8">
					</label>
				</span>
				<span class="input input--minoru">
					<input class="input__field input__field--minoru" name="lname" type="text" id="input-8" placeholder="Last name"/>
					<label class="input__label input__label--minoru" for="input-8">
					</label>
				</span>
			</div>
			<div class="custom-info">
				<p>Set up your Ease password</p>
			</div>
			<span class="input input--minoru">
				<input class="input__field input__field--minoru" name="password" type="password" id="input-8" placeholder="Password"/>
				<label class="input__label input__label--minoru" for="input-8">
				</label>
			</span>
			<span class="input input--minoru">
				<input class="input__field input__field--minoru" name="confirmPassword" type="password" id="input-8" placeholder="Confirm password"/>
				<label class="input__label input__label--minoru" for="input-8">
				</label>
			</span>
		</form>
		<div class="alertDiv">
			<p></p>
		</div>
		<p class="registrationTips">Set this password carefully.</br>In case you forget it, we won’t be able to recover it for you ! </p>
		<div class="custom-button">
			<button type="submit" form="registrationForm" value="Submit">GO</button>
			<div class="loadHelper centeredItem">
				<div class="sk-fading-circle">
				  <div class="sk-circle1 sk-circle"></div>
				  <div class="sk-circle2 sk-circle"></div>
				  <div class="sk-circle3 sk-circle"></div>
				  <div class="sk-circle4 sk-circle"></div>
				  <div class="sk-circle5 sk-circle"></div>
				  <div class="sk-circle6 sk-circle"></div>
				  <div class="sk-circle7 sk-circle"></div>
				  <div class="sk-circle8 sk-circle"></div>
				  <div class="sk-circle9 sk-circle"></div>
				  <div class="sk-circle10 sk-circle"></div>
				  <div class="sk-circle11 sk-circle"></div>
				  <div class="sk-circle12 sk-circle"></div>
				</div>
			</div>
			<div class="successHelper centeredItem"><p>Success !</p></div>
		</div>
	</div>
	<%@ include file="templates/ChatButton.jsp" %>
</body>
</html>