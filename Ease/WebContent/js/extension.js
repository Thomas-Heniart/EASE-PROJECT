var waitForExtension = true;
$(document).ready(function(){
	setTimeout(function(){
		waitForExtension = false;
		showExtensionPopup();
		if (!showExtensionPopup())
			$("#tutorial").addClass("myshow");
	},800);
	
	
});

function showExtensionPopup(){
	if (!($('#ease_extension').length)) {
        if(!waitForExtension){
        	$('#extension').addClass("myshow");
            return true;
        } else {
        	setTimeout(function(){
        		return showExtensionPopup();
        	},200);
        }
    } else {
    	if(getUserNavigator() == "Safari"){
        	if(!$('#ease_extension').attr("safariversion") || $('#ease_extension').attr("safariversion") !="2.2.2"){
        		$('#extension .title p').text("Update your extension");
        		$('#extension #download #line1').text("A new version of the extension is now available.");
        		$('#extension #download #line2').text("We added new features and made it faster !");
        		$('#extension #download button').text("Update Ease Extension");
        		$('#extension').addClass("myshow");
        		return true;
        	}
        }
    	return false;
    }
}

function sendEvent(obj) {
	if(testApp){
		if (!($(obj).hasClass('waitingLinkImage'))) {
	        var appId = $(obj).closest('.siteLinkBox').attr('id');
	        var link = $(obj).closest('.siteLinkBox').attr('link');
	        var logoImage = $(obj).find('.linkImage');
	        var json = new Object();
	        var event;
	        $(obj).addClass('waitingLinkImage');
	        $(obj).addClass('scaleinAnimation');
	        setTimeout(function() {
	            $(obj).removeClass("waitingLinkImage");
	            $(obj).removeClass('scaleinAnimation');
	        }, 1000);
	        if (typeof link !== typeof undefined && link !== false) {
	        } else {
	        	postHandler.post("AskInfo", {
	        		appId : appId
	        	}, function() {
	        	}, function(retMsg) {
	        		json.detail = JSON.parse(retMsg);
	        		event = new CustomEvent("Test", json);
	        		document.dispatchEvent(event);
	        	}, function(retMsg) {
	        		showAlertPopup(retMsg, true);
	        	}, 'text');
	        }
	    }
	} else {
    if (!($(obj).hasClass('waitingLinkImage'))) {
        var appId = $(obj).closest('.siteLinkBox').attr('id');
        var link = $(obj).closest('.siteLinkBox').attr('link');
        var logoImage = $(obj).find('.linkImage');
        var json = new Object();
        var event;
        
        //easeTracker.trackEvent("App clicks");

        if(showExtensionPopup())
        	return;
        else {
        $(obj).addClass('waitingLinkImage');
        $(obj).addClass('scaleinAnimation');
        setTimeout(function() {
            $(obj).removeClass("waitingLinkImage");
            $(obj).removeClass('scaleinAnimation');
        }, 1000);
        	postHandler.post("AskInfo", {
        		appId : appId,
        	}, function() {
        	}, function(retMsg) {
        		json.detail = JSON.parse(retMsg);
        		var message = "NewConnection";
        		//easeTracker.trackEvent("ClickOnApp");
        		if(json.detail[0] && json.detail[0].url){
        			json.detail = json.detail[0];
        			message = "NewLinkToOpen";
        			
        			easeTracker.trackEvent("ClickOnApp", {"type":"LinkApp", "appName":json.detail.app_name});
        		} else {
        			var jsonDetail = json.detail[json.detail.length - 1];
        			easeTracker.trackEvent("ClickOnApp", {"type":jsonDetail.type, "appName":jsonDetail.app_name, "websiteName": jsonDetail.wbesite_name});
        		}
        		var now = "" + new Date;
        		easeTracker.setOnce("TutoDateFirstClickOnApp", now);
                json.detail.highlight = !ctrlDown;
        		event = new CustomEvent(message, json);
        		document.dispatchEvent(event);
        	}, function(retMsg) {
        		//easeTracker.trackEvent("App fail clicks");
        		showAlertPopup(retMsg, true);
        	}, 'text');
        }
    	}
	}
}

/*function checkForExtension() {
    var ext = $('#ease_extension');

        $('#downloadExtension').css('display', 'block');
    	$('#downloadExtension').find(".classicContent").css('display', 'block');
    	$('#downloadExtension').find('.install-button').css('display', 'inline-block');
       	$('#downloadExtension').find(".safariUpdate").css('display', 'none');
        if(getUserNavigator() == "Safari"){$('#safariInfoButton').css('display', 'block');}
        $('#downloadExtension').find('.install-button').click(
            function() {
                var NavigatorName = getUserNavigator();
                if (NavigatorName == "Chrome") {
                    chrome.webstore
                    .install(
                        'https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm',
                        function() {
                            window.location
                            .replace("index.jsp");
                        },
                        function() {
                            window.location
                            .replace("index.jsp");
                        })
                }
                else if (NavigatorName == "Safari"){
                    window.location.replace("https://ease.space/safariExtension/EaseExtension.safariextz");
                    $('#downloadExtension').find('.popupContent').hide();
                    $('#downloadExtension').find('#afterdownload.safariHelper').show();
                }
            });
}

function safariExtensionUpdate(){
	 $('#downloadExtension').css('display', 'block');
     $('#downloadExtension').find(".safariUpdate").css('display', 'block');
     $('#downloadExtension').find('.install-button').css('display', 'inline-block');
     $('#downloadExtension').find(".classicContent").css('display', 'none');
     $('#safariInfoButton').css('display', 'block');
     $('#downloadExtension').find('.install-button').click(
         function() {
                 window.location.replace("https://ease.space/safariExtension/EaseExtension.safariextz");
                 $('#downloadExtension').find('.popupContent').hide();
                 $('#downloadExtension').find('#afterdownload.safariHelper').show();
         });
}*/

$(document).ready(function() {
	 $('#homePageSwitch').change(function() {
		var homepageState = $(this).is(":checked");
		easeTracker.setHomepage(homepageState);
		easeTracker.trackEvent("HomepageSwitch");
		var stateString = homepageState.toString();
		postHandler.post("HomepageSwitch", {
			homepageState: stateString
		}, function() {
			
		}, function(data) {
			
		}, function(date) {
			
		});
	 });
	 
});
