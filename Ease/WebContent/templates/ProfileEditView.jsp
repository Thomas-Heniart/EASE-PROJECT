<script>
	$(document)
			.ready(
					function() {
						$('#PopupAddApp #password')
								.keyup(
										function(event) {
											if (event.keyCode == 13) {
												$(
														"#PopupAddApp .md-content .buttonSet #accept")
														.click();
											}
										});
						$('#PopupAddApp #login').keyup(function(event) {
							if (event.keyCode == 13) {
								$('#PopupAddApp #password').focus();
							}
						});
						$('.helpIntegrateApps #integrateAppForm #integrateApp').keyup(function(e){
						    if(e.keyCode == 13)
						    	$('.helpIntegrateApps #integrateAppForm #integrate').trigger("click");
						});
						$('.helpIntegrateApps #integrateAppForm #integrate').click(
										function() {
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
												},
												function(retMsg){},
												function(retMsg){},
												'text');
										});
					});

	function reinitCarousel() {
		var owl = $(".owl-carousel").data('owlCarousel');

		$(".ProfilesHandler").addClass('editMode');
		owl.destroy();
		setupOwlCarousel();
	}

	function enterEditMode() {

		$('#dragAndDropHelper').css('display', 'block');
		$(".ProfilesHandler").addClass('editMode');
		$('.CatalogViewTab').addClass('show');
		var addProfileHelper = $('#addProfileHelper').find('.item');
		var owl = $(".owl-carousel").data('owlCarousel');
		owl.destroy();
		var nbProfiles = $('.owl-carousel > *').length;

		if (nbProfiles < 3) {
			$('.owl-carousel').append($(addProfileHelper));
		}
		setupOwlCarousel();
		$('.MenuButtonSet').addClass('editMode');
		$('.MenuButtonSet.editMode .openCatalogHelper').css('height', $('.CatalogViewTab.show').height() + 'px');
		enterEditModeTutorial();
	}

	function leaveEditMode() {
		$('.MenuButtonSet.editMode .openCatalogHelper').css('height', '50px');
		$('#dragAndDropHelper').css('display', 'none');
		$(".ProfilesHandler").removeClass('editMode');
		$('.CatalogViewTab').removeClass('show');

		var owl = $(".owl-carousel").data('owlCarousel');
		owl.destroy();
		var addProfileHelper = $('.AddProfileView').closest('.item');

		$('#addProfileHelper').append($(addProfileHelper));
		setupOwlCarousel();
		$('.scaleOutAnimation').removeClass('scaleOutAnimation');
		$('.MenuButtonSet').removeClass('editMode');
		leaveEditModeTutorial();
	}

	$(document).ready(function() {
		$('.CatalogViewTab #quit').click(function() {
			leaveEditMode();
		});
	});
	
	$(document).ready(
		function() {
			$(document).click(
				function(event) {
					if ($(".CatalogViewTab").hasClass("show") && !($(event.target).closest('.MenuButtonSet').length) && $('.md-show').length == 0) {
						if (!($(event.target).closest('.header, .owl-wrapper-outer, .md-modal, .md-overlay, .CatalogViewTab').length))
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
		for(var j=0;j<otherApps.length;j++){
			if(otherApps[j].getAttribute('logWith')==app.attr('id')){
				hasRelatedApps = true;
				break;
			}
		}
		
		if(hasRelatedApps){
			showAlertPopup("This app is used to connect to several websites. You cannot delete it.", true);
		} else {
		
		var popup = $('#PopupDeleteApp');
		popup.addClass('md-show');
		popup.find("#close").unbind('click');
		popup.find("#close").click(function() {
			popup.removeClass('md-show');
		});
		popup.find("#accept").unbind('click');
		popup.find("#accept").click(function() {
			popup.removeClass('md-show');
			image.addClass('easyScaling');
			postHandler.post(
					'deleteApp',
					{
						appId: $(app).attr('id')
					}, 
					function(){image.removeClass('easyScaling');},
					function(retMsg){
						image.addClass('deletingApp');
						setTimeout(function() {
							console.log(app);
							app.remove();
						}, 500);
					},
					function(retMsg){
						showAlertPopup(retMsg, true);
					}, 
					'text'
				);
		});
		}
	}
$(document)
			.ready(
					function() {
						$('.catalogApp')
								.draggable(
										{
											cursor : 'none',
											cursorAt : {
												left : 25,
												top : 25
											},
											helper : function(e, ui) {
												var ret;
												ret = $('<div class="dragHelperLogo" style="position: fixed;"/>');
												ret.attr("idx", $(this).attr(
														"idx"));
												ret.attr("name", $(this).find(
														'p').text());
												ret.attr("connect", $(this)
														.attr("connect"));
												ret.attr("data-login", $(this)
														.attr("data-login"));
												ret.attr("data-sso", $(this)
														.attr("data-sso"));
												ret.attr("data-nologin", $(this)
														.attr("data-nologin"));
												ret.append($('<img />'));
												ret.find('img').attr(
														"src",
														$(this).find('img')
																.attr("src"));
												return ret; //Replaced $(ui) with $(this)
											}
										});
									});
								</script>
