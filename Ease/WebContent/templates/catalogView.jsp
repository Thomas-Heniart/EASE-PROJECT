<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.Ease.context.SiteManager"%>
<%@ page import="com.Ease.context.Site"%>
<%@ page import="com.Ease.context.Tag"%>
<%@ page import="java.util.List"%>


<script src="js/catalog.js"></script>

<div class="catalogView">
	<div class="catalogHeader">
		<p>Integrate your accounts</p>
	</div>
	<div class="scaleContainer">
		<img class="Scaler" src="https://placehold.it/4x4" style="width: 100%; height: auto; visibility: hidden;"/>
		<div id="catalog" class="catalogContainer">
<div class="CatalogViewTab">
	<div class="catalogView">
		<div class="catalogSearchbar">
			<div class="selectedTagsContainer"></div>
			<input type="text" name="catalogSearch" class="form-control"
				placeholder="Search your favorite websites" />
		</div>

		<div class="scaleContainerView">
			<div class=tagContainer>
				<c:forEach items='${tags}' var="item">
					<a href="#" tagId="${item.getId()}" class="tag btn btn-default"
						style="background-color: ${item.getColor()}; border-color: ${item.getColor()}">
						${item.getName()}</a>
				</c:forEach>
			</div>
			<div class="scaleContainer">
				<img class="Scaler" src="https://placehold.it/4x4"
					style="width: 100%; height: auto; visibility: hidden;" />
				<div id="catalog" class="catalogContainer">
					<%@ include file="catalogApps.jsp"%>
				</div>
			</div>
		</div>
		<div class="helpIntegrateApps">
			<div class="DivHeader">
				<p>We build this catalog thanks to users suggestions</p>
			</div>
			<div id="integrateAppForm">
				<div class="inputs">
					<input id="integrateApp" name="name" type="text"
						placeholder="Tell us websites you love !" />
					<p class="hidden">Done! We check asap :)</p>
				</div>
				<div class="buttonSet">
					<button id="integrate">
						<i class="fa fa-arrow-circle-right"></i>
					</button>
					<i class="fa fa-check-circle hidden"></i>
				</div>
			</div>
		</div>
	</div>
</div>
