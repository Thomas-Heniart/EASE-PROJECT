<div class="custom-select-language">
	<!-- -->
	<form style="display:none;">
		<select id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="fr" ${language == 'fr' ? 'selected' : ''}>Français</option>
		</select>
	</form>
	<div class="custom-sel">
		<a class="selected" data="${language}">
			<div class="img">
			</div>
			<i class="fa fa-caret-down lightblue" aria-hidden="true"></i>
		</a>
		<a class="selection hidden ${language == 'en' ? 'hide' : ''}" data="en">
			<img src="resources/flags/english.jpg"/>
		</a>
		<a class="selection hidden ${language == 'fr' ? 'hide' : ''}" data="fr">
			<img src="resources/flags/french.png"/>
		</a>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {


  // Show dropdown
  $('.custom-sel .selected').click(function() {
  	if ($('.custom-sel').hasClass('show-sel')){
  		$('.custom-sel').removeClass('show-sel');
  		$('.custom-sel a.selection').addClass('hidden');
  	} else {
  		$('.custom-sel').addClass('show-sel');
  		$('.custom-sel a').removeClass('hidden');
  	}
  });

  $('.custom-sel .selection').click(function(){
  	$('.custom-sel .selected').attr('data', $(this).attr('data'));
  	$('#language').prop("selectedIndex", $(this).index() - 1).change();
  	$('.custom-sel').removeClass('show-sel');
  	$('.custom-sel a.selection').addClass('hidden');
  });

  // Hide dropdown when not focused
  $('.custom-sel').focusout(function() {
  	$('.custom-sel').removeClass('show-sel');
  	$('.custom-sel a:not(:first)').addClass('hidden');
  }).blur(function() {
  	$('.custom-sel').removeClass('show-sel');
  	$('.custom-sel a:not(:first)').addClass('hidden');
  });

});
</script>
