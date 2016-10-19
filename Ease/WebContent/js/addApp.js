$(document).ready(function() {
	$(".login-group-input input[name='login']").focus(function() {
		$("#email-suggestions").addClass("show");
	});
	
	$('.login-group-input .email-suggestions').click(function() {
		$("#email-suggestions").toggleClass("show");
	});
	$("#email-suggestions .email-suggested").click(function() {
		$(".login-group-input input[name='login']").val($(this).find("span").html());
		$("#email-suggestions").removeClass("show");
		$("#AddAppForm input[name='password']").focus();
	});
	
	$('#PopupAddApp .loginWithButton').click(function() {
		$(".loginAppChooser p").text("Select your account");
		var parent = $(this).closest('.md-content');
		var AppChooser = $(this).closest('.md-content').find('.loginAppChooser .ChooserContent');
		var webid = $(this).attr('webid');
		var AppHelper = $("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>");
		AppChooser.empty();
		var AppHelperCloned;
		parent.find('.loginWithButton').removeClass('locked');
		$(this).addClass('locked');
		parent.find('.loginAppChooser').css('display', 'block');
		parent.find('.classicLogin').removeClass("show");
		parent.find('.or').css('display', 'none');
		var apps = $(".siteLinkBox[webid='" + webid + "']");
		if (apps.length == 0) {
			if (webid == "7")
				$(".loginAppChooser p").text("No Facebook account detected");
			if (webid == "28")
				$(".loginAppChooser p").text("No Linkedin account detected");
		}
		for (var i = 0; i < apps.length; i++) {
			AppHelper.attr('aId', $(apps[i]).attr("id"));
			AppHelper.find('p').text($(apps[i]).attr('login'));
			AppHelper.find('img').attr('src', $(apps[i]).find('img.logo').attr('src'));
			AppHelperCloned = $(AppHelper).clone();
			AppHelperCloned.click(function() {
				$(parent).find('.AccountApp.selected').removeClass('selected');
				$(this).addClass('selected');
				popupAddApp.appId($(this).attr("aid"));
				popupAddApp.setPostName('addLogWith');
			});
			if (i == 0) {
				$(AppHelperCloned).addClass("selected");
				popupAddApp.appId($(AppHelperCloned).attr("aid"));
				popupAddApp.setPostName('addLogWith');
			}
			AppChooser.append(AppHelperCloned);
		}
	});
});

function showAddAppPopup(container, helper) {
	var popup = $('#PopupAddApp');
	var item = $($('#boxHelper').html());
	
	popupAddApp.open();
	popupAddApp.setHelper($(helper));
	popupAddApp.setAppsContainer($(container));
	popupAddApp.setNewAppItem(item);
	popupAddApp.setVal($(helper).attr("name"));

	popup.find('.loginWithButton').removeClass('locked');
	popup.find('.or').css('display', 'block');
	popup.find('.loginAppChooser .ChooserContent').empty();
	popup.find('.loginAppChooser').css('display', 'none');

	popup.find('.loginSsoChooser .ChooserContent').empty();
	popup.find('.loginSsoChooser').css('display', 'none');

	item.attr('name', $(helper).attr("name"));
	item.find('img.logo').attr('src', $(helper).find('img').attr("src"));
	item.find('.siteName p').text($(helper).attr("name"));
	popup.find('.logoApp').attr('src', $(helper).find('img').attr("src"));

	var loginChooser = $('#PopupAddApp .loginWithChooser');
	var loginWith = $(helper).attr('data-login').split(',');

	loginChooser.addClass('hidden');
	loginChooser.find('.loginWithButton').addClass('hidden');
	if ($(helper).attr('data-login') != "") {
		loginChooser.removeClass('hidden');
		for (var i = 0; i < loginWith.length; i++) {
			loginChooser.find(".loginWithButton[webid='" + loginWith[i] + "']")
					.removeClass('hidden');
		}
	}

	var SsoChooserContent = $('.loginSsoChooser .ChooserContent');
	var SsoHelper = $("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>");

	var ssoChooser = $('#PopupAddApp .loginSsoChooser');
	var ssoId = $(helper).attr('data-sso');

	if (ssoId != "" && ssoId != null) {
		var apps = $(".siteLinkBox[ssoId='" + ssoId + "']");
		var logins = [];
		if (apps.length != 0)
			ssoChooser.css('display', 'block');
		for (var i = 0; i < apps.length; i++) {
			if (!logins.includes($(apps[i]).attr('login'))) {
				SsoHelper.find('p').text($(apps[i]).attr('login'));
				SsoHelper.attr('aId', $(apps[i]).attr("id"));
				SsoHelper.find('img').attr('src',
						'resources/sso/' + ssoId + ".png");
				SsoHelperCloned = $(SsoHelper).clone();
				SsoHelperCloned.click(function() {
					$(this).closest('.ChooserContent').find(
							'.AccountApp.selected').removeClass('selected');
					$(this).addClass('selected');
					popupAddApp.appId(SsoHelper.attr("aid"));
					popupAddApp.setPostName('addAppWithSso');
				});
				SsoChooserContent.append(SsoHelperCloned);
				logins.push($(apps[i]).attr('login'));
			}
		}
	}

	if ($(helper).attr('data-nologin') == "true") {
		popup.find('#AddAppForm').css('display', 'none');
		$('.or').css('display', 'none');
	}

	$('#PopupAddApp .buttonBack').unbind("click");
	$('#PopupAddApp .buttonBack').click(function() {
		var parent = $(this).closest('.md-content');
		parent.find('.loginWithButton').removeClass('locked');
		parent.find('.loginAppChooser .ChooserContent').empty();
		parent.find('.loginAppChooser').css('display', 'none');
		if ($(helper).attr('data-nologin') != "true") {
			popupAddApp.appId(null);
			parent.find('.classicLogin').addClass("show");
			parent.find('.or').css('display', 'block');
		}
	});
	addAppTutorial();
}