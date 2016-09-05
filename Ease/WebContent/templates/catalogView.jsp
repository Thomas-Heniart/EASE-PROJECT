<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="CatalogViewTab">
<button id="quit"><i class="fa fa-times"></i></button>

<div class="catalogView">
	<div class="catalogHeader">
		<p>Integrate your accounts</p>
	</div>
	<div class="scaleContainer">
		<img class="Scaler" src="https://placehold.it/4x4" style="width: 100%; height: auto; visibility: hidden;"/>
		<div class="catalogContainer">

			<c:forEach items='${siteList}' var="item">
				<div class="catalogApp" idx="${item.getId()}" connect="${item.getFolder()}connect.json" data-login="${item.getLoginWith()}" name="${item.getName()}">
					<div class="catalogAppLogo">
						<img src="${item.getFolder()}logo.png"/>
					</div>
					<div class="catalogAppName">
						<c:choose>
					    	<c:when test="${item.getName().length() > 14}">
								<p>${item.getName().substring(0,14)}...<p>
						    </c:when>
						    <c:otherwise>
								<p>${item.getName()}</p>					    
						    </c:otherwise>
						</c:choose>
					</div>     
					
				</div>
			</c:forEach>
		</div>
	</div>
	<div class="helpIntegrateApps">
		<div class="DivHeader">
			<p>We build this catalog thanks to users suggestions</p>
		</div>
		<div id="integrateAppForm">
			<div class="inputs">
				<input id="integrateApp" name="name" type="text" placeholder="Tell us websites you love !"/>
				<p class="hidden">Done! We check asap :)</p>
			</div>
			<div class="buttonSet">
				<button id="integrate"><i class="fa fa-arrow-circle-right"></i></button>		
				<i class="fa fa-check-circle hidden"></i>
			</div>
		</div>
	</div>
</div>
</div>