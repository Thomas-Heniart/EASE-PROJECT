<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<c:forEach items='${siteManager.getSitesList()}' var="item">
	<div class="catalogApp" idx="${item.getId()}"
		connect="${item.getFolder()}connect.json"
		data-login="${item.getLoginWith()}" data-sso="${item.getSso()}" name="${item.getName()}">
		<div class="catalogAppLogo">
			<img src="${item.getFolder()}logo.png" />
		</div>
		<div class="catalogAppName">
			<c:choose>
				<c:when test="${item.getName().length() > 14}">
					<p>${item.getName().substring(0,14)}...
					<p>
				</c:when>
				<c:otherwise>
					<p>${item.getName()}</p>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</c:forEach>