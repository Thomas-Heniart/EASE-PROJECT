<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.Base64.Encoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>

<script src="js/connection.js"></script>
<script src="js/rAF.js"></script>

<script>
	$(document).ready(function(){
		setTimeout(function(){
			var event = new CustomEvent("NewEaseUser", {"detail":"anonymous"});
			document.dispatchEvent(event);
		}, 500);

	});
	easeTracker.trackEvent("LoginpageVisit");
</script>
<div id="loginBody">
	<div class="ease-logo">
		<img src="resources/icons/Ease_logo_blue.png" />
	</div>

	<%@ include file="Logout.jsp" %>
	<% 
	Cookie 	cookie = null;
	Cookie 	cookies[] = request.getCookies();
	String 	fname = "";
	String 	email = "";
	int		iden = 0;
	if (cookies != null){
	for (int i = 0;i < cookies.length ; i++) {
	cookie = cookies[i];
	if ((cookie.getName()).compareTo("email") == 0){
	email = cookie.getValue();
	if (email.length() > 0)
	iden++;
}
else if ((cookie.getName()).compareTo("fname") == 0){
fname = cookie.getValue();
if (fname.length() > 0)
iden++;
}
}
if (iden == 2){
try {
new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8);
} catch (IllegalArgumentException e){
for (int i = 0;i < cookies.length ; i++) {
cookies[i].setMaxAge(-1);
cookies[i].setValue(null);
response.addCookie(cookies[i]);
}
fname = "";
response.sendRedirect("/");
}
}
}
boolean knownUser = iden == 2 ? true : false;
%>
<div class="popupHandler myshow">
	<div class="sk-fading-circle" id="loading">
		<div class="sk-circle1 sk-circle"></div>
		<div class="sk-circle2 sk-circle"></div>
		<div class="sk-circle3 sk-circle"></div>
		<div class="sk-circle4 sk-circle"></div>
		<div class="sk-circle5 sk-circle"></div>
		<div class="sk-circle6 sk-circle"></div>
		<div class="sk-circle7 sk-circle"></div>
		<div class="sk-circle8 sk-circle"></div>
		<div class="sk-circle9 sk-circle"></div>
		<div class="sk-circle10 sk-circle"></div>
		<div class="sk-circle11 sk-circle"></div>
		<div class="sk-circle12 sk-circle"></div>
	</div>
	<!-- known user popup -->
	<% if (knownUser) {%>
	<div class="easePopup landingPopup <c:if test='${empty param.codeExpiration}'>show</c:if>" id="knownUser">
		<div class="bodysHandler">
			<div class="popupBody show">
				<div class="handler">
					<div class="row">
						<div class="title">
							<p>Hello<br><span><%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %></span> !</p>
						</div>
					</div>
					<form method="POST" action="connection" id="knownUserForm">
						<div class="row text-center">
							<p class="popupText">Please type your password to access your space</p>
						</div>
						<div class="row" style="margin-bottom:0.3vw">
							<input type="hidden" name="email" placeholder="Email" value="<%= email %>"/>
							<input type="password" name="password" placeholder="Password"/>
						</div>
						<div class="row">
							<p class="buttonLink floatRight pwdLostButton">Password lost ?</p>
						</div>
						<div class="row alertDiv text-center">
							<p></p>
						</div>
						<div class="row text-center">
							<button class="btn" type="submit">Login</button>
						</div>
						<div class="row">
							<p class="buttonLink floatLeft otherAccountButton">Other account</p>
							<a class="buttonLink floatRight createAccount" href="/discover">Create an account</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%}%>
	<!-- unknown user popup -->
	<div class="easePopup landingPopup <% if (!knownUser){ %><c:if test='${empty param.codeExpiration}'>show</c:if><% }%>" id="unknownUser">
		<div class="bodysHandler">
			<div class="popupBody show">
				<div class="handler">
					<div class="row">
						<div class="title">
							<p>Hello</p>
						</div>
					</div>
					<form method="POST" action="connection" id="unknownUserForm">
						<div class="row" style="margin-bottom:0.3vw">
							<input type="email" name="email" placeholder="Email"/>
							<input type="password" name="password" placeholder="Password"/>
						</div>
						<div class="row">
							<p class="buttonLink floatRight pwdLostButton">Password lost ?</p>
						</div>
						<div class="row alertDiv text-center">
							<p></p>
						</div>
						<div class="row text-center">
							<button class="btn" type="submit">Login</button>
						</div>
						<div class="row">
							<% if(knownUser) {%>
							<p class="buttonLink floatLeft knownAccountButton"><%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %>'s account</p>
							<%}%>
							<a class="buttonLink floatRight createAccount" href="/discover">Create an account</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- password lost popup -->
	<div class="easePopup landingPopup <c:if test='${not empty param.codeExpiration}'>show</c:if>" id="passwordLost">
		<div class="bodysHandler">
			<div class="popupBody show">
				<div class="handler">
					<div class="row">
						<div class="title">
							<p>Lost password ?</p>
						</div>
					</div>
					<form method="POST" action="passwordLost" id="passwordLostForm">
						<div class="row text-center">
							<p class="popupText">For security reasons, resetting your EASE password will delete all account passwords you added to the platform.</p>
						</div>
						<div class="row">
							<input type="email" name="email" placeholder="Email"/>
						</div>
						<div class="row alertDiv text-center <c:if test='${not empty param.codeExpiration}'>show</c:if>">
							<p><c:if test='${not empty param.codeExpiration}'>The link is not valid anymore, please type your email again.</c:if></p>
						</div>
						<div class="row text-center">
							<button class="btn" type="submit">Reset password</button>
						</div>
						<div class="row text-center">
							<p class="buttonLink backButton">Go back</p>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>

<p class="homepageOnoffContainer displayedByPlugin">
	<span>Homepage</span>
	<span class="onoffswitch">
		<input	type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="homePageSwitch" />
		<label class="onoffswitch-label" for="homePageSwitch"></label>
	</span>
</p>
	<!-- <div class='univ-presentation'>
		<h2>We are ease.space</h2>
		<p>We built a home page that enables you to easily connect to website's accounts, without using passwords, and regardless of the computer</p>
		<p>This page is the new way to access your school's web services</p>
		<p>If you do not have your account yet, <a target='_blank' href='http://www.ease-app.co'>let's go</a></p>
	</div>
-->
</div>