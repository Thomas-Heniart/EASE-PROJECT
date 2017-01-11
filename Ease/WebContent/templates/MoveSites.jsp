<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="RightSideViewTab" id="MoveSitesTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	<div class="movable-websites">
		<c:forEach items="${siteManager.getSitesList()}" var="item"
			varStatus="loop">
			<c:if test="${item.work()}">
				<div class="website" position="${item.getPosition()}" siteId="${item.getId()}" style="margin: 10px 0;">
					<img width="50" src="${item.getFolder()}logo.png" /> <span
						style="margin-left: 10px;">${item.getName()}</span> <span class="website-position">position
						: ${item.getPosition()}</span>
					<i class="goTop fa fa-angle-double-up" aria-hidden="true"></i>
					<i class="top fa fa-angle-up" aria-hidden="true"></i>
					<i class="down fa fa-angle-down" aria-hidden="true"></i>
					<i class="goDown fa fa-angle-double-down" aria-hidden="true"></i>
				</div>
			</c:if>
		</c:forEach>
		<form action="" id="ChangePositionForm" oClass="ChangePositionForm">
			<input type="hidden" name="position" />
		</form>
		<script>
			var changePositionForm = new Form["ChangePositionForm"]($("#ChangePositionForm"));
		</script>
	</div>
</div>