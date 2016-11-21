<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="RequestedWebsitesTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div class="requestedWebsitesView">
		<div class="requestedWebsitesHeader">
			<p>People asked if we could add these websites</p>
		</div>
	</div>
</div>

<div class="md-modal md-effect-15 popup" id="PopupSendEmailWebsite">
	<div class="md-content">
		<form class="popupForm">
			<p class="title"></p>
			<textarea class="desc"></textarea>
			<input type="hidden" name="appId" />
			<div class="buttonSet">
    			<button type="button" id="accept" class="btn btn-default btn-primary btn-group btn-lg">Remove</button>
    			<button type="button" id="close" class="btn btn-default btn-primary btn-group btn-lg">Cancel</button>
    		</div>
    	</form>
</div>
</div>