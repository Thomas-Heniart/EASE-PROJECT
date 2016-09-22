function getOnlyAndSites(sites) {
	return sites.filter(function(e) {
		return e[4] == 'hasAllTags';
	});
}

function getOnlyOrSites(sites) {
	return sites.filter(function(e) {
		return e[4] == 'hasSomeTags';
	});
}

function sortSites(sites) {
	var names = sites.map(function(e, i) {
		return { index: i, value: e };
	});
	names.sort(function(a, b) {
		return +(a.value[3].toLowerCase() > b.value[3].toLowerCase()) || +(a.value[3].toLowerCase() === b.value[3].toLowerCase()) - 1;
	});
	var result = names.map(function(e) {
		return sites[e.index];
	});
	return result;
}

function displaySite(fields, index, appendSelector) {
	console.log(fields);
	appendSelector.append("<div class='catalogApp' idx='"+ fields[0] +"' connect='"+ fields[1] +"connect.json' data-login='" + fields[2] + "' name='" + fields[3] + "' data-sso='" + fields[4] + "'></div>");
	$("div[idx='" + fields[0] + "']").append("<div class='catalogAppLogo'><img src='" + fields[1] + "logo.png' /></div>");
	if (fields[3].length > 14)
		$("div[idx='" + fields[0] + "']").append("<div class='catalogAppName'><p>" + fields[3].substring(0, 14) + "...</p></div>");
	else
		$("div[idx='" + fields[0] + "']").append("<div class='catalogAppName'><p>" + fields[3] + "</p></div>");
}

function displayAndSites(sites) {
	if (sites.length > 0) {
		$('.catalogContainer .search-result').show();
		sites.forEach(function(fields, index) {
			displaySite(fields, index, $('.catalogContainer div.search-result'));
		});
	} else {
		$('.catalogContainer .search-result').hide();
	}
}

function displayOrSites(sites) {
	if (sites.length > 0) {
		$('.catalogContainer .relatedApps').show();
		sites.forEach(function(fields, index) {
			displaySite(fields, index, $('.catalogContainer div.relatedApps'));
		});
	} else {
		$('.catalogContainer .relatedApps').hide();
	}
}

function refreshCatalogContent(data) {
		if (data[0] == '[') {
			var json = JSON.parse(data);
			var andSites = sortSites(getOnlyAndSites(json));
			var orSites = sortSites(getOnlyOrSites(json));
			$('.catalogApp').remove();
			if (andSites.length > 0 || orSites.length > 0) {
			displayAndSites(andSites);
			displayOrSites(orSites);
		} else {
			$('.catalogContainer .search-result').hide();
			$('.catalogContainer .relatedApps').hide();
			json.forEach(function(fields, index) {
				displaySite(fields, index, $('.catalogContainer'));
			});
		}

			/*json.forEach(function(fields, index) {
				$('.catalogContainer').append("<div class='catalogApp' idx='"+ fields[0] +"' connect='"+ fields[1] +"connect.json' data-login='" + fields[2] + "' name='" + fields[3] + "' ></div>");
				$("div[idx='" + fields[0] + "']").append("<div class='catalogAppLogo'><img src='" + fields[1] + "logo.png' /></div>");
						if (fields[3].length > 14)
							$("div[idx='" + fields[0] + "']").append("<div class='catalogAppName'><p>" + fields[3].substring(0, 14) + "...</p></div>");
						else
							$("div[idx='" + fields[0] + "']").append("<div class='catalogAppName'><p>" + fields[3] + "</p></div>");
			});*/
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
					ret.attr("data-sso", $(this).attr("data-sso"));
					ret.append($('<img />'));
					ret.find('img').attr("src", $(this).find('img').attr("src"));
					return ret; //Replaced $(ui) with $(this)
					}
				});
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

$(document).ready(function() {
	$("#catalog-quit").click(function(event) {
		event.stopPropagation();
		leaveEditMode();
	})
	$(".tag").click(function(event) {
		event.stopPropagation();
		var nbOfTags = $(".tag-active").length;
		if(nbOfTags < 3){
		$(event.target).toggleClass("tag-active");
		$(event.target).toggleClass("hvr-grow");
		nbOfTags++;
		updateCatalogFront($(event.target));
		}
	});
	$("input[name='catalogSearch']").keyup(function(event) {
		event.stopPropagation();
		updateCatalogWith($(event.target).val(), $(".selectedTagsContainer .tag"));
	});
});
