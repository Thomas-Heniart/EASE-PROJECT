<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="EditGroupsTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	
	<div class="infraList">
		<c:forEach items="${groupManager.getInfras()}" var="infra">
			<button class="infraButton" infraId="${infra.getSingleId()}">${infra.getName()}</button>
		</c:forEach>
		<div class="addNewInfra">
			<button class="show" id="showNewInfraForm">Add new infra</button>
			<form method="POST" enctype="multipart/form-data">
				<input type="text" placeholder="Infra name" name="infraName"/>
				<label for="infraImage">Infra image (png and 175 * 175)</label>	
				<input id="infraImage" type="file" accept=".png" name="infraImage" />
			</form>
		</div>
	</div>
	<div class="groupList">
		<div class="group">
			<span>Group1</span>
			<div class="groupChildren">
				<div class="group">
					<span>Group1 child 1</span>
					<div class="groupChildren">
						<div class="group">
							<span>Group1 child 1 child</span>
						</div>
					</div>
				</div>
				<div class="group">
					<span>Group1 child 2</span>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	$(".infraButton").click(function() {
		$(this).addClass("selected");
		var infraId = $(this).attr("infraId");
		postHandler.post("GetInfraGroups", {
			infraId : infraId
		}, function() {
			
		}, function(data) {
			var groups = JSON.parse(data);
			groups.forEach(displayGroup);
		}, function(data) {
			
		});
	});
	
	$(".addNewInfra #showNewInfraForm").click(function() {
		$(".addNewInfra form").addClass("show");
		$(this).removeClass("show");
	});
	
	function displayGroup(groupaJson) {
		$(".groupList").append()
		var children = groupJson.children;
		if (children == null)
			return;
	}
</script>