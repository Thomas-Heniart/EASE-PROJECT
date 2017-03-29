$('#enterpriseContactForm').submit(function(e){
	var form = $(this);
	var errorHandler = form.find('.responseHandler');

	errorHandler.addClass('hidden');
	e.preventDefault();
	form.find('button[type=submit]').prop('disabled', true);
	postHandler.post(
		form.attr('action'),
		form.serialize(),
		function(){
			form.find('button[type=submit]').prop('disabled', false);
			errorHandler.removeClass('hidden');
		},
		function(data){
			errorHandler.css('color', '#45C997');
			errorHandler.find('p').text(data);
			var teamSize = $("#enterpriseContactForm input[name='teamSize']").val();
			if (teamSize == null || teamSize === "")
				teamSize = "0";
			easeTracker.trackEvent("PricingContactSubmit", {"teamSize": teamSize});
		},
		function(data){
			errorHandler.css('color', '#ec555b');
			errorHandler.find('p').text(data);
		}
	);
});
