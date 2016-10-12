$(document).on("contextmenu", ".linkImage", function(e){
	e.preventDefault();
	$(this).trigger('mouseover');
	$(this).find('.showAppActionsButton').trigger('mouseover');
	return false;
});
function sendEvent(obj) {
	if (!($(obj).hasClass('waitingLinkImage'))) {
		var appIdx = $(obj).closest('.siteLinkBox').index();
		var logoImage = $(obj).find('.linkImage');
		var profileIndex = $(obj).closest('.owl-item').index();
		var json = new Object();
		var event;

		if (!($('#ease_extension').length)){
			checkForExtension();
			return;
		}
		$(obj).addClass('waitingLinkImage');
		$(obj).addClass('scaleinAnimation');
		setTimeout(function() {
			$(obj).removeClass("waitingLinkImage");
			$(obj).removeClass('scaleinAnimation');				
		}, 1000);
		postHandler.post(
			"askInfo",
			{
				profileIndex : profileIndex,
				appIndex : appIdx,
			}, 
			function(){},
			function(retMsg){
				console.log(retMsg);
				json.detail = JSON.parse(retMsg);
				event = new CustomEvent("NewConnection", json);
				document.dispatchEvent(event);
			},
			function(retMsg){
				showAlertPopup(retMsg, true);
			},
			'text'
			);
	}
}
function setupSortableContainer(container){
	$(container).sortable({
		animation: 300,
		group:"sites",
		forceFallback: true,
		filter: ".siteLinkBox[move='false']",
		handle: ".logo, .emptyAppIndicator",
		fallbackTolerance: 1,
		onStart: function(evt){
			var item = $(evt.item);
			item.css({
				'pointer-events': 'none',
				'opacity': '0'
			});
			$('body').css('cursor', 'move');
		},
		onEnd: function(evt){
			var item = $(evt.item);
			$('body').css('cursor', '');
			item.css({
				'pointer-events': '',
				'opacity': ''
			});
			if (!($(evt.to).is($(evt.from))) || evt.oldIndex != evt.newIndex){
				postHandler.post(
					"moveApp",
					{
						appId: item.attr('id'),
						profileId: item.closest('.item').attr('id') ,
						index: item.index()
					},
					function(){},
					function(retMsg){},
					function(retMsg){},
					'text'
					);
			}
		},
		onMove: function(evt){
			if ($(evt.dragged).attr('move') == 'false'){
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
	.find(
		'.ProfileControlPanel #modifyNameForm .buttonSet #validate')
	.click(
		function() {
			var index = $(this).closest('.item').attr('id');
			var name = $(this).closest('#modifyNameForm').find(
				'#profileName').val();
			var button = $(this);
			var closeSectionButton = $(this).closest(
				'.buttonSet').find('#cancel');

			$('#loading').addClass('la-animate');
			postHandler.post(
				'editProfileName',
				{
					name : name,
					index : index
				},
				function(){
					$('#loading').removeClass(
						'la-animate');
					$(button).closest(
						'#modifyNameForm')
					.find('#profileName')
					.val('');
				},
				function(retMsg){
					$(closeSectionButton)
					.click();
					$(button)
					.closest(
						'.ProfileBox')
					.find(
						'.ProfileName p')
					.text('@' + name);
					$(button)
					.closest(
						'.profileSettingsTab')
					.find(
						'.sectionHeader .directInfo p')
					.text(name);
					showAlertPopup(
						'Modifications successfully applied !',
						false);
				},
				function(retMsg){
					showAlertPopup(retMsg,true);
				},
				'text');
		});

	$(profile).find(".colorChooser .color").click(function() {
		var parent = $(this).closest('.colorChooser');
		$(parent).find('.selected').removeClass('selected');
		$(parent).find('#color').val($(this).attr('color'));
		$(this).addClass('selected');
	});

	$(profile)
	.find(
		'.ProfileControlPanel #modifyColorForm .buttonSet #validate')
	.click(
		function() {
			var index = $(this).closest('.item').attr('id');
			var color = $(this).closest('#modifyColorForm')
			.find('#color').val();
			var button = $(this);
			var closeSectionButton = $(this).closest(
				'.buttonSet').find('#cancel');

			$('#loading').addClass('la-animate');
			postHandler.post(
				'editProfileColor',
				{
					color : color,
					index : index
				},
				function(){
					$('#loading').removeClass(
						'la-animate');
				},
				function(retMsg){
					$(closeSectionButton)
					.click();
					$(button)
					.closest(
						'.ProfileBox')
					.find(
						'.ProfileName')
					.css(
						'background-color',
						color);
					$(button).closest(
						'.ProfileBox')
					.attr('color',
						color);
					var string = '5px solid '
					+ color;
					$(button).closest(
						'.ProfileBox').css(
						'border-bottom',
						string);
						$(button)
						.closest(
							'.profileSettingsTab')
						.find(
							'#ColorSection .directInfo')
						.css(
							'background-color',
							color);
						showAlertPopup(
							'Modifications successfully applied !',
							false);
					},
					function(retMsg){
						showAlertPopup(retMsg, true);
					},
					'text');
		});
	$(profile).find('#deleteProfileForm .buttonSet #validate').click(
		function() {
			var idx;
			var name;
			var popup;

			parent = $(this).closest(".ProfileBox");
			idx = $(this).closest(".item").attr('id');
			popup = $('#PopupDeleteProfile');

			popup.find("#index").val(idx);
			popup.find('#password').val('');
			popup.find('span').text(
				$(parent).find('.ProfileName p').text());
			$('#PopupDeleteProfile').addClass("md-show");
			setTimeout(function() {
				$(popup).find('#password').focus();
			}, 100);
		});
}

	function closeAllSettingsTabs() {
		$('.ProfileSettingsButton.settings-show').click();
	}

		$(document).ready(function() {
		$('.SitesContainer').each(function() {
			makeViewDroppable($(this));
		});
	});

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
		makeViewDroppable($(container));
		setupProfileSettings($(profile));
		makeSettingsAccordion($($(profile).find(".ProfileSettingsButton")));
		$(profile).find('.profileSettingsTab').accordion({
			active : 10,
			collapsible : true,
			autoHeight : false,
			heightStyle : "content"
		});
		owl.destroy();
		$('.owl-carousel').append($(profile));
		$('.owl-carousel').append($(parent));
		var nbProfiles = $('.owl-carousel > *').length;
		if (nbProfiles > 3) {
			var addProfileHelper = $(elem).closest('.item');
			$('#addProfileHelper').append($(addProfileHelper));
		}
		setupOwlCarousel();
	}
	$(document).ready(function() {
		$('.AddProfileView .scalerContainer').click(function() {
			addProfileView($(this));
		});
	});
	$(document).ready(
		function() {
			$('.AddProfileView .scalerContainer').droppable(
			{
				accept : ".catalogApp",

				drop : function(event, ui) {
					event.preventDefault();
					event.stopPropagation();
					$(this).css('border', 'none');
					var nbProfiles = $('.owl-wrapper > *').length;
					var profile = $($('#profileHelper').html());
					var container = $(profile).find(
						'.SitesContainer');
					var parent = $(this).closest('.item');
					var owl = $(".owl-carousel")
					.data('owlCarousel');

					postHandler.post(
							'addProfile',
							{
								name : 'Profile name',
								color : '#35a7ff'
							}, 
							function(){},
							function(retMsg){$(profile).attr('id', retMsg);},
							function(retMsg){},
							'text'
					);
					makeViewDroppable($(container));
					setupProfileSettings($(profile));
					makeSettingsAccordion($($(profile).find(
						".ProfileSettingsButton")));
					$(profile).find('.profileSettingsTab')
					.accordion({
						active : 10,
						collapsible : true,
						autoHeight : false,
						heightStyle : "content"
					});
					owl.destroy();
					$('.owl-carousel').append($(profile));
					$('.owl-carousel').append($(parent));
					var nbProfiles = $('.owl-carousel > *').length;
					if (nbProfiles > 3) {
						var addProfileHelper = $(this).closest(
							'.item');
						$('#addProfileHelper').append(
							$(addProfileHelper));
					}
					setupOwlCarousel();
					showAddAppPopup($(container), $(ui.helper));

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

	function makeViewDroppable(v) {
		var parent = $(v).closest('.ProfileBox');

		$(v).droppable({
			accept : ".catalogApp",

			drop : function(event, ui) {
				event.preventDefault();
				event.stopPropagation();
				$(this).css('border', '');

				showAddAppPopup($(this), $(ui.helper));

			},
			over : function(event, ui) {
				event.preventDefault();
				event.stopPropagation();
				$(this).css('border', '1px solid ' + $(parent).attr('color'));
			},

			out : function(event, ui) {
				event.preventDefault();
				event.stopPropagation();
				$(this).css('border', '');
			}
		});
	}
