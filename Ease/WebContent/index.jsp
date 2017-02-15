<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.Ease.Dashboard.User.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	
	<title> Ease.space | The easiest way to connect to your professional and personal web.</title>
	<!-- Description shown in Google -->
	<!-- Facebook metadata -->
	<meta name="description" content="Ease is an intuitive password manager working as a browser homepage."/>
	<meta property="og:url" content="http://ease.space/" />
	<meta property="og:type" content="website" />
	<meta property="og:title" content="Ease.space | The easiest way to connect to your professional and personal web."/>
	<meta property="og:description" content="Ease is an intuitive password manager working as a browser homepage."/>
	<meta property="og:image" content="https://ease.space/resources/images/fbmeta-en.png"/>
	
	<!-- Twitter metadata -->
	<meta name="twitter:card" content="summary_large_image"/>
	<meta name="twitter:site" content="@Ease_app"/>
	<meta name="twitter:creator" content="@Ease_app"/>
	<meta name="twitter:title" content="Ease.space | The easiest way to connect to your professional and personal web."/>
	<meta name="twitter:description" content="Ease is an intuitive password manager working as a browser homepage."/>
	<meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png"/>
	
	<link rel="chrome-webstore-item"
	href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<script src="js/jquery1.12.4.js"></script>
	<script src="js/jquery-ui-1.12.0.js"></script>
	<link rel="stylesheet" href="css/default_style.css" />
	<link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro'
	rel='stylesheet' type='textcss' />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway" />
	<link rel="stylesheet" href="css/bootstrap.css" />
	<link rel="stylesheet" href="css/owl.carousel.css" />
	<link rel="stylesheet" href="css/owl.theme.css" />
	<link rel="stylesheet" href="css/owl.transitions.css" />

	<link rel="stylesheet" href="css/lib/vicons-font/vicons-font.css">
	<link rel="stylesheet" href="css/lib/vicons-font/buttons.css">
	<link rel="stylesheet" href="css/lib/borderLoading/component.css">
	<link rel="stylesheet" href="css/lib/niftyPopupWindow/component.css">
	<link rel="stylesheet" href="css/lib/ColorSelect/cs-select.css">
	<link rel="stylesheet" href="css/lib/ColorSelect/cs-skin-boxes.css">
	<link rel="stylesheet" href="css/hover.css">
	<link rel="manifest" href="manifest.json">

	<script src="js/postHandler.js"></script>
	<script src="js/form/form.js"></script>
	<script src="js/form/errorMsg.js"></script>
	<script src="js/form/input.js"></script>
	<script src="js/form/popup.js"></script>
	<script src="js/form/emailSuggestions.js"></script>

	<script src="js/classie.js"></script>
	<script src="js/Sortable.js"></script>
	<script src="js/jquery.binding.js"></script>
	<script src="js/owl.carousel.js"></script>
	<script src="js/basic-utils.js" ></script>
	<script src="js/jquery.mousewheel.min.js"></script>
	<script src="js/footer.js"></script>
	<script src="js/catalog/catalogApp.js"></script>
	<script src="js/catalog/updates/update.js"></script>
	<script src="js/catalog/updates/updatesManager.js"></script>
	<script src="js/catalog/catalog.js"></script>
	<script src="js/extension.js" ></script>
	<script src="js/alertPopup.js"></script>
	<script src="js/loading.js"></script>
	<script src="js/header.js"></script>
	<script src="js/app.js"></script>
	<script src="js/profiles.js"></script>
	<script src="js/shortcut.js"></script>
	<script src="js/dashboard.js"></script>
	<script src="js/tracker.js"></script>
	<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
	<script src="js/snap.svg-min.js"></script>
	<script src="js/modalEffects.js"></script>
	<script src="js/selectFx.js"></script>
	<script src="js/websocket.js"></script>

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

  /* Prod */
	//amplitude.getInstance().init("74f6ebfba0c7743a0c63012dc3a9fef0");

	/* Test */
  amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
</script>

	<!-- Amplitude script -->
	<link rel="stylesheet" type="text/css" href="component.css" />

	<%
	if (user != null){
		SessionSave sessionSave = (SessionSave) (user.getSessionSave());
		if (user.isAdmin()){%>
			<script src="js/robotest.js"></script>
		<%}
	} else {
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
	<script src="js/isMobile.js"></script>
	<script src="js/getNavigator.js"></script>
	<script type="text/javascript">$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="https://client.crisp.im/l.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();</script>

	<!-- start Mixpanel --><script type="text/javascript">(function(e,a){if(!a.__SV){var b=window;try{var c,l,i,j=b.location,g=j.hash;c=function(a,b){return(l=a.match(RegExp(b+"=([^&]*)")))?l[1]:null};g&&c(g,"state")&&(i=JSON.parse(decodeURIComponent(c(g,"state"))),"mpeditor"===i.action&&(b.sessionStorage.setItem("_mpcehash",g),history.replaceState(i.desiredHash||"",e.title,j.pathname+j.search)))}catch(m){}var k,h;window.mixpanel=a;a._i=[];a.init=function(b,c,f){function e(b,a){var c=a.split(".");2==c.length&&(b=b[c[0]],a=c[1]);b[a]=function(){b.push([a].concat(Array.prototype.slice.call(arguments,
0)))}}var d=a;"undefined"!==typeof f?d=a[f]=[]:f="mixpanel";d.people=d.people||[];d.toString=function(b){var a="mixpanel";"mixpanel"!==f&&(a+="."+f);b||(a+=" (stub)");return a};d.people.toString=function(){return d.toString(1)+".people (stub)"};k="disable time_event track track_pageview track_links track_forms register register_once alias unregister identify name_tag set_config reset people.set people.set_once people.increment people.append people.union people.track_charge people.clear_charges people.delete_user".split(" ");
for(h=0;h<k.length;h++)e(d,k[h]);a._i.push([b,c,f])};a.__SV=1.2;b=e.createElement("script");b.type="text/javascript";b.async=!0;b.src="undefined"!==typeof MIXPANEL_CUSTOM_LIB_URL?MIXPANEL_CUSTOM_LIB_URL:"file:"===e.location.protocol&&"//cdn .mxpnl.com/libs/mixpanel-2-latest.min.js".match(/^\/\//)?"https://cdn.mxpnl.com/libs/mixpanel-2-latest.min.js":"//cdn.mxpnl.com/libs/mixpanel-2-latest.min.js";c=e.getElementsByTagName("script")[0];c.parentNode.insertBefore(b,c)}})(document,window.mixpanel||[]);
mixpanel.init("e87ca36e156107ebbd9a672735e6612c");</script><!-- end Mixpanel -->

</head>

<body role="document" class=<%= ((user != null && user.getOptions().isBackground_picked()) ? "'mainBody picBckgrnd'" : "'mainBody logoBckgrnd'") %>>
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
		<%} else {%>
			<%@ include file="templates/Header.jsp"%>
			<%@ include file="templates/loggedBody.jsp"%>
			<%@ include file="templates/Footer.jsp" %>
		<%}%>
	</div>

</body>
</html>
