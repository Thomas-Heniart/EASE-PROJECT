<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html lang="${language}">
<head>
	
	<title> Ease.space | The easiest way to connect to your professional and personal web.</title>
	<!-- Description shown in Google -->
	<meta name="description" content="Ease is an intuitive password manager working as a browser homepage."/>
	<!-- Facebook metadata -->
	<meta property="og:url" content="https://www.ease.space/"/>
	<meta property="og:type" content="website"/>
	<meta property="og:title" content="Ease.space | The easiest way to connect to your professional and personal web."/>
	<meta property="og:description" content="Ease is an intuitive password manager working as a browser homepage."/>
	<meta property="og:image" content="https://ease.space/resources/images/fbmeta-en.png"/>
	
	<!-- Twitter metadata -->
	<meta name="twitter:card" content="summary_large_image"/>
	<meta name="twitter:site" content="@Ease_app"/>
	<meta name="twitter:creator" content="@Ease_app"/>
	<meta name="twitter:title" content="Ease.space | The easiest way to connect to your professional and personal web."/>
	<meta name="twitter:description" content="Ease is an intuitive password manager working as a browser homepage."/>
	<meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png"/>
	
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<meta property="og:image"
	content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
	<link rel="stylesheet" href="css/default_style.css" />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Droid+Serif" />
	<link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro' rel='stylesheet' type='textcss' />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway" />



	<link rel="manifest" href="manifest.json">

	<script src="js/basic-utils.js"></script>
	<script src="js/postHandler.js"></script>
	<script src="js/websocket.js"></script>
	<script src="js/checkForInvitation.js"></script>
	<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js">
