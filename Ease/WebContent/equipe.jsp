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
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00022/lib/fonts/museo-font.css"/>



	<link rel="manifest" href="manifest.json">

    <script src="/jsMinified.v00022/jquery1.12.4.js"></script>
    <script src="/jsMinified.v00022/bootstrap.js"></script>
    <script src="/jsMinified.v00022/basic-utils.js"></script>
    <script src="/jsMinified.v00022/tracker.js"></script>
    <script src="/jsMinified.v00022/postHandler.js"></script>
    <script src="/jsMinified.v00022/languageChooser.js"></script>

	<link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" href="/cssMinified.v00022/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00022/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00022/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00022/teamBody.css"/>
    <link rel="stylesheet" href="/cssMinified.v00022/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <script type="text/javascript">$crisp = [];
    CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
    (function () {
        d = document;
        s = d.createElement("script");
        s.src = "https://client.crisp.chat/l.js";
        s.async = 1;
        d.getElementsByTagName("head")[0].appendChild(s);
    })();</script>
	<script type="text/javascript">
		(function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
            r.async = true;
            r.src = "/jsMinified.v00022/amplitude-analytics.js";
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

        if (location.hostname === "ease.space")
            amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
        else
            amplitude.getInstance().init("5f012a5e604acb0283ed11ed8da5414f");
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
					<p class="join-us"><a href="https://easespace.welcomekit.co" target="_blank"><u><fmt:message key="team.underTitle.joinUs"/></u></a></p>
				</div>
			</div>
			<div class="row">
				<div id="members" class="ui grid" style="justify-content: center;">
					<div class="ui row cards">
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/ben.jpg"/>
							</div>
							<div class="content">
								<div class="header name">
									<p>Benjamin Prigent</p>
								</div>
								<div class="post">
									<p>Customer Care</p>
								</div>
							</div>
						</div>
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/lanive.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Victor Nivet</p>
								</div>
								<div class="post">
									<p>Product Manager</p>
								</div>
							</div>
						</div>
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/fifi.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Sergii Fisun</p>
								</div>
								<div class="post">
									<p>Front-end Developer</p>
								</div>
							</div>
						</div>
					</div>
					<div class="ui row cards">
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/Toto.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Thomas Heniart</p>
								</div>
								<div class="post">
									<p>Security & Back-end</p>
								</div>
							</div>
						</div>
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/victorien.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Victorien Caquant</p>
								</div>
								<div class="post">
									<p>Extension Developer</p>
								</div>
							</div>
						</div>
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/Fefe.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Félix Richard</p>
								</div>
								<div class="post">
									<p>Security Engineer</p>
								</div>
							</div>
						</div>
					</div>
					<div class="ui row cards">
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/ethan.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Ethan Adjedj</p>
								</div>
								<div class="post">
									<p>Growth Hacker</p>
								</div>
							</div>
						</div>
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/alice.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Alice Zagury</p>
								</div>
								<div class="post">
									<p>Advisor</p>
								</div>
							</div>
						</div>
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/abraham.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Abraham Thomas</p>
								</div>
								<div class="post">
									<p>Advisor</p>
								</div>
							</div>
						</div>
					</div>
					<div class="ui row cards">
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/alex.png"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Alexandre Nicolau</p>
								</div>
								<div class="post">
									<p>Advisor</p>
								</div>
							</div>
						</div>
						<div class="card memberDescription">
							<div class="memberPhoto">
								<img src="resources/team/yves.jpg"/>
							</div>
							<div class="content">
								<div class="name">
									<p>Yves Delnatte</p>
								</div>
								<div class="post">
									<p>Advisor</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div style="margin: auto; border-bottom: 1px solid #979797; width:200px; margin-top: 120px;"></div>
			<div>
				<p class="join-us" style="margin-top: 30px;"><a href="https://easespace.welcomekit.co"  target="_blank"><u><fmt:message key="team.joinUs"/></u></a></p>
			</div>
		</section>
		<%@ include file="templates/landingPage/landingFooter.jsp" %>
	</body>
	</html>