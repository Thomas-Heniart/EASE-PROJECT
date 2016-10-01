	var TutorialSteps = 0;

	function enterEditModeTutorial(){
		if ($('#tutorialView').length == 0)
			return;
		if (TutorialSteps == 0){
			TutorialSteps++;
		}
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
			$('.step4').css('display', 'block');
		}
	}
	function leaveEditModeTutorial (){
		if ($('#tutorialView').length == 0)
			return;
		$('.step3').css('display', 'none');
		$('.step4').css('display', 'none');
	}
	function modifyAppTutorial(){
		if ($('#tutorialView').length == 0)
			return;
		if (TutorialSteps == 1){
			TutorialSteps++;
			$('.step3').css('display', 'none');
			$('.step4').css('display', 'block');
		}
	}
	function addAppTutorial (){
		if ($('#tutorialView').length == 0)
			return;
		if (TutorialSteps == 2){
			TutorialSteps++;
			$('.step4').css('display', 'none');
		}
	}
	$(document).ready(function(){
		if ($('#tutorialView').length == 0)
			return;
		setTimeout(function (){
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