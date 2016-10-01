<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Ease registration</title>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
	<meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
	<link rel="chrome-webstore-item" href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm">
	<meta name="description"
	content="Activate your EASE account." />
	<meta property="og:image" content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
	<link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
	<link rel="stylesheet" href="css/default_style.css" />
	<link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro' rel='stylesheet' type='textcss' />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />




	<link rel="stylesheet" href="css/lib/vicons-font/vicons-font.css">
	<link rel="stylesheet" href="css/lib/vicons-font/buttons.css">
	<link rel="stylesheet" href="css/lib/textInputs/set1.css">
	<link rel="stylesheet" href="css/lib/borderLoading/component.css">
	<link rel="stylesheet" href="css/lib/niftyPopupWindow/component.css">
	<link rel="stylesheet" href="css/lib/ColorSelect/cs-select.css">
	<link rel="stylesheet" href="css/lib/ColorSelect/cs-skin-boxes.css">
	<link rel="manifest" href="manifest.json">
	
	<script src="js/classie.js"></script>
	<script src="js/owl.carousel.js"></script>
	<script src="js/basic-utils.js" ></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"> </script>
	<script src="js/jquery.mousewheel.min.js"></script>

	<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
	<script src="js/snap.svg-min.js"></script>
	<script src="js/modalEffects.js"></script>
	<script src="js/selectFx.js"></script>
	<link rel="stylesheet" type="text/css" href="component.css" />

</head>
<% String activity = request.getParameter("activity");
	if (activity == null){
		activity = "";
	}
%> 

<body id="invitationBody">
	<div class="logo">
		<img src="resources/images/Ease_Logo.png"/>
	</div>
	<h1>Welcome</h1>
	<div class="registrationBlock" id="checkInvitation">
		<form action="letsgo" id="registrationForm" style="text-align: center;">
			<div class="custom-info" style="margin-bottom: 20px;font-size: 17px;">
				<p>Please enter your <%= activity %> email to receive your activation link.</p>
			</div>
			<div class="custom-info confirmDiv" style="margin-bottom: 20px;margin-top:20px;font-size: 17px;display: none;">
				<p>Thank you !</p>
			</div>
			<span class="input input--minoru">
				<div class="confirmDiv confirmCheckCircle" style="display: none;"><i class="fa fa-check-circle" aria-hidden="true"></i></div>
				<input style="text-align: center;" class="input__field input__field--minoru" name="email" type="email" id="input-8" placeholder="Email"/>
				<label class="input__label input__label--minoru" for="input-8">
				</label>
			</span>
		</form>
		<div class="alertDiv">
			<p></p>
		</div>
		<div class="confirmDiv confirmBox" style="display: none;">
			<p>Take a look in your mail box ;)</p>
		</div>
		<div class="custom-button">
			<button type="submit" form="registrationForm" value="Submit">Goo !</button>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$('#registrationForm').submit(function(e){
			e.preventDefault();
			var email = $("#registrationForm input[name='email']").val();
			$('.alertDiv').removeClass('show');
			$.post(
				$('#registrationForm').attr('action'),
				{
					email: email
				},
				function(data){
					if (data[0] == 's'){
						$('#checkInvitation').find('.title, .custom-info, .custom-button').css('display', 'none');
						$('#checkInvitation').find('.confirmDiv').css('display', 'block');						
					} else {
						var str = data.substring(7, data.length);
						$('.alertDiv').css('color', 'red');
						$('.alertDiv').text(str);
						$('.alertDiv').addClass('show');
					}
				},
				'text'
				);
		});
	});
</script>
</html>