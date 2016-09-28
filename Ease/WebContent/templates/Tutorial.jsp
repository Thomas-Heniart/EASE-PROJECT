<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="tutorialView"  style="z-index: 500;">
	<div class="bublePopup step0" style="display:none;">
		<div class="popupContent">
			<p style="font-size: 20px;">Welcome <%= ((User)(session.getAttribute("User"))).getFirstName() %> !</p>
		</div>
	</div>
	<div class="bublePopup step1" style="display:none;">
		<div class="popupContent">
			<p>To add your apps (facebook, google drive…), click the red button to access the catalogue !</p>
		</div>
		<div class="caretHelper">
			<i class="fa fa-caret-right" aria-hidden="true"></i>
		</div>
	</div>
	<div class="bublePopup step2" style="display: none;">
		<div class="popupContent" style="float: right;">
			<p>Here are the apps <span>IESEG</span> gives for your university life.</p>
		</div>
		<div class="caretHelper" style="float: right;">
			<i class="fa fa-caret-left" aria-hidden="true"></i>
		</div>
	</div>
	<div class="bublePopup step3" style="display: none;">
		<div class="popupContent" style="float: right;">
			<p>Hey, please confirm</br>your password to keep going :)</p>
		</div>
		<div class="caretHelper" style="float: right;">
			<i class="fa fa-caret-left" aria-hidden="true"></i>
		</div>
	</div>
	<div class="bublePopup step4" style="display: none;">
		<img src="resources/other/Arrow.png" />
	</div>
</div>

<script type="text/javascript">
	var TutorialSteps = 0;

	function enterEditModeTutorial(){
		if (TutorialSteps == 0){
			TutorialSteps++;
		}
		$('.step0').css('display', 'none');
		$('.step1').css('display', 'none');
		$('.step2').css('display', 'none');
		if (TutorialSteps == 1){
			if ($(".emptyAppIndicator").length){
				$('.step3').css({
					'display': 'block',
					'top': $(".emptyAppIndicator").offset().top - $(".siteLinkBox").height() + $(".emptyAppIndicator").height() / 2,
					'left': $(".emptyAppIndicator").offset().left + $(".emptyAppIndicator").width()
				});			
			}else {
				TutorialSteps++;
			}
		} 
		if (TutorialSteps == 2){
			$('step4').css('display', 'block');
		}
	}
	function leaveEditModeTutorial (){
		$('.step3').css('display', 'none');
		$('.step4').css('display', 'none');
		if (TutorialSteps == 3){
			checkForExtension();
		}
	}
	function modifyAppTutorial(){
		if (TutorialSteps == 1){
			TutorialSteps++;
			$('.step3').css('display', 'none');
			$('.step4').css('display', 'block');
		}
	}
	function addAppTutorial (){
		if (TutorialSteps == 2){
			TutorialSteps++;
			$('.step4').css('display', 'none');
		}
	}
	$(document).ready(function(){
		setTimeout(function (){
		$('.step0').css({
			'display': 'block',
			'top': '1%',
			'right': '2%'
		});
		$('.step1').css({
			'display': 'block',
			'right': '2%',
			'top': '40%',
			'transform': 'translateX(-17%)'
		});
		if ($(".ProfileBox[custom='true']").length){
			$('.step2').css({
				'display': 'block',
				'top': $(".ProfileBox[custom='true']").offset().top,
				'left': $(".ProfileBox[custom='true']").offset().left + $(".ProfileBox[custom='true']").width()
			});			
		}
		}, 1000);

	});
</script>