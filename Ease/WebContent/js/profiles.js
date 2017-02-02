
var ease;
var easeRoot = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.apps = [];
	self.rootEl.find(".SitesContainer .siteLinkBox").each(function (index, elem) {
		self.apps.push(new easeApp($(elem)));
	});
	this.mainContent = this.rootEl.find('#loggedBody');

	if (!(this.mainContent))
		return;
	this.onResize = function(){
		self.mainContent.css('height', $(window).height() - 43 + 'px');
	}
	this.onResize();
	$(window).resize(function(){
		self.onResize();
	});
	this.mainContent.find('.col-left').scroll(function(){
		$(this).scrollTop() == 0 && easeHeader.rootEl.removeClass('scrolling') || easeHeader.rootEl.addClass('scrolling');
	});
};

var easeHiddenProfile;
$(document).ready(function(){
	easeHiddenProfile = new hiddenProfile($('.hiddenProfile'));
	ease = new easeRoot($('#onComputer'));
});

var hiddenProfile = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.appContainer = this.rootEl.find('.hiddenProfileContainer');
	this.openHelper = this.rootEl.find('.hiddenProfileHelper');

	this.onResize = function(){
//		this.appContainer.find('.siteLinkBox').height(this.appContainer.find('.siteLinkBox').width());
}

this.onResize();
$(window).resize(function(){
	self.onResize();
});
this.show = function(){
	self.rootEl.addClass('show');
}
this.hide = function(){
	self.rootEl.removeClass('show');
}
this.openHelper.on('click', function(){
	self.show();
});
this.rootEl.on('mouseleave', function(){
	self.hide();
});
this.appContainer.droppable({
	accept: ".catalogApp",
	drop: function(event, ui){
		event.preventDefault();
		event.stopPropagation();
		$(this).css('border', '');
		showAddAppPopup($(this), $(ui.helper));
		console.log($(ui.helper));
		popupAddApp.oForm.profile_id = 0;
	},
	over: function(event, ui){
		event.preventDefault();
		event.stopPropagation();
		$(this).css('border', '1px solid red');
	},
	out: function(event, ui){
		event.preventDefault();
		event.stopPropagation();
		$(this).css('border', '');
	}
});
setupSortableContainer(this.appContainer);
};

function enterEditMode() {
	easeDashboard.enterEditMode();
	catalog.open();
	$('.MenuButtonSet').addClass('editMode');
}

function leaveEditMode() {
	easeDashboard.leaveEditMode();
	catalog.close();
	$('.scaleOutAnimation').removeClass('scaleOutAnimation');
	$('.MenuButtonSet').removeClass('editMode');
}

var profiles = [];

$(document).ready(function(){
	$('.ProfileBox:not(.helper)').each(function(){
		var profile = new Profile($(this));
		profiles.push(profile);
	});
	
	$("#enterEditMode").click(enterEditMode);
	
});

