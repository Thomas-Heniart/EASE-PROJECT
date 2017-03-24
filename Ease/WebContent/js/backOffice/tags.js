var tagManager;

var TagManager = function(rootEl) {
	var self = this;
	this.rootEl = rootEl;
	this.tags = [];
	this.tagWebsites = [];
	this.catalogWebsites = [];
	this.currentTag = null;
	this.current
	this.tagsRow = $("#tags", self.rootEl);
	this.tagWebsitesRow = $("#tag-websiteList", self.rootEl);
	this.tagSettingsRow = $("#tag-settings", self.rootEl);
	this.selectedColor = $(".tag-settings-color-selected", self.tagSettingsRow);
	this.tagColorsRow = $(".tag-settings-colors", self.tagSettingsRow);
	this.catalogWebsitesRow = $("#catalogWebsites", self.rootEl);
	this.loadTags = function() {
		postHandler.post("GetTags", {
			
		}, function() {
			self.tags.forEach(function(tag) {
				tag.removeFromDocument();
			});
			self.tags = [];
		}, function(data) {
			json = JSON.parse(data);
			json.forEach(function(tag) {
				self.addTag(tag);
			})
		}, function(data) {
			
		});
	}
	this.addCatalogWebsite = function(website) {
		var tmp = new CatalogWebsite(self.catalogWebsitesRow, website.name, website.imgSrc, website.single_id);
		self.catalogWebsites.push(tmp);
		tmp.printOnDocument();
	}
	this.loadCatalogWebsites = function() {
		postHandler.post("GetCatalog", {
			
		}, function() {
			
		}, function(data) {
			var json = JSON.parse(data);
			json.forEach(function(website) {
				self.addCatalogWebsite(website);
			});
		}, function(data) {
			
		});
	}
	this.addTag = function(tag) {
		var tmp = new Tag(self.tagsRow, tag.name, tag.color, tag.colorId, tag.single_id);
		self.tags.push(tmp);
		tmp.printOnDocument();
		tmp.onClick(function() {
			self.currentTag = tmp;
			self.loadTagWebsites();
			$("#tag-settings-editName", self.tagSettingsRow).val(tag.name);
			self.selectedColor.css('background-color', tag.color);
			self.selectedColor.attr("index", tag.colorId);
			self.selectedColor.text(tag.color);
			$("#tag-delete", self.tagSettingsRow).removeClass("hide");
		});
	}
	this.loadTagWebsites = function() {
		self.tagWebsitesRow.addClass("hidden");
		postHandler.post("GetTagWebsites", {
			single_id: self.currentTag.single_id
		}, self.emptyTagWebsites
		, function(data) {
			var json = JSON.parse(data);
			json.forEach(function(tagWebsite) {
				self.addTagWebsite(tagWebsite);
				self.catalogWebsites.find(function(catalogWebsite) {
					return catalogWebsite.single_id == tagWebsite.single_id;
				}).select();
			});
			setTimeout(function() {
				self.tagWebsitesRow.removeClass("hidden");
			}, 300);
		}, function(data) {
			
		});
	}
	this.emptyTagWebsites = function() {
		self.tagWebsites.forEach(function(websiteTag) {
			websiteTag.removeFromDocument();
		});
		self.tagWebsites = [];
	}
	this.addTagWebsite = function(tagWebsite) {
		var tmp = new TagWebsite(self.tagWebsitesRow, tagWebsite.single_id, tagWebsite.name, tagWebsite.imgSrc);
		self.tagWebsites.push(tmp);
		tmp.printOnDocument();
	}
	self.selectedColor.click(function() {
		self.tagColorsRow.toggleClass("show");
	});
	$("#addTagWebsite").click(function() {
		self.catalogWebsitesRow.toggleClass("hidden");
	});
	$("#tag-delete", self.tagSettingsRow).click(function() {
		var index = self.tags.indexOf(self.currentTag);
		if (index == -1)
			return;
		self.currentTag.remove();
		self.tags.splice(index, 1);
		self.tagWebsites.forEach(function(tagWebsite) {
			tagWebsite.removeFromDocument();
		});
		self.tagWebsites = [];
	});
	$(".tag-settings-color", self.tagColorsRow).click(function() {
		self.selectedColor.css('background-color', $(this).css('background-color'));
		self.selectedColor.attr("index", $(this).attr("index"));
		self.selectedColor.text($(this).text());
		self.tagColorsRow.removeClass("show");
	});
	$("#tag-settings-submit", self.tagSettingsRow).click(function() {
		var name = $("#tag-settings-editName", self.tagSettingsRow).val();
		var colorId = self.selectedColor.attr("index");
		var websitesSelected = self.catalogWebsites.filter(function(catalogWebsite) {
			return catalogWebsite.selected;
		}); 
		var websiteIds = websitesSelected.map(function(catalogWebsite) {
			console.log(catalogWebsite);
			return catalogWebsite.single_id.toString();
		});
		console.log(websiteIds);
		var websiteIdsString = JSON.stringify(websiteIds);
		if (self.currentTag != null) {
			postHandler.post("EditTag", {
				single_id: self.currentTag.single_id,
				name: name,
				colorId: colorId,
				websiteIds: websiteIdsString
			}, function() {
				
			}, function(data) {
				self.currentTag.setName(name);
				self.currentTag.setColor(self.selectedColor.css("background-color"), self.selectedColor.attr("index"));
				self.loadTagWebsites();
			}, function(data) {
				
			});
		} else {
			postHandler.post("AddTag", {
				name: name,
				colorId: colorId
			}, function() {
				
			}, function(data) {
				var jsonTag = JSON.parse(data);
				self.addTag(jsonTag);
				self.currentTag = null;
			}, function(data) {
				
			});
		}
		
	});
}

