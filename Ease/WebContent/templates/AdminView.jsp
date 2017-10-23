<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="admin-menu">
	<div>
		<button class="button adminButton"
			target="GrowthHackingTab">
			<img src="resources/icons/opened-email-envelope.png" />
		</button>
	</div>
	<div>
		<button class="button adminButton"
			target="UnregisteredUsersTab">
			<img src="resources/icons/lost.png" />
		</button>
	</div>
	<div>
		<button id="enterChangeBackMode" class="button adminButton"
			target="ChangeBackTab">
			<img src="resources/icons/upload_back.png" />
		</button>
	</div>
	<div>
		<button id="enterAddTesterMode" state="off" class="button adminButton"
			target="AddTesterTab">
			<img src="resources/icons/commercial-buldings.png" />
		</button>
	</div>
	<div>
		<button id="enterEditGroupsMode" state="off" class="button adminButton"
			target="EditGroupsTab">
			<img src="resources/icons/group.png" />
		</button>
	</div>
	<div>
		<button id="enterAddUsersMode" state="off" class="button adminButton"
			target="AddUsersTab">
			<img src="resources/icons/add_users_icon.png" />
		</button>
	</div>
	<div>
		<button id="enterRequestedWebsitesMode" state="off"
			class="button adminButton" target="RequestedWebsitesTab">
			<img src="resources/icons/requested_websites.png" />
		</button>
	</div>
	<div>
		<button id="enterWebsitesVisitedMode" state="off" class="button adminButton"
			target="WebsitesVisitedTab">
			<img src="resources/icons/internet.png" />
		</button>
	</div>
	<div>
		<button id="enterAddSiteMode" state="off" class="button adminButton"
			target="AddSiteTab">
			<img src="resources/icons/add_website_icon.png" />
		</button>
	</div>
	<div>
		<button id="testWebsites" state="off" class="button adminButton"
			target="TestWebsitesTab">
			<img src="resources/icons/robo_test.png" />
		</button>
	</div>
	<div>
		<button id="enterServerKeysManagerMode" state="off" class="button adminButton"
			target="ServerKeysManagerTab">
			<img src="resources/icons/earth-lock.png" />
		</button>
	</div>
	<div>
		<button id="enterMoveSitesMode" state="off" class="button adminButton"
			target="MoveSitesTab">
			<img src="resources/icons/apps.png" />
		</button>
	</div>
	<div>
		<button class="button adminButton" 
			target="TagsManagerTab">
			<img src="resources/icons/tags_icon.png" />
		</button>
	</div>
	<!-- <div>
		<button id="enterStatisticsMode" state="off"
			class="button adminButton" target="StatisticsTab">
			<img src="resources/icons/ascendant-bars-graphic.png" />
		</button>
	</div>  -->
	<div>
		<button id="cleanSavedSessions" state="off"
			class="button adminButton">
			<img src="resources/icons/vacuum-cleaner.png" />
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
<%@ include file="AddUsers.jsp"%>
<%@ include file="RequestedSitesView.jsp"%>
<%@ include file="UploadWebsite.jsp"%>
<%@ include file="MoveSites.jsp" %>
<%@ include file="ChangeBackground.jsp"%>
<%@ include file="ServerKeysManager.jsp"%>
<%@ include file="TestWebsites.jsp"%>
<%@ include file="addTester.jsp" %>
<%@ include file="AdminEditGroups.jsp" %>
<%@ include file="backofficeAdmin/WebsitesVisitedBackOffice.jsp" %>
<%@ include file="backofficeAdmin/GrowthHacking.jsp" %>
<%@ include file="backofficeAdmin/UnregisteredUsers.jsp" %>