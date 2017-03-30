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

  if (!($('.custom-select-language .selection.hide').length)){
    $('.custom-sel .selected').attr('data', 'en');
    $(".custom-sel .selection[data='en']").addClass('hide');    
  }
});
