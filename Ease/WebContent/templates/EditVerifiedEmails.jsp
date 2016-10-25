<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.Ease.session.User"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		<div class="emailLine">
			<input type="email" value="${item}" readonly/>
			<span class="verifiedEmail">Verified</span>
			<i class="fa fa-question-circle emailInfo" aria-hidden="true">
				<span class="info">Verifying an email enables updates for that email, as well as increases security.</span>
			</i>
			<c:if test="${item ne user.getEmail()}">
				<i class="fa fa-trash removeEmail" aria-hidden="true"></i>
			</c:if>
		</div>
	</c:forEach>
	<c:forEach items="<%= unverifiedEmails %>" var="item"
		varStatus="loop">
		<div class="emailLine">
			<input type="email" value="${item}" readonly/>
			<span class="unverifiedEmail">Verified ?</span>
		</div>
	</c:forEach>
</form>
<div oClass="DeleteEmailPopup" class="md-modal md-effect-15 popup" id="DeleteEmailPopup">
	<div class="md-content">
		<h2>Do you really want to remove this email</h2>
		<form action="DeleteEmail" id="DeleteEmail" oClass="DeleteEmailForm">
			<input type="hidden" name="email" value="" oClass="BasicInput">
			<input type="submit" value="Delete"/>
			<button type="button" oClass="CloseButton">Cancel</button>
		</form>
	</div>
</div>
<script>
	var deleteEmailPopup = new Popup["DeleteEmailPopup"]($("#DeleteEmailPopup"));
</script>