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
						<img src="resources/landing/Sylvain_Montant.png"/>
					</div>
					<div class="message">
						<p class="mess">
							<fmt:message key="landing.section-feedbacks-edhec.text-1"/> 
						</p>
						<a href="https://www.linkedin.com/in/sylvain-montant-482a59105">Sylvain Montant</a>
						<p class="position">Président de Total EDHEC Entreprendre</p>
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
						<img src="resources/landing/Anne-Victoire_de_Mirman.png"/>
					</div>
					<div class="message">
						<p class="mess">
							<fmt:message key="landing.section-feedbacks.text-3"/> 
						</p>
						<a  target="_blank" href="https://www.linkedin.com/in/anne-victoire-de-mirman-73652b109/">Anne-Victoire de Mirman</a>
						<p class="position">Master 2 Panthéon Assas</p>
					</div>
				</div>
				<div class="comment">
					<div class="photo">
						<img src="resources/landing/Marc_Traverse.png"/>
					</div>
					<div class="message">
						<p class="mess">
							<fmt:message key="landing.section-feedbacks.text-4"/>  
						</p>
						<a target="_blank" href="https://www.linkedin.com/in/marc-traverse-187118113/">Marc Traverse</a>
						<p class="position">Président du BDE Osmoz</p>
					</div>
				</div>
			</div>
		</div>
	</section>