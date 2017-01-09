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
response.sendRedirect("/index.jsp");
}
}
}
boolean knownUser = iden == 2 ? true : false;
%>
<div class="FormsContainer">
	<div class="handler show" id="connection">
		<% if (knownUser){ %>
		<p id="userName" style="display:none;"><%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %></p>
		<div class="form show" id="knownUser">			
			<div class="savedUser">
				<div class="line emoji">
					<img src="resources/emojis/wink.png" />
				</div>
				<h2 class="title">Hello <%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %>,</h2>
				<div class="line">
					<p>Please type your password to access your space</p>
				</div>
				<div class="line">
					<input id="password" name="password" type="password" id="input-8" placeholder="Password"/>
				</div>
				<div class="alertDiv">
					<p>Incorrect password !</p>
				</div> 
				<button id="savedUserButton">Login</button>
				<!-- <a class='create-account' href='/getEmailLink'>create an account</a> -->
			</div>
			<div class="sk-fading-circle" id="loadingKnownUser" style="display:none;">
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
		</div>
		<%}%>
		<div class="form <% if (!knownUser){ %> show <% }%>" id="unknownUser" >
			<!--			<img class='ease-logo' src='resources/icons/Ease_Logo_couleur.png'/>-->
			<form action="connection" method="POST" id="loginForm" role="form">
				<div class="line emoji">
					<img src="resources/emojis/smile.png" />
				</div>
				<h2 class="title">Hello,</h2>
				<div class="line">
					<input id="email" name="email" type="email" id="input-8" placeholder="Email"/>
				</div>
				<div class="line">
					<input id="password" name="password" type="password" id="input-8" placeholder="Password"/>
				</div>
				<div class="alertDiv">
					<p>Incorrect password or email !</p>
				</div>
				<button id="helloButton">Login</button>
				<!-- <a class='create-account' href='/getEmailLink'>create an account</a> -->
			</form>
			<div class="sk-fading-circle" id="loadingUnknownUser" style="display:none;">
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
		</div>
	</div>
	<div class="handler" id="passwordLost">
		<div class="form show">
			<form action="PasswordLost" method="POST" id="passwordLost" role="form">
				<div class="line emoji">
					<img src="resources/emojis/wondering.png" />
				</div>
				<h2 class="title">Lost password ?</h2>
				<div class="line">
					<p>For security reasons, resetting your EASE password will delete all account passwords you added to the platform.</p>
				</div>
				<div class="line">
					<input id="email" name="email" type="email" id="input-8" placeholder="Email"/>
				</div>
				<div class="alertDiv">
					<p>Incorrect password !</p>
				</div> 
				<div class="line buttonSet">
					<button id="lostPasswordButton" class="show">Reset password</button>
					<div class="sk-fading-circle centeredItem">
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
				</div>
				<div class="line">
					<p id="goBack">Go back</p>
				</div>
			</form>
		</div>
	</div>
</div>

<div class="controls show">
	<% if (knownUser) {%>
	<a href="discover" target="_blank">New to Ease</a>
	<i class="fa fa-circle" aria-hidden="true"></i>
	<a id="changeAccount">Other account</a>
	<i class="fa fa-circle" aria-hidden="true"></i>
	<% } %>
	<a id="passwordLost">Password lost</a>
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