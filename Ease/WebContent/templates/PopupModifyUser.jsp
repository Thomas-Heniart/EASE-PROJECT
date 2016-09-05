<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 <div class="md-modal md-effect-15 popup" id="PopupModifyUser">
	<div class="md-content">
		<h3>Modify your informations !</h3>
		<div class="form">
		<form action="editUser" method="POST" id="FormModifyUser" role="form">
				<span class="input input--kuro">
					<input class="input__field input__field--kuro" id="profileColor" name="name" type="text" id="input-8" />
					<label class="input__label input__label--kuro" for="input-8">
						<span class="input__label-content input__label-content--kuro">Your name</span>
					</label>
				</span>
				<span class="input input--kuro">
					<input class="input__field input__field--kuro" id="profileColor" name="email" type="email" id="input-8" />
					<label class="input__label input__label--kuro" for="input-8">
						<span class="input__label-content input__label-content--kuro">Email</span>
					</label>
				</span>
				<span class="input input--kuro">
					<input class="input__field input__field--kuro" id="profileColor" name="newPassword" type="password" id="input-8" />
					<label class="input__label input__label--kuro" for="input-8">
						<span class="input__label-content input__label-content--kuro">New password</span>
					</label>
				</span>
				<span class="input input--kuro">
					<input class="input__field input__field--kuro" id="profileColor" name="confirmNewPassword" type="password" id="input-8" />
					<label class="input__label input__label--kuro" for="input-8">
						<span class="input__label-content input__label-content--kuro">Confirm new password</span>
					</label>
				</span>
				<span class="input input--kuro">
					<input class="input__field input__field--kuro" id="profileColor" name="password" type="password" id="input-8" />
					<label class="input__label input__label--kuro" for="input-8">
						<span class="input__label-content input__label-content--kuro">Current password</span>
					</label>
				</span>
    	<button class="btn btn-default btn-primary btn-group btn-lg">Evaluate</button>
		</form>
</div>
</div>
</div>
 
 <script>
$(document).ready( function() {
	  var form = $('#FormModifyUser');

	  
	  form.submit( function(e) {
		     e.preventDefault(); // on empeche l'envoi du formulaire par le navigateur
				$('#loading').addClass("la-animate");
		     var datas = $(this).serialize();
		     $.ajax({
		          type: 'POST',      // envoi des données en POST
		          url: $(this).attr('action'),     // envoi au fichier défini dans l'attribut action
		          data: datas,     // sélection des champs à envoyer
		          success: function(msg) {
		        	  	if (msg[0] == 's'){
	                    	window.location.replace("/HelloWorld/index.jsp");		        	  		
	    		       		$('#loading').removeClass("la-animate");
		        	  	}
    		       		$('#loading').removeClass("la-animate");
		          }
		     });
	  } );
	} );
</script>
 