var Profile = function(rootEl){
	var self = this;
	this.qRoot = rootEl;
	this.parentItem = this.qRoot.closest('.item');
	this.profileHeader = this.qRoot.find('.ProfileName');
	this.SettingsButton = this.qRoot.find('.ProfileSettingsButton');
	this.ControlPanel = this.qRoot.find('.ProfileControlPanel');
	this.appContainer = this.qRoot.find('.SitesContainer');
	this.isSettingsOpen = false;
	this.id = this.parentItem.attr('id');

	this.profileHeader.on('contextmenu', function(e){
		e.preventDefault();
		e.stopPropagation();
		self.showSettings();
	});
	this.setId = function(tId){
		self.id = tId;
		self.parentItem.attr('id', tId);
		self.appContainer.attr('id', tId);
	};
	this.remove = function(){
		profiles.splice(profiles.indexOf(self), 1);
		self.parentItem.animate({
			height: '0',
			'margin-bottom': '0'
		}, 300);
		setTimeout(function(){
			self.parentItem.remove();
		}, 300);
		if (profiles.length <= 15)
			easeDashboard.adder.rootEl.css('display', '');
		if (self.parentItem.parent().find('.item').length == 1)
			self.parentItem.parent().width('0px');
	}
	this.showSettings = function(){
		if (self.isSettingsOpen)
			return;
		self.SettingsButton.addClass('fa-rotate-90');
		self.SettingsButton.addClass('settings-show');
		self.ControlPanel.css('max-height', '500px');
		self.isSettingsOpen = true;
		profiles.forEach(function(elem, idx){
			if (elem.isSettingsOpen && elem != self)
				elem.hideSettings();
		});
	};
	this.hideSettings = function (){
		if (!(self.isSettingsOpen))
			return;
		self.SettingsButton.removeClass('fa-rotate-90');
		self.SettingsButton.removeClass('settings-show');
		self.ControlPanel.css('max-height', '');
		self.isSettingsOpen = false;
	}
	this.SettingsButton.click(function(e){
		e.stopPropagation();
		$('.ProfileSettingsButton.settings-show').each(function(){
			if (!($(this).is(self.SettingsButton))){
				$(this).click();
			}
		});
		(self.isSettingsOpen) ? self.hideSettings() : self.showSettings();
	});

	this.addApp = function(app){
		self.appContainer.append(app.qRoot);
		easeAppsManager.addApp(app);
	}
	
	//catalog droppable
	self.appContainer.droppable({
		accept: ".catalogApp,.updateBox",
		drop: function(event, ui){
			event.preventDefault();
			event.stopPropagation();
			$(this).css('border', '');
			self.parentItem.css('z-index', '');
			self.parentItem.css('transform', 'scale(1)');
			setTimeout(function(){
				self.parentItem.css('transform', '');
			}, 300);
			easeAddAppPopup.open(currentDraggedApp, self);
//			showAddAppPopup($(this), ui.helper);	
		},
		over: function(event, ui){
			event.preventDefault();
			event.stopPropagation();
			self.parentItem.css('z-index', '1');
			self.parentItem.css('transform','scale(1)');
			self.parentItem.css('transform', 'scale(1.1)');
			$(this).css('border', '1px solid ' + self.qRoot.attr('color'));
		},
		out: function(event, ui){
			event.preventDefault();
			event.stopPropagation();
			self.parentItem.css('z-index', '');
			self.parentItem.css('transform', 'scale(1)');
			setTimeout(function(){
				self.parentItem.css('transform', '');
			}, 300);
			$(this).css('border', '');
		}
	});
	//apps move
	setupSortableContainer(this.appContainer);
	//settings
	//delete profile
	this.qRoot.find('#deleteProfileForm #validate').click(function() {
		if (self.appContainer.find('.siteLinkBox').length > 0)
			deleteProfilePopup.open(self);
		else {
			postHandler.post('RemoveProfile', {
				profileId : self.id
			}, function() {
				easeLoadingIndicator.hide();
			}, function(retMsg) {
				easeTracker.trackEvent('DeleteProfile');
				self.remove();
			}, function(retMsg) {
			}, 'text');			
		}
	});
	//edit name
	this.qRoot.find('#modifyNameForm #validate').click(function(){
		var name = $(this).parent().find('input').val();
		if (name.length){
			easeLoadingIndicator.show();
			postHandler.post('EditProfileName', {
				name : name,
				profileId : self.id
			}, function() {
				easeLoadingIndicator.hide();
			}, function(retMsg) {
				easeTracker.trackEvent('EditProfileName', {"newProfileName" : name});
				self.setName(name);
				self.qRoot.find('#modifyNameForm input').val('');
			}, function(retMsg) {
			}, 'text');
		}
	});
	this.qRoot.find('#modifyNameForm input').keyup(function(e){
		if (e.which == 13)
			self.qRoot.find('#modifyNameForm #validate').click();
	});
	this.setName = function(tName){
		self.profileHeader.find('p').text('@' + tName);
	};
	this.setColor = function(tColor){
		self.profileHeader.css('background-color', tColor);
		self.qRoot.attr('color', tColor);		
	};
	//edit color section
	this.qRoot.find('#modifyColorForm .color').click(function(){
		var color = $(this).attr('color');
		easeLoadingIndicator.show();
		self.qRoot.find('#modifyColorForm .color.choosen').removeClass('choosen');
		$(this).addClass('choosen');
		postHandler.post('EditProfileColor', {
			color : color,
			profileId : self.id
		}, function() {
			easeLoadingIndicator.hide();
		}, function(retMsg) {
			easeTracker.trackEvent('EditProfileColor', {"newProfileColor": color});
			self.setColor(color);
		}, function(retMsg) {
		}, 'text');
	});
//	setupProfileSettings(self.qRoot);
};
$(document).click(function (e){
	var profile = $(e.target).closest('.ProfileControlPanel');
	var settingsButton = null;

	if (profile.length){
		settingsButton = profile.closest('.ProfileBox').find('.ProfileSettingsButton.settings-show');
	}

	$('.ProfileSettingsButton.settings-show').each(function(){
		if (!($(this).is($(settingsButton)))){
			$(this).click();
		}
	});
}); 

