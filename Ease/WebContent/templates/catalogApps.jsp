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
					<p>${item.getName()}</p>
		</div>
	</div>
</c:forEach>