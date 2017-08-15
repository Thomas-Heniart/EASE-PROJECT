<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="tipsHandler">
	<div class="tip display-flex flex_direction_column" id="0" step="1">
		<h3>This is your Space and your Apps</h3>
		<span class="content">You'll be able to add more later. Apps are the quick & ease way to connect to a website. Just click on one and let us take care of everything.</span>
		<div class="buttonHandler">
			<button class="btn" type="submit">Got it</button>
		</div>
	</div>
	<div class="tip" id="1" step="2">
		<h3>If you need create your team here</h3>
		<div class="buttonHandler">
			<button class="btn" type="submit">Got it</button>
		</div>
	</div>
	<div class="tip" id="2" step="3">
		<h3>Find more apps in our catalog</h3>
		<div class="buttonHandler">
			<button class="btn" type="submit">Got it</button>
		</div>
	</div>
</div>
<script type="text/javascript">
	window.addEventListener('load',function(){
		$('#tipsHandler .tip#0').addClass('show');
		$('#tipsHandler .tip#0 button').click(() => {
			$('#tipsHandler .tip#0').removeClass('show');
			$('#tipsHandler .tip#1').addClass('show');
		});
		$('#tipsHandler .tip#1 button').click(() => {
			$('#tipsHandler .tip#1').removeClass('show');
			$('#tipsHandler .tip#2').addClass('show');
		});
		$("#tipsHandler .tip#2 button").click(function(e) {
			var self = $(this).parent().parent();
			var step = self.attr("step");
			postHandler.post('/api/v1/common/TutoDone', {}, function() {
				$('#tipsHandler .tip#2').removeClass('show');
				$('#tipsHandler').remove();
			}, function(retMsg) {
				//succes
				self.remove();
			}, function(retMsg) {
				//error
			}, 'text');
		});
	});
</script>