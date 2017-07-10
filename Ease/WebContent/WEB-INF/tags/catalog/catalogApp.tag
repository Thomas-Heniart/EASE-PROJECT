<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/catalog" prefix="catalog"%>

<%@ attribute name="site" type="com.Ease.Context.Catalog.Website" required="true"%>
<%@ attribute name="newApp" type="java.lang.Boolean" required="false" %>

<div class="catalogApp" idx="${site.getDb_id()}"
	connect="${site.getFolder()}connect.json"
	data-login="${site.getLoginWith()}" data-sso="${site.getSso()}"
	data-nologin="${site.noLogin()}" name="${site.getName()}"
	url="${site.getUrl() }" newApp="${newApp}"
	<c:forEach items='${site.getInformations()}' var="item">
		${item.getInformationName()}="${item.getInformationType()}"
	</c:forEach>>
	<div class="catalogAppLogo">
		<img src="${site.getFolder()}logo.png" />
		<a href="${ site.getHomePageUrl()}" target="_blank" class="siteUrl">
			<span class="fa-stack fa-lg">
				<i class="fa fa-circle fa-stack-2x"></i>
				<i class="fa fa-stack-1x fa-link fa-rotate-90"></i>
			</span>
		</a>
		<span class="fa-stack fa-lg apps-integrated">
			<i class="fa fa-circle fa-stack-2x"></i>
			<i class="count">0</i>
		</span>
		<c:if test="${newApp == true}">
			<span class="newCatalogApp">New!</span>
		</c:if>
	</div>
	<div class="catalogAppName">
		<p>${site.getName()}</p>
	</div>
</div>