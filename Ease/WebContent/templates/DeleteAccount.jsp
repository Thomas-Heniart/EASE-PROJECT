<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="setting">
	<button id="deleteAccountButton">I want to delete my account</button>
	<div class="md-modal md-effect-15 popup" oClass="PopupDeleteAccount" id="PopupDeleteAccount">
		<div class="md-content">
			<div class="delete-advertising show">
				<p>You are about to delete your Ease account.</p>
				<p>This action will delete all your data in Ease for ever and you will not be able to recover it.</p>
				<p>Do you confirm ?</p>
				<button type="button" id="confirmDeleteAccount">Yes</button>
				<button type="button" oClass="CloseButton">No</button>
			</div>
			<form action="DeleteAccount" oClass="DeleteAccountForm" id="DeleteAccountForm">
				<p>Enter Ease password.</p>
				<input type="password" name="password" oClass="PasswordInput" placeholder="Enter password"/>
				<p class="errorMessage">Incorrect password</p>
				<div>
					<button type="submit">Delete my account</button>
					<button type="button" oClass="CloseButton">Cancel</button>
				</div>
			</form>
			<div class="wait">
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
</div>
<script>
	var deleteAccountPopup = new Popup["PopupDeleteAccount"]($("#PopupDeleteAccount"));
	$("#deleteAccountButton").click(function(e) {
		e.stopPropagation();
		deleteAccountPopup.open();
	})
	$("#PopupDeleteAccount #confirmDeleteAccount").click(function() {
		$(".delete-advertising").removeClass("show");
		$("#DeleteAccountForm").addClass("show");
	});
</script>