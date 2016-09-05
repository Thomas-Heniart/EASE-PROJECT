<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.Base64" %>
<%@ page import="java.util.Base64.Encoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div id="loginBody">
<canvas id="demo-canvas" style="position: absolute;"></canvas>
<div class="logo">
<img src="resources/images/Ease_Logo.png"/>
</div>
<% 
	Cookie 	cookie = null;
	Cookie 	cookies[] = request.getCookies();
	String 	fname = "";
	String 	lname = "";
	String 	email = "";
	int		iden = 0;

	if (cookies != null){
	for (int i = 0;i < cookies.length ; i++) {
		cookie = cookies[i];
		if ((cookie.getName()).compareTo("email") == 0){
			email = cookie.getValue();
			if (email.length() > 0)
				iden++;
		}
		else if ((cookie.getName()).compareTo("lname") == 0){
			lname = cookie.getValue();
			if (lname.length() > 0)
				iden++;
		}
		else if ((cookie.getName()).compareTo("fname") == 0){
			fname = cookie.getValue();
			if (fname.length() > 0)
				iden++;
		}
	}
}
%>
<div class="FormsContainer">
<% if (iden == 3){ %>
	<div class="form" style="visibility:hidden;">
	</div>
	<div class="form">
		<div class="savedUser">
		<p>Hello again, <%= new String(Base64.getDecoder().decode(fname), StandardCharsets.UTF_8) %> !</p>
		<span class="input input--minoru">
			<input class="input__field input__field--minoru" id="password" name="password" type="password" id="input-8" placeholder="Password"/>
			<label class="input__label input__label--minoru" for="input-8">
			</label>
		</span>
		<div class="alertDiv">
				<p>Incorrect password !</p>
			</div> 
	    <button id="savedUserButton">Login</button>
	    </div>
	</div>
<%}%>
<div class="form" <% if (iden == 3){ %> style="transform: scale(0.8);" <% }%>>
<form action="connection" method="POST" id="loginForm" role="form">
				<span class="input input--minoru">
					<input class="input__field input__field--minoru" id="email" name="email" type="email" id="input-8" placeholder="Email"/>
					<label class="input__label input__label--minoru" for="input-8">
					</label>
				</span>
				<span class="input input--minoru">
					<input class="input__field input__field--minoru" id="password" name="password" type="password" id="input-8" placeholder="Password"/>
					<label class="input__label input__label--minoru" for="input-8">
					</label>
				</span>

			<div class="alertDiv">
				<p>Incorrect password or email !</p>
			</div> 


    <button class="" id="helloButton">Login</button>
</form>


</div>
</div>
</div>
<script type="text/javascript">
$(document).ready( function() {
	var nbForms = $('.FormsContainer > *').length;

	if (nbForms > 1){
		$('.FormsContainer .form').click(function(){
			var idx = $(this).index();

			if (idx == 2){
				$(this).css('transform', '');
				$('.FormsContainer div:nth-child(2)').css('transform', 'scale(0.8)');
				$('.FormsContainer').append($('.FormsContainer div:nth-child(1)'));
				$('.FormsContainer div:nth-child(1)').append($('.savedUser'));
			}
			else if (idx == 0){
				$(this).css('transform', '');
				$('.FormsContainer div:nth-child(2)').css('transform', 'scale(0.8)');
				$('.FormsContainer').append($(this));
				$('.FormsContainer').append($('.FormsContainer div:nth-child(1)'));
				$('.FormsContainer div:nth-child(2)').append($('.savedUser'));
			}
		});
	}
});	
</script>
<script type="text/javascript">
$(document).ready( function() {
	if ($('.savedUser').length){
		$('.savedUser #password').focus();
	}
		$('.savedUser #password').keyup(function(event){
	    if(event.keyCode == 13){
	    	$('.savedUser #savedUserButton').click();
	    }
	});
	$('.savedUser #savedUserButton').click(function(){
		var parent = $(this).closest('.form');
		var email = getCookie('email');
		var password = parent.find('#password').val();

		email = email.substring(1, email.length - 1);
  		$('#loading').addClass("la-animate");
	    $.post(
                'connection', 
                {
                    email : email, 
                    password : password
                },

                function(data){ 
                	if (data[0] == 's'){
                		$('#loading').removeClass("la-animate");
                    	window.location.replace("index.jsp");
                	}else {
                		$('#loading').removeClass("la-animate");
                		$(parent).find('.alertDiv').addClass('show');
                		$(parent).find('#password').val('');
                	}
                },

                'text' // Nous souhaitons recevoir "Success" ou "Failed", donc on indique text !
	    );
    });
});
</script>

         <script src="js/rAF.js"></script>
        <script src="js/demo-2.js"></script> 
		<script>
		$(document).ready( function() {
			$('.logo').draggable({snap: true});
		});
			(function() {
				if (!String.prototype.trim) {
					(function() {
						// Make sure we trim BOM and NBSP
						var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
						String.prototype.trim = function() {
							return this.replace(rtrim, '');
						};
					})();
				}

				[].slice.call( document.querySelectorAll( 'input.input__field' ) ).forEach( function( inputEl ) {
					// in case the input is already filled..
					if( inputEl.value.trim() !== '' ) {
						classie.add( inputEl.parentNode, 'input--filled' );
					}

					// events:
					inputEl.addEventListener( 'focus', onInputFocus );
					inputEl.addEventListener( 'blur', onInputBlur );
				} );

				function onInputFocus( ev ) {
					classie.add( ev.target.parentNode, 'input--filled' );
				}

				function onInputBlur( ev ) {
					if( ev.target.value.trim() === '' ) {
						classie.remove( ev.target.parentNode, 'input--filled' );
					}
				}
			})();
		</script>
