<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<form action="DeleteAccount" oClass="DeleteAccountForm" id="DeleteAccountForm">
	<input type="password" oClass="PasswordInput" />
	<button type="button">Delete</button>
</form>
<div class="md-modal md-effect-15 popup" id="PopupDeleteAccount" oClass="PopupDeleteAccount">
	<div class="md-content">
		<h2>Do you really want to delete your account</h2>
		<button type="submit">Yes</button>
		<button type="button" oClass="CloseButton">No</button>
	</div>
</div>
<div class="md-modal md-effect-15 popup" id="DeleteAccountWait">
	<div class="md-content">
		<div class="wait show">
			<h2>We are deleting your account</h2>
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
	</div>
</div>
<script>
	var deleteAccountPopup = new Popup["PopupDeleteAccount"]($("#PopupDeleteAccount"));
	$("#DeleteAccountForm button").click(function() {
		deleteAccountPopup.open();
	});
</script>