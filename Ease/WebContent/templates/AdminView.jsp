<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="com.Ease.session.User"%>
<%@ page import="com.Ease.session.Profile"%>
<%@ page import="com.Ease.context.Site"%>
<%@ page import="com.Ease.context.SiteManager"%>
<%@ page import="com.Ease.context.Color"%>
<%@ page import="com.Ease.session.App"%>
<%@ page import="com.Ease.session.ClassicAccount"%>
<%@ page import="com.Ease.session.LogWithAccount"%>
<%@ page import="java.util.List"%>
<script src="js/postHandler.js"></script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	com.Ease.session.User user = (com.Ease.session.User) (session.getAttribute("User"));
%>

<div class="admin-menu">
	<div>
		<button id="enterChangeBackMode" class="button adminButton">
			<img src="resources/icons/upload_back.png" />
		</button>
	</div>
	<div>
		<button id="enterAddUsersMode" state="off" class="button adminButton">
			<img src="resources/icons/add_users_icon.png" />
		</button>
	</div>
	<div>
		<button id="enterRequestedWebsitesMode" state="off"
			class="button adminButton">
			<img src="resources/icons/requested_websites.png" />
		</button>
	</div>
	<div>
		<button id="enterAddSiteMode" state="off" class="button adminButton">
			<img src="resources/icons/add_website_icon.png" />
		</button>
	</div>
	<div>
		<button id="enterTagsManagerMode" state="off"
			class="button adminButton">
			<img src="resources/icons/tags_icon.png" />
		</button>
	</div>
	<div>
		<button id="enterStatisticsMode" state="off"
			class="button adminButton">
			<img src="resources/icons/ascendant-bars-graphic.png" />
		</button>
	</div>
</div>

<div class="AdminMenu show">
	<%@ include file="Statistics.jsp"%>
	<%@ include file="AddUsers.jsp"%>
	<%@ include file="RequestedSitesView.jsp"%>
	<%@ include file="UploadWebsite.jsp"%>
	<%@ include file="TagsManager.jsp"%>
	<%@ include file="ChangeBackground.jsp"%>
</div>