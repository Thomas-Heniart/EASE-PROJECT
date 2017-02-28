<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="WebsitesVisitedTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	
	<c:forEach items="${websitesVisitedManager.getWeightedWebsitesVisited()}" var="item">
		<div>Url: ${item.getKey()} and count: ${item.getValue()}</div>
	</c:forEach>
	
</div>