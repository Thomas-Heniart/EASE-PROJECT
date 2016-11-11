<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="md-overlay"></div>
<div oClass="RegisterPopup" class="md-modal md-effect-15 popup" id="RegisterPopup">
	<div class="md-content">
		<h2>Final step</h2>
		<form oClass="RegisterForm" id="RegisterForm" action="letsgo">
			<div>
				<label for="school-email">School email</label>
				<input type="text" style="cursor: initial;" readonly id="school-email" value=<%= request.getParameter("email") %> name="email"/>
			</div>
			<div>
				<input type="hidden" value=<%= request.getParameter("invitationCode") %> name="invitationCode"/>
				<label for="fname">How would you like us to call you ?</label>
				<input oClass="NoEmptyInput" required type="text" autocomplete="off" name="fname" id="fname" placeholder="..." />
				<label for="password">Create your password :</label>
				<div class="advice">
					<p>
						<strong>Advice:</strong> make it memorable, add digits, upper & lower cases. It must be more than 8 characters long.
					</p>
				</div>
				<div class="advice">
					<p>
						<strong>Info:</strong> As we don’t know your Ease password, if you forget it, you will have to reset your account and enter again the passwords of the apps you added.
					</p>
				</div>
				<span class="input input--minoru password">
					<input oClass="NoEmptyInput" required autocomplete="off" class="input__field input__field--minoru" name="password" type="password" id="input-8" placeholder="Password"/>
					<label class="input__label input__label--minoru" for="input-8"></label>
					<div class="showPassDiv">
						<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
					</div>
					<img src="./resources/icons/error.png" id="validatorPassword" style="display:none;"/>
				</span>
				<span class="input input--minoru password">
					<input oClass="NoEmptyInput" required autocomplete="off" class="input__field input__field--minoru" name="confirmPassword" type="password" id="input-9" placeholder="Confirm password"/>
					<label class="input__label input__label--minoru" for="input-9"></label>
					<div class="showPassDiv">
						<i class="fa fa-eye centeredItem" aria-hidden="true"></i>
					</div>
					<img src="./resources/icons/error.png" id="validatorConfirmPass" style="display:none;"/>
				</span>
			</div>
			<div class="terms">
				<a href="http://www.ease-app.co/privacy" target="_blank">Here is a link to our terms and conditions</a>
				<label for="termsAndConditions">I accept the terms and conditions</label>
				<input oClass="CheckboxInput" type="checkbox" required id="termsAndConditions" name="termsAndConditions" />
			</div>
			<button type=submit>GO</button>
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
			<div class="successHelper centeredItem"><p></p></div>
			<div class="errorHelper centeredItem"><p></p></div>
		</form>
	</div>
</div>
<script type="text/javascript">
	var popupRegister = new Popup["RegisterPopup"]($("#RegisterPopup"));
	$("input[name='password']").keyup(function(e){
		$("#validatorPassword").css("display","inline-block");
		if($("input[name='password']").val().length < 8) 
			$("#validatorPassword").attr("src","./resources/icons/error.png");
		else
			$("#validatorPassword").attr("src","./resources/icons/success.png");
		if($("input[name='password']").val().length < 8 || $("input[name='confirmPassword']").val() != $("input[name='password']").val()) 
			$("#validatorConfirmPass").attr("src","./resources/icons/error.png");
		else
			$("#validatorConfirmPass").attr("src","./resources/icons/success.png");
	});
	
	$("input[name='confirmPassword']").keyup(function(e){
		$("#validatorConfirmPass").css("display","inline-block");
		if($("input[name='password']").val().length < 8 || $("input[name='confirmPassword']").val() != $("input[name='password']").val()) 
			$("#validatorConfirmPass").attr("src","./resources/icons/error.png");
		else
			$("#validatorConfirmPass").attr("src","./resources/icons/success.png");
	});
</script>