function getAndSites(json) {
	var andJson = json.filter(function(e, i) {
		return e[1] == "and";
	});
	//console.log(andJson);
	return $('.catalogApp').filter(function(i, e) {
		andJson.includes($(e).attr("idx").toString());
	});
}

function sortSites(sites) {
	var names = sites.map(function(e, i) {
		return {
			index : i,
			value : $(e).attr("name").toLowerCase()
		};
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
		if (json.length > 0) {
			$('.catalogContainer .no-result-search').hide();
			var appsToShow = $('.catalogApp').filter(function(index, element) {
				return json.includes($(element).attr("idx"));
			});
			appsToShow.show();
		} else
		$('.catalogContainer .no-result-search').show();
	}
}

function updateCatalogWith(searchVal, tags) {
	
	var ids = [];
	var tagNames = ""
	tags.each(function(index, tag) {
		ids.push($(tag).attr("tagid"));
		tagNames += $(tag).text();
		if (index < tags.length)
			tagNames += " ";
	});
	var json = JSON.stringify(ids);
	postHandler.post('SearchApp', {
		tags : json,
		search : searchVal
	}, function() {
		
	}, function(retMsg) {
		easeTracker.trackEvent("CatalogSearch", {"SearchContent":searchVal, "SelectedTags": tagNames});
		refreshCatalogContent(retMsg);
	}, function(retMsg) {
	}, 'text');
}

function newButtonGroup(tagId) {
	$(".selectedTagsContainer").append(
		"<div tagid='" + tagId
		+ "' class='btn-group btn-group-xs tags-group'></div>");
	return $("div[tagid='" + tagId + "'].tags-group");
}

function newCrossButton(tagId) {
	return ("<a href='#' tagid='" + tagId + "' class='btn btn-default delete-tag'>x</button>");
}

function setNewCrossCss(tagId) {
	var tag = $("a[tagid='" + tagId + "'].tag")
	$("a[tagid='" + tagId + "'].delete-tag").css("background-color",
		lighterColor(tag.css("background-color"), 0.3));
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
	var searchIconWidth = parseInt($(".catalogSearchbar i.fa-search").css(
		"width"));
	$(".tags-group").each(function(index, grp) {
		fullWidth += parseInt($(grp).css("width"));
	});
	fullWidth += searchIconWidth + 7;
	// $(".selectedTagsContainer").css("left", (fullWidth));
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
		$(".tagContainer .tags").prepend(tagButton);
		btnGroup.remove();
	}
	updateTagsInSearchBar();
	updateCatalogWith($(".catalogSearchbar input").val(),
		$(".selectedTagsContainer .tag"));
	var appsShow = $('.catalogApp').filter(function() {
		return this.style.display != "none";
	});
	if (appsShow.length == 0) {
	}

}

function removeLastSelectedTag() {
	var lastTag = $(".selectedTagsContainer .tag").last();
	lastTag.removeClass('tag-active');
	updateCatalogFront(lastTag);
}

function addTagIfExists(input) {
	var name;
	if (input.val().slice(-1) == " ")
		name = input.val().toLowerCase().slice(0, -1);
	else
		name = input.val().toLowerCase();
	var tag = $('.tagContainer').find('.tag[name="' + name + '"]');
	var activeTag = $('.tag-active');
	if (tag.length == 0)
		return;
	else if (activeTag.length == 0){
		tag.addClass('tag-active');
		updateCatalogFront(tag);
		input.val("");
	} else {
		activeTag.toggleClass("hvr-grow");
		activeTag.toggleClass("tag-active");
		updateCatalogFront(activeTag);
		tag.addClass('tag-active');
		updateCatalogFront(tag);
		input.val("");
	}
}

function updateVerfiedEmailsCount() {
	var x = $(".verifiedEmail").length;
	if (x > 1)
		$(".integrated-emails-count span").html(x + " validated emails");
	else
		$(".integrated-emails-count span").html(x + " validated email");
}

function removeActiveTagFromFront(activeTag) {
	var btnGroup = activeTag.parent();
	$(".tagContainer .tags").prepend(activeTag);
	btnGroup.remove();
}

$(document).ready(function() {

	$("#catalog-quit").click(function(event) {
		event.stopPropagation();
		leaveEditMode();
	});
	
	$(".tag").click(function(event) {
		event.stopPropagation();
		var activeTag = $(".selectedTagsContainer .tag-active");
		if (activeTag.length)
			removeActiveTagFromFront(activeTag);
		/*activeTag.toggleClass("hvr-grow");
		activeTag.toggleClass("tag-active");
		updateCatalogFront(activeTag);*/
		if(!$(event.target).hasClass("tag-active")){
			$(event.target).toggleClass("tag-active");
			$(event.target).toggleClass("hvr-grow");
		}
		updateCatalogFront($(event.target));
		var tagName = $(event.target).text();
		easeTracker.trackEvent("ClickOnTag", {"TagName": tagName});
	});
	
	$("input[name='catalogSearch']").keydown(function(event) {
		if (event.keyCode == 8) {
			if ($(event.target).val() == "")
				removeLastSelectedTag();
			updateCatalogWith($(event.target).val(),$(".selectedTagsContainer .tag"));
		}
	});

	$("input[name='catalogSearch']").keyup(function(event) {
		if (event.keyCode == 8) {
			event.preventDefault();
			event.stopPropagation();
		}
		if (event.keyCode == 32 || event.keyCode == 13)
			addTagIfExists($(event.target));
		updateCatalogWith($(event.target).val(),
			$(".selectedTagsContainer .tag"));
	});

	$('.tagContainer i.fa-angle-right').click(function() {
		amount = '+='+ $('.tagContainer .tags').css('width');
		scroll();
		$('.tagContainer i.fa-angle-left').show();
		if ($('.tagContainer .tags').scrollLeft() + parseInt($('.tagContainer .tags').width()) >= getTagsFullWidth())
			$('.tagContainer i.fa-angle-right').hide();
		else
			$('.tagContainer i.fa-angle-right').show();
	});

	$('.tagContainer i.fa-angle-left').click(function() {
		amount = '-='+ $('.tagContainer .tags').css('width');
		$('.tagContainer i.fa-angle-right').css("display", "inline-block");
		scroll();
		if ($('.tagContainer .tags').scrollLeft()- parseInt($('.tagContainer .tags').css('width')) <= 0)
			$('.tagContainer i.fa-angle-left').css("display", "none");
	});
	
	updateVerfiedEmailsCount();
	$(".integrated-emails-count span").click(function() {
		$("#ModifyUserButton").click();
	});
});

