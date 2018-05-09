<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
	<c:set var="language" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="languages/text" />
<html lang="${language}">
<head>
	<title> Ease.space | Pricing</title>
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<!-- Description shown in Google -->
	<!-- Facebook metadata -->
	<meta name="description"
		  content="Mettez facilement en place une politique de sécurité au niveau de l’utilisation, le partage et le stockage des mots de passe de l’entreprise."/>
	<meta property="og:url" content="https://ease.space/"/>
	<meta property="og:type" content="website"/>
	<meta property="og:title" content="Ease.space - Prenez soin des mots de passe de votre entreprise"/>
	<meta property="og:image" content="https://ease.space/resources/metadescription.png"/>
	<meta property="og:logo" content="https://ease.space/resources/icons/APPEASE.png"/>
	<meta property="og:description"
		  content="Mettez facilement en place une politique de sécurité au niveau de l’utilisation, le partage et le stockage des mots de passe de l’entreprise."/>
	<meta property="og:image" content="https://ease.space/resources/images/metadescription.png"/>
	<!-- Twitter metadata -->
	<meta name="twitter:card" content="summary_large_image"/>
	<meta name="twitter:site" content="@ease_space"/>
	<meta name="twitter:creator" content="@ease_space"/>
	<meta name="twitter:title" content="Ease.space - Prenez soin des mots de passe de votre entreprise"/>
	<meta name="twitter:description"
		  content="Mettez facilement en place une politique de sécurité au niveau de l’utilisation, le partage et le stockage des mots de passe de l’entreprise."/>
	<meta name="twitter:image" content="https://ease.space/resources/images/metadescription.png"/>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />

	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
    <link rel="stylesheet" type="text/css" href="/css/lib/fonts/museo-font.css?cssv=11"/>
    <link rel="stylesheet" href="/css/default_style.css?cssv=11"/>
    <link rel="stylesheet" href="/css/bootstrap.css?cssv=11"/>
    <link rel="stylesheet" href="/css/landingPage.css?cssv=11"/>
    <link rel="stylesheet" href="/css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css?cssv=11"/>
    <link rel="stylesheet" href="/css/pricingPage.css?cssv=11"/>



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
    <script src="/jsMinified/jquery1.12.4.js?jsv=2"></script>
    <script src="/jsMinified/languageChooser.js?jsv=2" async></script>
    <script src="/jsMinified/basic-utils.js?jsv=2" async></script>
    <script src="/jsMinified/ajaxHandler.js?jsv=2" async></script>
    <script src="/jsMinified/jquery.complexify.js?jsv=2" async></script>
    <script src="/jsMinified/registrationPopup.js?jsv=2" async></script>
    <script src="/jsMinified/tracker.js?jsv=2" async></script>
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
                s.src = "https://client.crisp.chat/l.js";
                s.async = 1;
                d.getElementsByTagName("head")[0].appendChild(s);
            })();
		});
	</script>
</body>
</html>