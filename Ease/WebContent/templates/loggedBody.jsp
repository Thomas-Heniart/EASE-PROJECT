<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<<<<<<< HEAD

=======
>>>>>>> 7e055193cb71fb75a2d884aaf1d546041c63912a
<%@ page import="java.util.LinkedList"%>
<%@ page import="com.Ease.context.Tag"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/dashboard" prefix="dashboard" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>

<%
Cookie sessionId = new Cookie("sId",sessionSave.getSessionId());
Cookie sessionToken = new Cookie("sTk",sessionSave.getToken());

DateFormat dateFormat = new SimpleDateFormat("HH");
Date date = new Date();
int duration = 29 - Integer.parseInt(dateFormat.format(date));
if(duration > 24) duration = duration - 24;
duration = (duration*60-30)*60;
sessionId.setMaxAge(duration);
sessionToken.setMaxAge(duration);
response.addCookie(sessionId);
response.addCookie(sessionToken);
%>
<%
pageContext.setAttribute("selectedTags", new LinkedList<Tag>());

Cookie fname = new Cookie("fname",
	Base64.getEncoder().encodeToString(user.getFirstName().getBytes(StandardCharsets.UTF_8)));
Cookie lname = new Cookie("lname",
	Base64.getEncoder().encodeToString(user.getLastName().getBytes(StandardCharsets.UTF_8)));
Cookie email = new Cookie("email", user.getEmail());

fname.setMaxAge(60 * 60 * 24 * 31);
lname.setMaxAge(60 * 60 * 24 * 31);
email.setMaxAge(60 * 60 * 24 * 31);
response.addCookie(fname);
response.addCookie(lname);
response.addCookie(email);
%>
<c:set var="session"		scope="session" value="${pageContext.getSession()}"/>
<c:set var="servletContext" scope="session" value="${session.getServletContext()}"/>
<c:set var="user"			scope="session" value='${session.getAttribute("User")}'/>
<c:set var="colors"			scope="session" value='${servletContext.getAttribute("Colors")}'/>
<c:set var="profiles"		scope="session" value='${user.getProfiles()}'/>
<c:set var="siteManager"	scope="session" value='${servletContext.getAttribute("siteManager")}'/>
<c:set var="siteList"		scope="session" value='${siteManager.getSitesList()}'/>
<c:set var="tags"			scope="session"	value='${servletContext.getAttribute("Tags")}'/>
<c:set var="tagAndSiteMapping"	scope="session" value='${servletContext.getAttribute("TagAndSiteMapping")}'/>

<script type="text/javascript">
$(document).ready(function(){
	setTimeout(function(){
		var event = new CustomEvent("isConnected", {"detail":"true"});
		document.dispatchEvent(event);
	}, 500)});
</script>

<div id="loggedBody">
    <div class="col-left show" style="width: 100%; float:left">
		<%@ include file="ProfileView.jsp"%>
		<%@ include file="extension.jsp" %>		
		<%@ include file="catalog/catalogView.jsp"%>
	</div>
	<dashboard:hiddenProfile profile="${user.getProfile(0)}"/>
	<%@ include file="SettingsView.jsp" %>
	<%@ include file="PopupDeleteProfile.jsp" %>
	<%@ include file="PopupDeleteApp.jsp" %>
	<%@ include file="PopupAddApp.jsp" %>	
	<%@ include file="PopupModifyApp.jsp" %>
	<div class="md-overlay"></div>
</div>

<script>

$(document).ready(function(){
	$('.md-overlay').click(function(){
		popupAddApp.close();
		modifyAppPopup.close();
		$('.md-show').removeClass('md-show');
	});
	$('.popupClose').click(function () {
		popupAddApp.close();
		modifyAppPopup.close();
		$('.md-show').removeClass('md-show');
	});
});
</script>
		<script>
$(document).ready(function(){
	$('.cookiesInfo').css('display', 'none');
});
		(function() {
				if (!String.prototype.trim) {
					(function() {
						// Make sure we trim BOM and NBSP
						var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
						String.prototype.trim = function() {
							return this.replace(rtrim, '');
						};
					})();
				}

				[].slice.call( document.querySelectorAll( 'input.input__field' ) ).forEach( function( inputEl ) {
					// in case the input is already filled..
					if( inputEl.value.trim() !== '' ) {
						classie.add( inputEl.parentNode, 'input--filled' );
					}

					// events:
					inputEl.addEventListener( 'focus', onInputFocus );
					inputEl.addEventListener( 'blur', onInputBlur );
				} );
			})();
		</script>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-75916041-5', 'auto');
  ga('send', 'pageview');
</script>