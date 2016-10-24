$(document).ready(function() {
			$('#settingsTab #modifyNameForm #validate').click(
					function() {
						var parent = $(this).closest('#modifyNameForm');
						var lname = $(parent).find('#lastName').val();
						var fname = $(parent).find('#firstName').val();

						$('#loading').addClass('la-animate');
						postHandler.post('editUserName', {
							fname : fname,
							lname : lname
						}, function() {
							$('#loading').removeClass('la-animate');
						}, function(retMsg) {
							showAlertPopup(
									'Modifications successfully applied !',
									false);
							$(parent).closest('#settingsTab').find(
									'#nameSection .directInfo').text(
									fname + ' ' + lname);
							$('#menu .menu__label span').text(fname);
						}, function(retMsg) {
							showAlertPopup(retMsg, true);
						}, 'text');
					});

			$('#settingsTab #modifyEmailForm #validate').click(
					function() {
						var parent = $(this).closest('#modifyEmailForm');
						var email = $(parent).find('#email').val();
						var password = $(parent).find('#password').val();

						$('#loading').addClass('la-animate');
						postHandler.post('editUserEmail', {
							email : email,
							password : password
						}, function() {
							$('#loading').removeClass('la-animate');
							$(parent).find('#email').val('');
							$(parent).find('#password').val('');
						}, function(retMsg) {
							showAlertPopup(
									'Modifications successfully applied !',
									false);
							$(parent).closest('#settingsTab').find(
									'#contactSection .directInfo').text(email);
						}, function(retMsg) {
							showAlertPopup(retMsg, true);
						}, 'text');
					});

			$('#settingsTab #modifyPasswordForm #validate').click(function() {
				var parent = $(this).closest('#modifyPasswordForm');
				var newPass = $(parent).find('#newPassword').val();
				var confNewPass = $(parent).find('#confirmNewPassword').val();
				var currentPass = $(parent).find('#currentPassword').val();
				var alert = $(parent).find('.alertDiv');

				$(alert).removeClass('show');
				$('#loading').addClass('la-animate');
				postHandler.post('editUserPassword', {
					password : newPass,
					confirmPassword : confNewPass,
					oldPassword : currentPass
				}, function() {
					$('#loading').removeClass('la-animate');
					$(parent).find('#newPassword').val('');
					$(parent).find('#confirmNewPassword').val('');
					$(parent).find('#currentPassword').val('');
				}, function(retMsg) {
					$(parent).find('.alertDiv').removeClass('show');
					showAlertPopup('Your password was successfully changed')
				}, function(retMsg) {
					$(alert).find('p').text(retMsg);
					$(parent).find('.alertDiv').addClass('show');
				}, 'text');
			});
		});

$(function() {
	$('.quit').click(function() {
		$('.SettingsView').removeClass('show');
		$('.col-left').addClass('show');
	});
	$("#settingsTab").accordion({
		active : 10,
		collapsible : true,
		autoHeight : false,
		heightStyle : "content"
	});
	$("#settingsTab [oClass='CloseButton']").click(function() {
		var Accordion = $(this).closest('.ui-accordion');

		$(Accordion).find('input').val('');
		$(Accordion).accordion("option", "active", 10);

	});
});