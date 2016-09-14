<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div class="md-modal md-effect-15 popup" id="PopupAddApp">
	<div class="md-content">
		<div class="popupHeader">
			<img class="logoApp" src="" />
			<div class="textContent">
				<p>Type your password for the last time ;)</p>
			</div>
		</div>
		<input  id="name" name="name" type="text" placeholder="Name" maxlength="14"/>
		<div class="loginWithChooser">
			<div class="linedSeparator">
				<p>Log in with</p>
			</div>
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
		<div id="AddAppForm">
			<input  id="login" name="login" type="text" placeholder="Login"/>
			<input  id="password" name="password" type="password" placeholder="Password"/>
    	</div>
		<div class="buttonSet">
   			<button id="accept" class="btn btn-default btn-primary btn-group btn-lg">Add</button>
   			<button id="close" class="btn btn-default btn-primary btn-group btn-lg">Cancel</button>
    	</div>
</div>
</div>
 
 <script>
 $(document).ready(function(){
 	$('#PopupAddApp .buttonBack').click(function(){
 		var parent = $(this).closest('.md-content');

 		parent.find('.loginAppChooser .ChooserContent').empty();
 		parent.find('.loginAppChooser').css('display', 'none');
 		parent.find('#AddAppForm').css('display', 'block');
 		parent.find('.or').css('display', 'block');
 	});
});
$(document).ready(function(){
	$('#PopupAddApp .loginWithButton').click(function(){
		var parent = $(this).closest('.md-content');
		var AppChooser = $(this).closest('.md-content').find('.loginAppChooser .ChooserContent');
		var webid = $(this).attr('webid');
		var AppHelper = $("<div class='AccountApp'><div class='imageHandler'><img src='' /></div><p></p></div>");
		AppChooser.empty();
		var AppHelperCloned;

		parent.find('.loginAppChooser').css('display', 'block');
		parent.find('#AddAppForm').css('display', 'none');
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

  function showAddAppPopup(container, helper){
		var popup = $('#PopupAddApp');
		var item = $($('#boxHelper').html());

		popup.find('#AddAppForm').css('display', 'block');
		popup.find('.or').css('display', 'block');
		popup.find('.loginAppChooser .ChooserContent').empty();
		popup.find('.loginAppChooser').css('display', 'none');

		item.attr('name', $(helper).attr("name"));
		item.find('img').attr('src', $(helper).find('img').attr("src"));
		item.find('.siteName p').text($(helper).attr("name"));
		popup.find('.logoApp').attr('src', $(helper).find('img').attr("src"));
		popup.find('span').text($(helper).attr("name"));
		$(popup).find('#login').val('');
		$(popup).find('#password').val('');
		$(popup).find('#name').val($(helper).attr("name"));
		popup.find('#close').unbind('click');
		popup.find('#close').click(function(){
			popup.removeClass('md-show');
		}) ;

		var loginChooser = $('#PopupAddApp .loginWithChooser');
		var abc = $(helper).attr('data-login').split(',');

		loginChooser.addClass('hidden');
		loginChooser.find('.loginWithButton').addClass('hidden');
		if ($(helper).attr('data-login') != ""){
			loginChooser.removeClass('hidden');
			for (var i = 0; i < abc.length; i++) {
				loginChooser.find(".loginWithButton[webid='" + abc[i] + "']").removeClass('hidden');
			}
		}
		popup.find('#accept').unbind('click');
		popup.find('#accept').click(function(){
			$(container).append(item);
			$(item).find('.linkImage').append($('<img class="tmp" src="resources/other/loading-effect.svg" style="position:absolute;top:0;left:0;"/>'));

			var name = $(popup).find('#name').val();
			var login = '';
			var password = '';
			var aId = '';

			var AppToLoginWith = $(popup).find('.AccountApp.selected');

			if (AppToLoginWith.length){
				aId = AppToLoginWith.attr('aId');
				$.post(
					'addLogWith',
					{
						name: name,
						profileId: $(container).closest('.item').attr('id'),
						siteId: $(helper).attr('idx'),
						appId: aId
					},
					function(data){	
						$(item).find('.tmp').remove();
						if (data[0] == 's'){
							$(item).find('.linkImage').addClass('scaleOutAnimation');
							setTimeout(function() {
								$(item).find('.linkImage').removeClass('scaleOutAnimation');
							}, 1000);
							$(item).attr('onclick', "sendEvent(this)");
							$(item).attr('webId', $(helper).attr('idx'));
							$(item).attr('name', name);
							$(item).find('.siteName p').text(name);
							$(item).attr('id', data.substring(9, data.length))

						} else {
							if (data[0] != 'e'){
								document.location.reload(true);
							} else {
								showAlertPopup(null, true);
								$(item).remove();
							}													
						}
					},
					'text'
				);
			}else {
				login = $(popup).find('#login').val();
				password = $(popup).find('#password').val();
				$.post(
					'addApp',
					{
						name: name,
						login: login,
						password: password,
						profileId: $(container).closest('.item').attr('id'),
						siteId: $(helper).attr('idx'),
					},
					function(data){
						$(item).find('.tmp').remove();
						if (data[0] == 's'){
							$(item).find('.linkImage').addClass('scaleOutAnimation');
							setTimeout(function() {
								$(item).find('.linkImage').removeClass('scaleOutAnimation');
							}, 1000);
							$(item).attr('onclick', "sendEvent(this)");
							$(item).attr('login', login);
							$(item).attr('webId', $(helper).attr('idx'));
							$(item).attr('name', name);
							$(item).find('.siteName p').text(name);
							$(item).attr('id', data.substring(9, data.length))
						} else {
							if (data[0] != 'e'){
								document.location.reload(true);
							} else {
								showAlertPopup(null, true);
								$(item).remove();
							}						
						}
					},
					'text'
				);
			}
			popup.removeClass('md-show');
		}) ;	
		setTimeout(function() {
				popup.find('#login').focus();
		}, 100);
		popup.addClass('md-show');
 	}
 </script>
