<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="js/modifyApp.js"></script>
<div oClass="ModifyAppPopup" class="md-modal md-effect-15 popup"
	id="PopupModifyApp">
	<div class="md-content">
		<div class="popupHeader">
			<img class="logoApp" src="resources/images/Deezer.jpeg" />
			<div class="textContent">
				<p class="title">
					Modify informations related to <span></span>
				</p>
			</div>
		</div>
		<form action="editApp" oClass="ModifyAppForm" id="modifyAppForm">
			<div class="lineInput">
				<label for="app-name">App name :</label>
				<input oClass="NoEmptyInput" id="app-name" name="name" type="text"
					placeholder="Name" maxlength="14" />
			</div>
			<div class="loginWithChooser">
				<div class="linedSeparator">
					<div class="backgroundLine"></div>
					<p>Log in with</p>
				</div>
				<div class="loginWithButton facebook hidden" webid="7">
					<p>Facebook</p>
				</div>
				<div class="loginWithButton linkedin hidden" webid="28">
					<p>Linkedin</p>
				</div>
				<div class="linedSeparator or">
					<div class="backgroundLine"></div>
					<p>or</p>
				</div>
			</div>
			<div class="loginAppChooser" style="display: none;">
				<p>Select your account</p>
				<div class="buttonBack">
					<i class="fa fa-arrow-circle-o-left" aria-hidden="true"></i>
				</div>
				<div class="ChooserContent"></div>
			</div>

			<div class="classicLogin">
				<%@ include file="inputs/SuggestInput.jsp" %>
				<div class="disabledInput">
					<input oClass="BasicInput" name="password" type="password"
						placeholder="Click on the wheel to modify password" />
					<div class="activateInput">
						<i class="fa fa-cog"></i>
					</div>
				</div>
			</div>
			<div class="buttonSet">
				<button type="submit">Update</button>
				<button type="button" oClass="CloseButton">Cancel</button>
			</div>
		</form>
	</div>
</div>
<script>
	var modifyAppPopup = new Popup["ModifyAppPopup"]($("#PopupModifyApp"));
</script>