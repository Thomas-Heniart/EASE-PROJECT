<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="languages/text" />
<html lang="${language}">
<head>
	<title> Ease.space | Team</title>
	<!-- Description shown in Google -->
	<meta name="description" content="The Ease Team works to ease your web life experience and fulfil your expectations.">
	
	
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
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
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
    <link rel="stylesheet" type="text/css" href="/css/lib/fonts/museo-font.css?cssv=5"/>



	<link rel="manifest" href="manifest.json">

    <script src="/jsMinified/jquery1.12.4.js?jsv=2"></script>
    <script src="/jsMinified/bootstrap.js?jsv=2"></script>
    <script src="/jsMinified/basic-utils.js?jsv=2"></script>
    <script src="/jsMinified/tracker.js?jsv=2"></script>
    <script src="/jsMinified/postHandler.js?jsv=2"></script>
    <script src="/jsMinified/languageChooser.js?jsv=2"></script>

	<link rel="stylesheet" href="semantic/dist/semantic.min.css">
    <link rel="stylesheet" href="/css/default_style.css?cssv=5"/>
    <link rel="stylesheet" href="/css/bootstrap.css?cssv=5"/>
    <link rel="stylesheet" href="/css/landingPage.css?cssv=5"/>
    <link rel="stylesheet" href="/css/teamBody.css?cssv=5"/>
    <link rel="stylesheet" href="/css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css?cssv=5"/>
    <script type="text/javascript">$crisp = [];
    CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
    (function () {
        d = document;
        s = d.createElement("script");
        s.src = "https://client.crisp.chat/l.js";
        s.async = 1;
        d.getElementsByTagName("head")[0].appendChild(s);
    })();</script>
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