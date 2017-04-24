<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html lang="${language}">
<head>
	
	<title>Ease.space | Contact</title>
	<!-- Description shown in Google -->
	<meta name="description" content="We are there to answer your questions. If you do want to use passwords anymore, feel free.">
	
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
	<meta name="viewport" content="initial-scale=1, maximum-scale=1" />
	<meta property="og:image"
	content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />

	<link rel="manifest" href="manifest.json">

	<script src="/jsMinified.v00008/jquery1.12.4.js"></script>
	<script src="/jsMinified.v00008/bootstrap.js"></script>
	<script src="/jsMinified.v00008/basic-utils.js"></script>
	<script src="/jsMinified.v00008/postHandler.js"></script>
	<script src="/jsMinified.v00008/languageChooser.js"></script>
	<script src="/jsMinified.v00008/tracker.js"></script>

	<link rel="stylesheet" href="/cssMinified.v00007/default_style.css" />
	<link rel="stylesheet" href="/cssMinified.v00007/bootstrap.css" />
	<link rel="stylesheet" href="/cssMinified.v00007/landingPage.css" />
	<link rel="stylesheet" href="/cssMinified.v00007/teamBody.css" />
	<link rel="stylesheet" href="/cssMinified.v00007/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
	<script type="text/javascript">$crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="/jsMinified.v00008/crisp.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();</script>
	<script type="text/javascript">
		(function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
			r.async=true;r.src="/jsMinified.v00008/amplitude-analytics.js";
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
	easeTracker.trackEvent("HomepageContactVisit");
</script>
</head>

<body id="contactBody">
	<!-- Navigation -->
	<%@ include file="templates/landingPage/landingHeader.jsp"%>

	<section id="contact_us">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 text-center">
					<h2 class="section-heading">
						<fmt:message key="contactus.title" />
					</h2>
					<h3 class="section-subheading text-muted">
						<fmt:message key="contactus.sub-title"/>
					</h3>
				</div>
			</div>
			<div class="row">
				<div id="contactUsForm">
					<form method="POST" action="ContactUs" class="contactForm">
						<div class="formRaw">
							<input type="email" name="email" placeholder="Email" />
						</div>
						<div class="formRaw">
							<textarea name="message" placeholder=<fmt:message key="contactus.textarea.placeholder"/> ></textarea>
						</div>
						<div class="formRaw errorMessage">
							<p></p>
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
				self.find('.formRaw.errorMessage').removeClass('show');
				self.find(".formRaw button[type='submit']").addClass('waiting');
				postHandler.post(
					self.attr('action'),
					{
						email: self.find("input[name='email']").val(),
						message: self.find("textarea[name='message']").val()
					},
					function(){
						self.find(".formRaw button[type='submit']").removeClass('waiting');
					},
					function(msg){
						self.find("textarea[name='message']").val('');
						self.find('.formRaw.errorMessage p').text(msg);
						self.find('.formRaw.errorMessage').addClass('show');
						easeTracker.trackEvent("HomepageContactSubmit");
					},
					function(msg){
						self.find('.formRaw.errorMessage p').text(msg);
						self.find('.formRaw.errorMessage').addClass('show');
					},
					'text');
			});
		</script>
		<%@ include file="templates/landingPage/landingFooter.jsp" %>
	</body>
	</html>