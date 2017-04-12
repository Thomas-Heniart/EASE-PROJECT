<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
<c:set var="language" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html lang="${language}">
<head>
	<title> Ease.space | Le meilleur moyen de se connecter à ses sites préférés.</title>
	<!-- Description shown in Google -->
	<meta name="description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<!-- Facebook metadata -->
	<meta property="og:url" content="https://ease.space/" />
	<meta property="og:type" content="website" />
	<meta property="og:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés." />
	<meta property="og:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png" />
	<!-- Twitter metadata -->
	<meta name="twitter:card" content="summary_large_image"/>
	<meta name="twitter:site" content="@Ease_app"/>
	<meta name="twitter:creator" content="@Ease_app"/>
	<meta name="twitter:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés."/>
	<meta name="twitter:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
	<meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png"/>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<meta property="og:image"
	content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />



	<link rel="manifest" href="manifest.json">

	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
	<link rel="stylesheet" href="/cssMinified.v00005/default_style.css" />
	<link rel="stylesheet" href="/cssMinified.v00005/bootstrap.css" />
	<link rel="stylesheet" href="/cssMinified.v00005/landingPage.css" />
	<link rel="stylesheet" type="text/css" href="/cssMinified.v00005/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
</head>

<body id="landingBody" class="school">
	<!-- Navigation -->
	<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
		<div class="container">
			<div class="navbar-header page-scroll">
				<a class="navbar-brand page-scroll" href="/"><img src="resources/landing/ease-white-logo.svg" /></a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li class="hidden">
						<a href="#page-top"></a>
					</li>
					<li>
						<a href="secure.jsp" target="_blank">
							<fmt:message key="landing.header.security-link" />
						</a>
					</li>
					<li>
						<a href="/pricing">
							<fmt:message key="landing.header.price-link" />
						</a>
					</li>
					<li>
						<a href="contact.jsp">
							<fmt:message key="landing.header.contact-link" />
						</a>
					</li>
					<li>
						<a href="/?skipLanding=true" id="connexionButton" target="_blank">
							<fmt:message key="landing.header.connexion-link" />
						</a>
					</li>
					<li>
						<%@ include file="templates/LanguageChooser.jsp" %>
					</li>
				</ul>
			</div>
		</div>
	</nav>
	<header>
		<div class="container">
			<div class="intro-text">
				<div class="intro-heading">
					<fmt:message key="landing.school.title" />
				</div>
				<a href="#services" class="btn btn-xl signUpButton" trackEvent="HomepageSignUpButton1">
					<c:if test="${param.name != null}">
					<c:out value="${param.name}" />,
					<br>
				</c:if>
				<fmt:message key="schoolLanding.signup-button.text" />
			</a>
			<div class="intro-lead-in">
				<fmt:message key="schoolLanding.sub-title" />
			</div>
			<div class="schoolLogos">
				<c:forTokens items="${param.schoolImageSrcs}" delims="," var="name">
				<div class="logoHandler">
					<img src=<c:out value="${name}"/> />
				</div>
			</c:forTokens>
		</div>
	</div>
</div>
</header>
<section id="productPresentation">
	<div class="container">
		<div class="row">
			<div class="col-lg-12 text-center">
				<h2 class="section-heading">
					<fmt:message key="schoolLanding.section-presentation.title1" />
					<c:out value="${param.schoolName}"/>
					<fmt:message key="schoolLanding.section-presentation.title2" />
				</h2>
				<h3 class="section-subheading text-muted">
					<fmt:message key="landing.section-presentation.sub-title" />
				</h3>
			</div>
		</div>
		<div class="mockupPresentation">
			<video width="100%" height="auto" controls="noshow">
				<source src="resources/videos/${param.video}" type="video/mp4">
					Your browser does not support the video tag.
				</video>
			</div>
		</div>
	</section>
	<section id="features" style="background-color: #eee;">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">
						<fmt:message key="landing.section-features.title"/>
					</h2>
					<h3 class="section-subheading text-muted">
						<fmt:message key="landing.section-features.sub-title"/>
					</h3>
				</div>
			</div>
			<div class="row">
				<div class="fList left">
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-1"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-2"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-3"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-4"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-5"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
				</div>
				<div class="fList right">
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-6"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-7"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-8"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>
					<div class="feature">
						<span>
							<fmt:message key="landing.section-features.sentence-9"/>
						</span>
						<i class="fa fa-check" aria-hidden="true"></i>
					</div>

				</div>
			</div>
		</div>
	</section>
	<section class="vp" style="background-color:#eee;">
		<div clas="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h3 class="section-subheading">
						<fmt:message key="schoolLanding.2ndvp.title1"/>
						<c:out value="${param.schoolName}"/>
						<fmt:message key="schoolLanding.2ndvp.title2"/>
					</h3>
				</div>
			</div>
			<div class="row">
				<a href="#services" class="btn btn-xl signUpButton" trackEvent="HomeSignUpButton3">
					<fmt:message key="landing.signup-button.3rd-text"/>
				</a>
			</div>
		</div>
	</section>
	<jsp:include page="templates/landingPage/${param.commentFile}" flush="true"/>
	<%@ include file="templates/landingPage/landingFooter.jsp" %>
	<%@ include file="templates/landingPage/registrationPopup.jsp" %>
</body>
<script src="/jsMinified.v00005/jquery1.12.4.js"></script>
<script src="/jsMinified.v00005/jquery.complexify.js" ></script>
<script src="/jsMinified.v00005/bootstrap.js" async></script>
<script src="/jsMinified.v00005/registrationPopup.js" async></script>
<script src="/jsMinified.v00005/landingPage.js" async></script>
<script src="/jsMinified.v00005/basic-utils.js" async></script>
<script src="/jsMinified.v00005/postHandler.js" async></script>
<script src="/jsMinified.v00005/languageChooser.js" async></script>
<script src="/jsMinified.v00005/tracker.js" async></script>
<script type="text/javascript">
	window.addEventListener('load',function(){
		$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="/jsMinified.v00005/crisp.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();
	});
</script>
<script type="text/javascript">
	window.addEventListener('load',function(){
		(function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
			r.async=true;r.src="/jsMinified.v00005/amplitude-analytics.js";
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
				easeTracker.trackEvent("HomepageVisit", {"SchoolName": schoolToTrack});
			});
			var schoolToTrack = '${param.schoolName}';
		</script>
		<script type="text/javascript">
			window.addEventListener('load',function(){
				$('.signUpButton').click(function(){
					easeTracker.trackEvent($(this).attr("trackEvent"), {"SchoolName": schoolToTrack});
					easeSignUpPopup.open();
				});
				if ($('#2').find("input[name='email']").val() != 'null'){
					$("#1 input[name='email']").val($('#2').find("input[name='email']").val());
					$("#1 input[name='name']").val($('#2').find("input[name='fname']").val());
				}
				$("#connexionButton").click(function() {
					easeTracker.trackEvent("HomepageLogin", {"SchoolName": schoolToTrack});
				});
			});
		</script>
		</html>