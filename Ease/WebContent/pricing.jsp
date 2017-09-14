<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
	<c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html lang="${language}">
<head>
	<title> Ease.space | Pricing</title>
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<!-- Description shown in Google -->
	<meta name="description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<!-- Facebook metadata -->
	<meta property="og:url" content="https://ease.space/" />
	<meta property="og:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés." />
	<meta property="og:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png" />
	<meta property="og:type" content="website" />
	<!-- Twitter metadata -->
	<meta name="twitter:card" content="summary_large_image" />
	<meta name="twitter:site" content="@Ease_app" />
	<meta name="twitter:creator" content="@Ease_app" />
	<meta name="twitter:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés." />
	<meta name="twitter:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
	<meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png" />
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<meta property="og:image"
	content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />

	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
	<link rel="stylesheet" type="text/css" href="/cssMinified.v00012/lib/fonts/museo-font.css"/>
	<link rel="stylesheet" href="/cssMinified.v00012/default_style.css"/>
	<link rel="stylesheet" href="/cssMinified.v00012/bootstrap.css"/>
	<link rel="stylesheet" href="/cssMinified.v00012/landingPage.css"/>
	<link rel="stylesheet" href="/cssMinified.v00012/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
	<link rel="stylesheet" href="/cssMinified.v00012/pricingPage.css"/>



	<link rel="manifest" href="manifest.json">
</head>

<body id="landingBody">
	<%@ include file="templates/landingPage/landingHeader.jsp"%>
	<section id="pricing">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h1>
						<fmt:message key="pricing.title"/>
					</h1>
				</div>
			</div>
		</div>
		<div class="container" id="prices">
			<div class="row">
				<div class="col-lg-12">
					<h4 class="text-center">
						<fmt:message key="pricing.sub-title"/>
					</h4>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6" id="free">
					<div class="row text-center offerType">
						<strong><fmt:message key="pricing.free.title"/></strong>
					</div>
					<div class="row text-center priceRow">
						<div class="price">
							<img src="/resources/images/8_32e.svg" class="priceImage center-block"/>
						</div>
					</div>
					<div class="row text-center offerTip">
						<p><fmt:message key="pricing.free.sub-tip"/></p>
						<p>9,99€ <fmt:message key="pricing.free.sub-tip-2"/></p>
					</div>
					<div class="row text-center cta">
						<div class="col-lg-8 col-lg-offset-2">
							<p class="floatingTip">
								<fmt:message key="pricing.free.sub-tip-3"/>
							</p>
							<button type="submit" class="btn btn-block btn-lg btn-success">
								<fmt:message key="pricing.free.button-text"/>
							</button>
						</div>
					</div>
					<div class="row features">
						<div class="featureList center-block">
							<p class="featureTitle">
								<fmt:message key="pricing.free.features.title"/>
							</p>
							<ul>
								<li><p><fmt:message key="pricing.free.features.1"/></p></li>
								<li><p><fmt:message key="pricing.free.features.2"/></p></li>
								<li><p><fmt:message key="pricing.free.features.3"/></p></li>
								<li><p><fmt:message key="pricing.free.features.4"/></p></li>
								<li><p><fmt:message key="pricing.free.features.5"/></p></li>
								<li>
									<p><fmt:message key="pricing.free.features.6"/>
										<small class="commingSoon">
											<span class="fa-stack fa-lg">
												<i class="fa fa-circle-thin fa-stack-2x"></i>
												<i class="fa fa-bolt fa-stack-1x"></i>
											</span>
											Coming soon
										</small>
									</p>
								</li>
								<li>
									<p><fmt:message key="pricing.free.features.7"/>
										<small class="commingSoon">
											<span class="fa-stack fa-lg">
												<i class="fa fa-circle-thin fa-stack-2x"></i>
												<i class="fa fa-bolt fa-stack-1x"></i>
											</span>
											Coming soon
										</small>
									</p>
								</li>
								<li>
									<p><fmt:message key="pricing.free.features.8"/>
										<small class="commingSoon">
											<span class="fa-stack fa-lg">
												<i class="fa fa-circle-thin fa-stack-2x"></i>
												<i class="fa fa-bolt fa-stack-1x"></i>
											</span>
											Coming soon
										</small>
									</p>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="col-lg-6" id="#pro">
					<div class="row text-center offerType">
						<strong><fmt:message key="pricing.pro.title"/></strong>
					</div>
					<div class="row text-center priceRow">
						<div class="price">
							<img src="/resources/images/12_49e.svg" class="priceImage center-block"/>
						</div>
					</div>
					<div class="row text-center offerTip">
						<p><fmt:message key="pricing.pro.sub-tip"/></p>
						<p>14,99€ <fmt:message key="pricing.pro.sub-tip-2"/></p>
					</div>
					<div class="row text-center cta">
						<div class="col-lg-8 col-lg-offset-2">
							<p class="floatingTip">
								<fmt:message key="pricing.pro.sub-tip-3"/>
							</p>
							<a href="/companyContact" type="submit" class="btn btn-block btn-lg btn-success">
								<fmt:message key="pricing.pro.button-text"/>
							</a>
						</div>
					</div>
					<div class="row features">
						<div class="featureList center-block">
							<p class="featureTitle">
								<fmt:message key="pricing.pro.features.title"/>
							</p>
							<strong><fmt:message key="pricing.pro.features.title-2"/></strong>
							<ul>
								<li><p><fmt:message key="pricing.pro.features.1"/></p></li>
								<li><p><fmt:message key="pricing.pro.features.2"/></p></li>
								<li><p><fmt:message key="pricing.pro.features.3"/></p></li>
								<li><p><fmt:message key="pricing.pro.features.4"/></p></li>
								<li><p><fmt:message key="pricing.pro.features.5"/></p></li>
								<li><p><fmt:message key="pricing.pro.features.6"/></p></li>
								<li><p><fmt:message key="pricing.pro.features.7"/></p></li>
								<li><p><fmt:message key="pricing.pro.features.8"/></p></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="templates/landingPage/landingFooter.jsp" %>
	<%@ include file="templates/landingPage/registrationPopup.jsp" %>
    <script src="/jsMinified.v00017/jquery1.12.4.js"></script>
    <script src="/jsMinified.v00017/languageChooser.js" async></script>
    <script src="/jsMinified.v00017/basic-utils.js" async></script>
    <script src="/jsMinified.v00017/ajaxHandler.js" async></script>
    <script src="/jsMinified.v00017/jquery.complexify.js" async></script>
    <script src="/jsMinified.v00017/registrationPopup.js" async></script>
    <script src="/jsMinified.v00017/tracker.js" async></script>
	<script type="text/javascript">
		window.addEventListener('load',function(){
			$('#free button[type=submit]').click(function(){
				easeSignUpPopup.open();
			});
		});
	</script>
	<script type="text/javascript">
		window.addEventListener('load',function(){
            $crisp = [];
            CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
            (function () {
                d = document;
                s = d.createElement("script");
                s.src = "/jsMinified.v00017/thirdParty/crisp.js";
                s.async = 1;
                d.getElementsByTagName("head")[0].appendChild(s);
            })();
		});
	</script>
	<script type="text/javascript">
		window.addEventListener('load',function(){
			(function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
                r.async = true;
                r.src = "/jsMinified.v00017/amplitude-analytics.js";
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
	easeTracker.trackEvent("PricingPageVisit");
});
</script>
</body>
</html>