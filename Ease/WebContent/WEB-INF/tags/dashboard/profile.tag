<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/dashboard" prefix="dashboard" %>

<%@ attribute name="profile" type="com.Ease.session.Profile" required="true"%>

<div class="item" id='${profile.getProfileId()}'>
	<div class="ProfileBox" custom="${profile.isCustom()}"
		style="border-bottom: 5px solid ${profile.getColor()};"
		color="${profile.getColor()}">
		<div class="ProfileName"
	     	 style="background-color: ${profile.getColor()};">
			<p>@<c:out value='${profile.getName()}' /></p>
			<div class="ProfileSettingsButton">
				<i class="fa fa-fw fa-ellipsis-v"></i>
			</div>
		</div>
		<div class="ProfileContent">
			<img class="Scaler" src="resources/other/placeholder-36.png"
				 style="width: 100%; height: auto; visibility: hidden;" />
			<div class=content>
				<div class="ProfileControlPanel">
					<div class="profileSettingsTab">
						<div class="sectionHeader" id="NameSection">
							<p class="title">Profile name</p>
							<div class="directInfo">
								<p>${profile.getName() }</p>
							</div>
						</div>
						<div class="sectionContent" id="contentName">
							<div id="modifyNameForm">
								<input id="profileName" name="profileName" type="text"  maxlength="20"
								placeholder="Profile name..." />
								<div class="buttonSet">
									<button class="button" id="validate">Validate</button>
									<button class="button" oClass="CloseButton" id="cancel">Cancel</button>
								</div>
							</div>
						</div>
						<div class="sectionHeader" id="ColorSection">
							<p class="title">Color</p>
							<div class="directInfo"
								 style="background-color: ${profile.getColor()}"></div>
						</div>
						<div class="sectionContent" id="contentColor">
							<p>Choose your color</p>
							<div id="modifyColorForm">
								<div class="colorChooser">
									<input name="color" type="hidden" id="color" />
									<c:forEach items='${colors}' var="color" varStatus="loop">
										<c:if test='${(loop.index % 5) == 0}'>
											<div class="lineColor">
										</c:if>
										<div class="color" color="${color.getColor()}"
											 style="background-color: ${color.getColor()}"></div>
										<c:if test='${((loop.index + 1) % 5) == 0}'>
											</div>
										</c:if>
									</c:forEach>
									<c:if test='${(colors.size() % 5) != 0}'>
										</div>
									</c:if>
			</div>
			<div class="buttonSet">
				<button class="button" id="validate">Validate</button>
				<button class="button" oClass="CloseButton" id="cancel">Cancel</button>
			</div>
		</div>
	</div>
	<div class="sectionHeader" id="DeleteProfilSection">
		<p class="title">Delete profile</p>
	</div>
	<div class="sectionContent" id="contentDeleteProfil">
		<div id="deleteProfileForm">
			<p>By deleting your profile you will lose all related
				information and associated accounts</p>
				<div class="buttonSet">
					<button class="button" id="validate">Validate</button>
					<button class="button" oClass="CloseButton" id="cancel">Cancel</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div  class="SitesContainer">
	<c:forEach items='${profile.getApps()}' var="app">
		<dashboard:app app='${app}'/>
	</c:forEach>
</div>
</div>
</div>
</div>
</div>