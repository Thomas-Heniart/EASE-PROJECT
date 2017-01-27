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
		<form action="CreateInfra" id="addNewInfraForm" method="POST" enctype="multipart/form-data">
			<input type="text" placeholder="Infra name" name="infraName"/>
			<label for="infraImage">Infra image (png and 175 * 175)</label>	
			<input id="infraImage" type="file" accept=".png" name="infraImage" />
			<button type="submit">Submit</button>
		</form>
	</div>
	</div>
	
	<div class="groupList">
		
	</div>
	<div class="userList">
		<!-- <button class="show" id="showAddUserInGroup">Add a user</button>
		<form id="formAddUserInGroup" action="AddUserInGroup" method="POST">
			<input type="email" name="email" placeholder="Email" />
			<input type="text" name="name" placeholder="Name" />
			<button type="submit">Invite user</button>
		</form>  -->
	</div>
	<form style="display: none;" id="CreateGroup" action="CreateGroup" method="POST">
		<input type="text" placeholder="Parent id" id="parentId" />
		<input type="text" placeholder="Group name" id="groupName" />
		<button type="submit">Add group</button>
	</form>
</div>

<script>
	$("#CreateGroup").submit(function(e) {
		e.preventDefault();
		var parentId = $("#parentId", $(this)).val();
		var groupName = $("#groupName", $(this)).val();
		var infraId = $(".infraSelected").attr("infraId");
		var params = { groupName: groupName, infraId: infraId };
		if (parentId != "" && parentId != null)
			params.parentId = parentId;
		postHandler.post($(this).attr("action"), params,
		function() {
			
		}, function(data) {
			$(".infraSelected").click();
		}, function(data) {
			
		});
	});
	$(".infraButton").click(function() {
		$(".infraButton").removeClass("infraSelected");
		$(this).addClass("infraSelected");
		var infraId = $(this).attr("infraId");
		postHandler.post("GetInfraGroups", {
			infraId : infraId
		}, function() {
			
		}, function(data) {
			var infra = JSON.parse(data);
			$(".groupList .group").remove();
			infra.groups.forEach(function(group) {
				displayGroup(group, $(".groupList"));
			});
			$(".addGroup").click(addGroup);
			$("#CreateGroup").show();
			$(".group").click(function(e) {
				e.stopPropagation();
				var self = $(this);
				var groupId = self.attr("groupId");
				postHandler.post("GetGroupUsers", {
					groupId : groupId
				}, function() {
					
				}, function(data) {
					var users = JSON.parse(data);
					$(".userList .user").remove();
					users.forEach(function(user) {
						displayUser(user);	
					});
				}, function(data) {
					
				});
			});
		}, function(data) {
			
		});
	});
	
	$(".addNewInfra #showNewInfraForm").click(function() {
		$(this).removeClass("show");
		$(".addNewInfra form").addClass("show");
	});
	
	
	function addGroup() {
		var input = $(this).parent().find("> input");
		var parentId = input.attr("parentId");
		var groupName = input.val();
		var infraId = $(".infraSelected").attr("infraId");
		if (parentId == "undefined")
			postHandler.post("CreateGroup", {
				infraId : infraId,
				groupName : groupName
			}, function() {
				
			}, function(data) {
				$(".infraSelected").click();
			}, function(date) {
				
			});
		else
			postHandler.post("CreateGroup", {
				parentId : parentId,
				infraId : infraId,
				groupName : groupName
			}, function() {
			
			}, function(data) {
				$(".infraSelected").click();
			}, function(date) {
			
			});
	}
	
	function displayGroup(groupJson, parent) {
		var groupId = groupJson.groupId;
		parent.append(newGroupToDisplay(groupJson.name, groupId));
		var parentGroup = $(".group[groupId='" + groupId + "']");
		var children = groupJson.children;
		if (children.length == 0) {
			return;			
		}
		else {
			children.forEach(function(child, index) {
				var elemWhereAppend = $("> .groupChildren", parentGroup)
				displayGroup(child, elemWhereAppend);
			});
			
		}
	}
	
	function displayUser(user) {
		$(".userList").append("<div class='user'>"
				+ "Name : "
				+user.name
				+ " email : "
				+ user.email
				+ "</div>");
	}
	
	function newGroupToDisplay(name, groupId) {
		return "<div class='group' groupId='" + groupId + "'>"
		+ "<span class='groupName'>" + name + " => GroupId : " + groupId + "</span>"
		+ "<div class='groupChildren'></div>"
		+ "</div>";
	}
</script>