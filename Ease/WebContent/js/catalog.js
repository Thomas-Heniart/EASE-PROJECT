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
			$('.catalogApp').draggable(
				{
					cursor : 'none',
					cursorAt : {
					left : 25,
					top : 25
				},
				helper : function(e, ui) {
					var ret;
					ret = $('<div class="dragHelperLogo" style="position: fixed;"/>');
					ret.attr("idx", $(this).attr("idx"));
					ret.attr("name", $(this).find('p').text());
					ret.attr("connect", $(this).attr("connect"));
					ret.attr("data-login", $(this).attr("data-login"));
					ret.append($('<img />'));
					ret.find('img').attr("src", $(this).find('img').attr("src"));
					return ret; //Replaced $(ui) with $(this)
					}
				});
		}
	}

	function updateCatalogWhenClickOnTag(tags) {
		var ids = [];
		tags.each(function(index, tag) {
			ids.push(parseInt($(tag).attr("tagid")));
		});
		var json = JSON.stringify(ids);
		$.post(
			'updateSelectedTags',
			{
				tagIds : json
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

function newButtonGroup(tagId) {
	$(".selectedTagsContainer").append("<div tagid='"+ tagId +"' class='btn-group btn-group-xs tags-group'></div>");
	return $("div[tagid='" + tagId + "'].tags-group");
}

function newCrossButton(tagId) {
	return ("<a href='#' tagid='" + tagId + "' class='btn btn-default delete-tag'>x</button>");
}

function setNewCrossCss(tagId) {
	var tag = $("a[tagid='" + tagId + "'].tag")
	$("a[tagid='" + tagId + "'].delete-tag").css("background-color", tag.css("background-color"));
	$("a[tagid='" + tagId + "'].delete-tag").css("color", tag.css("color"));
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
		setNewCrossCss(tagId);
		addActionOnCrossButton(tagId);
	} else {
		var btnGroup = tagButton.parent();
		$(".tagContainer").append(tagButton);
		btnGroup.remove();
	}
	updateTagsInSearchBar();
	updateCatalogWhenClickOnTag($(".selectedTagsContainer .tag"));
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
