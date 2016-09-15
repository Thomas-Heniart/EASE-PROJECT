function refreshCatalogContent(data) {
		if (data[0] == '[') {
			var json = JSON.parse(data);
			$('.catalogApp').remove();
			json.forEach(function(fields, index) {
				$('.catalogContainer').append("<div class='catalogApp' idx='"+ fields[0] +"' connect='"+ fields[1] +"connect.json' data-login='" + fields[2] + "' name='" + fields[3] + "' ></div>");
				$("div[idx='" + fields[0] + "']").append("<div class='catalogAppLogo'><img src='" + fields[1] + "logo.png' /></div>");
						if (fields[3].length > 14)
							$("div[idx='" + fields[0] + "']").append("<div class='catalogAppName'><p>" + fields[3].substring(0, 14) + "...</p></div>");
						else
							$("div[idx='" + fields[0] + "']").append("<div class='catalogAppName'><p>" + fields[3] + "</p></div>");
			});
		}
	}

	function updateCatalogWhenClickOnTag(tagDiv) {
		var id = tagDiv.attr("tagId");
		$.post(
			'updateSelectedTags',
			{
				tagId : id
			},
			function(data) {
				refreshCatalogContent(data);
			},
			'text');
	}

	function updateCatalogWith(search) {
		$.post(
			'searchInCatalog',
			{
				search : search
			},
			function(data) {
				refreshCatalogContent(data);
			},
			'text'
		);
	}

function setTagsPosition(tagsGrp) {
	var grpSize = tagsGrp.length;
	var marginSize = 0;
	if (grpSize == 1) {
		marginSize = parseFloat(tagsGrp.slice(0,1).css("width"));
	}
	else {
		marginSize = parseFloat(tagsGrp.slice(0,1).css("width")) + parseFloat(tagsGrp.slice(1,2).css("width"));
	}
	$("div[tagid='" + tagId + "'].btn-group").css("margin-right", - (marginSize + 10));
	tagsGrp.css("margin-left", 0);
	tagsGrp.first().css("margin-left", -(parseFloat(tagsGrp.slice(1,2).css("margin-right"))));
}

function moveButtonToSearchBar(tagButton) {
	tagId = tagButton.attr("tagid");
	if (tagButton.hasClass("tag-active")) {
		$(".catalogSearchbar").prepend("<div tagid='"+ tagId +"' class='btn-group btn-group-xs tags-group'></div>");
		$("div[tagid='" + tagId + "'].btn-group").append(tagButton);
		$("div[tagid='" + tagId + "'].btn-group").append("<button tagid='" + tagId + "' class='btn btn-default delete-tag'>x</button>");
		$("button[tagid='" + tagId + "'].delete-tag").click(function() {
			$("button[tagid='" + tagId + "'].tag").removeClass("tag-active");
			updateCatalogWhenClickOnTag(tagButton);
			var parent = tagButton.parent();
			$(".tagContainer").append(tagButton);
			parent.remove();
		});
		setTagsPosition($("div.tags-group"));
	}
	else {
		var parent = tagButton.parent();
		$(".tagContainer").append(tagButton);
		parent.remove();
	}
}

function newButtonGroup(tagId) {
	$(".selectedTagsContainer").append("<div tagid='"+ tagId +"' class='btn-group btn-group-xs tags-group'></div>");
	return $("div[tagid='" + tagId + "'].tags-group");
}

function newCrossButton(tagId) {
	return ("<a href='#' tagid='" + tagId + "' class='btn btn-default delete-tag'>x</button>");
}

function addActionOnCrossButton(tagId) {
	$("a[tagid='" + tagId + "'].delete-tag").click(function(event) {
		event.stopPropagation();
		var tagButton = $(event.target).parent().find("a.tag");
		$(tagButton).removeClass("tag-active");
		updateCatalogFront($(tagButton));
	})
}

function updateTagsInSearchBar() {
	var fullWidth = 0.;

	$(".tags-group").each(function(index, grp) {
		fullWidth += parseInt($(grp).css("width"));
	});
	$(".selectedTagsContainer").css("margin-right", -(fullWidth));
	$(".catalogSearchbar input").css("padding-left", fullWidth);
	return;
}

function updateCatalogFront(tagButton) {
	var tagId = tagButton.attr("tagid");
	if (tagButton.hasClass("tag-active")) {
		var btnGroup = newButtonGroup(tagId);
		btnGroup.append(tagButton);
		btnGroup.append(newCrossButton(tagId));
		addActionOnCrossButton(tagId);
	} else {
		var btnGroup = tagButton.parent();
		$(".tagContainer").append(tagButton);
		btnGroup.remove();
	}
	updateTagsInSearchBar();
	updateCatalogWhenClickOnTag($(event.target));
}

$(document).ready(function() {
	$(".tag").click(function(event) {
		event.stopPropagation();
		$(event.target).toggleClass("tag-active");
		updateCatalogFront($(event.target));
	});
	$("input[name='catalogSearch']").keyup(function(event) {
		updateCatalogWith($(event.target).val());
	});
});
