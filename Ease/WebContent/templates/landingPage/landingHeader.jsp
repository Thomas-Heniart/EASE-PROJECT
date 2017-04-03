<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top affix">
	<div class="container">
		<div class="navbar-header page-scroll">
			<a class="navbar-brand page-scroll" href="discover"><img src="resources/landing/ease-white-logo.svg" /></a>
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
					<a href="pricing">
						<fmt:message key="landing.header.price-link" />
					</a>
				</li>
				<li>
					<a href="contact.jsp">
						<fmt:message key="landing.header.contact-link" />
					</a>
				</li>
				<li>
					<a href="/?skipLanding=true" id="connexionButton">
						<fmt:message key="landing.header.connexion-link" />
					</a>
				</li>
				<li>
					<%@ include file="/templates/LanguageChooser.jsp" %>
				</li>
			</ul>
		</div>
	</div>
</nav>

