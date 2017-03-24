<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/catalog" prefix="catalog" %>

<c:set var="user"			scope="session" value='${session.getAttribute("user")}'/>
<c:set var="catalog"	scope="session" value='${servletContext.getAttribute("catalog")}'/>
<c:set var="siteList"		scope="session" value='${catalog.getWebsites()}'/>
<c:set var="tags"			scope="session"	value='${servletContext.getAttribute("Tags")}'/>
<c:set var="tagAndSiteMapping"	scope="session" value='${servletContext.getAttribute("TagAndSiteMapping")}'/>

	<div class="catalogView">
		<div id="catalog-quit"> 
			<i class="fa fa-times" aria-hidden="true"></i>
		</div>
		<%@ include file="updates.jsp"%>

		<div class="catalogHeader title" >
			<span class="fa-stack fa-lg icon">
				<i class="fa fa-circle fa-stack-2x"></i>
				<i class="fa fa-home fa-stack-1x"></i>
			</span>
			<p> Find your websites </p>
		</div>
		<div class="catalogSearchbar">
			<i class="fa fa-search" aria-hidden="true"></i>
			<div class="selectedTagsContainer"></div>
			<input type="text" name="catalogSearch" class="form-control"
			placeholder="Search your favorite websites" />
		</div>
		<div class=tagContainer>
			<div class="container">
				<div class="shadowHelper"></div>
				<i class="fa fa-angle-left" aria-hidden="true"></i>
				<span class="tags">
					<c:forEach items='${catalog.getTags()}' var="item">
						<c:if test="${user.canSeeTag(item)}">
							<a href="#" tagId="${item.getSingleId()}" class="tag btn btn-default ease-button hvr-grow" name="${item.getName()}"
							style="background-color: ${item.getHexaColor()};">
							${item.getName()}</a>					
						</c:if>
				</c:forEach>
			</span>
			<i class="fa fa-angle-right" aria-hidden="true"></i>
		</div>
	</div>
	<div class="scaleContainerView">
		<div class="catalogArea">
			<div class="scaleContainer">
				<img class="Scaler" src="resources/other/placeholder-64.png"
				style="width: 100%; height: auto; visibility: hidden;" />
				<div id="catalog" class="catalogContainer">
					<h3 class="search-result"><span>Search result</span></h3>
					<div class="search-result"></div>
					<h4 class="relatedApps"><span>Related apps</span></h4>
					<div class="relatedApps"></div>
	<div class="no-result-search">
		<h2>No results yet...<img alt="no-result" src="resources/images/umbrella.png"/></h2>
		<p>Fine ! Take a look there</p>
		<img alt="arrow" src="resources/images/curved_arrow.png" />
	</div>
</div>
<div class="shadowHelper"> </div>
</div>
</div>
</div>
<div class="helpIntegrateApps">
	<div class="DivHeader">
		<h4><i class="fa fa-magic" aria-hidden="true"></i>Can't find a website ?</h4>
		<p>Suggest us any website ! We will integrate it asap.</p>
	</div>
	<div id="integrateAppForm">
		<div class="inputs input-group">
			<input id="integrateApp" name="name" type="text" class="form-control" placeholder="Enter website url" />
			<span class="input-group-btn">
				<button id="integrate" class="btn btn-default" type="button">Go!</button>
			</span>
		</div>
		<div class="confirmation">
			<p>Your suggestion has been sent</p>
			<i class="fa fa-check-circle"></i>
		</div>
		<i class="fa fa-check-circle hidden"></i>
	</div>
</div>
</div>
	<script src="js/catalog/catalogApp.js"></script>
	<script src="js/catalog/updates/update.js"></script>
	<script src="js/catalog/updates/updatesManager.js"></script>
	<script src="js/catalog/catalog.js"></script>

