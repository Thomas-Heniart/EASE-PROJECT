<div id="alertPopup">
	<div class="popup-content">
	<p class="msg"><i class="fa fa-smile-o"></i> <span>Cette action comporte une erreur, veuillez réessayer !</span></p>
	</div>
</div>

<script>
function showAlertPopup(msg, error){
	var popup = $('#alertPopup');
	var valid = 'fa-smile-o';
	var unvalid = 'fa-meh-o';
	var defaultErrorMsg = 'Cette action comporte une erreur. Veuillez reessayer.';
	var defaultSuccesMsg = 'Succes !';
	var ErrorStyle = 'background-color:#fbeeed;border: 1px solid #f7d8dd;color: #d9534f;';	
	var SuccessStyle = 'background-color:#f1f9f7; border: 1px solid #e0f1e9; color:	#1d9d74;';	

	$(popup).find('i').removeClass(valid);
	$(popup).find('i').removeClass(unvalid);
	if (error == true){
		$(popup).find('i').addClass(unvalid);
		if (msg == '' || msg == null)
			msg = defaultErrorMsg;
		msg = 'Oops! ' + msg;
		popup.attr('style', ErrorStyle);
	}else {
		$(popup).find('i').addClass(valid);		
		if (msg == '' || msg == null)
			msg = defaultSuccesMsg;
		popup.attr('style', SuccessStyle);
	}
	popup.removeClass('show');
	popup.find('span').text(msg);

	if (error == true)
		msg = "Oops! " + msg;
	
	popup.addClass('show');
	
	setTimeout(function(){
		popup.removeClass('show');
	}, 2900);
}
</script>