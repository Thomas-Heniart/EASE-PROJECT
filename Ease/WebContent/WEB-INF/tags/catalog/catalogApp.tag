<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/catalog" prefix="catalog" %>

<%@ attribute name="site" type="com.Ease.context.Site" required="true"%>

<div class="catalogApp" idx="${site.getId()}"
	connect="${site.getFolder()}connect.json"
	data-login="${site.getLoginWith()}" data-sso="${site.getSso()}" data-nologin="${site.noLogin()}" name="${site.getName()}">
	<div class="catalogAppLogo">
		<img src="${site.getFolder()}logo.png" />
	</div>
	<div class="catalogAppName">
		<p>${site.getName()}</p>
	</div>
</div>