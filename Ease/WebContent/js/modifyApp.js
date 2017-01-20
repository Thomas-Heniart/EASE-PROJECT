	function resetDisabledInput(elem){
		$(elem).find('input').prop('disabled', true);
		$(elem).find('.activateInput').css('display', 'block');
		if($(elem).find('input').attr('name')=="password"){
			$(elem).find('input').attr('placeholder','Click on the wheel to modify password');
		}
	}
	function enableDisabledInput(elem) {
		$(elem).find('input').prop('disabled', false);
		$(elem).find('.activateInput').css('display', 'none');
		if($(elem).find('input').attr('name')=="password"){
			$(elem).find('input').attr('placeholder','Password');
		}
	}
	$(document).ready(function(){
		$('.disabledInput .activateInput').click(function(){
			var inpt = $(this).closest('.disabledInput').find('input');
			if (inpt.prop('disabled') == true){
				inpt.prop('disabled', false);
				if(inpt.attr('name')=="password"){
					inpt.attr('placeholder','Password');
				}
			}
			
			$(this).css('display', 'none');
			$(inpt).focus();
		});

		$('#PopupModifyApp .loginWithButton').click(function(){
			var parent = $(this).closest('.md-content');
			var AppChooser = $(this).closest('.md-content').find('.loginAppChooser .ChooserContent');
			var webid = $(".catalogApp[name='"+$(this).attr('name')+"']").attr("idx");
			var AppHelper = $("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>");
			AppChooser.empty();
			var AppHelperCloned;

			parent.find('.loginWithButton').removeClass('locked');
			$(this).addClass('locked');

			parent.find('.loginAppChooser').css('display', 'block');
			parent.find('.or').css('display', 'none');
			parent.find(".classicLogin").removeClass("show");

			var apps = $(".siteLinkBox[webid='" + webid + "']");
			if (apps.length == 0) {
				var websiteName = $(".catalogApp[idx='"+webid+"']").attr("name");
				if(websiteName)
					$(".loginAppChooser p").text("No "+websiteName+" account detected");
			}
			for (var i = 0; i < apps.length; i++) {
				AppHelper.attr('aId', $(apps[i]).attr('id'));			
				AppHelper.find('p').text($(apps[i]).attr('login'));
				AppHelper.find('img').attr('src',$(apps[i]).find('img.logo').attr('src'));
				AppHelperCloned = $(AppHelper).clone();
				AppHelperCloned.click(function(){
					$(parent).find('.AccountApp.selected').removeClass('selected');
					$(this).addClass('selected');
				});
				if (i == 0) {
					$(AppHelperCloned).addClass("selected");
					$("#PopupModifyApp .buttonSet button[type='submit']").addClass("Active");
					$("#PopupModifyApp .buttonSet button[type='submit']").prop("disabled",false);
				}
				AppChooser.append(AppHelperCloned);

			}
		});
	});

	function showModifyAppPopup(elem, event){
		event.preventDefault();  
		event.stopPropagation();
		modifyAppPopup.open();
		var popup = $('#PopupModifyApp');
		var profile = $(elem).closest('.owl-item');
		var app = $(elem).closest('.siteLinkBox');
		modifyAppPopup.setApp($(app));
		popup.find('.logoApp').attr('src', $(app).find('img.logo').attr('src'));
		var image = $(app).find('.linkImage');
		
		if($(app).hasClass('emptyApp')){
			popup.find('.disabledInput').each(function(){
				enableDisabledInput($(this));
			});
			popup.find('.loginWithButton').removeClass('locked');
			popup.find('.classicLogin').addClass("show");
			popup.find('.or').css('display', 'block');
			popup.find('.loginAppChooser .ChooserContent').empty();
			popup.find('.loginAppChooser').css('display', 'none');
			popup.find('p.title').html('Finalize <span></span>.</br>Type your info for the last time');
			
			var loginChooser = $('#PopupModifyApp .loginWithChooser');
			var temp;
			if((temp = $(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-login'))){
				var abc = temp.split(',');
			} else {
				var abc = [];
			}

			loginChooser.addClass('hidden');
			loginChooser.find('.loginWithButton').addClass('hidden');
			if ($(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-login') != ""){
				loginChooser.removeClass('hidden');
				for (var i = 0; i < abc.length; i++) {
					loginChooser.find(".loginWithButton[name='" + $(".catalogApp[idx='"+abc[i]+"']").attr("name") + "']").removeClass('hidden');
				}
			}

			$('#PopupModifyApp .buttonBack').unbind("click");
			$('#PopupModifyApp .buttonBack').click(function(){
				var parent = $(this).closest('.md-content');

				parent.find('.loginWithButton').removeClass('locked');
				parent.find('.loginAppChooser .ChooserContent').empty();
				parent.find('.loginAppChooser').css('display', 'none');
				if ($(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-nologin') != "true"){
					parent.find('.classicLogin').addClass("show");
					parent.find('.or').css('display', 'block');
				}
			});
			$("#PopupModifyApp .popupHeader .title span").text($(app).attr("name"));
			modifyAppTutorial();
		} else {
			popup.find('.disabledInput').each(function(){
				resetDisabledInput($(this));
			});
			
			var loginChooser = $('#PopupModifyApp .loginWithChooser');
			
			popup.find('p.title').html('Modify informations related to <span></span>');
			if (app.attr('logwith') != 'false'){
				var logid = app.attr('logwith');
				var webid = $(".siteLinkBox[id='" + logid + "']").attr('webid');
				loginChooser.removeClass('hidden');
				popup.find('.loginAppChooser').css('display', 'block');
				loginChooser.find(".loginWithButton").addClass('hidden');
				loginChooser.find(".loginWithButton[name='" + $(".catalogApp[idx='"+webid+"']").attr("name") + "']").removeClass('hidden');
				popup.find(".loginWithButton[name='" + $(".catalogApp[idx='"+webid+"']").attr("name") + "']").click();
				popup.find(".AccountApp[aid='" + logid + "']").click();
				popup.find('.classicLogin').removeClass("show");
			} else {
				popup.find('.classicLogin').addClass("show");
				loginChooser.addClass('hidden');
				loginChooser.find('.loginWithButton').addClass('hidden');
				//popup.find('.loginAppChooser .ChooserContent').empty();
				popup.find('.loginAppChooser').css('display', 'none');
				var loginChooser = $('#PopupModifyApp .loginWithChooser');
			}
			$("#PopupModifyApp .popupHeader .title span").text($(app).attr("name"));
			modifyAppTutorial();
		}
		
		
		
		
		/*
		 * 
		 * if ($(app).hasClass('emptyApp')){
			popup.find('.disabledInput').each(function(){
				enableDisabledInput($(this));
			});
		}
		else {
			popup.find('.disabledInput').each(function(){
				resetDisabledInput($(this));
			});			
		}
		popup.find('.loginWithButton').removeClass('locked');
		popup.find('.classicLogin').addClass("show");
		popup.find('.or').css('display', 'block');
		popup.find('.loginAppChooser .ChooserContent').empty();
		popup.find('.loginAppChooser').css('display', 'none');
		if (app.hasClass('emptyApp')){
			popup.find('p.title').html('Finalize <span></span>.</br>Type your info for the last time');
		}else {
			popup.find('p.title').html('Modify informations related to <span></span>');
		}
		
		var loginChooser = $('#PopupModifyApp .loginWithChooser');
		var temp;
			if((temp = $(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-login'))){
				var abc = temp.split(',');
			} else {
				var abc = [];
			}
			

		loginChooser.addClass('hidden');
		loginChooser.find('.loginWithButton').addClass('hidden');
		if ($(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-login') != ""){
			loginChooser.removeClass('hidden');
			for (var i = 0; i < abc.length; i++) {
				loginChooser.find(".loginWithButton[name='" + $(".catalogApp[idx='"+abc[i]+"']").attr("name") + "']").removeClass('hidden');
			}
		}

		if (app.attr('logwith') != 'false'){
			
			var logid = app.attr('logwith');
			var webid = $(".siteLinkBox[id='" + logid + "']").attr('webid');
			popup.find(".loginWithButton[name='" + $(".catalogApp[idx='"+webid+"']").attr("name") + "']").click();
			popup.find(".AccountApp[aid='" + logid + "']").click();
			popup.find('.classicLogin').removeClass("show");
		}
		
		$('#PopupModifyApp .buttonBack').unbind("click");
		$('#PopupModifyApp .buttonBack').click(function(){
			var parent = $(this).closest('.md-content');

			parent.find('.loginWithButton').removeClass('locked');
			parent.find('.loginAppChooser .ChooserContent').empty();
			parent.find('.loginAppChooser').css('display', 'none');
			if ($(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-nologin') != "true"){
				parent.find('.classicLogin').addClass("show");
				parent.find('.or').css('display', 'block');
			}
		});
		$("#PopupModifyApp .popupHeader .title span").text($(app).attr("name"));
		modifyAppTutorial();*/
	}