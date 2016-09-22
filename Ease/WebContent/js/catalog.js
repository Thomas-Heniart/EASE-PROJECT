function getAndSites(json) {
	var andJson = json.filter(function(e, i) {
		return e[1] == "and";
	});
	console.log(andJson);
	return $('.catalogApp').filter(function(i , e) {
		andJson.includes($(e).attr("idx").toString());
	});
}

function sortSites(sites) {
	var names = sites.map(function(e, i) {
		return { index: i, value: $(e).attr("name").toLowerCase() };
	});
	names.sort(function(a, b) {
		return +(a > b) || +(a === b) - 1;
	});
	var result = names.map(function(e) {
		return sites[e.index];
	});
	return result;
}

function refreshCatalogContent(data) {
		if (data[0] == '[') {
			var json = JSON.parse(data);
			$('.catalogApp').hide();
			var appsToShow = $('.catalogApp').filter(function(index, element) {
				return json.includes($(element).attr("idx"));
			});
			appsToShow.show();
		}
	}

	function updateCatalogWith(searchVal, tags) {
		var ids = [];
		tags.each(function(index, tag) {
			ids.push(parseInt($(tag).attr("tagid")));
		});
		var json = JSON.stringify(ids);
		$.post(
			'searchInCatalog',
			{
				tagIds : json,
				search : searchVal
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
	$("a[tagid='" + tagId + "'].delete-tag").css("background-color", lighterColor(tag.css("background-color"), 0.3));
	$("a[tagid='" + tagId + "'].delete-tag").css("color", tag.css("color"));
}

function addActionOnCrossButton(tagId) {
	$("a[tagid='" + tagId + "'].delete-tag").click(function(event) {
		event.stopPropagation();
		var tagButton = $(event.target).parent().find("a.tag");
		$(tagButton).removeClass("tag-active");
		$(tagButton).addClass("hvr-grow");
		updateCatalogFront($(tagButton));
	})
}

function updateTagsInSearchBar() {
	var fullWidth = 0.;
	var searchIconWidth = parseInt($(".catalogSearchbar i.fa-search").css("width"));
	$(".catalogSearchbar i.fa-search").css("left", "1.5%");
	$(".tags-group").each(function(index, grp) {
		fullWidth += parseInt($(grp).css("width"));
	});
	fullWidth += searchIconWidth + 7;
	$(".selectedTagsContainer").css("margin-right", -(fullWidth));
	if (fullWidth < 25)
		$(".catalogSearchbar input").css("padding-left", 25);
	else
		$(".catalogSearchbar input").css("padding-left", fullWidth);
	return;
}

function updateCatalogFront(tagButton) {
	var tagId = tagButton.attr("tagid");
	if (tagButton.hasClass("tag-active")) {
		var btnGroup = newButtonGroup(tagId);
		btnGroup.append(tagButton);
		btnGroup.append(newCrossButton(tagId));
		btnGroup.addClass('scaleinAnimation');
		setNewCrossCss(tagId);
		addActionOnCrossButton(tagId);
	} else {
		var btnGroup = tagButton.parent();
		$(".tagContainer").append(tagButton);
		btnGroup.remove();
	}
	updateTagsInSearchBar();
	updateCatalogWith($(".catalogSearchbar input").val() , $(".selectedTagsContainer .tag"));
}

function removeLastSelectedTag() {
	var lastTag = $(".selectedTagsContainer .tag").last();
	lastTag.removeClass('tag-active');
	updateCatalogFront(lastTag);
}

function addTagIfExists(input) {
	var tag = $('.tagContainer').find('.tag[name="' + input.val().toLowerCase().slice(0, -1) + '"]');
	console.log(tag);
	if (tag.length == 0)
		return;
	tag.addClass('tag-active');
	updateCatalogFront(tag);
	input.val("");
}

$(document).ready(function() {
	$("#catalog-quit").click(function(event) {
		event.stopPropagation();
		leaveEditMode();
	});
	$(".tag").click(function(event) {
		event.stopPropagation();
		var nbOfTags = $(".tag-active").length;
		if(nbOfTags < 3){
		$(event.target).toggleClass("tag-active");
		$(event.target).toggleClass("hvr-grow");
			$(event.target).toggleClass("tag-active");
			nbOfTags++;
			updateCatalogFront($(event.target));
		}
	});
	$("input[name='catalogSearch']").keydown(function(event) {
		if (event.keyCode == 8) {
			if ($(event.target).val() == "")
				removeLastSelectedTag();
			updateCatalogWith($(event.target).val(), $(".selectedTagsContainer .tag"));
		}
	});
	$("input[name='catalogSearch']").keyup(function(event) {
		if (event.keyCode == 8) {
			event.preventDefault();
			event.stopPropagation();
		}
		if (event.keyCode == 32) {
			addTagIfExists($(event.target));
		}
		updateCatalogWith($(event.target).val(), $(".selectedTagsContainer .tag"));
	});
});
