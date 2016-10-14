<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div oClass="DeleteAppPopup" class="md-modal md-effect-15 popup" id="PopupDeleteApp">
	<div class="md-content">
		<form oClass="DeleteAppForm" action="deleteApp">
			<p class="title">Are you sure you want to remove this app?</p>
			<p class="desc">Corresponding data will not be accessible</p>
			<input oClass="NoEmptyInput" type="hidden" name="appId" />
			<div oClass="ClassicErrorMsg" class="buttonSet">
    			<button type="submit" id="accept" class="btn btn-default btn-primary btn-group btn-lg">Remove</button>
    			<button type="button" oClass="CloseButton" id="close" class="btn btn-default btn-primary btn-group btn-lg">Cancel</button>
    		</div>
    	</form>
</div>
</div>
 
<script>
	var popupDeleteApp = new Popup["DeleteAppPopup"]($("#PopupDeleteApp"));
</script>
 