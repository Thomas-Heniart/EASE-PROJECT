<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Discover Ease !</title>
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
  	easeTracker.trackEvent("VisitLandingPage");
</script>
</head>

<body id="landingBody">
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
						<a href="http://www.ease-app.co/secure" target="_blank">Sécurité</a>
					</li>
					<li>
						<a href="equipe.jsp">Equipe</a>
					</li>
					<li>
						<a href="contact.jsp">Contact</a>
					</li>
					<li>
						<a href="/" target="_blank">Connexion</a>
					</li>
				</ul>
			</div>
		</div>
	</nav>
	<header>
		<div class="container">
			<div class="intro-text">
				<div class="intro-heading">Se connecter à ses sites préférés<br>n'a jamais été aussi simple</div>
				<div class="intro-lead-in">Une page d'accueil qui rassemble vos comptes web,<br>pour vous y connecter en un clic</div>
				<a href="#services" class="btn btn-xl signUpButton">Commencez gratuitement !</a>
			</div>
		</div>
	</header>
	<section id="productPresentation">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">Voici comment on vous connecte</h2>
					<h3 class="section-subheading text-muted">Petite démo ;)</h3>
				</div>
			</div>
			<div class="mockupPresentation">
				<video width="100%" height="auto" controls="noshow">
					<source src="resources/videos/video5.mp4" type="video/mp4">
						Your browser does not support the video tag.
					</video>
				</div>
			</div>
		</section>
		<section id="avantages">
			<div class="container">
				<div class="row">
					<div class="col-lg-12 text-center">
						<h2 class="section-heading">Voici comment on vous simplifie la vie</h2>
						<h3 class="section-subheading text-muted">Avantages</h3>
					</div>
				</div>
				<div class="row w70 showcase">
					<span class="fa-stack fa-lg">
						<i class="fa fa-circle-thin fa-stack-2x"></i>
						<i class="fa fa-magic fa-stack-1x"></i>
					</span>
					<p><span>N'utilisez plus jamais de mots de passe:</span> Ease vous connecte en 1 clic automatiquement</p>
				</div>
				<div class="row w70 showcase">
					<span class="fa-stack fa-lg">
						<i class="fa fa-circle-thin fa-stack-2x"></i>
						<i class="fa fa-rocket fa-stack-1x"></i>
					</span>
					<p><span>Gagnez du temps et de la tranquillité:</span> Ease fait économiser en moyenne 55 heures par an</p>
				</div>
				<div class="row w70 showcase">
					<span class="fa-stack fa-lg">
						<i class="fa fa-circle-thin fa-stack-2x"></i>
						<i class="fa fa-globe fa-stack-1x"></i>
					</span>
					<p><span>Accédez à vos sites depuis n’importe où:</span> Votre espace Ease est accessible de la même manière quel que soit l'ordinateur</p>
				</div>
				<div class="row w70 showcase">
					<span class="fa-stack fa-lg">
						<i class="fa fa-circle-thin fa-stack-2x"></i>
						<i class="fa fa-refresh fa-stack-1x"></i>
					</span>
					<p><span>Retrouvez votre web facilement:</span> Ease centralise vos comptes, même avec une connexion Facebook, LinkedIn, etc...</p>
				</div>
			</div>
		</section>
		<section class="vp" style="background-color:#eee;">
			<div clas="container">
				<div class="row">
					<a href="#services" class="btn btn-xl signUpButton">Essayez maintenant, c'est gratuit !</a>
				</div>
			</div>
		</section>
		<section id="features">
			<div class="container">
				<div class="row">
					<div class="col-lg-12 text-center">
						<h2 class="section-heading">Fonctionnalités</h2>
						<h3 class="section-subheading text-muted">Rien que pour vous !</h3>
					</div>
				</div>
				<div class="row">
					<div class="fList left">
						<div class="feature">
							<span>
								Connexion à vos sites en 1 clic
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
						<div class="feature">
							<span>
								Gestion de plusieurs comptes sur un même site
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
						<div class="feature">
							<span>
								Mise à jour automatique de vos mots de passe
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
						<div class="feature">
							<span>
								Organisation selon vos besoin
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
						<div class="feature">
							<span>
								Compatibilité navigateurs Chrome et Safari
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
					</div>
					<div class="fList right">
						<div class="feature">
							<span>
								Déconnexion de tous vos comptes en 1 clic, depuis Ease
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
						<div class="feature">
							<span>
								Données sécurisées avec AES 256 et Bcrypt
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
						<div class="feature">
							<span>
								Partage d’identifiants avec votre équipe
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>
						<div class="feature">
							<span>
								Gestion des comptes avec une connexion Facebook, LinkedIn, etc...
							</span>
							<i class="fa fa-check" aria-hidden="true"></i>
						</div>

					</div>
				</div>
			</div>
		</section>
		<section id="markSection">
			<div class="container">
				<div class="row">
					<div class="col-lg-12 text-center">
						<h2 class="section-heading">Ils nous font confiance</h2>
					</div>
				</div>
				<div class="row">
					<div class="markColumn">
						<div class="markHandler">
							<a href="http://bunkrapp.com/" >
								<img src="resources/landing/bunkr-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="http://jeflabel.com/" >
								<img src="resources/landing/jef-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="https://www.sosav.fr/" >
								<img src="resources/landing/sosav-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="http://www.ineat-conseil.com/" >
								<img src="resources/landing/ineat-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="https://tribe.pm/" >
								<img src="resources/landing/tribe-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="http://ecota.co/" >
								<img src="resources/landing/ecotaco-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="http://www.side-law.com/" >
								<img src="resources/landing/side-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="https://www.nestorparis.com/" >
								<img src="resources/landing/nestor-logo.png" />
							</a>
						</div>
						<div class="markHandler">
							<a href="https://www.mobeye-app.com" >
								<img src="resources/landing/mobeye-logo.png" />
							</a>
						</div>
					</div>
				</div>
			</div>
		</section>
		<section id="userFeedbacks">
			<div class="container">
				<div class="row">
					<div class="col-lg-12 text-center">
						<h2 class="section-heading">Ce que disent nos utilisateurs</h2>
						<h3 class="section-subheading text-muted">Notre priorité, c’est vous !</h3>
					</div>
				</div>
				<div class="row">
					<div class="comment">
						<div class="photo">
							<img src="resources/landing/JPA.png"/>
						</div>
						<div class="message">
							<p class="mess">"A l’IESEG, nos étudiants et staffs sont quotidiennement connectés sur différents outils web: intranet, calendrier, e-learnings, etc. Ease a permis une centralisation des comptes pour un accès plus facile." 
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
							<p class="mess">"J’ai plusieurs comptes instagram, twitter et Facebook, et régulièrement j’ai besoin de switcher entre eux. Ease s’occupe de gérer les différents comptes et de me connecter sur le bon instantanément." 
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
							<p class="mess">"J’utilise Ease pour accéder à l’ensemble de mon web depuis n’importe où ! Très pratique en stage ou en voyage lorsque je ne suis pas sur mon ordinateur personnel."
							</p>
							<a  target="_blank" href="https://fr.linkedin.com/in/clémentine-prud-homme-691945103">Clémentine Prud’homme</a>
							<p class="position">Etudiante à l’EDHEC</p>
						</div>
					</div>
					<div class="comment">
						<div class="photo">
							<img src="resources/landing/JBroux.png"/>
						</div>
						<div class="message">
							<p class="mess">"Ease me permet d’être sur le web depuis un  ordinateur aussi facilement que sur un smartphone: j’ai mes apps dans un dashboard, je clique dessus, je suis connecté !" 
							</p>
							<a target="_blank" href="https://uk.linkedin.com/in/rouxjeanbaptiste">Jean-Batiste Roux</a>
							<p class="position">Account Manager à Barnebys</p>
						</div>
					</div>
				</div>
			</div>
		</section>
		<section class="vp" style="background-color:white;">
			<div clas="container">
				<div class="row">
					<a href="#services" class="btn btn-xl signUpButton">Commencez gratuitement !</a>
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
						Welcome!
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
									Create your Ease password :
								</p>
								<div class="infoText">
									<p>
										<span>Info:</span> Make your password memorable. As we don’t know your Ease password, if you forget it, you will have to reset your account and enter again the passwords of the apps you added.
									</p>
								</div>
							</div>
							<div class="row">
								<input type="hidden" name="fname" value=<%=request.getParameter("name")%>/>
								<input type="hidden" name="email" value=<%= request.getParameter("email")%> />
								<input type="hidden" name="invitationCode" value=<%= request.getParameter("invitationCode") %> />
								<span class="input">
									<input type="password" name="password" placeholder="Your password..." />
									<div class="showPassDiv">
										<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
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
									<input type="password" name="confirmPassword" placeholder="Confirm password..." />
									<div class="showPassDiv">
										<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
									</div>
									<div id="validatorConfirmPass" class="passwordValidator" style="display:none">
										<i class="fa fa-times error" aria-hidden="true" style="color:#d75a4a;"></i>
										<i class="fa fa-check success" aria-hidden="true" style="color:#24d666;"></i>
									</div>
								</span>
							</div>
							<div class="row text-center">
								<img class="loading" src="resources/other/facebook-loading.svg"/>
								<button class="btn submitButton" type="submit">I'm ready !</button>
								<p class="alert-message"></p>
							</div>
						</form>
					</div>
					<div class="popupBody show" id="1">
						<form class="handler" action="checkInvitation">
							<div class="row">
								<p class="row-heading">
									How would you like us to call you ?
								</p>
								<input type="text" name="name" placeholder="Name..." />
							</div>
							<div class="row">
								<p class="row-heading">
									What's your email ?
								</p>
								<input type="email" name="email" placeholder="@something..." />
							</div>
							<div class="row terms">
								<p>I accept the <a href="#">terms and conditions</a></p>
								<input type="checkbox" value="">
							</div>
							<div class="row text-center">
								<img class="loading" src="resources/other/facebook-loading.svg"/>
								<button class="btn submitButton" type="submit">Let's start !</button>
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
				$(document).click(function(e){
					if ($(e.target).hasClass('popupHandler')){
						$(e.target).css('display', 'none');
						$(e.target).removeClass('myshow');
						$('body').css('overflow', '');
						setTimeout(function(){
							$(e.target).css('display', '');
						}, 100);
					}
				});
				this.qRoot.find('#1 form').submit(function(e){
					e.preventDefault();
					var emailVal = $(this).find("input[name='email']").val();
					var name = $(this).find("input[name='name']").val();
					var loading = $(this).find('.loading');
					var submitButton = $(this).find(".submitButton");
					var alertMessage = $(this).find(".alert-message");

					if (!emailVal.length)
						return;
					if (!($(this).find(".terms input[type='checkbox']").prop('checked'))){
						return;
					}

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
						alertMessage.text(retMsg.substring(2, retMsg.length));
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
						easeTracker.trackEvent("LandingPageSignUp");
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
				easeSignUpPopup.open();
			});
		</script>
		<script type="text/javascript">
			$(document).ready(function(){
				if ($('#2').find("input[name='email']").val() != 'null'){
					easeSignUpPopup.openRegistration();
				}
			});
		</script>
	</body>
	</html>