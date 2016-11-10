<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<div class="ProfilesView show">
	<div class="MenuButtonSet">
		<button id="enterEditMode" state="off" class="button"><img src="resources/icons/menu_icon.png"/>
			<div class="openCatalogHelper"></div>
		</div>
		<c:if test="${user.getTuto() == '0'}">
		<%@ include file="Tutorial.jsp"%>
	</c:if>
	<div class="ProfilesHandler">
		<c:forEach items='${dashboardColumns}' var='column' varStatus="loop">
		<c:if test="${loop.index != 0}">
		<div class="dashboardColumn" style="${column.size() == 0 ? 'width:0;': ''}">
			<c:forEach items='${column}' var="profile">
			<dashboard:profile profile="${profile}"/>
		</c:forEach>
	</div>
</c:if>
</c:forEach>
<div class="profileAdder">
	<span class="opener fa-stack fa-lg">
		<i class="fa fa-circle fa-stack-1x" aria-hidden="true"></i>
		<i class="fa fa-plus-circle fa-stack-1x" aria-hidden="true"></i>
	</span>
	<div class="colorChooser">
		<c:forEach items="${colors}" var="color" varStatus="loop">
			<c:if test="${loop.index < 8}">
				<div class="colorHolder">
					<div class="color" color="${color.getColor()}" style="background-color: ${color.getColor()}">
					</div>
				</div>
			</c:if>
		</c:forEach>
	</div>
	<div class="profileHeaderPreview">
		<div class="nameInput">
			<p>@</p>
			<input type="text" placeholder="Profile name..." maxlength="20" />
		</div>
		<div class="confirm">
			Go
		</div>
	</div>
</div>
</div>
<%@ include file="ObjectHelpers.jsp"%>
</div>
<p class="shortcutInfo" style="text-shadow: 0 0 30px black, 0 0 20px black, 0 0 10px black; color: white; font-size:12px; font-weight:500; position: fixed; bottom: 1.5%; left:2%; display:none;">Hold ctrl and click to open multiple apps.</p>
