<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="md-modal md-effect-15 popup" id="PopupModifyApp">
	<div class="md-content">
		<div class="popupHeader">
			<img class="logoApp" src="resources/images/Deezer.jpeg" />
			<div class="textContent">
				<p class="title">Modify informations related to <span>Deezer</span></p>
			</div>
		</div>
		<div class="lineInput">
			<p class="inputTitle">App name :</p>
			<input  id="name" name="name" type="text" placeholder="Name" maxlength="14"/>
		</div>
		<div class="loginWithChooser">
			<div class="linedSeparator">
				<div class="backgroundLine"></div>
				<p>Log in with</p>
			</div>
			<div class="loginWithButton facebook hidden" webid="7"><p>Facebook</p></div>
			<div class="loginWithButton linkedin hidden" webid="28"><p>Linkedin</p></div>
			<div class="linedSeparator or">
				<div class="backgroundLine"></div>
				<p>or</p>
			</div>
		</div>
		<div class="loginAppChooser" style="display:none;">
			<p>Select your account </p>
			<div class="buttonBack">
				<i class="fa fa-arrow-circle-o-left" aria-hidden="true"></i>
			</div>
			<div class="ChooserContent">
			</div>
		</div>
		<div id="modifyAppForm">
			<input  name="login" type="text" placeholder="Login" style="display:none;"/>
			<input  id="login" name="login" type="text" placeholder="Login" />
			<div class="disabledInput">
				<input  id="password" name="password" type="password" placeholder="Click on the wheel to modify password"/>
				<div class="activateInput">
					<i class="fa fa-cog"></i>
				</div>
			</div>
		</div>
		<div class="buttonSet">
			<button id="accept" class="btn btn-default btn-primary btn-group btn-lg">Update</button>
			<button id="close" class="btn btn-default btn-primary btn-group btn-lg">Cancel</button>
		</div>
	</div>
</div>

