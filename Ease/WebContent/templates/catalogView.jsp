<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.Ease.context.SiteManager"%>
<%@ page import="com.Ease.context.Site"%>
<%@ page import="com.Ease.context.Tag"%>
<%@ page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/catalog" prefix="catalog" %>


<script src="js/catalog.js"></script>

<div class="CatalogViewTab">
	<catalog:catalogContainer siteManager="${siteManager}"/>
</div>
