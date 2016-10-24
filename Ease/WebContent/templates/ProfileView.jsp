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
	<div class="owl-carousel">
		<c:forEach items='${profiles}' var="item" varStatus="loop">
			<c:if test='${item.getProfileId() != 0}'>
				<dashboard:profile profile="${item}"/>
			</c:if>
		</c:forEach>
	</div>
</div>
<%@ include file="ObjectHelpers.jsp"%>
</div>
