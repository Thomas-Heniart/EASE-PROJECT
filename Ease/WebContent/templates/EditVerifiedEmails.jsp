<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.LinkedList"%>

<%
	List<String> verifiedEmails = user.getVerifiedEmails();
	List<String> unverifiedEmails = user.getUnverifiedEmails();
%>
<div class="setting show">
	<form action="AddUserEmail" oClass="AddEmailForm" id="AddEmailForm"
		class="show">
		<div class="newEmail show">
			<i class="fa fa-plus-circle" aria-hidden="true"></i> Add an email
		</div>
		<div class="newEmailInput">
			<input type="email" name="email" id="newEmail" oClass="EmailInput"
				placeholder="Enter email..." />
			<button type="submit">Go</button>
		</div>
	</form>
	<div class="md-modal md-effect-15 popup" id="AddEmailPopup">
		<div class="md-content">
			<div class="waiting show">
				<h2>We are sending you an email</h2>
				<div class="sk-fading-circle">
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
			<div class="email-sent">
				<h2>Email sent</h2>
				<i class="fa fa-check" aria-hidden="true"></i>
			</div>
			<div class="error">
				<h2>Unfortunately we cannot send you an email</h2>
			</div>
		</div>
	</div>
	<script>
		var addEmailInput = Form["AddEmailForm"]($("#AddEmailForm"));
	</script>
	<form action="editVerifiedEmails" id="editVerifiedEmails"
		oClass="EditVerifiedEmailsForm" class="setting show">
		<c:forEach items="<%=verifiedEmails%>" var="item" varStatus="loop">
			<div class="emailLine">
				<input type="email" value="${item}" readonly /> <span
					class="verifiedEmail">Verified</span> <i
					class="fa fa-question-circle emailInfo" aria-hidden="true"> <span
					class="info">Verifying an email enables updates for that
						email, as well as increases security.</span>
				</i>
			</div>
		</c:forEach>
		<c:forEach items="<%=unverifiedEmails%>" var="item" varStatus="loop">
			<div class="emailLine">
				<input type="email" name="email" oClass="HiddenInput"
					value="${item}" readonly /> <span class="unverifiedEmail"><span
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
				<span class="email-sent">Email has been successfully sent <i class="fa fa-check" aria-hidden="true"></i></span>
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
	<form action="AskVerificationEmail" id="SendVerificationEmail"
		oClass="SendVerificationEmailForm">
		<input type="hidden" name="email" oClass="HiddenInput">
		<button type="submit">Send email</button>
	</form>
</div>
<script>
	var deleteEmailPopup = new Popup["DeleteEmailPopup"]($("#DeleteEmailPopup"));
	var emailConfirmationForm = new Form["SendVerificationEmailForm"](
			$("#SendVerificationEmail"));
</script>