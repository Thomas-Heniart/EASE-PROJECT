<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.Base64" %>
<%@ page import="java.util.Base64.Encoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="js/connection.js"></script>
<script src="js/rAF.js"></script>
<script src="js/demo-2.js"></script> 
<div id="loginBody">
	<canvas id="demo-canvas" style="position: absolute;"></canvas>
	<div id='search-google'>
		<%@ include file="SearchBar.jsp"%>
	</div>
<% 
	Cookie 	cookie = null;
	Cookie 	cookies[] = request.getCookies();
	String 	fname = "";
	String 	lname = "";
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
		else if ((cookie.getName()).compareTo("lname") == 0){
			lname = cookie.getValue();
			if (lname.length() > 0){
				iden++;
			}
		}
		else if ((cookie.getName()).compareTo("fname") == 0){
			fname = cookie.getValue();
			if (fname.length() > 0)
				iden++;
		}
	}
	if (iden == 3){
		try {
		new String(Base64.getDecoder().decode(lname), StandardCharsets.UTF_8);
		new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException e){
			for (int i = 0;i < cookies.length ; i++) {
				 cookies[i].setMaxAge(-1);
	             cookies[i].setValue(null);
                 response.addCookie(cookies[i]);
			}
			fname = "";
			lname = "";
			response.sendRedirect("/index.jsp");
		}
	}
}
%>
	<div class="FormsContainer">
	<% if (iden == 3){ %>
		<div class="form" id="knownUser">
			<a class='forget-password' href="PasswordLost">Forgot your password ?</a>
			<img class='ease-logo' src='resources/icons/Ease_Logo_couleur.png'/>
			<div class="savedUser">
				<p>Hello <%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %> !</p>
				<span class="input input--minoru">
					<input class="input__field input__field--minoru" id="password" name="password" type="password" id="input-8" placeholder="Password"/>
					<label class="input__label input__label--minoru" for="input-8"></label>
				</span>
				<div class="alertDiv">
					<p>Incorrect password !</p>
				</div> 
	    		<button id="savedUserButton">Login</button>
	    		<!-- <a class='create-account' href='/getEmailLink'>create an account</a> -->
			</div>
			<div id="changeAccount">Other account <img class='switch-account' src="resources/icons/account.png" /></div>   
		</div>
	<%}%>
		<div class="form" id="unknownUser" <% if (iden == 3){ %> style="visibility:hidden;" <% }%>>
			<img class='ease-logo' src='resources/icons/Ease_Logo_couleur.png'/>
			<form action="connection" method="POST" id="loginForm" role="form">
				<a class='forget-password' href="PasswordLost">Forgot your password ?</a>
				<span class="input input--minoru">
					<input class="input__field input__field--minoru" id="email" name="email" type="email" id="input-8" placeholder="Email"/>
					<label class="input__label input__label--minoru" for="input-8"></label>
				</span>
				<span class="input input--minoru">
					<input class="input__field input__field--minoru" id="password" name="password" type="password" id="input-8" placeholder="Password"/>
					<label class="input__label input__label--minoru" for="input-8"></label>
				</span>
				<div class="alertDiv">
					<p>Incorrect password or email !</p>
				</div>
    			<button id="helloButton">Login</button>
    			<!-- <a class='create-account' href='/getEmailLink'>create an account</a> -->
			</form>
			<% if (iden == 3) {%>
				<div id="back"><%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %> account <img class='switch-account' src="resources/icons/account.png" /></div>
			<% } %>
		</div>
	</div>

	<!-- <div class='univ-presentation'>
		<h2>We are ease.space</h2>
		<p>We built a home page that enables you to easily connect to website's accounts, without using passwords, and regardless of the computer</p>
		<p>This page is the new way to access your school's web services</p>
		<p>If you do not have your account yet, <a target='_blank' href='http://www.ease-app.co'>let's go</a></p>
	</div>
	-->
</div>