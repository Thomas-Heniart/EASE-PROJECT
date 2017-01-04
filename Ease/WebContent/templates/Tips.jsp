<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="tipsHandler">
	<div class="tip" id="0" step="click_on_app">
		<div class="leftIcon">
			<img class="icon" src="resources/emojis/finger.png" />
		</div>
		<div class="rightText">
			<p>You can click on any app to access it !</p>
			<button class="btn" type="submit">Got it!</button>
		</div>
	</div>
	<div class="tip xCentered" id="1" step="drag_and_drop">
		<div class="leftIcon">
			<img class="icon" src="resources/emojis/handMove.png" />
		</div>
		<div class="rightText">
			<p>You can move apps to organise them !</p>
			<button class="btn" type="submit">Got it!</button>
		</div>
	</div>
	<div class="tip" id="2" step="saw_group">
		<div class="leftIcon">
			<img class="icon" src="resources/emojis/schoolCap.png" />
		</div>
		<div class="rightText">
			<p>Above are the apps your school gives you to study !</p>
			<button class="btn" type="submit">Got it!</button>
		</div>
	</div>
	
	<div class="tip" id="3" step="open_catalog">
		<div class="leftIcon">
			<img class="icon" src="resources/emojis/world.png" />
		</div>
		<div class="rightText">
			<p>You can find more apps in the catalog.</p>
			<button class="btn" type="submit">Got it!</button>
		</div>
	</div>
	<div class="tip xCentered catalogNeed" id="4" step="add_an_app">
		<div class="leftIcon">
			<img class="icon" src="resources/emojis/lightning.png" />
		</div>
		<div class="rightText">
			<p>To add apps, drag & drop them from the catalog.</p>
			<button class="btn" type="submit">Got it!</button>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		var headerHeight = $('.header').height();

		$('#tipsHandler #0').css({
			'left': $('.ProfileBox').offset().left - $('#tipsHandler #0').outerWidth(true),
			'top': $('.ProfileBox').offset().top - headerHeight
		});
		$('#tipsHandler #1').css({
			'left': $('.ProfileBox').offset().left + $('.ProfileBox').outerWidth(true) / 2,
			'top': $('.ProfileBox').offset().top + $('.ProfileBox').height() - headerHeight
		});
		
		if ($(".ProfileBox[custom='true']").length > 0){
			$('#tipsHandler #2').css({
				'left': $(".ProfileBox[custom='true']").offset().left - $('#tipsHandler #2').outerWidth(true),
				'top': $(".ProfileBox[custom='true']").offset().top + $('#tipsHandler #2').height() - headerHeight
			});
		}
		
		$('#tipsHandler #3').css({
			'left': $('.MenuButtonSet').offset().left - $('#tipsHandler #3').outerWidth(true),
			'top': $('.MenuButtonSet').offset().top - headerHeight
		});
		
		<c:choose>
			<c:when test='${user.clickOnAppDone() eq false}'>
				$("#tipsHandler #0").addClass("show");
			</c:when>
			<c:when test='${user.clickOnAppDone() && (user.moveAppDone() eq false)}'>
				$("#tipsHandler #1").addClass("show");
			</c:when>
			<c:when test='${user.moveAppDone() && (user.sawGroupProfile() eq false)}'>
				$("#tipsHandler #2").addClass("show");
			</c:when>
			<c:when test='${user.sawGroupProfile() && (user.openCatalogDone() eq false)}'>
				$('#tipsHandler #3').addClass('show');
			</c:when>
		</c:choose>
		
		$('#tipsHandler #0 button').click(function(){
			$('#tipsHandler #1').css({
				'left': $('.ProfileBox').offset().left + $('.ProfileBox').outerWidth(true) / 2,
				'top': $('.ProfileBox').offset().top + $('.ProfileBox').height() - headerHeight
			});
			$('#tipsHandler #0').removeClass('show');
			$('#tipsHandler #1').addClass('show');
		});
		$('#tipsHandler #1 button').click(function(){
			if ($(".ProfileBox[custom='true']").length == 0){
				$('#tipsHandler #3').css({
					'left': $('.MenuButtonSet').offset().left - $('#tipsHandler #3').outerWidth(true),
					'top': $('.MenuButtonSet').offset().top - headerHeight
				});
				$('#tipsHandler #1').removeClass('show');
				$('#tipsHandler #3').addClass('show');
			} else{
				$('#tipsHandler #2').css({
					'left': $(".ProfileBox[custom='true']").offset().left - $('#tipsHandler #2').outerWidth(true),
					'top': $(".ProfileBox[custom='true']").offset().top + $('#tipsHandler #2').height() - headerHeight
				});
				$('#tipsHandler #1').removeClass('show');
				$("#tipsHandler #2").addClass('show');	
			}
		});
		$('#tipsHandler #2 button').click(function(){
			$('#tipsHandler #3').css({
				'left': $('.MenuButtonSet').offset().left - $('#tipsHandler #3').outerWidth(true),
				'top': $('.MenuButtonSet').offset().top - headerHeight
			});
			$('#tipsHandler #2').removeClass('show');
			$('#tipsHandler #3').addClass('show');		
		});
		var clickOnCatalog = false;
		$('#tipsHandler #3 button').click(function(){
			$('#tipsHandler #4').css({
				'left': '50%',
				'top': '2%'
			});
			if (!clickOnCatalog) {
				clickOnCatalog = true;
				enterEditMode();
			}
				
			$('#tipsHandler #3').removeClass('show');
			$('#tipsHandler #4').addClass('show');
		});
		$('#tipsHandler #4 button').click(function(){
			$('#tipsHandler #4').removeClass('show');
		});
		
		$("#tipsHandler .tip button").click(function(e) {
			var self = $(this).parent().parent();
			var step = self.attr("step");
			postHandler.post('TutoStep', {
				"tutoStep" : step
			}, function() {
				//always
			}, function(retMsg) {
				//succes
				var amplitudeStep = "";
				step.split("_").forEach(function(element) {
					amplitudeStep += (element.charAt(0).toUpperCase() + element.slice(1));
				});
				easeTracker.trackEvent(amplitudeStep);
				self.remove();
			}, function(retMsg) {
				//error
			}, 'text');
		});
	});
</script>