function getTagsFullWidth() {
	var res = 0;
	$(".tag").each(function(index, e) {
		res += $(e).outerWidth();
	});
	return res;
}

var amount = "";

function scroll() {
	$('.tagContainer .tags').animate({
		scrollLeft : amount
	}, 200, 'linear');
}

var ssoObject = function(name, id, imgSrc){
	var self = this;
	this.name = name;
	this.id = id;
	this.imgSrc = imgSrc;
};

var catalog;
var Catalog = function(rootEl){
	var self = this;
	this.qRoot = rootEl;
	this.isOpen = false;
//	this.oUpdate = new UpdateManager(this.qRoot.find('.catalogUpdates'));
	this.quitButton = this.qRoot.find('#quit');
	this.appsHolder = this.qRoot.find('.scaleContainerView');
	this.appsArea = this.qRoot.find('#catalog');
	this.searchBar = this.qRoot.find('.catalogSearchbar');
	this.tagContainer = this.qRoot.find('.tagContainer');
	this.integrateAppArea = this.qRoot.find('.helpIntegrateApps');
	this.apps = [];
	this.ssos = [];

	postHandler.post(
		'GetCatalogApps',
		{},
		function(){
		},
		function(msg){
			var apps = JSON.parse(msg);
			var app;
			for (var i = 0; i < apps.length; i++) {
				app = apps[i];
				self.addApp(new catalogApp(app.name, app.singleId, app.logo, app.loginWith, app.ssoId, app.url, app.inputs, app.isNew));
			}
		},
		function(msg){
		},
		'text'
		);
	postHandler.post(
		'GetSso',
		{},
		function(){},
		function(msg){
			var ssos = JSON.parse(msg);
			for (var i = 0; i < ssos.length; i++) {
				self.ssos.push(new ssoObject(ssos[i].name, ssos[i].singleId, ssos[i].imgSrc));
			}
		},
		function(msg){

		},
		'text'
	);
	this.getSsoById = function(id){
		for (var i = 0; i < self.ssos.length; i++) {
			if (self.ssos[i].id == id)
				return self.ssos[i];
		}
		return null;
	}
	this.getAppByName = function(name){
		for (var i = 0; i < self.apps.length; i++) {
			if (self.apps[i].name == name)
				return self.apps[i];
		}
	}
	this.getAppById = function(id){
		for (var i = 0; i < self.apps.length; i++) {
			if (self.apps[i].id == id)
				return self.apps[i];
		}
	}
	this.getAppsBySsoId = function(ssoId){
		var retVal = [];
		for (var i = 0; i < self.apps.length; i++) {
			if (self.apps[i].ssoId == ssoId)
				retVal.push(self.apps[i]);
		}
		return retVal;
	}
	this.addApp = function(app){
		self.apps.push(app);
		self.appsArea.append(app.qRoot);
	};
	this.open = function(){
		self.qRoot.addClass('show');
		self.isOpen = true;
		easeTracker.trackEvent('OpenCatalog');
//		self.oUpdate.onCatalogOpen();
		self.onResize();
	};
	this.close = function(){
		self.qRoot.removeClass('show');
		self.isOpen = false;
//		self.oUpdate.hide();
	};
	this.onResize = function(){
		self.appsHolder.height(self.qRoot.outerHeight(true) 
			- (/*(self.oUpdate.isShown ? self.oUpdate.qRoot.outerHeight(true) : 0)
				+ */self.qRoot.find(".catalogHeader.title").outerHeight(true)
				+ self.searchBar.outerHeight(true)
				+ self.tagContainer.outerHeight(true)
				+ (self.tagContainer.outerHeight(true) / 2)
				+ self.integrateAppArea.outerHeight(true)));
		//self.isOpen && $('.openCatalogHelper').height(self.qRoot.height());
		var width = 0;
		self.apps.forEach(function(elem) {
			if (width == 0)
				width = elem.qRoot.width();
			elem.qRoot.height(width);
		});
	};
	lastLineViewed = 0;
	$(".catalogContainer").scroll(function(event) {
		var appHeight = $(".catalogApp").height();
		var st = $(this).scrollTop();
		var newLastLine = Math.floor(st / appHeight);
		if (newLastLine > lastLineViewed) {
			lastLineViewed = newLastLine;
			easeTracker.trackEvent("CatalogScroll", {"CatalogLinesViewed":lastLineViewed});
		}
	});
	this.onResize();
	this.quitButton.click(function(){
		leaveEditMode();
	});
	$(window).resize(function(){
		self.onResize();
	});
	this.haveThisUrl = function (url) {
		var apps = [];
		for (var cpt = 0; cpt < self.apps.length; ++cpt){
			if (self.apps[cpt].url.indexOf(url) > -1)
				apps.push(self.apps[cpt]);
		}
		return apps;
	};
	//this.oUpdate.test();
}

$(document).ready(function(){
	catalog = new Catalog($('.CatalogViewTab'));
});