<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<%@ page import="java.util.LinkedList"%>
<%@ page import="com.Ease.Context.Catalog.Tag"%>
<%@ page import="com.Ease.Dashboard.User.SessionSave" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/dashboard" prefix="dashboard" %>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<script src="js/checkConnection.js"></script>
<%
if (user != null) {
SessionSave sessionSave = user.getSessionSave();
Cookie sessionId = new Cookie("sId",sessionSave.getSessionId());
Cookie sessionToken = new Cookie("sTk",sessionSave.getToken());
Cookie skipLanding = new Cookie("skipLanding", "true");
DateFormat dateFormat = new SimpleDateFormat("HH");
Date date = new Date();
int duration = 29 - Integer.parseInt(dateFormat.format(date));
if(duration > 24) duration = duration - 24;
duration = (duration*60-30)*60;
sessionId.setMaxAge(duration);
sessionToken.setMaxAge(duration);
skipLanding.setMaxAge(60*60*24*14);
response.addCookie(skipLanding);
response.addCookie(sessionId);
response.addCookie(sessionToken);
}
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
<c:set var="user"			scope="session" value='${session.getAttribute("user")}'/>
<c:set var="colors"			scope="session" value='${servletContext.getAttribute("Colors")}'/>
<c:set var="groupManager"	scope="session" value='${servletContext.getAttribute("groupManager")}' />
<c:set var="dashboard" scope="session" value='${user.getDashboardManager()}' />
<c:set var="dashboardColumns" scope="session" value="${dashboard.getProfiles()}"/>
<c:set var="catalog"	scope="session" value='${servletContext.getAttribute("catalog")}'/>
<c:set var="siteList"		scope="session" value='${catalog.getWebsites()}'/>
<c:set var="tags"			scope="session"	value='${servletContext.getAttribute("Tags")}'/>
<c:set var="tagAndSiteMapping"	scope="session" value='${servletContext.getAttribute("TagAndSiteMapping")}'/>
<c:set var="settingsOpen" scope="session" value="${param.openSettings}"/>
<script type="text/javascript">
	$(document).ready(function(){
		setTimeout(function(){
			var event = new CustomEvent("NewEaseUser", {"detail":$("#userEmail").data("content")});
			document.dispatchEvent(event);
		}, 500)});
	</script>
	<div id="loggedBody">
		<div class="col-left <c:if test='${settingsOpen eq null}'>show</c:if>" style="width: 100%; float:left">
			<%@ include file="ProfileView.jsp"%>
			<div class="MenuButtonSet">
				<button id="enterEditMode" state="off" class="button<c:if test="${param.catalogOpen}"> editMode</c:if>">
					<img src="resources/icons/menu_icon.png"/>
					<div class="openCatalogHelper"></div>
				</button>
			</div>
			<div class="CatalogViewTab <c:if test="${param.catalogOpen}">show</c:if>">

			</div>
			<script type="text/javascript">
				$(document).ready(function(){
					$.get('/templates/catalog/catalogView.jsp').success(function(data)
					{
						$('.col-left .CatalogViewTab').append(data);
					});
				});
			</script>
			<c:if test='${user.appsImported() && (user.allTipsDone() eq false)}'>
			<%@ include file="Tips.jsp" %>
		</c:if>
	</div>
	<div class="SettingsView <c:if test='${settingsOpen ne null}'>show</c:if>">
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			$.get('/templates/SettingsView.jsp').success(function(data)
			{
				$('#loggedBody .SettingsView').append(data);
			});
		});
	</script>
	
</div>
<%@include file="PopupsHandler.jsp" %> 

<c:if test='${(user.appsImported() eq false) || (param.importAccounts)}'>
<%@ include file="TutorialView.jsp"%>
</c:if>


<%@ include file="new_extension.jsp" %>
<script>
	$(document).ready(function(){
		$('.cookiesInfo').css('display', 'none');
		var appCount = $(".SitesContainer .siteLinkBox").length;
		var verifiedEmailCount = $(".verifiedEmail").length;
		var unverifiedEmailCount = $(".unverifiedEmail").length;
		var emailCount = verifiedEmailCount + unverifiedEmailCount;
		easeTracker.setUserProperty("AppCount", appCount);
		easeTracker.setUserProperty("EmailCount", emailCount);
		easeTracker.setUserProperty("EmailVerifiedCount", verifiedEmailCount);
		easeTracker.setUserProperty("EmailNonVerifiedCount", unverifiedEmailCount);
		
	});
	easeTracker.setDailyPhoto($('#backgroundSwitch').is("checked"));
</script>
<script>
	(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

	ga('create', 'UA-75916041-5', 'auto');
	ga('send', 'pageview');
</script>