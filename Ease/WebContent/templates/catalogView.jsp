<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.Ease.context.SiteManager"%>
<%@ page import="com.Ease.context.Site"%>
<%@ page import="com.Ease.context.Tag"%>
<%@ page import="java.util.List"%>


<script src="js/catalog.js"></script>

<div class="CatalogViewTab">
	<div class="catalogView">
		<i id="catalog-quit" class="fa fa-times" aria-hidden="true"></i>
		<div class="catalogSearchbar">
			<i class="fa fa-search" aria-hidden="true"></i>
			<div class="selectedTagsContainer"></div>
			<input type="text" name="catalogSearch" class="form-control"
				placeholder="Search your favorite websites" />
		</div>

		<div class="scaleContainerView">
			<div class=tagContainer>
				<c:forEach items='${siteManager.getTagsList()}' var="item">
					<a href="#" tagId="${item.getId()}" class="tag btn btn-default ease-button" name="${item.getName()}"
						style="background-color: ${item.getColor()}; border-color: ${item.getColor()}">
						${item.getName()}</a>
				</c:forEach>
			</div>
			<div class="scaleContainer">
				<img class="Scaler" src="resources/other/placeholder-63.png"
					style="width: 100%; height: auto; visibility: hidden;" />
				<div id="catalog" class="catalogContainer">
					<h3 class="search-result"><span>Search result</span></h3>
					<div class="search-result"></div>
					<h4 class="relatedApps"><span>Related apps</span></h4>
					<div class="relatedApps"></div>
					<%@ include file="catalogApps.jsp"%>
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
	</div>
</div>
