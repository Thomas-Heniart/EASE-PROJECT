$(document).ready(function(){
		$('#registrationForm').submit(function(e){
			e.preventDefault();
			$('.alertDiv').removeClass('show');
			var email = $('#helpInformations').attr('email');
			var invitationCode = $('#helpInformations').attr('code');
			var fname = $("#registrationForm input[name='fname']").val();
			var lname = $("#registrationForm input[name='lname']").val();
			var password = $("#registrationForm input[name='password']").val();
			var confirmPassword = $("#registrationForm input[name='confirmPassword']").val();
			var button = $(".registrationBlock .custom-button");
			if (!$('#registrationForm').find('.term .check').prop("checked")){
				$('.alertDiv').text("Please accept terms and conditions");
				$('.alertDiv').addClass('show');
			} else {
			
			button.addClass('loading');
			postHandler.post(
				$('#registrationForm').attr('action'),
				{
					email: email,
					invitationCode: invitationCode,
					fname: fname,
					lname: lname,
					password: password,
					confirmPassword: confirmPassword
				},
				function(){},
				function(retMsg){
					button.removeClass('loading');
					button.addClass('success');
					postHandler.post(
						'connection', 
						{
							email : email, 
							password : password
						},
						function(){
							easeTracker.trackEvent("Connect");
							window.location.replace("index.jsp");
						},
						function(retMsg){},
						function(retMsg){},
    			        'text'
    			     );
				},
				function(retMsg){
					button.removeClass('loading');
					var str = retMsg;
					$('.alertDiv').text(str);
					$('.alertDiv').addClass('show');
				},
				'text'
			);
			}
		});
	});