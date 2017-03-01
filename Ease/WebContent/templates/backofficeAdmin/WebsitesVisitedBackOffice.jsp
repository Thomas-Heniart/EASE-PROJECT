<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="WebsitesVisitedTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	
	<c:forEach items="${websitesVisitedManager.getWeightedWebsitesVisited()}" var="item">
		<div><button class='quit'>X</button> <a href="${item.getKey()}" target="_blank">${item.getKey()}</a> count: <strong>${item.getValue()}</strong></div>
	</c:forEach>
</div>
<script>
	$("#WebsitesVisitedTab .quit").click(function() {
		var self = $(this);
		var url = $("a", self.parent()).attr("href");
		postHandler.post("DeleteWebsiteVisited",
				{
					url: url
				},
				function() {
					
				},
				function(data) {
					self.parent().remove();
				}, function(data) {
					
				});
	});
</script>