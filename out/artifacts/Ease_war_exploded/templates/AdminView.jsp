<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="admin-menu">
	<div>
		<button id="enterChangeBackMode" class="button adminButton"
			target="ChangeBackTab">
			<img src="resources/icons/upload_back.png" />
		</button>
	</div>
</div>
<div class="popupHandler" id="easePopupsHandler">
	<div class="easePopup show" id="editRequestedWebsitePopup">
		<div class="title">
			<p>Edit requested website</p>
		</div>
		<div class="bodysHandler">
			<div class="popupBody show" id="editRequestedWebsite">
				<div class='handler'>
					<div class="row text-center">
						<div class="input">
							<input id="websiteUrl" type="text" placeholder="Set website name" />
						</div>
						<div class="hidden" id="emailsToSend"></div>
					</div>
					<div class="row text-center errorText errorHandler">
						<p></p>
					</div>
					<div class="row text-center">
						<button class="btn locked" id="nextStep">Next</button>
					</div>
					<div class="row text-center">
						<a id="goBack" class="liteTextButton">Go back</a>
					</div>				
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="ChangeBackground.jsp"%>