// var for started dragging app's parent Id
//Cause bug in lib where (evt.to == evt.from) in onEnd callback;
var appDragCurrentIdHelper = 0;

//drag and drop initializing function on a container 
function setupSortableContainer(container) {
	$(container).sortable({
		animation : 300,
		group : "sites",
		forceFallback : true,
		filter : ".siteLinkBox[move='false']",
		handle : ".logo, .emptyAppIndicator",
		fallbackTolerance : 1,
		fallbackOnBody: true,
		onStart : function(evt) {
			var item = $(evt.item);
			appDragCurrentIdHelper = item.parent().attr('id');
			item.css({
				'pointer-events' : 'none',
				'opacity' : '0'
			});
			$('body').css('cursor', 'move');
			easeHiddenProfile.rootEl.on('mouseenter', function(){
				easeHiddenProfile.show();
			});
		},
		onEnd : function(evt) {
			easeHiddenProfile.rootEl.off('mouseenter');
			var item = $(evt.item);
			$('body').css('cursor', '');
			item.css({
				'pointer-events' : '',
				'opacity' : ''
			});
			if (appDragCurrentIdHelper != item.parent().attr('id') || evt.oldIndex != evt.newIndex) {
				postHandler.post("MoveApp", {
					appId : item.attr('id'),
					profileIdDest : item.parent().attr('id'),
					positionDest : item.index()
				}, function() {
				}, function(retMsg) {
					easeTracker.trackEvent("MoveApp");
				}, function(retMsg) {
				}, 'text');
			}
		},
		onMove : function(evt) {
			if ($(evt.dragged).attr('move') == 'false') {
				return false;
			}
		}
	});
}

/* Next lines come from ProfileEditView.jsp */

$(document).ready(function() {
	$('#PopupAddApp #password').keyup(function(event) {
		if (event.keyCode == 13) {
			$("#PopupAddApp .md-content .buttonSet #accept").click();
		}
	});
	$('#PopupAddApp #login').keyup(function(event) {
		if (event.keyCode == 13) {
			$('#PopupAddApp #password').focus();
		}
	});
	$('.helpIntegrateApps #integrateAppForm #integrateApp').keyup(function(e) {
		if (e.keyCode == 13)
			$('.helpIntegrateApps #integrateAppForm #integrate').trigger("click");
	});
	$('.helpIntegrateApps #integrateAppForm #integrate').click(function(e) {
		e.preventDefault();
		var form = $(this).closest('#integrateAppForm');
		var url = $(form).find('#integrateApp').val();
		postHandler.post(
			'WebsiteRequest',
			{
				ask : url
			},
			function() {
				$(form).find('.inputs input').val('');
				$(form).find('.inputs').hide();
				$(form).find('.confirmation').show().delay(1000).fadeOut(function() {
					$(form).find('.inputs').show();
				});
			}, function(retMsg) {
				easeTracker.trackEvent("RequestWebsite", {"AskedWebsiteName": url});
			}, function(retMsg) {
			}, 'text');
	});
});



$(document)
.ready(
	function() {
		$(document)
		.click(
			function(event) {
				if ($(".CatalogViewTab").hasClass("show") && !($(event.target).closest('.MenuButtonSet').length) && $('.md-show, .popupHandler.myshow').length == 0) {
					if (!($(event.target).closest('.profileAdder, .header, .dashboardColumn, .md-modal, .md-overlay, .CatalogViewTab, .AddProfileView, .updateButton, .hiddenProfile, #tipsHandler, .updateBox, .popupHandler').length))
						leaveEditMode();
				}
			});
	});

function showConfirmDeleteAppPopup(elem, event) {
	event.preventDefault();
	event.stopPropagation();

	var app = $(elem).closest('.siteLinkBox');
	var image = $(app).find('.linkImage');

	var hasRelatedApps = false;
	var otherApps = $(".siteLinkBox");
	for (var j = 0; j < otherApps.length; j++) {
		if (otherApps[j].getAttribute('logWith') == app.attr('id')) {
			hasRelatedApps = true;
			break;
		}
	}
	if (hasRelatedApps) {
		showAlertPopup(
			"This app is used to connect to several websites. You cannot delete it.",
			true);
	} else {
		popupDeleteApp.open(app);
	}
}


(function(old) {
	$.fn.attr = function() {
		if(arguments.length === 0) {
			if(this.length === 0)
				return null;
			var obj = {};
			$.each(this[0].attributes, function() {
				if(this.specified)
					obj[this.name] = this.value;
			});
			return obj;
		}
		return old.apply(this, arguments);
	};
})($.fn.attr);