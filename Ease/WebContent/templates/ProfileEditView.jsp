<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.Ease.session.User"%>
<%@ page import="com.Ease.session.Profile"%>
<%@ page import="com.Ease.context.Site"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	int itr = 0;
%>
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
											$.post(
												'askForNewApp',
												{
													ask : $(form).find('#integrateApp').val()
												},
												function(data) {
													$(form).find('.inputs input').val('');
													$(form).find('.inputs').hide();
													$(form).find('.confirmation').show().delay(1000).fadeOut(function() {
														$(form).find('.inputs').show();
													});
												},
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
		if (('#tutorialView').length)
			enterEditModeTutorial();
	}

	function leaveEditMode() {

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
		if (('#tutorialView').length)
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
							if ($(".CatalogViewTab").hasClass("show")) {
								if ( $(event.target).is($(".ProfilesView.show")) && $(".md-modal").hasClass('md-show'))
									return;
								if ($(event.target).is($(".md-overlay")))
									return;
								if ($(event.target).is($(".col-left.show")) || $(event.target).is($(".ProfilesView.show")) || $(event.target).is($(".ProfilesHandler.editMode")) || $(event.target).is($(".owl-item")))
									leaveEditMode();
							}
						});
			});

	function showConfirmDeleteAppPopup(elem, event) {
		event.preventDefault();
		event.stopPropagation();
		var popup = $('#PopupDeleteApp');
		var app = $(elem).closest('.siteLinkBox');
		var image = $(app).find('.linkImage');

		popup.addClass('md-show');
		popup.find("#close").unbind('click');
		popup.find("#close").click(function() {
			popup.removeClass('md-show');
		});
		popup.find("#accept").unbind('click');
		popup.find("#accept").click(function() {
			popup.removeClass('md-show');
			image.addClass('easyScaling');
			$.post('deleteApp', {
				appId: $(app).attr('id')
			}, function(data) {
				if (data[0] == 's') {
					image.removeClass('easyScaling');
					image.addClass('deletingApp');
					setTimeout(function() {
						console.log(app);
						app.remove();
					}, 500);
				} else {
					if (data[0] != 'e') {
						document.location.reload(true);
					} else {
						showAlertPopup(null, true);
						image.removeClass('easyScaling');
					}

				}
			}, 'text');
		});
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
