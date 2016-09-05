<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div class="md-modal md-effect-15 popup" id="PopupModifyApp">
	<div class="md-content">
		<div class="popupHeader">
			<img class="logoApp" src="resources/images/Deezer.jpeg" />
			<div class="textContent">
				<p class="title">Modifiez les informations relatives à <span>Deezer</span></p>
			</div>
		</div>
		<input  id="name" name="name" type="text" placeholder="Nom" maxlength="14"/>
		<div class="loginWithChooser">
			<p>Log in with</p>
			<div class="loginWithButton hidden" webid="7" style="background-color:#3B5998;"><p>Facebook</p></div>
			<div class="loginWithButton hidden" webid="28" style="background-color:#007BB6;"><p>Linkedin</p></div>
			<p class="or">or</p>
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
			<input  name="login" type="text" placeholder="Indentifiant" style="display:none;"/>
			<input  id="login" name="login" type="text" placeholder="Indentifiant" />
			<input  id="password" name="password" type="password" placeholder="Mot de passe"/>
    	</div>
		<div class="buttonSet">
   			<button id="accept" class="btn btn-default btn-primary btn-group btn-lg">Ajouter</button>
   			<button id="close" class="btn btn-default btn-primary btn-group btn-lg">Annuler</button>
    	</div>
</div>
</div>
 
 <script>	
 $(document).ready(function(){
 	$('#PopupModifyApp .buttonBack').click(function(){
 		var parent = $(this).closest('.md-content');

 		parent.find('.loginAppChooser .ChooserContent').empty();
 		parent.find('.loginAppChooser').css('display', 'none');
 		parent.find('#modifyAppForm').css('display', 'block');
 		parent.find('.or').css('display', 'block');
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

		parent.find('.loginAppChooser').css('display', 'block');
		parent.find('#modifyAppForm').css('display', 'none');
		parent.find('.or').css('display', 'none');

		var apps = $(".siteLinkBox[webid='" + webid + "']");
		for (var i = 0; i < apps.length; i++) {
			AppHelper.attr('pId', $(apps[i]).closest('.owl-item').index());
			AppHelper.attr('aId', $(apps[i]).index());			
			AppHelper.find('p').text($(apps[i]).attr('login'));
			AppHelper.find('img').attr('src',$(apps[i]).find('img').attr('src'));
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

		popup.find('#modifyAppForm').css('display', 'block');
		popup.find('.or').css('display', 'block');
		popup.find('.loginAppChooser .ChooserContent').empty();
		popup.find('.loginAppChooser').css('display', 'none');

	popup.find('.logoApp').attr('src', $(app).find('img').attr('src'));
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

	popup.find("#close").unbind('click');
	popup.find("#close").click(function(){
		popup.removeClass('md-show');
	});
	popup.find("#accept").unbind('click');
	popup.find("#accept").click(function(){
		popup.removeClass('md-show');
		image.append($('<img class="tmp" src="resources/other/loading-effect.svg" style="position:absolute;top:0;left:0;"/>'));
		var connect = app.attr('connect');
		image.removeClass('scaleOutAnimation');

			var name = popup.find('#name').val();
			var login = '';
			var password = '';
			var pId = '';
			var aId = '';

			var AppToLoginWith = $(popup).find('.AccountApp.selected');
			if (AppToLoginWith.length){
				pId = AppToLoginWith.attr('pId');
				aId = AppToLoginWith.attr('aId');
				$.post(
					'editLogWith',
					{
						name: name,
						profileIndex: $(profile).index(),
						appIndex: $(app).index(),
						lwProfileIndex: pId,
						lwAppIndex: aId
					},
					function(data){
						$(app).find('.tmp').remove();
						if (data[0] == 's'){
							image.addClass('scaleOutAnimation');
							app.attr('onclick', "sendEvent(this)");
							app.attr('login', '');
							app.attr('name', name);
							app.find('.siteName p').text(name);
						}else {
							if (data[0] != 'e'){
								document.location.reload(true);
							} else {
				        	  	showAlertPopup(null, true);
							}
						}
					},
					'text'
				);
			} else {
				login = popup.find('#login').val();
				password = popup.find('#password').val();
				$.post(
					'editApp',
					{
						name: name,
						profileIndex: $(profile).index(),
						appIndex: $(app).index(),
						login: login,
						wPassword: password
					},
					function(data){
						$(app).find('.tmp').remove();
						if (data[0] == 's'){
							image.addClass('scaleOutAnimation');
							app.attr('onclick', "sendEvent(this)");
							app.attr('login', login);
							app.attr('name', name);
							app.find('.siteName p').text(name);
						}else {
							if (data[0] != 'e'){
								document.location.reload(true);
							} else {
				        	  	showAlertPopup(null, true);
							}
						}
					},
					'text'
		 		);
			}
	});
	 $(popup).addClass('md-show');
	setTimeout(function(){
		$(popup).find('#login').focus();
	}, 100);
};
</script>
 