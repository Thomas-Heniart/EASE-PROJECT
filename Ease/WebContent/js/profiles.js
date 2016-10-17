var profiles = [];

$(document).ready(function(){
	$('.ProfileBox').each(function(){
		var profile = new Profile($(this));
		profiles.push(profile);
	});
	
	$("#enterEditMode").click(enterEditMode);
});


function enterEditMode() {
	//mixpanel.track("Open Catalog");
	$(".ProfilesHandler").addClass('editMode');
	catalog.open();
	var addProfileHelper = $('#addProfileHelper').find('.item');
	var owl = $(".owl-carousel").data('owlCarousel');
	owl.destroy();
	var nbProfiles = $('.owl-carousel > *').length;

	if (nbProfiles < 3) {
		$('.owl-carousel').append($(addProfileHelper));
	}
	setupOwlCarousel();
	$('.MenuButtonSet').addClass('editMode');
	$('.MenuButtonSet.editMode .openCatalogHelper').css('height',
		$('.CatalogViewTab.show').height() + 'px');
	enterEditModeTutorial();
}

function leaveEditMode() {
	$('.MenuButtonSet.editMode .openCatalogHelper').css('height', '50px');
	$(".ProfilesHandler").removeClass('editMode');
	catalog.close();
	var owl = $(".owl-carousel").data('owlCarousel');
	owl.destroy();
	var addProfileHelper = $('.AddProfileView').closest('.item');

	$('#addProfileHelper').append($(addProfileHelper));
	setupOwlCarousel();
	$('.scaleOutAnimation').removeClass('scaleOutAnimation');
	$('.MenuButtonSet').removeClass('editMode');
	leaveEditModeTutorial();
}

