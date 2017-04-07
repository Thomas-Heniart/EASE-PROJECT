<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html lang="${language}">
<head>
	<title> Ease.space | Team</title>
	<!-- Description shown in Google -->
	<meta name="description" content="The Ease Team works to ease your web life experience and fulfil your expectations.">
	
	
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<meta property="og:image"
	content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />



	<link rel="manifest" href="manifest.json">

	<script	src="/jsMinified.v00004/jquery1.12.4.js"></script>
	<script src="/jsMinified.v00004/bootstrap.js"></script>
	<script src="/jsMinified.v00004/basic-utils.js"></script>
	<script src="/jsMinified.v00004/tracker.js"></script>
	<script src="/jsMinified.v00004/postHandler.js"></script>
	<script src="/jsMinified.v00004/languageChooser.js"></script>

	<link rel="stylesheet" href="/cssMinified.v00004/default_style.css" />
	<link rel="stylesheet" href="/cssMinified.v00004/bootstrap.css" />
	<link rel="stylesheet" href="/cssMinified.v00004/landingPage.css" />
	<link rel="stylesheet" href="/cssMinified.v00004/teamBody.css" />
	<link rel="stylesheet" href="/cssMinified.v00004/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
	<script type="text/javascript">$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="/jsMinified.v00004/crisp.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();</script>
	<script type="text/javascript">
		(function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
			r.async=true;r.src="/jsMinified.v00004/amplitude-analytics.js";
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
	easeTracker.trackEvent("HomepageTeam");
</script>
</head>

<body id="teamBody">
	<!-- Navigation -->
	<%@ include file="templates/landingPage/landingHeader.jsp"%>

	<section id="team">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">
						<fmt:message key="team.title" />
					</h2>
				</div>
			</div>
			<div class="row">
				<div id="members">
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/ben.png"/>
						</div>
						<div class="name">
							<a>Benjamin Prigent</a>
						</div>
						<div class="post">
							<p>CEO</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/lanive.png"/>
						</div>
						<div class="name">
							<a>Victor Nivet</a>
						</div>
						<div class="post">
							<p>COO</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/Fefe.png"/>
						</div>
						<div class="name">
							<a>Félix Richard</a>
						</div>
						<div class="post">
							<p>Data security</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/Fifi.png"/>
						</div>
						<div class="name">
							<a>Sergii Fisun</a>
						</div>
						<div class="post">
							<p>Design</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/Pedro.png"/>
						</div>
						<div class="name">
							<a>Pierre De Bruyne</a>
						</div>
						<div class="post">
							<p>Infrastructure</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/Toto.png"/>
						</div>
						<div class="name">
							<a>Thomas Heniart</a>
						</div>
						<div class="post">
							<p>Back-end</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/ines.png"/>
						</div>
						<div class="name">
							<a>Inès Yous</a>
						</div>
						<div class="post">
							<p>Care</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/alex.png"/>
						</div>
						<div class="name">
							<a>Alexandre Nicolau</a>
						</div>
						<div class="post">
							<p>Marketing</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/agathe.png"/>
						</div>
						<div class="name">
							<a>Agathe Meslier</a>
						</div>
						<div class="post">
							<p>Law</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/barthos.png"/>
						</div>
						<div class="name">
							<a>Barthelemy Leveque</a>
						</div>
						<div class="post">
							<p>Analyst</p>
						</div>
					</div>
					<div class="memberDescription">
						<div class="memberPhoto">
							<img src="resources/team/Adrien.png"/>
						</div>
						<div class="name">
							<a>Adrien Guillen</a>
						</div>
						<div class="post">
							<p>Advisor</p>
						</div>
					</div>
				</div>
			</div>
		</section>
		<%@ include file="templates/landingPage/landingFooter.jsp" %>
	</body>
	</html>