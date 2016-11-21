<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="RequestedWebsitesTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div class="requestedWebsitesView">
		<div class="requestedWebsitesHeader">
			<button id="sendAllMails">Send all emails</button>
			<p>People asked if we could add these websites</p>
		</div>
	</div>
</div>

<div class="md-modal md-effect-15 popup" id="PopupSendEmailWebsite">
	<div class="md-content">
		<p class="title"></p>
		<input></input>
		<div class="buttonSet">
    		<button type="button" id="accept" class="btn btn-default btn-primary btn-group btn-lg">Yes</button>
    		<button type="button" id="close" class="btn btn-default btn-primary btn-group btn-lg">No</button>
    	</div>
	</div>
</div>