<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<section id="userFeedbacks">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">
						<fmt:message key="landing.section-feedbacks.title"/>
					</h2>
					<h3 class="section-subheading text-muted">
						<fmt:message key="landing.section-feedbacks.sub-title"/>
					</h3>
				</div>
			</div>
			<div class="row">
				<div class="comment">
					<div class="photo">
						<img src="resources/landing/JPA.png"/>
					</div>
					<div class="message">
						<p class="mess">
							<fmt:message key="landing.section-feedbacks.text-1"/> 
						</p>
						<a href="">Jean Philippe Ammeux</a>
						<p class="position">Directeur de IESEG School of Management</p>
					</div>
				</div>
				<div class="comment">
					<div class="photo">
						<img src="resources/landing/alice-zagury.png"/>
					</div>
					<div class="message">
						<p class="mess">
							<fmt:message key="landing.section-feedbacks.text-2"/> 
						</p>
						<a href="https://fr.linkedin.com/in/alicezagu" target="_blank">Alice Zagury</a>
						<p class="position">CEO de TheFamily</p>
					</div>
				</div>
				<div class="comment">
					<div class="photo">
						<img src="resources/landing/clem.png"/>
					</div>
					<div class="message">
						<p class="mess">
							<fmt:message key="landing.section-feedbacks.text-3"/> 
						</p>
						<a  target="_blank" href="https://fr.linkedin.com/in/clémentine-prud-homme-691945103">Clémentine Prud’homme</a>
						<p class="position">Etudiante à l’EDHEC</p>
					</div>
				</div>
				<div class="comment">
					<div class="photo">
						<img src="resources/landing/TDesmarets.png"/>
					</div>
					<div class="message">
						<p class="mess">
							<fmt:message key="landing.section-feedbacks.text-4"/>  
						</p>
						<a target="_blank" href="https://fr.linkedin.com/in/thibaut-desmarets-13a390139">Thibaut Desmarets</a>
						<p class="position">Président du BDE de l'ESPAS</p>
					</div>
				</div>
			</div>
		</div>
	</section>