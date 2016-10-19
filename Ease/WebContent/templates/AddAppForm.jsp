<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<form action="addApp" id="AddAppForm" class="addAppForm"
	oClass="AddAppForm">
	<div class="lineInput">
		<label for="app-name">App name :</label> <input oClass="NoEmptyInput"
			type="text" id="app-name" name="name" maxlength="14"
			placeholder="Name" />
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
	<div class="loginSsoChooser" id="ssoChooser" style="display: none;">
		<p>Select existing account</p>
		<div class="ChooserContent"></div>
		<div class="linedSeparator or">
			<div class="backgroundLine"></div>
			<p>or</p>
		</div>
	</div>
	<div class="classicLogin show">
		<%@ include file="inputs/SuggestInput.jsp" %>
		<input oClass="NoEmptyInput" type="password" name="password"
			placeholder="Password" value="" />
	</div>
	<div class="buttonSet">
		<button type="submit">Add</button>
		<button type="button" oClass="CloseButton">Cancel</button>
	</div>
</form>