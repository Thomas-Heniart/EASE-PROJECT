<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.List"%>
<script src="js/postHandler.js"></script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="admin-menu">
	<div>
		<button id="enterChangeBackMode" class="button adminButton"
			target="ChangeBackTab">
			<img src="resources/icons/upload_back.png" />
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
		<button id="enterAddSiteMode" state="off" class="button adminButton"
			target="AddSiteTab">
			<img src="resources/icons/add_website_icon.png" />
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
		<button id="enterTagsManagerMode" state="off"
			class="button adminButton" target="TagsManagerTab">
			<img src="resources/icons/tags_icon.png" />
		</button>
	</div>
	<div>
		<button id="enterStatisticsMode" state="off"
			class="button adminButton" target="StatisticsTab">
			<img src="resources/icons/ascendant-bars-graphic.png" />
		</button>
	</div>
	<div>
		<button id="cleanSavedSessions" state="off"
			class="button adminButton">
			<img src="resources/icons/vacuum-cleaner.png" />
		</button>
	</div>
	
</div>

<%@ include file="Statistics.jsp"%>
<%@ include file="AddUsers.jsp"%>
<%@ include file="RequestedSitesView.jsp"%>
<%@ include file="UploadWebsite.jsp"%>
<%@ include file="MoveSites.jsp" %>
<%@ include file="TagsManager.jsp"%>
<%@ include file="ChangeBackground.jsp"%>
<%@ include file="ServerKeysManager.jsp"%>