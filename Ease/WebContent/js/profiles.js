
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
		$(this).scrollTop() == 0 && $("body").hasClass("picBckgrnd") && easeHeader.rootEl.removeClass('scrolling') || easeHeader.rootEl.addClass('scrolling');
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
	enterEditModeTutorial();
}

function leaveEditMode() {
	easeDashboard.leaveEditMode();
	catalog.close();
	$('.scaleOutAnimation').removeClass('scaleOutAnimation');
	$('.MenuButtonSet').removeClass('editMode');
	leaveEditModeTutorial();
}

var profiles = [];

$(document).ready(function(){
	$('.ProfileBox').each(function(){
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

/*	this.ControlPanel.find(".profileSettingsTab").accordion({
		active : 10,
		collapsible : true,
		autoHeight : false,
		heightStyle : "content"
	});*/
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
			easeDashboard.profileAdder.css('display', '');
	}
	this.showSettings = function(){
		self.SettingsButton.addClass('fa-rotate-90');
		self.SettingsButton.addClass('settings-show');
		self.ControlPanel.css('max-height', '500px');
		self.isSettingsOpen = true;
	};
	this.hideSettings = function (){
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
	//catalog droppable
	self.appContainer.droppable({
		accept: ".catalogApp",
		drop: function(event, ui){
			event.preventDefault();
			event.stopPropagation();
			$(this).css('border', '');
			self.parentItem.css('z-index', '');
			self.parentItem.css('transform', 'scale(1)');
			setTimeout(function(){
				self.parentItem.css('transform', '');
			}, 300);
			showAddAppPopup($(this), $(ui.helper));
			easeHiddenProfile.rootEl.off('mouseenter');
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
		deleteProfilePopup.open(self);
	});
	//edit name
	this.qRoot.find('#modifyNameForm #validate').click(function(){
		var name = $(this).parent().find('input').val();
		easeLoadingIndicator.show();
		postHandler.post('editProfileName', {
			name : name,
			index : self.id
		}, function() {
			easeLoadingIndicator.hide();
		}, function(retMsg) {
			self.profileHeader.find('p').text('@' + name);
			$(this).parent().find('input').val('');
		}, function(retMsg) {
		}, 'text');
	});
	//edit color section
	this.qRoot.find('#modifyColorForm .color').click(function(){
		var color = $(this).attr('color');
		easeLoadingIndicator.show();
		self.qRoot.find('#modifyColorForm .color.choosen').removeClass('choosen');
		$(this).addClass('choosen');
		postHandler.post('editProfileColor', {
			color : color,
			index : self.id
		}, function() {
			easeLoadingIndicator.hide();
		}, function(retMsg) {
			self.profileHeader.css('background-color', color);
			self.profileHeader.attr('color', color);
			}, function(retMsg) {
			}, 'text');
	});
//	setupProfileSettings(self.qRoot);
};

$(document).on("contextmenu", ".linkImage", function(e) {
	e.preventDefault();
	$(this).trigger('mouseover');
	$(this).find('.showAppActionsButton').trigger('mouseover');
	return false;
});
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
			if (!($(evt.to).is($(evt.from))) || evt.oldIndex != evt.newIndex) {
				postHandler.post("moveApp", {
					appId : item.attr('id'),
					profileId : item.parent().attr('id'),
					index : item.index()
				}, function() {
				}, function(retMsg) {
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
/*
function setupProfileSettings(profile) {
	$(profile).find('.ProfileControlPanel #cancel').click(function() {
		var Accordion = $(this).closest('.ui-accordion');

		$(Accordion).find('input').val('');
		$(Accordion).find('.color.selected').removeClass('selected');
		$(Accordion).accordion("option", "active", 10);

	});
	$(profile)
	.find('.ProfileControlPanel #modifyNameForm .buttonSet #validate')
	.click(
		function() {
			var index = $(this).closest('.item').attr('id');
			var name = $(this).closest('#modifyNameForm').find(
				'#profileName').val();
			var button = $(this);
			var closeSectionButton = $(this).closest('.buttonSet')
			.find('#cancel');

			$('#loading').addClass('la-animate');
			postHandler.post('editProfileName', {
				name : name,
				index : index
			}, function() {
				$('#loading').removeClass('la-animate');
				$(button).closest('#modifyNameForm').find(
					'#profileName').val('');
			}, function(retMsg) {
				$(closeSectionButton).click();
				$(button).closest('.ProfileBox').find(
					'.ProfileName p').text('@' + name);
				$(button).closest('.profileSettingsTab').find(
					'.sectionHeader .directInfo p').text(name);
				showAlertPopup(
					'Modifications successfully applied !',
					false);
			}, function(retMsg) {
				showAlertPopup(retMsg, true);
			}, 'text');
		});

	$(profile).find(".colorChooser .color").click(function() {
		var parent = $(this).closest('.colorChooser');
		$(parent).find('.selected').removeClass('selected');
		$(parent).find('#color').val($(this).attr('color'));
		$(this).addClass('selected');
	});

	$(profile).find(
		'.ProfileControlPanel #modifyColorForm .buttonSet #validate')
	.click(
		function() {
			var index = $(this).closest('.item').attr('id');
			var color = $(this).closest('#modifyColorForm').find(
				'#color').val();
			var button = $(this);
			var closeSectionButton = $(this).closest('.buttonSet')
			.find('#cancel');

			$('#loading').addClass('la-animate');
			postHandler.post('editProfileColor', {
				color : color,
				index : index
			}, function() {
				$('#loading').removeClass('la-animate');
			}, function(retMsg) {
				$(closeSectionButton).click();
				$(button).closest('.ProfileBox').find(
					'.ProfileName').css('background-color',
					color);
					$(button).closest('.ProfileBox').attr('color',
						color);
					//var string = '5px solid ' + color;
					//$(button).closest('.ProfileBox').css(
					//	'border-bottom', string);
					$(button).closest('.profileSettingsTab').find(
						'#ColorSection .directInfo').css(
						'background-color', color);
						showAlertPopup(
							'Modifications successfully applied !',
							false);
					}, function(retMsg) {
						showAlertPopup(retMsg, true);
					}, 'text');
		});
}
*/


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
	$('.helpIntegrateApps #integrateAppForm #integrate').click(function() {
		var form = $(this).closest('#integrateAppForm');
		postHandler.post(
			'askForNewApp',
			{
				ask : $(form).find('#integrateApp').val()
			},
			function() {
				$(form).find('.inputs input').val('');
				$(form).find('.inputs').hide();
				$(form).find('.confirmation').show().delay(1000).fadeOut(function() {
					$(form).find('.inputs').show();
				});
			}, function(retMsg) {
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
				if ($(".CatalogViewTab").hasClass("show") && !($(event.target).closest('.MenuButtonSet').length) && $('.md-show').length == 0) {
					if (!($(event.target).closest('.profileAdder, .header, .dashboardColumn, .md-modal, .md-overlay, .CatalogViewTab, .AddProfileView, .updateButton, .hiddenProfile').length))
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

$(document).ready(function() {
	$('.catalogApp').draggable({
		cursor : 'move',
		cursorAt : {
			left : 25,
			top : 25
		},
		appendTo: "body",
		helper : function(e, ui) {
			easeHiddenProfile.rootEl.on('mouseenter', function(){
				easeHiddenProfile.show();
			});
			var ret;
			ret = $('<div class="dragHelperLogo" style="position: fixed; z-index: 3;pointer-events:none;"/>');
			ret.attr("connect", $(this).attr("connect"));
			ret.attr("data-login", $(this).attr("data-login"));
			ret.attr("data-sso", $(this).attr("data-sso"));
			ret.attr("data-nologin", $(this).attr("data-nologin"));
			ret.append($('<img />'));
			ret.attr("idx", $(this).attr("idx"));
			ret.attr("name", $(this).attr("name"));
			ret.find('img').attr("src", $(this).find('img').attr("src"));
			return ret;
		}
	});
});

