<?xml version="1.0" encoding="UTF-8" ?>
<!-- jsp inits -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@	page import="com.Ease.Dashboard.User.*"%>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.Base64.Encoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<% User user = (User) (session.getAttribute("user"));%>

<% if (user != null){%>
<c:redirect url="/" />
<%}%>
<!-- Amplitude script -->
<%
	Cookie cookie = null;
	Cookie cookies[] = request.getCookies();
	String sessionId = "";
	String token = "";
	String skipLanding = "";
	String email = "";
	String fname = "";

	if (cookies != null) {
		for (int i = 0; i < cookies.length; i++) {
			cookie = cookies[i];
			if ((cookie.getName()).compareTo("sId") == 0) {
				sessionId = cookie.getValue();
			} else if ((cookie.getName()).compareTo("sTk") == 0) {
				token = cookie.getValue();
			} else if ((cookie.getName()).compareTo("skipLanding") == 0) {
				skipLanding = cookie.getValue();
			} else if ((cookie.getName()).compareTo("email") == 0) {
				email = cookie.getValue();
			} else if ((cookie.getName()).compareTo("fname") == 0) {
				fname = cookie.getValue();
			}
		}
	}
	if (email.length() == 0 && skipLanding.length() == 0 && request.getParameter("skipLanding") == null) {%>
		<jsp:forward page="/discover"/>
<%
	}
	if (sessionId.length() > 0 && token.length() > 0) {
%>
<jsp:forward page="connectionWithCookies">
	<jsp:param name="sessionId" value="<%=sessionId%>"/>
	<jsp:param name="token" value="<%=token%>"/>
</jsp:forward>
<%}%>

<%
	boolean knownUser = (email.length() > 0 && fname.length() > 0) ? true : false;
	if (knownUser) {
		try {
			new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException e) {
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(-1);
				cookies[i].setValue(null);
				response.addCookie(cookies[i]);
			}
			fname = "";
			response.sendRedirect("/");
		}
	}
%>
<!-- =================== DOCUMENT START ==================== -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml" xmlns:og="http://ogp.me/ns#">
<head>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<title> Ease.space</title>
	<!-- Description shown in Google -->
	<!-- Facebook metadata -->
	<meta name="description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<meta property="og:url" content="https://ease.space/" />
	<meta property="og:type" content="website" />
	<meta property="og:title" content="Ease.space" />
	<meta property="og:logo" content="https://ease.space/resources/icons/APPEASE.png" />
	<meta property="og:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png" />
	<!-- Twitter metadata -->
	<meta name="twitter:card" content="summary_large_image" />
	<meta name="twitter:site" content="@Ease_app" />
	<meta name="twitter:creator" content="@Ease_app" />
	<meta name="twitter:title" content="Ease.space" />
	<meta name="twitter:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png" />
	
	<link rel="chrome-webstore-item"
	href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />

	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />

	<link rel="stylesheet" href="/cssMinified.v00005/default_style.css" />
	<link rel="stylesheet" href="/cssMinified.v00005/bootstrap.css" />
	<link rel="manifest" href="manifest.json">
</head>

