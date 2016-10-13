<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="js/deleteProfile.js"></script>
<div class="md-modal md-effect-15 popup" id="PopupDeleteProfile">
	<div class="md-content">
		<p class="title">Please confirm profile deletion <span></span></p>
		<input name="index" type="hidden" name="index" id="index"/>
		<input id="password" name="password" type="password" placeholder="Password"/>
		<div class="alertDiv">
			<p>Incorrect password !</p>
		</div>
		<div class="buttonSet">
    		<button id="accept" class="btn btn-default btn-primary btn-group btn-lg">Remove</button>
    		<button id="close" class="btn btn-default btn-primary btn-group btn-lg">Cancel</button>
    	</div>
	</div>
</div>