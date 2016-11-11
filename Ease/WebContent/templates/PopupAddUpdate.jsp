<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div oClass="AddUpdatePopup" class="md-modal md-effect-15 popup" id="AddUpdatePopup">
	<div class="md-content">
		<form oClass="AddUpdateForm" action="addUpdate">
			<p class="title">Verifiying email...</p>
			<p class="text"></p>
			<input oClass="NoEmptyInput" type="hidden" name="siteId" />
			<input oClass="NoEmptyInput" type="hidden" name="profileId" />
			<input oClass="NoEmptyInput" type="hidden" name="cryptedPassword" />
			<input oClass="NoEmptyInput" type="hidden" name="login" />
			<div class="app">
				<img src="">
				<p>App name:</p><input oClass="NoEmptyInput" type="text" name="name" />
			</div>
			<p class="error"></p>
			
			<div oClass="ClassicErrorMsg" class="buttonSet">
    			<button type="submit" id="accept" class="btn btn-default btn-primary btn-group btn-lg">OK</button>
    			<button type="button" oClass="CloseButton" id="close" class="btn btn-default btn-primary btn-group btn-lg">Cancel</button>
    		</div>
    	</form>
	</div>
</div>
 
<script>
	var popupAddUpdate = new Popup["AddUpdatePopup"]($("#AddUpdatePopup"));
</script>