<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.Ease.session.User"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.LinkedList"%>
<%
	List<String> verifiedEmails = ((User) session.getAttribute("User")).getVerifiedEmails();
	List<String> unverifiedEmails = ((User) session.getAttribute("User")).getUnverifiedEmails();
%>
<form action="AddEmail" oClass="AddEmailForm" id="AddEmailForm">
	<div class="newEmail show">
		<i class="fa fa-plus-circle" aria-hidden="true"></i> Add an email
	</div>
	<div class="newEmailInput">
		<input type="email" name="newEmail" id="newEmail" oClass="EmailInput" placeholder="Enter email..." />
		<button type="submit">Go</button>
	</div>
</form>
<script>
	var addEmailInput = Form["AddEmailForm"]($("#AddEmailForm"));
</script>
<form action="editVerifiedEmails" id="editVerifiedEmails" oClass="EditVerifiedEmailsForm">
	
	<c:forEach items="<%= verifiedEmails %>" var="item"
		varStatus="loop">
		<div>
			<input type="email" oClass="EmailInput" value="${item}"/>
			<span class="verifiedEmail">Verified</span>
		</div>
	</c:forEach>
	<c:forEach items="<%= unverifiedEmails %>" var="item"
		varStatus="loop">
		<div>
			<input type="email" oClass="EmailInput" value="${item}"/>
			<span class="unverifiedEmail">Verified ?</span>
		</div>
	</c:forEach>
</form>