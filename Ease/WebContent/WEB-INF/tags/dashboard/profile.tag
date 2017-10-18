<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/dashboard" prefix="dashboard" %>

<%@ attribute name="profile" type="com.Ease.Dashboard.Profile.Profile" required="true"%>

<div class="item" id='${profile.getDBid()}'>
	<div class="ProfileBox" 
	color="${profile.getColor()}">
	<div class="ProfileName"
	style="background-color: ${profile.getColor()};">
	<p>@<c:out value='${profile.getName()}' /></p>
	<div class="ProfileSettingsButton">
		<i class="fa fa-fw fa-ellipsis-v"></i>
	</div>
</div>
<div class="ProfileContent">
<!--			<img class="Scaler" src="resources/other/placeholder-32.png"
	style="width: 100%; height: auto; visibility: hidden;" />-->
	<div class=content>
		<div class="ProfileControlPanel">
			<div class="profileSettingsTab">
				<div class="sectionContent" id="contentName">
					<div id="modifyNameForm">
						<input id="profileName" name="profileName" type="text"  maxlength="20"
						placeholder="Profile name..." />
						<div id="validate">
							<i class="fa fa-refresh" aria-hidden="true"></i>
						</div>
					</div>
				</div>
				<div class="sectionContent" id="contentColor">
					<div id="modifyColorForm">
						<c:forEach items="${colors}" var="color" varStatus="loop">
							<c:if test="${loop.index < 8}">
									<div class="color ${profile.getColor() == color ? 'choosen': ''}" color="${color}" style="background-color: ${color}">
									</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
				<div class="sectionContent" id="contentDeleteProfil">
					<div id="deleteProfileForm">
						<div id="validate">Delete profile</div>
					</div>
				</div>
			</div>
		</div>
		<div  class="SitesContainer" id='${profile.getDBid()}'>
			<c:forEach items='${profile.getApps()}' var="app">
				<c:if test="${app.getType() eq 'ClassicAccount'}">
					<dashboard:app app='${app}' informations="${app.getVisibleInformations()}"/>	
				</c:if>
				<c:if test="${app.getType() ne 'ClassicAccount'}">
					<dashboard:app app='${app}'/>	
				</c:if>
		</c:forEach>
	</div>
</div>
</div>
</div>
</div>