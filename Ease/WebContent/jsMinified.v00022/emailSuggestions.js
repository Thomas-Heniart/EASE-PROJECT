function cleanEmails(){$.get("GetUserEmails",{},function(i){var e=i.split(";");console.log(i),e.length>0&&($(".suggested-emails").empty(),$("#editVerifiedEmails").empty(),appendNewEmail(e,0))},"text")}function appendNewEmail(i,e){if(e<i.length){var s=i[e].split(","),a=s[0];console.log(a),$(".suggested-emails").append("<p class='email-suggestion' email='"+a+"'><span>"+a+"</span></p>"),"0"==s[1]?$("#editVerifiedEmails").append("<div class='emailLine'><input type='email' name='email' oClass='HiddenInput' value='"+a+'\'readonly /> <span class=\'unverifiedEmail\'><span class=\'verify\'>Verified ?</span><span class=\'sendVerificationEmail\'>Send verification email</span></span><div class=\'sk-fading-circle email-loading\'>\t<span>We are sending you an email</span>\t<div class=\'sk-circle1 sk-circle\'></div>\t<div class="sk-circle2 sk-circle"></div>\t<div class="sk-circle3 sk-circle"></div>\t<div class="sk-circle4 sk-circle"></div>\t<div class="sk-circle5 sk-circle"></div>\t<div class="sk-circle6 sk-circle"></div>\t<div class="sk-circle7 sk-circle"></div>\t<div class="sk-circle8 sk-circle"></div>\t<div class="sk-circle9 sk-circle"></div> <div class="sk-circle10 sk-circle"></div>\t<div class="sk-circle11 sk-circle"></div>\t<div class="sk-circle12 sk-circle"></div></div></div>'):"1"==s[1]?$("#editVerifiedEmails").append("<div class='emailLine'><input type='email' value='"+a+"'readonly /> <span class='verifiedEmail'>Verified</span><i class='fa fa-question-circle emailInfo' aria-hidden='true'>\t<span>We are sending you an email</span>\t<span class='info'>Verifying an email enables updates for that email, as well as increases security.</span></i><i class=\"fa fa-trash removeEmail\" aria-hidden=\"true\"></i></div>"):"2"==s[1]&&$("#editVerifiedEmails").append("<div class='emailLine'><input type='email' value='"+a+"'readonly /> <span class='verifiedEmail'>Verified</span><i class='fa fa-question-circle emailInfo' aria-hidden='true'>\t<span>We are sending you an email</span>\t<span class='info'>Verifying an email enables updates for that email, as well as increases security.</span></i></div>"),appendNewEmail(i,e+1)}else console.log("end,new click event"),$(".suggested-emails .email-suggestion").unbind("click"),$(".suggested-emails .email-suggestion").click(function(){$(".login-group-input input[name='login']").val($(this).find("span").html()),$(".login-group-input input[name='login']").change(),$(".suggested-emails").removeClass("show"),$(".login-group-input + input[name='password']").click(),$(".login-group-input + input[name='password']").focus()}),$(".removeEmail").unbind("click"),$(".removeEmail").click(function(){emailToRemove=$(this).parent().parent().find("input").val(),deleteEmailPopup.open(),deleteEmailPopup.setEmail(emailToRemove)}),$(".sendVerificationEmail").unbind("click"),$(".sendVerificationEmail").click(function(){emailToVerify=$(".emailLine").has($(this)).find("input").val(),emailConfirmationForm.setEmail(emailToVerify),$("#SendVerificationEmail button[type='submit']").click()})}$(document).ready(function(){$(".login-group-input input[name='login']").on("focus",function(){$(".suggested-emails").addClass("show")}),$(".login-group-input + input[type='password']").on("focus",function(){$(".suggested-emails").removeClass("show")}),$(".login-group-input .email-suggestions").click(function(){$(".suggested-emails").toggleClass("show")}),$(".suggested-emails .email-suggestion").click(function(){$(".login-group-input input[name='login']").val($(this).find("span").html()),$(".login-group-input input[name='login']").change(),$(".suggested-emails").removeClass("show"),$(".login-group-input + input[name='password']").click(),$(".login-group-input + input[name='password']").focus()})});