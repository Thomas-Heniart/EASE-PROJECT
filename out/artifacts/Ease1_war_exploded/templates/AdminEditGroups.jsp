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
	<div class="results">
		<div class="groupList">
			<form style="display: none;" id="CreateGroup" action="CreateGroup" method="POST">
				<input type="text" placeholder="Parent id" id="parentId" />
				<input type="text" placeholder="Group name" id="groupName" />
				<button type="submit">Add group</button>
			</form>
		</div>
		<div class="userList">
			<!-- <button class="show" id="showAddUserInGroup">Add a user</button>
			<form id="formAddUserInGroup" action="AddUserInGroup" method="POST">
				<input type="email" name="email" placeholder="Email" />
				<input type="text" name="name" placeholder="Name" />
				<button type="submit">Invite user</button>
			</form>  -->
		</div>
		<div class="groupProfiles">
			<form action="CreateGroupProfile" id="CreateGroupProfile">
				<input type="hidden" name="groupId" />
				<input type="text" name="profileName" placeholder="Profile name" />
			</form>
		</div>
		<div class="groupApps"></div>
	</div>
</div>

<script>
	$("#CreateGroupProfile").submit(function(e) {
		e.preventDefault();
		var parameters = {}
		$(this).serializeArray().forEach(function(elem) {
			parameters[elem.name] = elem.value;
		});
		postHandler.post($(this).attr("action"),
				parameters,
				function(){},
				function(data) {
					var groupProfile = JSON.parse(data);
					displayGroupProfile(groupProfile);
					$("#CreateGroupProfile").hide();
				},
				function(data) {});
	});

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
	$(".infraButton").click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		$(".infraButton").removeClass("infraSelected");
		$(".groupList .group, .userList .user, .groupProfiles .groupProfile, .groupApps .groupApp").remove();
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
				e.preventDefault();
				e.stopPropagation();
				var groupId = $(this).attr("groupId");
				displayGroupUsers(groupId);
				displayGroupProfiles(groupId);
				displayGroupApps(groupId);
			});
		}, function(data) {
			
		});
	});
	
	function displayGroupUsers(groupId) {
		postHandler.post("GetGroupUsers", {
			groupId : groupId
		}, function() {
			
		}, function(data) {
			var users = JSON.parse(data);
			$(".userList .user").remove();
			users.forEach(displayUser);
		}, function(data) {
			
		});
	}
	
	function displayGroupProfiles(groupId) {
		$(".groupProfiles .groupProfile").remove();
		postHandler.post("GetGroupProfiles", {
			groupId : groupId
		}, function(){},
		function(data) {
			var groupProfiles = JSON.parse(data);
			groupProfiles.forEach(displayGroupProfile);
		}, function(data){});
	}
	
	function displayGroupApps(groupId) {
		$(".groupApps .groupApp").remove();
		postHandler.post("GetGrouApps", {
			groupId : groupId
		}, function(){},
		function(data) {
			var groupApps = JSON.parse(data);
			groupApps.forEach(displayGroupApp);
		}, function(data){});
	}
	
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
	
	function displayGroupProfile(groupProfile) {
		$(".groupProfiles").append("<div class='groupProfile' singleId='" + groupProfile.singleId + "'>"
				+ "Profile name : "
				+ groupProfile.infos.name
				+ "</div>");
	}
	
	function displayGroupApp(groupApp) {
		$(".groupApps").append("<div class='groupApp' singledId='" + groupApp.singleId + "'>"
				+ "App name : "
				+ groupApp.infos.name
				+ "</div>");
	}
	
	function newGroupToDisplay(name, groupId) {
		return "<div class='group' groupId='" + groupId + "'>"
		+ "<span class='groupName'>" + name + " => GroupId : " + groupId + "</span>"
		+ "<div class='groupChildren'></div>"
		+ "</div>";
	}
</script>