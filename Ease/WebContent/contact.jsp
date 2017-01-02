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
<link rel="stylesheet" href="css/teamBody.css" />
<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
<script src="js/selectFx.js"></script>
<script src="js/jquery.complexify.min.js"></script>
<script type="text/javascript">$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="https://client.crisp.im/l.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();</script>
<script src="js/tracker.js"></script>
</head>

<body id="contactBody">
	<!-- Navigation -->
	<%@ include file="templates/landingPage/landingHeader.jsp"%>

	<section id="contact_us">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">Nous contacter</h2>
				</div>
			</div>
			<div class="row">
			<div id="contactUsForm">
				<form method="POST" action="contactUs" class="contactForm">
					<div class="formRaw">
						<input type="email" name="email" placeholder="Email" />
					</div>
					<div class="formRaw">
						<textarea name="message" placeholder="Your message..."></textarea>
					</div>
					<div class="formRaw">
						<button class="btn submitButton" type="submit">Send !</button>
					</div>
				</form>
				</div>
			</div>
		</section>
		<script type="text/javascript">
			$('form.contactForm').submit(function(e){
				var self = $(this);
				e.preventDefault();
				postHandler.post(
					self.attr('action'),
					{
						email: self.find("input[name='email']").val(),
						message: self.find("textarea[name='message']").val()
					},
					function(){

					},
					function(msg){

					},
					function(msg){

					},
					'text');
			});
		</script>
		<%@ include file="templates/landingPage/landingFooter.jsp" %>
		<script src="js/bootstrapjq.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
	</body>
	</html>