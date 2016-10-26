<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.Ease.session.User"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
		<input type="email" name="newEmail" id="newEmail" oClass="EmailInput"
			placeholder="Enter email..." />
		<button type="submit">Go</button>
	</div>
</form>
<script>
	var addEmailInput = Form["AddEmailForm"]($("#AddEmailForm"));
</script>
<form action="editVerifiedEmails" id="editVerifiedEmails"
	oClass="EditVerifiedEmailsForm">
	<c:forEach items="<%=verifiedEmails%>" var="item" varStatus="loop">
		<div class="emailLine">
			<input type="email" value="${item}" readonly /> <span
				class="verifiedEmail">Verified</span> <i
				class="fa fa-question-circle emailInfo" aria-hidden="true"> <span
				class="info">Verifying an email enables updates for that
					email, as well as increases security.</span>
			</i>
			<c:if test="${item ne user.getEmail()}">
				<i class="fa fa-trash removeEmail" aria-hidden="true"></i>
			</c:if>
		</div>
	</c:forEach>
	<c:forEach items="<%=unverifiedEmails%>" var="item" varStatus="loop">
		<div class="emailLine">
			<input type="email" name="email" oClass="HiddenInput" value="${item}"
				readonly /> <span class="unverifiedEmail"><span
				class="verify">Verified ?</span><span class="sendVerificationEmail">Send
					verification email</span></span>
			<div class="sk-fading-circle email-loading">
				<span>We are sending you an email</span>
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
	</c:forEach>
</form>
<div oClass="DeleteEmailPopup" class="md-modal md-effect-15 popup"
	id="DeleteEmailPopup">
	<div class="md-content">
		<h2>Do you really want to remove this email</h2>
		<form action="DeleteEmail" id="DeleteEmail" oClass="DeleteEmailForm">
			<input type="hidden" name="email" oClass="BasicInput">
			<button type="submit">Delete</button>
			<button type="button" oClass="CloseButton">Cancel</button>
		</form>
	</div>
</div>
<form action="SendVerificationEmail" id="SendVerificationEmail"
	oClass="SendVerificationEmailForm">
	<input type="hidden" name="email" oClass="HiddenInput">
	<button type="submit">Send email</button>
</form>
<script>
	var deleteEmailPopup = new Popup["DeleteEmailPopup"]($("#DeleteEmailPopup"));
	var emailConfirmationForm = new Form["SendVerificationEmailForm"]($("#SendVerificationEmail"));
</script>