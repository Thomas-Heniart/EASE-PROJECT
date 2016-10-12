<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.LinkedList"%>
<%@ page import="com.Ease.context.Tag"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/dashboard" prefix="dashboard" %>

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
<c:set var="tagAndSiteMapping"	scope="session" value='servletContext.getAttribute("TagAndSiteMapping")'/>

<div class="ProfilesView show">
	<%@ include file="MenuButtonSet.jsp"%>

	<c:if test="${user.getTuto() == '0'}">
	<%@ include file="Tutorial.jsp"%>
</c:if>

<div class="ProfilesHandler">
	<div class="owl-carousel">
		<c:forEach items='${profiles}' var="item" varStatus="loop">
			<dashboard:profile profile="${item}"/>
		</c:forEach>
	</div>
</div>
<%@ include file="ObjectHelpers.jsp"%>
</div>
<script type="text/javascript">
	var forms = [];

	$(document).ready(function(){
		$('.ProfileControlPanel #contentName #modifyNameForm').each(function(){
			var thisForm = new Form($(this), new Input($(this).find('input')), $(this).find('#validate'));
			forms.push(thisForm);
		});
	});
</script>