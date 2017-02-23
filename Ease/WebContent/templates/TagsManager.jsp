<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="TagsManagerTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div>
		<div>
			<p>Manage tags of catalog</p>
		</div>

		<div>
			<form method="post" id="addTagForm" action="AddTag">
				<input type="text" name="tagName" class="form-control"
					style="margin-left: 25%; margin-top: 1%; width: 50%; position: relative;"
					placeholder="Tag name" /> <select name="tagColor"
					class="form-control"
					style="margin-left: 25%; margin-top: 1%; width: 50%; position: relative;">
					<option value="0">-- Color (random if not selected) --</option>
					<option value="1">Yellow</option>
					<option value="2">Blue</option>
					<option value="3">Green</option>
					<option value="4">Red</option>
					<option value="5">Violet</option>
					<option value="6">Orange</option>
					<option value="7">Grey</option>
					<option value="8">Pink</option>
				</select> <input type="submit" class="btn btn-default btn-primary"
					style="margin-left: 25%; margin-top: 1%; width: 50%; text-align: center; position: relative;"
					value="Add tag" />
			</form>

		</div>

		<div style="margin-top: 8%">
			<select id="websiteSelector" name="websiteToTag"
				onChange="showFormTags()" class="form-control"
				style="margin-left: 25%; margin-top: 1%; width: 50%; position: relative;">
				<option value="0">-- Website --</option>
				<c:forEach items="${catalog.getWebsites()}" var="item">
					<option value="${item.getSingleId()}">${item.getName()}</option>
				</c:forEach>
			</select>
			<div id="completeForm" style="display: none;">
				<div id="tagsContainer"
					style="margin-left: 25%; margin-top: 1%; width: 50%; text-align: center; position: relative;">
				</div>
				<select id="tagSelector" name="websiteToTag" onChange="addTag()"
					class="form-control"
					style="margin-left: 25%; margin-top: 1%; width: 50%; position: relative;">
					<option value=0>-- Select Tag --</option>
					<c:forEach items="${catalog.getTags()}" var="item">
						<option value="${item.getSingleId()}" tag-color="${colors.get(item.getColor() - 1)}">${item.getName()}</option>
					</c:forEach>
				</select>
			</div>

		</div>
	</div>
</div>

<script>
$("#addTagForm").submit(function(e) {
	e.preventDefault();
	console.log($(this));
	console.log($(e.target));
	var self = $(e.target);
	var tagNameInput = $("input[name='tagName']", self); 
	var tagName = tagNameInput.val();
	var tagColor = $("select[name='tagColor']", self).val();
	postHandler.post(self.attr("action"), {
		tagName: tagName,
		tagColor: tagColor
	}, function() {
		
	}, function(data) {
		var tagJson = JSON.parse(data);
		$("#tagSelector").append("<option value='" + tagJson.singleId + "' tag-color='" + tagJson.color + "'>" + tagJson.name + "</option>");
		tagNameInput.val("");
	}, function(data) {
		
	});
	
});
</script>