var easeApp = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.image = this.rootEl.find('.linkImage');
	this.nameArea = this.rootEl.find('.siteName p');
	this.name = this.rootEl.attr('name');
	this.id = this.rootEl.attr('id');
	this.webId = this.rootEl.attr('webid');
	this.login = this.rootEl.attr('login');
	this.increaseCatalagAppCount = function() {
		var x = parseInt($(".catalogApp[idx='" + self.webId + "'] span.apps-integrated i.count").html());
		$(".catalogApp[idx='" + self.webId + "'] span.apps-integrated i.count").html(x+1);
		$(".catalogApp[idx='" + self.webId + "'] span.apps-integrated").addClass("showCounter");
	};
	self.increaseCatalagAppCount();
	this.setName = function(name){
		self.name = name;
		self.nameArea.text(name);
		self.rootEl.attr('name', name);
	};
	this.setId = function(id){
		self.id = id;
		self.rootEl.attr('id', id);
	};
	this.remove = function(){
		easeApps.splice(apps.indexOf(self), 1);
		self.rootEl.remove();
	};
	this.image.on('click', function(){
		sendEvent(this);
	});
	this.rootEl.find('.modifyAppButton').length && this.rootEl.find('.modifyAppButton').on('click', function(e){
		showModifyAppPopup(this, e);
	});
	this.rootEl.find('.deleteAppButton').length && this.rootEl.find('.deleteAppButton').on('click', function(e){
		showConfirmDeleteAppPopup(this, e);
	});
}