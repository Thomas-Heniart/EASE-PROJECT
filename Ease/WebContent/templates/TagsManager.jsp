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
			<form method="post" id="addTagForm" action="addTag">
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
				<c:forEach items="${siteManager.getSitesList()}" var="item">
					<option value="${item.getId()}">${item.getName()}</option>
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
					<c:forEach items="${siteManager.getTagsList()}" var="item">
						<option value="${item.getId()}" tag-color="${item.getColor()}">${item.getName()}</option>
					</c:forEach>
				</select> <input type="submit" id="setTags"
					class="btn btn-default btn-primary"
					style="margin-left: 25%; margin-top: 1%; width: 50%; text-align: center; position: relative;"
					value="Validate" />
			</div>

		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		$("#setTags").click(function() {
			var tags = $('#tagsContainer div').map(function() {
				return $(this).attr("tagId");
			}).get();
			tags = JSON.stringify(tags);
			var websiteId = $("#websiteSelector").val();
			postHandler.post("setTags", {
				websiteId : websiteId,
				tagsId : tags
			}, function() {
			}, function(retMsg) {
				$('#setTags').val("Success");
				$('#setTags').prop('disabled', true);
				setTimeout(function() {
					$('#setTags').val("Validate");
					$('#setTags').prop('disabled', false);
				}, 1000);
			}, function(retMsg) {
				$('#setTags').val(retMsg);
				$('#setTags').prop('disabled', true);
				setTimeout(function() {
					$('#setTags').val("Validate");
					$('#setTags').prop('disabled', false);
				}, 1000);
			}, 'text');

		});
	});

	function showFormTags() {

		var websiteId = $("#websiteSelector").val();
		$('#tagsContainer').empty();

		if (websiteId == 0) {
			$('#completeForm').attr("style", "display:none");

		} else {
			postHandler
					.post(
							"getTags",
							{
								websiteId : websiteId
							},
							function() {
								$('#completeForm').attr("style",
										"display:visible");
							},
							function(retMsg) {
								var tags = JSON.parse(retMsg);
								for ( var i in tags) {
									$("#tagsContainer")
											.append(
													'<div tagId="'+ tags[i].id +'" class="btn-group tags-group" style="margin-left: 1px; margin-top: 3px;">'
															+ '<a href="#" class="tag btn btn-default"'
							+'style="background-color: '+  tags[i].color +'; border-color: '+  tags[i].color +'; color: white;">'
															+ tags[i].name
															+ '</a>'
															+ '<a href="#" onClick="deleteTag('
															+ tags[i].id
															+ ')" class="btn btn-default delete-tag">X</a>'
															+ '</div>');
								}
							}, function(retMsg) {
							}, 'text');
		}
	}

	function addTag() {
		var tagId = $("#tagSelector").val();
		if (tagId != 0) {
			if ($("#tagsContainer div[tagId=" + tagId + "]").length == 0) {

				var color = $("#tagSelector option[value=" + tagId + "]").attr(
						"tag-color");
				var name = $("#tagSelector option[value=" + tagId + "]").text();

				$("#tagsContainer")
						.append(
								'<div tagId="'+ tagId +'" class="btn-group tags-group" style="margin-left: 1px; margin-top: 3px;">'
										+ '<a href="#" class="tag btn btn-default"'
					+'style="background-color: '+ color +'; border-color: '+ color +'; color: white;">'
										+ name
										+ '</a>'
										+ '<a href="#" onClick="deleteTag('
										+ tagId
										+ ')" class="btn btn-default delete-tag">X</a>'
										+ '</div>');
			}
		}
	}

	function deleteTag(tagId) {
		$("#completeForm div div[tagId=" + tagId + "]").remove();
	}
</script>