</script>
<link rel="stylesheet" href="css/default_style.css" />
<link rel="stylesheet" href="css/landingPage.css" />
<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
<script src="js/selectFx.js"></script>
<script src="js/jquery.complexify.min.js"></script>
<script type="text/javascript">$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="https://client.crisp.im/l.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();</script>
<script src="js/tracker.js"></script>
<script type="text/javascript">
	(function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
		r.async=true;r.src="https://d24n15hnbwhuhn.cloudfront.net/libs/amplitude-3.0.1-min.gz.js";
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
	easeTracker.trackEvent("HomepageVisit");
</script>
</head>

<body id="landingBody" class="school">
	<!-- Navigation -->
	<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
		<div class="container">
			<div class="navbar-header page-scroll">
				<a class="navbar-brand page-scroll" href="header"><img src="resources/landing/ease-white-logo.png" /></a>
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
						<a href="equipe.jsp">
							<fmt:message key="landing.header.team-link" />
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
					<fmt:message key="landing.title" />
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
				<source src="resources/videos/video7.mp4" type="video/mp4">
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
	<%@ include file="templates/landingPage/landingFooter.jsp" %>
	<script src="js/landingPage.js"></script>
	<script src="js/bootstrapjq.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
	<div class="popupHandler">
		<div class="overlay"></div>
		<div class="easePopup show" id="signUpPopup">
			<div class="title">
				<p>
					<fmt:message key="landing.signup-popup.title"/>
				</p>
			</div>
			<div class="bodysHandler">
				<div class="popupBody" id="2">
					<form class="handler" action="register">
<!--							<div class="row">
								<p class="row-heading">
									How would you like us to call you ?
								</p>
							</div>-->
							<div class="row">
								<p class="row-heading">
									<fmt:message key="landing.signup-popup.page-2.password-title"/>
								</p>
								<div class="infoText">
									<p>
										<span>Info:</span> <fmt:message key="landing.signup-popup.page-2.password-info"/>
									</p>
								</div>
							</div>
							<div class="row">
								<input type="hidden" name="fname" value=<%=request.getParameter("name")%> />
								<input type="hidden" name="email" value=<%= request.getParameter("email")%> />
								<input type="hidden" name="invitationCode" value=<%= request.getParameter("invitationCode") %> />
								<span class="input">
									<input type="password" name="password" placeholder=<fmt:message key="landing.signup-popup.page-2.password-placeholder"/> />
									<div class="showPassDiv">
										<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
										<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
									</div>
									<div id="validatorPassword" class="passwordValidator" style="display:none">
										<i class="fa fa-times error" aria-hidden="true" style="color:#d75a4a;"></i>
										<i class="fa fa-check success" aria-hidden="true" style="color:#24d666;"></i>
									</div>
									<img src="./resources/icons/error.png" id="validatorPassword" style="display:none;"/>
								</span>
								<div class="progress">
									<div class="progress-bar">										
									</div>
								</div>
							</div>
							<div class="row">
								<span class="input">
									<input type="password" name="confirmPassword" placeholder=<fmt:message key="landing.signup-popup.page-2.password-confirm-placeholder"/> />
									<div class="showPassDiv">
										<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
										<i class="fa fa-eye-slash centeredItem" aria-hidden="true"></i>
									</div>
									<div id="validatorConfirmPass" class="passwordValidator" style="display:none">
										<i class="fa fa-times error" aria-hidden="true" style="color:#d75a4a;"></i>
										<i class="fa fa-check success" aria-hidden="true" style="color:#24d666;"></i>
									</div>
								</span>
							</div>
							<div class="row text-center">
								<img class="loading" src="resources/other/facebook-loading.svg"/>
								<button class="btn submitButton" type="submit">
									<fmt:message key="landing.signup-popup.page-2.button"/>
								</button>
								<p class="alert-message"></p>
							</div>
						</form>
					</div>
					<div class="popupBody show" id="1">
						<form class="handler" action="checkInvitation">
							<div class="row">
								<p class="row-heading">
									<fmt:message key="landing.signup-popup.page-2.name-title"/>
								</p>
								<input type="text" name="name" placeholder="Name..." />
							</div>
							<div class="row">
								<p class="row-heading">
									<fmt:message key="landing.signup-popup.email-title"/>
								</p>
								<input type="email" name="email" placeholder="@something..." />
							</div>
							<div class="row terms">
								<p><fmt:message key="landing.signup-popup.page-1.terms-accept"/> <a href="privacy.jsp" target="_blank"><fmt:message key="landing.signup-popup.page-1.terms"/></a></p>
							</div>
							<div class="row text-center">
								<img class="loading" src="resources/other/facebook-loading.svg"/>
								<button class="btn submitButton" type="submit">
									<fmt:message key="landing.signup-popup.page-1.button"/>
								</button>
								<p class="alert-message"></p>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			var signUpPopup = function(elem){
				var self = this;
				this.handler = $(elem).closest('.popupHandler');
				this.qRoot = $(elem);

				this.openRegistration = function(){
					self.qRoot.find('#2').addClass('show');
					self.qRoot.find('#1').removeClass('show');					
					self.handler.addClass('myshow');
					$('body').css('overflow', 'hidden');
				};
				this.open = function(){
					self.handler.addClass('myshow');
					$('body').css('overflow', 'hidden');
				};
				this.close = function(){
					self.handler.removeClass('myshow');
					$('body').css('overflow', '');
				};
				this.reset = function(){
					self.qRoot.find('input').val('').reset();
					self.qRoot.find('#1').addClass('show');
					self.qRoot.find('#2').removeClass('show');
					self.qRoot.find('.alert-message').removeClass('show');
					self.qRoot.find('button').removeClass('not-show');	
				};
				$(document).click(function(e){
					if ($(e.target).hasClass('popupHandler')){
						self.close();
						self.reset();
						setTimeout(function(){
							$(e.target).css('display', '');
						}, 100);
					}
				});
				this.qRoot.find('#1 form').submit(function(e){
					e.preventDefault();
					easeTracker.trackEvent("HomepageSignUp1");
					var emailVal = $(this).find("input[name='email']").val();
					var name = $(this).find("input[name='name']").val();
					var loading = $(this).find('.loading');
					var submitButton = $(this).find(".submitButton");
					var alertMessage = $(this).find(".alert-message");

					if (!emailVal.length)
						return;

					loading.addClass('show');
					submitButton.addClass('not-show');
					postHandler.post($(this).attr('action'),
					{
						email : emailVal,
						name : name
					},
					function(){
						loading.removeClass('show');
					},
					function(retMsg) {
						alertMessage.text(retMsg.substring(2, retMsg.length));
						alertMessage.css('color', '#24d666');
						alertMessage.addClass('show');
						if (retMsg[0] == '1'){
							setTimeout(function(){
								alertMessage.removeClass('show');
								submitButton.removeClass('not-show');
							}, 7000);
						} else if (retMsg[0] == '2'){
							self.qRoot.find("#2 input[name='fname']").val(name);
							self.qRoot.find("#2 input[name='email']").val(emailVal);
							self.openRegistration();
						}
					},
					function(retMsg) {
						alertMessage.text(retMsg);
						alertMessage.css('color', '#ec555b')
						alertMessage.addClass('show');
						setTimeout(function(){
							alertMessage.removeClass('show');
							submitButton.removeClass('not-show');
						}, 3000);
					},
					'text'
					);
				});
				this.qRoot.find('#2 form').submit(function(e){
					e.preventDefault();
					var self = $(this);
					var name = $(this).find("input[name='fname']").val();
					var email = $(this).find("input[name='email']").val();
					var code = $(this).find("input[name='invitationCode']").val();
					var password = $(this).find("input[name='password']").val();
					var confirmPassword = $(this).find("input[name='confirmPassword']").val();

					var loading = $(this).find('.loading');
					var submitButton = $(this).find(".submitButton");
					var alertMessage = $(this).find(".alert-message");

					if (!name.length || !email.length || !code.length || !(password == confirmPassword))
						return;

					loading.addClass('show');
					submitButton.addClass('not-show');
					postHandler.post($(this).attr('action'),
					{
						email : email,
						fname : name,
						invitationCode : code,
						password : password,
						confirmPassword : confirmPassword,
						lname : "unknown"
					},
					function(){
						loading.removeClass('show');
					},
					function(retMsg) {
						alertMessage.text(retMsg);
						alertMessage.css('color', '#24d666');
						alertMessage.addClass('show');
						easeTracker.setUserId(email);
						easeTracker.trackEvent("HomepageSignUp2");
						easeTracker.trackEvent("Connect");
						setTimeout(function() {
							window.location = "index.jsp";
						}, 750);
					}, 
					function(retMsg) {
						alertMessage.text(retMsg);
						alertMessage.css('color', '#ec555b')
						alertMessage.addClass('show');
						setTimeout(function(){
							alertMessage.removeClass('show');
							submitButton.removeClass('not-show');
						}, 3000);
					},
					'text'
					);
				});
				$("input[name='password']").keyup(function(e){
					$("#validatorPassword").css("display","inline-block");
					if($("input[name='password']").val().length < 8) 
						$("#validatorPassword").removeClass('valid');
					else
						$("#validatorPassword").addClass('valid');
					if($("input[name='password']").val().length < 8 || $("input[name='confirmPassword']").val() != $("input[name='password']").val())
						$("#validatorConfirmPass").removeClass('valid');
					else
						$("#validatorConfirmPass").addClass('valid');
				});
				$("input[name='confirmPassword']").keyup(function(e){
					$("#validatorConfirmPass").css("display","inline-block");
					if($("input[name='password']").val().length < 8 || $("input[name='confirmPassword']").val() != $("input[name='password']").val())
						$("#validatorConfirmPass").removeClass('valid');
					else
						$("#validatorConfirmPass").addClass('valid');
				});
				$("#2 input[name='password']").complexify({
					strengthScaleFactor: 0.7
				}, function(valid, complexity){
					$(".progress .progress-bar").css('width', complexity + "%");
					if (complexity < 20){
						$(".progress .progress-bar").css('background-color', '#e4543b');				
					} else if (complexity < 40){
						$(".progress .progress-bar").css('background-color', '#e07333');
					}else if (complexity < 60){
						$(".progress .progress-bar").css('background-color', '#ead94a');
					}else if (complexity < 80){
						$(".progress .progress-bar").css('background-color', '#ddf159');
					}else {
						$(".progress .progress-bar").css('background-color', '#b0df33');
					}
				});
			};
			var easeSignUpPopup = new signUpPopup($('#signUpPopup'));
			$('.signUpButton').click(function(){
				easeTracker.trackEvent($(this).attr("trackEvent"));
				easeSignUpPopup.open();
			});
		</script>
		<script type="text/javascript">
			$(document).ready(function(){
				if ($('#2').find("input[name='email']").val() != 'null'){
					easeSignUpPopup.openRegistration();
				}
				$("#connexionButton").click(function() {
					easeTracker.trackEvent("HomepageLogin");
				});
			});
		</script>
	</body>
	</html>