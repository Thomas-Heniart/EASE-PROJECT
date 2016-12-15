<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="app" type="com.Ease.Dashboard.App.App" required="true"%>
<%@ attribute name="informations" type="java.util.Map" required="false" %>
<c:choose>
	<c:when test="${app.getType() eq 'WebsiteApp'}">
	<div class="siteLinkBox emptyApp"
	login=""
	webId="${app.getSite().getSingleId()}"
	name="${app.getName()}"
	move="true"
	logWith="${app.getDataLogin()}"
	ssoId="${app.getSite().getSso()}"
	
	id="${app.getSingleId()}">
	<div class="linkImage">
		<div class="emptyAppIndicator" onclick="showModifyAppPopup(this, event)">
			<img src="resources/other/raise-your-hand-to-ask.svg" />
		</div>
		<div class="showAppActionsButton" >
			<i class="fa fa-cog"></i>
			<div class="appActionsPopup">
				<div class="buttonsContainer">
					<div class="modifyAppButton menu-item"
					onclick="showModifyAppPopup(this, event)">
						<p>Modify</p>
					</div>
					<c:if test="${app.havePerm(AppPermissions.Perm.DELETE)}">
						<div class="deleteAppButton menu-item" onclick="showConfirmDeleteAppPopup(this, event)">
							<p>Delete</p>
						</div>
					</c:if>
				</div>
			</div>
		</div>
		<img class="logo" src="<c:out value='${app.getSite().getFolder()}logo.png'/>" />
	</div>
</c:when>
<c:when test="${app.getType() eq 'LinkApp'}">
	<div class="siteLinkBox"
	 name="${app.getName()}"
	 id="${app.getSingleId()}"
	 move="true">
	<div class="linkImage">
	<c:if test="${app.havePerm(AppPermissions.Perm.DELETE)}">
	<div class="showAppActionsButton">
		<i class="fa fa-cog"></i>
		<div class="appActionsPopup">
			<div class="buttonsContainer">
			
				<div class="deleteAppButton menu-item"
					onclick="showConfirmDeleteAppPopup(this, event)">
					<p>Delete</p>
				</div>
			
			</div>
		</div>
	</div>
	</c:if>
	<img class="logo" src="resources/websites/Calendrier/logo.png" />
	</div>
</c:when>
<c:otherwise>
<c:if test="${app.getType() eq 'ClassicApp'}">
<div class="siteLinkBox"
	<c:forEach items="${app.getAccount().getAccountInformations()}" var="entry">
		${entry.getInformationName()}="${entry.getInformationValue()}"
	</c:forEach>
	 webId="${app.getSite().getSingleId()}"
	 name="${app.getName()}"
	 id="${app.getSingleId()}"
	 ssoId="${app.getSite().getSso()}"
	 move="true"
	 logwith="false">
</c:if>
<c:if test="${app.getType() eq 'LogwithApp'}">
<div class="siteLinkBox"
	 webId="${app.getSite().getSingleId()}"
	 name="${app.getName()}"
	 id="${app.getSingleId()}"
	 move="true"
	 logwith="${app.getLogwith().getSingleId()}">
</c:if>
<div class="linkImage">
	<div class="showAppActionsButton">
		<i class="fa fa-cog"></i>
		<div class="appActionsPopup">
			<div class="buttonsContainer">
				<div class="modifyAppButton menu-item"
				onclick="showModifyAppPopup(this, event)">
				<p>Modify</p>
			</div>
			<c:if test="${app.havePerm(AppPermissions.Perm.DELETE)}">
			<div class="deleteAppButton menu-item"
			onclick="showConfirmDeleteAppPopup(this, event)">
			<p>Delete</p>
		</div>
	</c:if>
</div>
</div>
</div>
<img class="logo" src="<c:out value='${app.getSite().getFolder()}logo.png'/>" />
</div>
</c:otherwise>
</c:choose>
<div class="siteName">
<p>${app.getName()}</p>
</div>
</div>