var Profile = function(rootEl){
	var self = this;
	this.qRoot = rootEl;
	this.SettingsButton = this.qRoot.find('.ProfileSettingsButton');
	this.ControlPanel = this.qRoot.find('.ProfileControlPanel');
	this.appContainer = this.qRoot.find('.SitesContainer');
	this.isSettingsOpen = false;

	this.ControlPanel.find(".profileSettingsTab").accordion({
        active : 10,
        collapsible : true,
        autoHeight : false,
        heightStyle : "content"
	});
	this.showSettings = function(){
		self.SettingsButton.addClass('fa-rotate-90');
		self.SettingsButton.addClass('settings-show');
		self.ControlPanel.css('display', 'inline-block');
		self.isSettingsOpen = true;
	};
	this.hideSettings = function (){
		self.SettingsButton.removeClass('fa-rotate-90');
		self.SettingsButton.removeClass('settings-show');
		self.ControlPanel.css('display', 'none');
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
	self.appContainer.droppable({
		accept: ".catalogApp",
		drop: function(event, ui){
			event.preventDefault();
			event.stopPropagation();
			$(this).css('border', '');
			showAddAppPopup($(this), $(ui.helper));
		},
		over: function(event, ui){
			event.preventDefault();
			event.stopPropagation();
			$(this).css('border', '1px solid ' + self.qRoot.attr('color'));
		},
		out: function(event, ui){
			event.preventDefault();
			event.stopPropagation();
			$(this).css('border', '');
		}
	});
	setupProfileSettings(self.qRoot);
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
		onStart : function(evt) {
			var item = $(evt.item);
			item.css({
				'pointer-events' : 'none',
				'opacity' : '0'
			});
			$('body').css('cursor', 'move');
		},
		onEnd : function(evt) {
			var item = $(evt.item);
			$('body').css('cursor', '');
			item.css({
				'pointer-events' : '',
				'opacity' : ''
			});
			if (!($(evt.to).is($(evt.from))) || evt.oldIndex != evt.newIndex) {
				postHandler.post("moveApp", {
					appId : item.attr('id'),
					profileId : item.closest('.item').attr('id'),
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
function setupProfileSettings(profile) {
	setupSortableContainer($(profile).find('.SitesContainer'));
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
					var string = '5px solid ' + color;
					$(button).closest('.ProfileBox').css(
						'border-bottom', string);
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
	$(profile).find('#deleteProfileForm .buttonSet #validate').click(
		function() {
			/*var idx;
			var name;
			var popup;

			parent = $(this).closest(".ProfileBox");
			idx = $(this).closest(".item").attr('id');
			popup = $('#PopupDeleteProfile');

			popup.find("#index").val(idx);
			popup.find('#password').val('');
			popup.find('span').text($(parent).find('.ProfileName p').text());
			$('#PopupDeleteProfile').addClass("md-show");
			setTimeout(function() {
				$(popup).find('#password').focus();
			}, 100);
			*/
			
			deleteProfilePopup.open($(this).closest(".item").attr('id'));
			
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
				if ($(".CatalogViewTab").hasClass(
					"show")
					&& !($(event.target).closest(
						'.MenuButtonSet').length)
					&& $('.md-show').length == 0) {
					if (!($(event.target)
						.closest(
							'.header, .owl-wrapper-outer, .md-modal, .md-overlay, .CatalogViewTab, .AddProfileView').length))
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

		/*var popup = $('#PopupDeleteApp');
		popup.addClass('md-show');
		popup.find("#close").unbind('click');
		popup.find("#close").click(function() {
			popup.removeClass('md-show');
		});
		popup.find("#accept").unbind('click');
		popup.find("#accept").click(function() {
			popup.removeClass('md-show');
			image.addClass('easyScaling');
			postHandler.post('deleteApp', {
				appId : $(app).attr('id')
			}, function() {
				image.removeClass('easyScaling');
			}, function(retMsg) {
				image.addClass('deletingApp');
				setTimeout(function() {
					app.remove();
				}, 500);
			}, function(retMsg) {
				showAlertPopup(retMsg, true);
			}, 'text');
		});
		*/
		popupDeleteApp.open(app);
	}
}
	function addProfileView(elem) {
		var profile = $($('#profileHelper').html());
		var container = $(profile).find('.SitesContainer');

		var parent = $(elem).closest('.item');
		var owl = $(".owl-carousel").data('owlCarousel');

		postHandler.post(
			'addProfile', 
			{
				name : 'Profile name',
				color : '#35a7ff'
			}, 
			function(){},
			function(retMsg){
				$(profile).attr('id', retMsg);
			},
			function(retMsg){},
			'text'
			);
		profiles.push(new Profile($(profile).find('.ProfileBox')));
		owl.destroy();
		$('.owl-carousel').append($(profile));
		$('.owl-carousel').append($(parent));
		var nbProfiles = $('.owl-carousel > *').length;
		if (nbProfiles > 3) {
			var addProfileHelper = $(elem).closest('.item');
			$('#addProfileHelper').append($(addProfileHelper));
		}
		setupOwlCarousel();
		return profile;
	}

	$(document).ready(
		function() {
			$('.AddProfileView .scalerContainer').click(function() {
				addProfileView($(this));
			});
			$('.AddProfileView .scalerContainer').droppable(
			{
				accept : ".catalogApp",

				drop : function(event, ui) {
					event.preventDefault();
					event.stopPropagation();
					$(this).css('border', 'none');
					var profile = addProfileView($(this));
					showAddAppPopup($(profile).find('.SitesContainer'), $(ui.helper));
				},
				over : function(event, ui) {
					event.preventDefault();
					event.stopPropagation();
					$(this).css('border', '1px solid #35a7ff');
				},

				out : function(event, ui) {
					event.preventDefault();
					event.stopPropagation();
					$(this).css('border', 'none');
				}
			});
		});


$(document).ready(function() {
	$('.catalogApp').draggable({
		cursor : 'none',
		cursorAt : {
			left : 25,
			top : 25
		},
		helper : function(e, ui) {
			var ret;
			ret = $('<div class="dragHelperLogo" style="position: fixed;"/>');
			addAppForm.siteId($(this).attr("idx"));
			addAppForm.oInputs[0].val($(this).find('p').text());
			ret.attr("connect", $(this).attr("connect"));
			ret.attr("data-login", $(this).attr("data-login"));
			ret.attr("data-sso", $(this).attr("data-sso"));
			ret.attr("data-nologin", $(this).attr("data-nologin"));
			ret.append($('<img />'));
			ret.find('img').attr("src", $(this).find('img').attr("src"));
			return ret; // Replaced $(ui) with $(this)
		}
	});
});

