<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="md-modal md-effect-15 popup" id="PopupDeleteProfile">
	<div class="md-content">
		<p class="title">Please confirm profile deletion <span></span></p>
		<input name="index" type="hidden" name="index" id="index"/>
		<input id="password" name="password" type="password" placeholder="Password"/>
		<div class="alertDiv">
			<p>Incorrect password !</p>
		</div>
		<div class="buttonSet">
    		<button id="accept" class="btn btn-default btn-primary btn-group btn-lg">Je supprime</button>
    		<button id="close" class="btn btn-default btn-primary btn-group btn-lg">Annuler</button>
    	</div>
</div>
</div>

 <script>
$(document).ready( function() {
	$('#PopupDeleteProfile').find('#close').click(function(){
		$('#PopupDeleteProfile').removeClass('md-show');
	});
	$('#PopupDeleteProfile').find('#password').keyup(function(event){
	    if(event.keyCode == 13){
	        $("#PopupDeleteProfile .md-content .buttonSet #accept").click();
	    }
	});

	$('#PopupDeleteProfile').find('#accept').click(function(){
		var parent = $(this).closest('.popup');
		
     	$('#loading').addClass("la-animate");
     	$(parent).find('.alertDiv').removeClass('show');
		$.post(
			'deleteProfile',
			{
				index: $(parent).find('#index').val(),
				password: $(parent).find('#password').val()
			},
	        function(msg) {
	       		$('#loading').removeClass("la-animate");
	      	  	if (msg[0] == 's'){
                  	window.location.replace("index.jsp");		        	  		
        	  	}
	      	  	else {
	      	  	if (data[0] != 'e'){
					document.location.reload(true);
				} else {
					$(parent).find('.alertDiv').addClass('show');
	      	  		$(parent).find('#password').val('');
	        	  	showAlertPopup(null, true);
				}		
	      	  	}
	          },
	         'text'
		);
	});
});

$(document).ready( function() {
	  var form = $('#FormDeleteProfile');

	  
	  form.submit( function(e) {
		     e.preventDefault(); // on empeche l'envoi du formulaire par le navigateur
				$('#loading').addClass("la-animate");
		     var datas = $(this).serialize();
		     $.ajax({
		          type: 'POST',      // envoi des données en POST
		          url: $(this).attr('action'),     // envoi au fichier défini dans l'attribut action
		          data: datas,     // sélection des champs à envoyer
		          success: function(msg) {
  		       		$('#loading').removeClass("la-animate");
		        	  	if (msg[0] == 's'){
	                    	window.location.replace("index.jsp");		        	  		
		        	  	}
		        	  	else {
		        	  		if (data[0] != 'e'){
		    					document.location.reload(true);
		    				}	        	  		
		        	  	}
		          }
		     });
	  } );
	} );
</script>
 