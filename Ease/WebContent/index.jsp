<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.Ease.Dashboard.User.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml" xmlns:og="http://ogp.me/ns#	">
<head>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<title> Ease.space</title>
	<!-- Description shown in Google -->
	<!-- Facebook metadata -->
	<meta name="description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<meta property="og:url" content="https://ease.space/" />
	<meta property="og:type" content="website" />
	<meta property="og:title" content="Ease.space" />
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

	<link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro'
	rel='stylesheet' type='textcss' />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway" />

	<link rel="stylesheet" href="css/default_style.css" />
	<link rel="stylesheet" href="css/bootstrap.css" />
	<link rel="stylesheet" href="css/lib/niftyPopupWindow/component.css">
	<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />

	<link rel="manifest" href="manifest.json">

	<script src="js/jquery1.12.4.js"></script>
	<script src="js/jquery-ui-1.12.0.js"></script>

	<script src="/js/asyncContentLoading.js"></script>
	<script src="js/postHandler.js" ></script>

	<script src="js/tracker.js"></script>
	<script src="js/Sortable.js" defer></script>
	<script src="js/jquery.binding.js" defer></script>
	<script src="js/basic-utils.js" defer></script>
	<script src="js/extension.js" defer></script>
	<script src="js/header.js" defer></script>
	<script src="js/app.js" defer></script>
	<script src="js/profiles.js" defer></script>
	<script src="js/dashboard.js" defer></script>
	<script src="js/selectFx.js" defer></script>
	<script src="js/websocket.js" defer></script>

	<script src="js/isMobile.js" async></script>
	<script src="js/getNavigator.js" async></script>
	<script src="js/shortcut.js" async></script>

	<% User user = (User) (session.getAttribute("user"));%>

	<!-- Amplitude script -->

	<script type="text/javascript">
  (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
  r.async=true;r.src="https://d24n15hnbwhuhn.cloudfront.net/libs/amplitude-3.0.1-min.gz.js";
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
</script>

	<!-- Amplitude script -->
	<%
	if (user != null){
		SessionSave sessionSave = (SessionSave) (user.getSessionSave());
		if (user.isAdmin()){%>
			<script src="js/robotest.js" async></script>
		<%}%>
	<%} else {
		Cookie 	cookie = null;
		Cookie 	cookies[] = request.getCookies();
		String	sessionId = "";
		String 	token = "";
		String 	skipLanding = "";
		String  email = "";
		if (cookies != null){
			for (int i = 0;i < cookies.length ; i++) {
				cookie = cookies[i];
				if((cookie.getName()).compareTo("sId") == 0){
					sessionId = cookie.getValue();
				} else if((cookie.getName()).compareTo("sTk") == 0){
					token = cookie.getValue();
				} else if ((cookie.getName()).compareTo("skipLanding") == 0) {
					skipLanding = cookie.getValue();
				} else if ((cookie.getName()).compareTo("email") == 0){
					email = cookie.getValue();
				}
			}
		}
		if (email.length() == 0 && skipLanding.length() == 0 && request.getParameter("skipLanding") == null){%>
			<jsp:forward page="discover" />
		<%}
		if ((cookie.getName()).compareTo("email") == 0){
			email = cookie.getValue();
		}
		if(sessionId.length() > 0 && token.length() > 0){ %>
			<jsp:forward page="connectionWithCookies">
				<jsp:param name="sessionId" value="<%=sessionId%>" />
				<jsp:param name="token" value="<%=token%>" />
			</jsp:forward>
		<%}
	}%>
	<script type="text/javascript">$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="https://client.crisp.im/l.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();</script>
</head>

<body role="document" class=<%= ((user != null && user.getOptions().isBackground_picked()) ? "'mainBody picBckgrnd'" : "'mainBody logoBckgrnd'") %>>
	<%@ page import="java.util.Base64" %>
	<%@ page import="java.util.Base64.Encoder" %>
	<%@ page import="java.nio.charset.StandardCharsets" %>
	<script>
		$.ajaxSetup({cache: true});
	</script>
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
		<%} else {%>
			<div class="header">
				<%@ include file="templates/Header.jsp"%>
			</div>
			<%@ include file="templates/loggedBody.jsp"%>
			<script src="js/checkConnection.js" async></script>
		<script type="text/javascript">
			$(document).ready(function(){
				asyncLoading.loadHtml({urls:['/templates/Footer.jsp'], appendTo : '#onComputer'});
				asyncLoading.loadScripts({urls: ['/js/footer.js'], async: true});
			});
			</script>
		<%}%>
	</div>
</body>
</html>