var Tag = function(rootEl, name, color, colorId, single_id) {
	var self = this;
	this.rootEl = rootEl;
	this.name = name;
	this.color = color;
	this.colorId = colorId;
	this.single_id = single_id;
	this.elem = null;
	this.websiteTagRow = $("#TagsManagerTab #tag-websiteList");
	this.printOnDocument = function() {
		self.elem = $("<span class='tag ease-button hvr-grow' style='background-color: "
						+ self.color
						+";'>"
						+ "<span>"
						+ self.name
						+ "</span>"
						+ "</span>").appendTo(self.rootEl);
	}
	this.remove = function() {
		postHandler.post("DeleteTag", {
			single_id: self.single_id
		}, function() {
			
		}, function(data) {
			self.removeFromDocument();
		}, function(data) {
			
		});
	}
	this.onClick = function(f) {
		self.elem.click(f);
	}
	this.removeFromDocument = function() {
		self.elem.remove();
	}
	this.setColor = function(color, colorId) {
		self.color = color;
		self.colorId = colorId;
		self.elem.css("background-color", color);
	}
	this.setName = function(name) {
		self.name = color;
		$("span", self.elem).text(name);
	}
}

var TagWebsite = function(rootEl, single_id, name, imgSrc) {
	var self = this;
	this.rootEl = rootEl;
	this.single_id = single_id;
	this.imgSrc = imgSrc;
	this.name = name;
	this.elem = null;
	this.printOnDocument = function() {
		self.elem = $("<div class='website'>"
						+ "<img src='"
						+ self.imgSrc
						+ "' />"
						+ "<p>"
						+ self.name
						+ "</p>"
						+ "</div>").prependTo(self.rootEl);
	}
	this.removeFromDocument = function() {
		self.elem.remove();
	}
	this.remove = function() {
		
	}
}

var CatalogWebsite = function(rootEl, name, imgSrc, single_id) {
	var self = this;
	this.rootEl = rootEl;
	this.name = name;
	this.elem = null;
	this.imgSrc = imgSrc;
	this.single_id = single_id;
	this.selected = false;
	this.printOnDocument = function() {
		self.elem = $("<div class='website'>"
			+ "<img class='websiteImg' src='"
			+ self.imgSrc
			+ "' />"
			+ "<img class='tick' src='resources/icons/checked.png' />"
			+ "<p>"
			+ self.name
			+ "</p>"
			+ "</div>").appendTo(self.rootEl);
		self.elem.click(function() {
			self.elem.toggleClass("selected");
			self.selected = !self.selected;
		})
	}
	this.select = function() {
		self.elem.addClass("selected");
		self.selected = true;
	}
	this.unselect = function() {
		self.elem.removeClass("selected");
		self.selected = false;
	}
}

$(document).ready(function() {
	tagManager = new TagManager($("#TagsManagerTab"));
	tagManager.loadTags();
	tagManager.loadCatalogWebsites();
});