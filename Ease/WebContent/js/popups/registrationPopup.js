var signUpPopup = function(elem){
	var self = this;
	this.handler = $(elem).closest('.popupHandler');
	this.qRoot = $(elem);

	this.openRegistration = function(){
		self.qRoot.find('#2').addClass('show');
		self.qRoot.find('#1').removeClass('show');					
		self.handler.addClass('myshow');
		$('body').css('overflow', 'hidden');
	};
	this.open = function(){
		self.handler.addClass('myshow');
		$('body').css('overflow', 'hidden');
	};
	this.close = function(){
		self.handler.removeClass('myshow');
		$('body').css('overflow', '');
	};
	this.reset = function(){
		self.qRoot.find('#1').addClass('show');
		self.qRoot.find('#2').removeClass('show');
		self.qRoot.find('.alert-message').removeClass('show');
		self.qRoot.find('button').removeClass('not-show');	
	};
	$(document).click(function(e){
		if ($(e.target).hasClass('popupHandler')){
			self.close();
			self.reset();
			setTimeout(function(){
				$(e.target).css('display', '');
			}, 100);
		}
	});
	this.qRoot.find('#1 form').submit(function(e){
		e.preventDefault();
		easeTracker.trackEvent("HomepageSignUp1");
		var emailVal = $(this).find("input[name='email']").val();
		var name = $(this).find("input[name='name']").val();
		var loading = $(this).find('.loading');
		var submitButton = $(this).find(".submitButton");
		var alertMessage = $(this).find(".alert-message");

		if (!emailVal.length || !name.length)
			return;

		loading.addClass('show');
		submitButton.addClass('not-show');
		postHandler.post($(this).attr('action'),
		{
			email : emailVal,
			name : name
		},
		function(){
			loading.removeClass('show');
		},
		function(retMsg) {
			alertMessage.text(retMsg.substring(2, retMsg.length));
			alertMessage.css('color', '#24d666');
			alertMessage.addClass('show');
			if (retMsg[0] == '1'){
				setTimeout(function(){
					alertMessage.removeClass('show');
					submitButton.removeClass('not-show');
				}, 7000);
			} else if (retMsg[0] == '2'){
				self.qRoot.find("#2 input[name='fname']").val(name);
				self.qRoot.find("#2 input[name='email']").val(emailVal);
				self.openRegistration();
			}
		},
		function(retMsg) {
			alertMessage.text(retMsg);
			alertMessage.css('color', '#ec555b')
			alertMessage.addClass('show');
			setTimeout(function(){
				alertMessage.removeClass('show');
				submitButton.removeClass('not-show');
			}, 3000);
		},
		'text'
		);
	});
	this.qRoot.find('#2 form').submit(function(e){
		e.preventDefault();
		var self = $(this);
		var name = $(this).find("input[name='fname']").val();
		var email = $(this).find("input[name='email']").val();
		var code = $(this).find("input[name='invitationCode']").val();
		var password = $(this).find("input[name='password']").val();
		var confirmPassword = $(this).find("input[name='confirmPassword']").val();

		var loading = $(this).find('.loading');
		var submitButton = $(this).find(".submitButton");
		var alertMessage = $(this).find(".alert-message");

		if (!name.length || !email.length || !code.length || !(password == confirmPassword))
			return;

		loading.addClass('show');
		submitButton.addClass('not-show');
		postHandler.post($(this).attr('action'),
		{
			email : email,
			fname : name,
			invitationCode : code,
			password : password,
			confirmPassword : confirmPassword,
			lname : "unknown"
		},
		function(){
			loading.removeClass('show');
		},
		function(retMsg) {
			alertMessage.text(retMsg);
			alertMessage.css('color', '#24d666');
			alertMessage.addClass('show');
			easeTracker.setUserId(email);
			easeTracker.trackEvent("HomepageSignUp2");
			easeTracker.trackEvent("Connect");
			setTimeout(function() {
				window.location = "/";
			}, 750);
		}, 
		function(retMsg) {
			alertMessage.text(retMsg);
			alertMessage.css('color', '#ec555b');
			alertMessage.addClass('show');
			setTimeout(function(){
				alertMessage.removeClass('show');
				submitButton.removeClass('not-show');
			}, 3000);
		},
		'text'
		);
	});
	$("input[name='password']").keyup(function(e){
		$("#validatorPassword").css("display","inline-block");
		if($("input[name='password']").val().length < 8) 
			$("#validatorPassword").removeClass('valid');
		else
			$("#validatorPassword").addClass('valid');
		if($("input[name='password']").val().length < 8 || $("input[name='confirmPassword']").val() != $("input[name='password']").val())
			$("#validatorConfirmPass").removeClass('valid');
		else
			$("#validatorConfirmPass").addClass('valid');
	});
	$("input[name='confirmPassword']").keyup(function(e){
		$("#validatorConfirmPass").css("display","inline-block");
		if($("input[name='password']").val().length < 8 || $("input[name='confirmPassword']").val() != $("input[name='password']").val())
			$("#validatorConfirmPass").removeClass('valid');
		else
			$("#validatorConfirmPass").addClass('valid');
	});
	$("#2 input[name='password']").complexify({
		strengthScaleFactor: 0.7
	}, function(valid, complexity){
		$(".progress .progress-bar").css('width', complexity + "%");
		if (complexity < 20){
			$(".progress .progress-bar").css('background-color', '#e4543b');				
		} else if (complexity < 40){
			$(".progress .progress-bar").css('background-color', '#e07333');
		}else if (complexity < 60){
			$(".progress .progress-bar").css('background-color', '#ead94a');
		}else if (complexity < 80){
			$(".progress .progress-bar").css('background-color', '#ddf159');
		}else {
			$(".progress .progress-bar").css('background-color', '#b0df33');
		}
	});
};
var easeSignUpPopup = null;
$(document).ready(function(){
	easeSignUpPopup = new signUpPopup($('#signUpPopup'));
});
