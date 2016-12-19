$(document).ready(function() {
	$(".login-group-input input[name='login']").on('focus', function() {
		$(".suggested-emails").addClass("show");
	});
	$(".login-group-input + input[type='password']").on('focus', function() {
		$(".suggested-emails").removeClass("show");
	})
	$('.login-group-input .email-suggestions').click(function() {
		$(".suggested-emails").toggleClass("show");
	});
	$(".suggested-emails .email-suggestion").click(function() {
		$(".login-group-input input[name='login']").val($(this).find("span").html());
		$(".login-group-input input[name='login']").change();
		$(".suggested-emails").removeClass("show");
		$(".login-group-input + input[name='password']").click();
		$(".login-group-input + input[name='password']").focus();
	});
});

function cleanEmails(){
	$.get("GetUserEmails",
			{},
			function(res){
				var emails = res.split(";");
				console.log(res);
				if(emails.length>0) {
					$(".suggested-emails").empty();
					$("#editVerifiedEmails").empty();
					appendNewEmail(emails,0);
				}				
			},
			"text"
		);
}

function appendNewEmail(emails, i){
	if(i < emails.length){
		var emailDatas = emails[i].split(",");
		var email = emailDatas[0];
		console.log(email);
		$(".suggested-emails").append(
				"<p class='email-suggestion' email='" + email + "'><span>"
						+ email + "</span></p>");
		if(emailDatas[1]=="0") {
			$("#editVerifiedEmails").append("<div class='emailLine'>"
					+ "<input type='email' name='email' oClass='HiddenInput' value='" + email + "'readonly />"
					+ " <span class='unverifiedEmail'><span class='verify'>Verified ?</span><span class='sendVerificationEmail'>Send verification email</span></span>"
					+ "<div class='sk-fading-circle email-loading'>"
					+ "	<span>We are sending you an email</span>"
					+ "	<div class='sk-circle1 sk-circle'></div>"
					+ '	<div class="sk-circle2 sk-circle"></div>'
					+ '	<div class="sk-circle3 sk-circle"></div>'
					+ '	<div class="sk-circle4 sk-circle"></div>'
					+ '	<div class="sk-circle5 sk-circle"></div>'
					+ '	<div class="sk-circle6 sk-circle"></div>'
					+ '	<div class="sk-circle7 sk-circle"></div>'
					+ '	<div class="sk-circle8 sk-circle"></div>'
					+ '	<div class="sk-circle9 sk-circle"></div>'
					+ ' <div class="sk-circle10 sk-circle"></div>'
					+ '	<div class="sk-circle11 sk-circle"></div>'
					+ '	<div class="sk-circle12 sk-circle"></div>'
					+ '</div>'
				+ "</div>");
		} else if(emailDatas[1] == "1"){
			$("#editVerifiedEmails").append("<div class='emailLine'>"
					+ "<input type='email' value='" + email + "'readonly />"
					+ " <span class='verifiedEmail'>Verified</span>"
					+ "<i class='fa fa-question-circle emailInfo' aria-hidden='true'>"
					+ "	<span>We are sending you an email</span>"
					+ "	<span class='info'>Verifying an email enables updates for that email, as well as increases security.</span>"
					+ '</i>'
					+ '<i class="fa fa-trash removeEmail" aria-hidden="true"></i>'
					+ '</div>');
		} else if(emailDatas[1] == "2"){
			$("#editVerifiedEmails").append("<div class='emailLine'>"
					+ "<input type='email' value='" + email + "'readonly />"
					+ " <span class='verifiedEmail'>Verified</span>"
					+ "<i class='fa fa-question-circle emailInfo' aria-hidden='true'>"
					+ "	<span>We are sending you an email</span>"
					+ "	<span class='info'>Verifying an email enables updates for that email, as well as increases security.</span>"
					+ '</i>'
					+ '</div>');
		}
		appendNewEmail(emails, i+1);
	} else {
		console.log("end,new click event");
		$(".suggested-emails .email-suggestion").unbind("click");
		$(".suggested-emails .email-suggestion").click(function() {
			$(".login-group-input input[name='login']").val($(this).find("span").html());
			$(".login-group-input input[name='login']").change();
			$(".suggested-emails").removeClass("show");
			$(".login-group-input + input[name='password']").click();
			$(".login-group-input + input[name='password']").focus();
		});
		
		$(".removeEmail").unbind("click");
		$(".removeEmail").click(function() {
			emailToRemove = $(this).parent().parent().find("input").val();
			deleteEmailPopup.open();
			deleteEmailPopup.setEmail(emailToRemove);
			
		});
		
		$(".sendVerificationEmail").unbind("click");
		$(".sendVerificationEmail").click(function() {
			emailToVerify = $(".emailLine").has($(this)).find("input").val();
			emailConfirmationForm.setEmail(emailToVerify);
			$("#SendVerificationEmail button[type='submit']").click();
		});
	}
}