<script>	
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
	});
	$(document).ready(function(){
		$('#PopupModifyApp .loginWithButton').click(function(){
			var parent = $(this).closest('.md-content');
			var AppChooser = $(this).closest('.md-content').find('.loginAppChooser .ChooserContent');
			var webid = $(this).attr('webid');
			var AppHelper = $("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>");
			AppChooser.empty();
			var AppHelperCloned;

			parent.find('.loginWithButton').removeClass('locked');
			$(this).addClass('locked');

			parent.find('.loginAppChooser').css('display', 'block');
			parent.find('#modifyAppForm').css('display', 'none');
			parent.find('.or').css('display', 'none');

			var apps = $(".siteLinkBox[webid='" + webid + "']");
			for (var i = 0; i < apps.length; i++) {
				AppHelper.attr('aId', $(apps[i]).attr('id'));			
				AppHelper.find('p').text($(apps[i]).attr('login'));
				AppHelper.find('img').attr('src',$(apps[i]).find('img.logo').attr('src'));
				AppHelperCloned = $(AppHelper).clone();
				AppHelperCloned.click(function(){
					$(parent).find('.AccountApp.selected').removeClass('selected');
					$(this).addClass('selected');
				});
				AppChooser.append(AppHelperCloned);

			}
		});

	});

	$(document).ready(function(){
		$('#PopupModifyApp #password').keyup(function(event){
			if(event.keyCode == 13){
				$("#PopupModifyApp .md-content .buttonSet #accept").click();
			}
		});
		$('#PopupModifyApp #login').keyup(function(event){
			if(event.keyCode == 13){
				$('#PopupModifyApp #password').focus();
			}
		});
	});

	function showModifyAppPopup(elem, event){
		event.preventDefault();  
		event.stopPropagation();
		var popup = $('#PopupModifyApp');
		var profile = $(elem).closest('.owl-item');
		var app = $(elem).closest('.siteLinkBox');
		var image = $(app).find('.linkImage');

		if ($(app).hasClass('emptyApp')){
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
		popup.find('#modifyAppForm').css('display', 'block');
		popup.find('.or').css('display', 'block');
		popup.find('.loginAppChooser .ChooserContent').empty();
		popup.find('.loginAppChooser').css('display', 'none');
		if (app.hasClass('emptyApp')){
			popup.find('p.title').html('Finalize <span></span>.</br>Type your info for the last time');
		}else {
			popup.find('p.title').html('Modify informations related to <span></span>');
		}
		popup.find('.logoApp').attr('src', $(app).find('img.logo').attr('src'));
		popup.find('span').text($(app).attr('name'));
		popup.find("#login").val($(app).attr('login'));
		popup.find("#password").val($(app).attr('hash'));
		popup.find("#name").val($(app).attr('name'));
		var loginChooser = $('#PopupModifyApp .loginWithChooser');
		var abc = $(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-login').split(',');

		loginChooser.addClass('hidden');
		loginChooser.find('.loginWithButton').addClass('hidden');
		if ($(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-login') != ""){
			loginChooser.removeClass('hidden');
			for (var i = 0; i < abc.length; i++) {
				loginChooser.find(".loginWithButton[webid='" + abc[i] + "']").removeClass('hidden');
			}
		}

		if (app.attr('logwith') != 'false'){
			var logid = app.attr('logwith');

			var webid = $(".siteLinkBox[id='" + logid + "']").attr('webid');
			popup.find(".loginWithButton[webid='" + webid + "']").click();
			popup.find(".AccountApp[aid='" + logid + "']").click();

		}
		
		$('#PopupModifyApp .buttonBack').unbind("click");
		$('#PopupModifyApp .buttonBack').click(function(){
			var parent = $(this).closest('.md-content');

			parent.find('.loginWithButton').removeClass('locked');
			parent.find('.loginAppChooser .ChooserContent').empty();
			parent.find('.loginAppChooser').css('display', 'none');
			if ($(".catalogContainer .catalogApp[idx='" + $(app).attr('webid') + "']").attr('data-nologin') != "true"){
				parent.find('#modifyAppForm').css('display', 'block');
				parent.find('.or').css('display', 'block');
			}
		});
		
		popup.find("#close").unbind('click');
		popup.find("#close").click(function(){
			popup.removeClass('md-show');
		});
		popup.find("#accept").unbind('click');
		popup.find("#accept").click(function(){
			

			var name = popup.find('#name').val();
			var login = '';
			var password = '';
			var aId = '';
			var AppToLoginWith = $(popup).find('.AccountApp.selected');
			
			if(name == "" || name == null){
				showAlertPopup("App name can't be empty !", true);
			} else if(!AppToLoginWith.length && !popup.find('#login').val()){
				showAlertPopup("Login can't be empty !", true);
			} else {
			
				popup.removeClass('md-show');
				image.append($('<img class="tmp" src="resources/other/loading-effect.svg" style="position:absolute;top:0;left:0;"/>'));
				var connect = app.attr('connect');
				image.removeClass('scaleOutAnimation');
	
				if (AppToLoginWith.length){
					aId = AppToLoginWith.attr('aId');
					postHandler.post(
						'editApp',
						{
							name: name,
							appId: $(app).attr('id'),
							lwId: aId
						},
						function(){$(app).find('.tmp').remove();},
						function(retMsg){
							image.addClass('scaleOutAnimation');
							setTimeout(function() {
								image.removeClass('scaleOutAnimation');
							}, 1000);
							app.attr('login', '');
							app.attr('name', name);
							app.attr('logwith', aId);
							app.find('.siteName p').text(name);
							app.find('.emptyAppIndicator').remove();
							app.removeClass('emptyApp');
						},
						function(retMsg){
							showAlertPopup(retMsg, true);
						},
						'text'
					);
				} else {
					login = popup.find('#login').val();
					password = popup.find('#password').val();
					postHandler.post(
						'editApp',
						{
							name: name,
							appId: $(app).attr('id'),
							login: login,
							wPassword: password
						},
						function(){$(app).find('.tmp').remove();},
						function(retMsg){
							image.addClass('scaleOutAnimation');
							setTimeout(function() {
								image.removeClass('scaleOutAnimation');
							}, 1000);
								app.attr('login', login);
							app.attr('name', name);
							app.attr('logwith', 'false');
							app.find('.siteName p').text(name);
							app.find('.emptyAppIndicator').remove();
							app.removeClass('emptyApp');
							},
						function(retMsg){
							showAlertPopup(retMsg, true);
						},
						'text'
					);
				}
			}
		});
		$(popup).addClass('md-show');
		setTimeout(function(){
			$(popup).find('#login').focus();
		}, 100);
		modifyAppTutorial();
	};
</script>
