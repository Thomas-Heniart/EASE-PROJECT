<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

		<div class="md-modal md-effect-15 popup" id="addProfilePopup">
			<div class="md-content">
				<h3>Add new profile !</h3>
<div class="form">
<form action="addProfile" method="POST" id="addProfileForm" role="form">
				<span class="input input--kuro">
					<input class="input__field input__field--kuro" id="profilName" name="name" type="text" id="input-8" />
					<label class="input__label input__label--kuro" for="input-8">
						<span class="input__label-content input__label-content--kuro">Profile name</span>
					</label>
				</span>
				<span class="input input--kuro">
					<input class="input__field input__field--kuro" id="email" name="email" type="email" id="input-8" />
					<label class="input__label input__label--kuro" for="input-8">
						<span class="input__label-content input__label-content--kuro">Email</span>
					</label>
				</span>
				<div class="colorChoose">
						<select class="cs-select cs-skin-boxes fs-anim-lower" name="color">
								<option value="" disabled selected>Pick a color</option>
								<option value="#588c75" data-class="color-588c75">#588c75</option>
								<option value="#b0c47f" data-class="color-b0c47f">#b0c47f</option>
								<option value="#f3e395" data-class="color-f3e395">#f3e395</option>
								<option value="#f3ae73" data-class="color-f3ae73">#f3ae73</option>
								<option value="#da645a" data-class="color-da645a">#da645a</option>
								<option value="#79a38f" data-class="color-79a38f">#79a38f</option>
								<option value="#c1d099" data-class="color-c1d099">#c1d099</option>
								<option value="#f5eaaa" data-class="color-f5eaaa">#f5eaaa</option>
								<option value="#f5be8f" data-class="color-f5be8f">#f5be8f</option>
								<option value="#e1837b" data-class="color-e1837b">#e1837b</option>
								<option value="#9bbaab" data-class="color-9bbaab">#9bbaab</option>
								<option value="#d1dcb2" data-class="color-d1dcb2">#d1dcb2</option>
								<option value="#f9eec0" data-class="color-f9eec0">#f9eec0</option>
								<option value="#f7cda9" data-class="color-f7cda9">#f7cda9</option>
								<option value="#e8a19b" data-class="color-e8a19b">#e8a19b</option>
								<option value="#bdd1c8" data-class="color-bdd1c8">#bdd1c8</option>
								<option value="#e1e7cd" data-class="color-e1e7cd">#e1e7cd</option>
								<option value="#faf4d4" data-class="color-faf4d4">#faf4d4</option>
								<option value="#fbdfc9" data-class="color-fbdfc9">#fbdfc9</option>
								<option value="#f1c1bd" data-class="color-f1c1bd">#f1c1bd</option>
							</select>
				</div>
    <button class="btn btn-default btn-primary btn-group btn-lg">Evaluate</button>
</form>


</div>
			</div>
		</div>

<script>

$(document).ready( function() {
	  var form = $('#addProfileForm');

	  
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
		        	  	showAlertPopup(null, true);
		          }
		     });
	  } );
	} );
</script>
