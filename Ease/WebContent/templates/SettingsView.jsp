<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="SettingsView">
	<div class="settingsWindow">
	<div class="quit">
	<i class="fa fa-times" aria-hidden="true"></i>
	</div>
	<div class="title">
		<i class="fa fa-fw fa-cogs"></i>
		<p>Settings</p>
	</div>		
	<div id="settingsTab">
      <div class="sectionHeader" id="nameSection"><p>Name</p><p class="directInfo"><%= user.getFirstName()%> <%= user.getLastName() %></p>
      <div class="iconEdit"><i class="fa fa-cog"></i><p>Edit</p></div>
      </div>
      <div class="sectionContent" id="contentName">
      <div>
      	<div   id="modifyNameForm">
      		<input  id="firstName" name="firstName" type="text" placeholder="First name"/>
			<input  id="lastName" name="lastName" type="text" placeholder="Last name"/>
			<div class="buttonSet">
			    <button class="button" id="validate">Validate</button>
			    <button class="button" id="cancel">Cancel</button>
			</div>
      	</div>
      </div>
      </div>
      <div class="sectionHeader" id="contactSection"><p>Contact</p><p class="directInfo"><%= user.getEmail() %></p>
            <div class="iconEdit"><i class="fa fa-cog"></i><p>Edit</p></div>
      </div>
      <div class="sectionContent" id="contentContact">
      	<div id="modifyEmailForm">
      		<input  id="email" name="email" type="email" placeholder="Email"/>
			<input  id="password" name="password" type="password" placeholder="Confirm with password"/>
			<div class="buttonSet">
			    <button class="button" id="validate">Validate</button>
			    <button class="button" id="cancel">Cancel</button>
			</div>
		</div>
      </div>
      <div class="sectionHeader" id="passwordSection"><p>Password</p><p class="directInfo"></p>
            <div class="iconEdit"><i class="fa fa-cog"></i><p>Edit</p></div>
      </div>
      <div class="sectionContent" id="contentPassword">
      	<div id="modifyPasswordForm">
      		<p>For safety reasons, your password needs to be at least 8 characters long,  including upper-case and lower-case letters, plus at least one numerical digit.</p>
			<input  id="currentPassword" name="currentPassword" type="password" placeholder="Current password"/>
      		<input  id="newPassword" name="newPassword" type="password" placeholder="New password"/>
			<input  id="confirmNewPassword" name="confirmNewPassword" type="password" placeholder="Confirm new password"/>
			<div class="alertDiv">
				<p>Incorrect password or email !</p>
			</div>
			<div class="buttonSet">
			    <button class="button" id="validate">Validate</button>
			    <button class="button" id="cancel">Cancel</button>
			</div>	
      	</div>      
      </div>
<!--      <div class="sectionHeader" id="phoneSection"><p>Phone</p><p class="directInfo"></p>
            <div class="iconEdit"><i class="fa fa-cog"></i><p>Edit</p></div>
      </div>
      <div class="sectionContent" id="contentPhone">
      	<div id="modifyPhoneForm">
      		<input  id="phoneNumber" name="phoneNumber" type="text" placeholder="Phone number"/>
			<div class="buttonSet">
			    <button class="button" id="validate">Validate</button>
			    <button class="button" id="cancel">Cancel</button>
			</div>	
      	</div>
      </div>-->
<!--  <div class="sectionHeader" id="languageSection"><p>Language</p><p class="directInfo"></p>
            <div class="iconEdit"><i class="fa fa-cog"></i><p>Edit</p></div>
      </div>      
      <div class="sectionContent" id="contentLanguage">
      	<div id="modifyLanguageForm">
      		<input  id="language" name="language" type="text" placeholder="Language"/>
			<div class="buttonSet">
			    <button class="button" id="validate">Validate</button>
			    <button class="button" id="cancel">Cancel</button>
			</div>	
      	</div>
      </div> -->
    </div>
</div>
</div>

<script>
$(document).ready(function(){
	$('#settingsTab #modifyNameForm #validate').click(function(){
		var parent = $(this).closest('#modifyNameForm');
		var lname = $(parent).find('#lastName').val();
		var fname = $(parent).find('#firstName').val();
		
		$('#loading').addClass('la-animate');
		$.post(
				'editUserName',
				{
					fname : fname,
					lname: lname
				},
				function(data){
					$('#loading').removeClass('la-animate');
					if (data[0] == 's'){
		        	  	showAlertPopup('Modifications successfully applied !', false);
						$(parent).closest('#settingsTab').find('#nameSection .directInfo').text(fname + ' ' + lname);
						$('#menu .menu__label span').text(fname);
					}else {
						if (data[0] != 'e'){
							document.location.reload(true);
						} else {
							showAlertPopup(null, true);
						}
					}
					$(parent).find('#firstName').val('');
					$(parent).find('#lastName').val('');
				},
				'text'
			);
	});

	$('#settingsTab #modifyEmailForm #validate').click(function(){
		var parent = $(this).closest('#modifyEmailForm');
		var email = $(parent).find('#email').val();
		var password = $(parent).find('#password').val();
		
		$('#loading').addClass('la-animate');
		$.post(
				'editUserEmail',
				{
					email : email,
					password : password
				},
				function(data){
					$('#loading').removeClass('la-animate');
					if (data[0] == 's'){
		        	  	showAlertPopup('Modifications successfully applied !', false);
						$(parent).closest('#settingsTab').find('#contactSection .directInfo').text(email);
					}else {
						if (data[0] != 'e'){
							document.location.reload(true);
						} else {
							showAlertPopup(null, true);
						}
					}
					$(parent).find('#email').val('');
					$(parent).find('#password').val('');
				},
				'text'
			);
	});
	
	$('#settingsTab #modifyPasswordForm #validate').click(function(){
		var parent = $(this).closest('#modifyPasswordForm');
		var newPass = $(parent).find('#newPassword').val();
		var confNewPass = $(parent).find('#confirmNewPassword').val();
		var currentPass = $(parent).find('#currentPassword').val();
		var alert = $(parent).find('.alertDiv');
		
		$(alert).removeClass('show');
		$('#loading').addClass('la-animate');
		$.post(
				'editUserPassword',
				{
					password : newPass,
					confirmPassword : confNewPass,
					oldPassword : currentPass
				},
				function(data){
					$('#loading').removeClass('la-animate');
					if (data[0] == 's'){
						$(parent).find('.alertDiv').removeClass('show');
						showAlertPopup('Your password was successfully changed')
					}else {
						if (data[0] != 'e'){
							document.location.reload(true);
						} else {
							$(alert).find('p').text(data.substring(7, data.length));
							$(parent).find('.alertDiv').addClass('show');
						}						
					}
					$(parent).find('#newPassword').val('');
					$(parent).find('#confirmNewPassword').val('');
					$(parent).find('#currentPassword').val('');
				},
				'text'
			);
	});	
});

$(function() {
	$('.quit').click(function(){
		$('.SettingsView').removeClass('show');
		$('.col-left').addClass('show');
	});
    $( "#settingsTab" ).accordion({
       active: 10,
       collapsible: true,
       autoHeight: false,
       heightStyle: "content"
    });
	$('#settingsTab #cancel').click(function(){
		var Accordion = $(this).closest('.ui-accordion');
		
		$(Accordion).find('input').val('');
		$(Accordion).accordion("option", "active", 10);
		
	});
});
</script>