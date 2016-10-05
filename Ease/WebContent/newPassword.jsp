<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Reset your password</title>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
	<meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
	<link rel="chrome-webstore-item" href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm">
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
	<link rel="stylesheet" href="css/default_style.css" />
	<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
	<script src="js/snap.svg-min.js"></script>
	<script src="js/modalEffects.js"></script>
	<script src="js/selectFx.js"></script>
	<link rel="stylesheet" type="text/css" href="component.css" />

</head>
<body id="lostPasswordBody">
	<div class="logo">
		<img src="resources/images/Ease_Logo.png"/>
	</div>
	<h1>New password</h1>
	<div class="lostPasswordBlock" id="lostPassword">
		<div id="goBack">
  				<i class="fa fa-chevron-left" aria-hidden="true"></i> Go back
		</div>
		<div id="security" style="text-align: center;" class="show">
			<p>Set you new password carefully</p>
		</div>
		<form action="ResetUser" id="lostPasswordForm" style="text-align: center;" autocomplete="off">
			<span class="input input--minoru show">
				<input required style="text-align: center;" class="input__field input__field--minoru" name="password" type="password" id="input-8" placeholder="New password" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');"/>
				<label class="input__label input__label--minoru" for="input-8">
				</label>
				
			</span>
			<span class="input input--minoru confirm-password show">
					<input required style="text-align: center;" class="input__field input__field--minoru" name="confirmPassword" type="password" id="input-8" placeholder="Confirm password" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');"/>
					<label class="input__label input__label--minoru" for="input-8">
					</label>
			</span>
			<input type="hidden" value="<%= request.getParameter("linkCode") %>" name="linkCode"/>
		</form>
		<div class="alertDiv">
			<p></p>
		</div>
		<div class="custom-button show">
			<button type="submit" form="lostPasswordForm" value="Submit"><span>Go !</span></button>
			<div class="loadHelper centeredItem">
				<div class="sk-fading-circle">
				  <div class="sk-circle1 sk-circle"></div>
				  <div class="sk-circle2 sk-circle"></div>
				  <div class="sk-circle3 sk-circle"></div>
				  <div class="sk-circle4 sk-circle"></div>
				  <div class="sk-circle5 sk-circle"></div>
				  <div class="sk-circle6 sk-circle"></div>
				  <div class="sk-circle7 sk-circle"></div>
				  <div class="sk-circle8 sk-circle"></div>
				  <div class="sk-circle9 sk-circle"></div>
				  <div class="sk-circle10 sk-circle"></div>
				  <div class="sk-circle11 sk-circle"></div>
				  <div class="sk-circle12 sk-circle"></div>
				</div>
			</div>
			<div class="successHelper centeredItem"><p>Your new password has been set</p></div>
		</div>
	</div>
	<%@ include file="templates/ChatButton.jsp" %>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$("#goBack").click(function() {
			window.location = 'index.jsp';
		});
		
		$('#lostPasswordForm').submit(function(e){
			e.preventDefault();
			var password = $("#lostPasswordForm input[name='password']").val();
			var confirmPassword = $("#lostPasswordForm input[name='confirmPassword']").val();
			var linkCode = $("#lostPasswordForm input[name='linkCode']").val();
			$('.alertDiv').removeClass('show');
			var button = $('.lostPasswordBlock .custom-button');
			button.addClass('loading');
			$("#lostPasswordForm .input").removeClass("show");
			$.post(
				$('#lostPasswordForm').attr('action'),
				{
					password: password,
					confirmPassword: confirmPassword,
					linkCode: linkCode
				},
				function(data){
					if (data[0] == 's'){
						button.removeClass('loading');
						button.addClass('success');
						setTimeout(function(){
							window.location = "index.jsp";
						}, 3000);
					} else {
						var str = data.substring(7, data.length);
						$('.alertDiv').css('color', 'red');
						$('.alertDiv').text(str);
						$('.alertDiv').addClass('show');
						$("#lostPasswordForm .input").addClass("show");
						button.removeClass('loading');
					}
				},
				'text'
				);
		});
	});
</script>
</html>