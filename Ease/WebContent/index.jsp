<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<meta name="description"
	content="The platform Ease allows you to gather all your university web services and your favorites websites (social networks, medias, tools, streaming etc.) in order to connect to them in 1 click, without using neither ids nor passwords! (only one remains: your Ease password)." />
	<title>Ease</title>
	<link rel="chrome-webstore-item"
	href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm">
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<script src="js/jquery1.12.4.js"></script>
	<script src="js/jquery-ui-1.12.0.js"></script>
	<link rel="stylesheet" href="css/default_style.css" />
	<link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro'
	rel='stylesheet' type='textcss' />
	<link rel="stylesheet" href="css/bootstrap.css" />
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
	<link rel="stylesheet" href="css/hover.css">
	<link rel="manifest" href="manifest.json">

	<script src="js/classie.js"></script>
	<script src="js/Sortable.js"></script>
	<script src="js/jquery.binding.js"></script>
	<script src="js/owl.carousel.js"></script>
	<script src="js/basic-utils.js" ></script>
	<script src="js/jquery.mousewheel.min.js"></script>
	<script src="js/footer.js"></script>
	<script src="js/tutorial.js"></script>
	<script src="js/postHandler.js"></script>

	<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
	<script src="js/snap.svg-min.js"></script>
	<script src="js/modalEffects.js"></script>
	<script src="js/selectFx.js"></script>
	<link rel="stylesheet" type="text/css" href="component.css" />
	<%com.Ease.session.User user = (com.Ease.session.User) (session.getAttribute("User"));%>
	<% if (user != null){ %>
	<script src="js/checkConnection.js"></script>
	<%} %>
	<script src="js/isMobile.js"></script>
	<script src="js/setupOwlCarousel.js"></script>
	<script type="text/javascript">
		function getUserNavigator() {
			var ua = navigator.userAgent;

			var x = ua.indexOf("MSIE");
			var y = "MSIE";
			if (x == -1) {
				x = ua.indexOf("Firefox");
				y = "Firefox";
				if (x == -1) {
					if (x == -1) {
						x = ua.indexOf("Chrome");
						y = "Chrome";
						if (x == -1) {
							x = ua.indexOf("Opera");
							y = "Opera";
							if (x == -1) {
								x = ua.indexOf("Safari");
								if (x != -1) {
									x = ua.indexOf("Version");
									y = "Safari";
								}
							}
						}
					}
				}
			}
			return (y);
		}

		function deleteOverlay(item) {
			var suppDiv = $(item).closest('.logoItem');

			suppDiv.remove();
		}
		function onInputFocus(ev) {
			classie.add(ev.target.parentNode, 'input--filled');
		}

		function onInputBlur(ev) {
			if (ev.target.value.trim() === '') {
				classie.remove(ev.target.parentNode, 'input--filled');
			}
		}

		function addAppRequest(item) {
			var profile = $(item).closest('.MobilePreview');
			var logoItem = $(item).closest('.logoItem');
			var content = $(item).closest('.content');
			var login = $(content).find('#login');
			var password = $(content).find('#password');

			$(logoItem).find('.imageBox').append(
				$('<i class="fa fa-spinner tmp"></i>'));
			postHandler.post(
				'addApp',
				{
					login : $(login).val(),
					password : $(password).val(),
					profileId : $(profile).attr("index"),
					siteId : $(logoItem).attr("index")
				}, 
				function(){
					$(logoItem).find('.tmp').removeClass('fa-spinner');
				},
				function(retMsg){
					$(logoItem).find('.tmp').addClass('fa-check');
					setTimeout(function() {
						$(logoItem).find('.tmp').remove();
					}, 1000);
				},
				function(retMsg){
					$(logoItem).find('.tmp').addClass('fa-times');
					$(logoItem).find('.tmp').css("color", "red");
					setTimeout(function() {
						$(logoItem).remove();
					}, 1000);
				},
				'text'
				);
			$(item).closest('.windowAddApp').remove();
		}
l	</script>
</head>

<body role="document" class="mainBody">
	<%@ page import="java.util.Base64" %>
	<%@ page import="java.util.Base64.Encoder" %>
	<%@ page import="java.nio.charset.StandardCharsets" %>
	<div id="userEmail" data-content=<%= (user != null) ? user.getEmail() : null %>></div>

	<div id="onMobile" style="display:none;">
		<%@ include file="templates/Mobile.jsp" %>
	</div>
	
	<div id="onComputer">
		<div class="cookiesInfo" style="display: none;">
			<p>En poursuivant votre navigation, vous acceptez l'utilisation de cookies dans le cadre de l’authentification, la sécurité et l’intégrité du site et des produits.</p>
			<button id="hideCookies"><i class="fa fa-times" aria-hidden="true"></i></button>
		</div>
		<% if (user == null){ %>
		<%@ include file="templates/loginBody.jsp"%>
		<%}else {%>
		<%@ include file="templates/Header.jsp"%>
		<%@ include file="templates/loggedBody.jsp"%>
		<%}%>
		<%@ include file="templates/Footer.jsp" %>
		<div class="la-anim-10" id="loading"></div>
		<%@ include file="templates/SimpleAlertPopup.jsp" %>
		<%@ include file="templates/ChatButton.jsp" %>
	</div>
	
</body>
</html>
