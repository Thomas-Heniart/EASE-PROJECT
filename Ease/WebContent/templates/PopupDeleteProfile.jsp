<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div oClass="DeleteProfilePopup" id="PopupDeleteProfile" class="md-modal md-effect-15 popup">
	<div class="md-content">
		<form oClass="DeleteProfileForm" action="RemoveProfile" class="popupForm">
			<p class="title">By deleting your profile, you will lose all related information and associated accounts.</br>-</br>Confirm by entering your ease password.<span></span></p>
			<input oClass="NoEmptyInput" type="hidden" name="profileId" />
			<input oClass="PasswordInput" name="password" type="password" placeholder="Enter your password"/>
			</span>
			<div oClass="ClassicErrorMsg" class="alertDiv">
				<p></p>
			</div>
			<div class="buttonSet">
				<button type="submit" id="accept" class="btn btn-default btn-primary btn-group btn-lg">Remove</button>
				<button type="button"id="close" class="btn btn-default btn-primary btn-group btn-lg" oClass="CloseButton">Cancel</button>
			</div>
		</form>
	</div>
</div>

<script>
	var deleteProfilePopup = new Popup["DeleteProfilePopup"]($("#PopupDeleteProfile"));
</script>