<body role="document" class="mainBody">
	<div id="userEmail" data-content="null"></div>
	<div id="onComputer">
		<div id="loginBody">
			<div class="ease-logo">
				<img src="resources/icons/Ease_logo_blue.svg" />
			</div>

			<%@ include file="/templates/Logout.html" %>
			<div class="popupHandler myshow">
				<div class="sk-fading-circle" id="loading">
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
				<!-- known user popup -->
				<% if (knownUser) {%>
				<div class="easePopup landingPopup <c:if test='${empty param.codeExpiration}'>show</c:if>" id="knownUser">
					<div class="bodysHandler">
						<div class="popupBody show">
							<div class="handler">
								<div class="row">
									<div class="title">
										<p>Hello<br><span><%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %></span> !</p>
									</div>
								</div>
								<form method="POST" action="connection" id="knownUserForm">
									<div class="row text-center">
										<p class="popupText">Please type your password to access your space</p>
									</div>
									<div class="row" style="margin-bottom:0.3vw">
										<input type="hidden" name="email" placeholder="Email" value="<%= email %>"/>
										<input type="password" name="password" placeholder="Password"/>
									</div>
									<div class="row">
										<p class="buttonLink floatRight pwdLostButton">Password lost ?</p>
									</div>
									<div class="row alertDiv text-center">
										<p></p>
									</div>
									<div class="row text-center">
										<button class="btn" type="submit">Login</button>
									</div>
									<div class="row">
										<p class="buttonLink floatLeft otherAccountButton">Other account</p>
										<a class="buttonLink floatRight createAccount" href="/discover">Create an account</a>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<%}%>
				<!-- unknown user popup -->
				<div class="easePopup landingPopup <% if (!knownUser){ %><c:if test='${empty param.codeExpiration}'>show</c:if><% }%>" id="unknownUser">
					<div class="bodysHandler">
						<div class="popupBody show">
							<div class="handler">
								<div class="row">
									<div class="title">
										<p>Hello</p>
									</div>
								</div>
								<form method="POST" action="connection" id="unknownUserForm">
									<div class="row" style="margin-bottom:0.3vw">
										<input type="email" name="email" placeholder="Email"/>
										<input type="password" name="password" placeholder="Password"/>
									</div>
									<div class="row">
										<p class="buttonLink floatRight pwdLostButton">Password lost ?</p>
									</div>
									<div class="row alertDiv text-center">
										<p></p>
									</div>
									<div class="row text-center">
										<button class="btn" type="submit">Login</button>
									</div>
									<div class="row">
										<% if(knownUser) {%>
										<p class="buttonLink floatLeft knownAccountButton"><%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %>'s account</p>
										<%}%>
										<a class="buttonLink floatRight createAccount" href="/discover">Create an account</a>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- password lost popup -->
				<div class="easePopup landingPopup <c:if test='${not empty param.codeExpiration}'>show</c:if>" id="passwordLost">
					<div class="bodysHandler">
						<div class="popupBody show">
							<div class="handler">
								<div class="row">
									<div class="title">
										<p>Lost password ?</p>
									</div>
								</div>
								<form method="POST" action="passwordLost" id="passwordLostForm">
									<div class="row text-center">
										<p class="popupText">For security reasons, resetting your EASE password will delete all account passwords you added to the platform.</p>
									</div>
									<div class="row">
										<input type="email" name="email" placeholder="Email"/>
									</div>
									<div class="row alertDiv text-center <c:if test='${not empty param.codeExpiration}'>show</c:if>">
										<p><c:if test='${not empty param.codeExpiration}'>The link is not valid anymore, please type your email again.</c:if></p>
									</div>
									<div class="row text-center">
										<button class="btn" type="submit">Reset password</button>
									</div>
									<div class="row text-center">
										<p class="buttonLink backButton">Go back</p>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>

			<p class="homepageOnoffContainer displayedByPlugin">
				<span>Homepage</span>
				<span class="onoffswitch">
					<input	type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="homePageSwitch" />
					<label class="onoffswitch-label" for="homePageSwitch"></label>
				</span>
			</p>
		</div>
	</div>
	<div id="onMobile" style="display:none;">
		<%@ include file="templates/Mobile.html" %>
	</div>
	<script src="/jsMinified.v00006/jquery1.12.4.js"></script>
	<script src="/jsMinified.v00006/postHandler.js" async></script>
	<script src="/jsMinified.v00006/basic-utils.js" async></script>
	<script src="/jsMinified.v00006/isMobile.js" async></script>
	<script src="/jsMinified.v00006/connection.js" async></script>
	<script src="/jsMinified.v00006/generalLogout.js" async></script>
	<script src="/jsMinified.v00006/tracker.js" async></script>
	<script>
		window.addEventListener('load',function(){
			$.ajaxSetup({cache: true});
		});
	</script>
	<script>
		window.addEventListener('load',function(){
			setTimeout(function(){
				var event = new CustomEvent("NewEaseUser", {"detail":"anonymous"});
				document.dispatchEvent(event);
			}, 500);
		});
	</script>
	<!--================ THIRD PARTY APIS ========= -->
	<script type="text/javascript">
		window.addEventListener('load',function(){
			(function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
				r.async=true;r.src="/jsMinified.v00006/amplitude-analytics.js";
				r.onload=function(){e.amplitude.runQueuedFunctions()};var i=t.getElementsByTagName("script")[0];
				i.parentNode.insertBefore(r,i);function s(e,t){e.prototype[t]=function(){this._q.push([t].concat(Array.prototype.slice.call(arguments,0)));
					return this}}var o=function(){this._q=[];return this};var a=["add","append","clearAll","prepend","set","setOnce","unset"];
					for(var u=0;u<a.length;u++){s(o,a[u])}n.Identify=o;var c=function(){this._q=[];return this;
					};var p=["setProductId","setQuantity","setPrice","setRevenueType","setEventProperties"];
					for(var l=0;l<p.length;l++){s(c,p[l])}n.Revenue=c;var d=["init","logEvent","logRevenue","setUserId","setUserProperties","setOptOut","setVersionName","setDomain","setDeviceId","setGlobalUserProperties","identify","clearUserProperties","setGroup","logRevenueV2","regenerateDeviceId"];
						function v(e){function t(t){e[t]=function(){e._q.push([t].concat(Array.prototype.slice.call(arguments,0)));
						}}for(var n=0;n<d.length;n++){t(d[n])}}v(n);n.getInstance=function(e){e=(!e||e.length===0?"$default_instance":e).toLowerCase();
						if(!n._iq.hasOwnProperty(e)){n._iq[e]={_q:[]};v(n._iq[e])}return n._iq[e]};e.amplitude=n;
					})(window,document);

					amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
					easeTracker.trackEvent("LoginpageVisit");
				});
			</script>
			<script type="text/javascript">
				window.addEventListener('load',function(){
					$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="/jsMinified.v00006/crisp.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();
				});
			</script>
		</body>
		</html>