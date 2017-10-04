<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<section id="footer">
	<div class="container">
		<div class="row">
			<div class="left-part left">
				<div class="left-menu ui row menu-column">
					<div class="links">
						<a href="product">
							<fmt:message key="landing.footer.section-product.title"/>
						</a>
						<a href="security">
							<fmt:message key="landing.header.security-link"/>
						</a>
						<a href="pricing">
							<fmt:message key="landing.header.price-link"/>
						</a>
						<a href="/?skipLanding=true">
							<fmt:message key="landing.header.connexion-link"/>
						</a>
						<a href="/teams#/registration">
							<fmt:message key="landing.footer.link.create-account"/>
						</a>
					</div>
				</div>
				<div class="right-menu ui row menu-column">
					<div class="links">
						<a href="equipe">
							<fmt:message key="landing.header.team-link"/>
						</a>
						<a href="contact">
							<fmt:message key="landing.header.contact-link"/>
						</a>
						<a href="/resources/CGU_Ease.pdf" target="_blank">
							<fmt:message key="landing.footer.link.terms"/>
						</a>
						<div><%@ include file="../../templates/LanguageChooser.jsp" %></div>
					</div>
				</div>
			</div>
			<div class="ui row easeExplore right">
				<div class="logo">
					<img class="ui small image" src="resources/icons/Ease_logo_blue.svg"/>
				</div>
				<div class="social_links">
					<div class="link">
						<a href="https://www.linkedin.com/company/ease.space"  target="_blank"><i class="fa fa-linkedin"></i></a>
					</div>
					<div class="link">
						<a href="https://twitter.com/ease_space"  target="_blank"><i class="fa fa-twitter"></i></a>
					</div>
					<div class="link">
						<a href="https://www.facebook.com/YourEaseSpace/"  target="_blank"><i class="fa fa-facebook"></i></a>
					</div>
					<div class="link">
						<a href="https://unsplash.com/@ease_space/collections"  target="_blank"><i class="fa fa-camera"></i></a>
					</div>
					<div class="link">
						<a href="https://www.instagram.com/ease.space/"  target="_blank"><i class="fa fa-instagram"></i></a>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
