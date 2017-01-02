var waitForExtension = true;
$(document).ready(function(){
	setTimeout(function(){
		waitForExtension = false;
	},800);
});

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

        if (!($('#ease_extension').length)) {
            if(!waitForExtension){
            	checkForExtension();
                return;
            } else {
            	setTimeout(function(){
            		sendEvent(obj);
            	},200);
            }
        }
        
        if(getUserNavigator() == "Safari"){
        	if(!$('#ease_extension').attr("safariversion") || $('#ease_extension').attr("safariversion") !="1.5"){
        		safariExtensionUpdate();
        		return;
        	}
        }
        
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
        		console.log(retMsg);
        		json.detail = JSON.parse(retMsg);
        		var message = "NewConnection";
        		json.detail.highlight = true;
        		if (ctrlDown) json.detail.highlight = false;
        		//easeTracker.trackEvent("ClickOnApp");
        		if(json.detail[0] && json.detail[0].url){
        			json.detail = json.detail[0];
        			message = "NewLinkToOpen";
        			//easeTracker.trackEvent("ClickOnLinkApp");
        			easeTracker.trackEvent("ClickOnApp", {type : "LinkApp", appName : json.detail.app_name});
        		} else {
        			var jsonDetail = json.detail[json.detail.length - 1];
        			easeTracker.trackEvent("ClickOnApp", {type : jsonDetail.type, appName : jsonDetail.app_name, websiteName : jsonDetail.wbesite_name});
        			//easeTracker.trackEvent(.website.name + " connections");
        		}

        		
        		/*event = new CustomEvent("ScrapChrome", {detail:{login:"fel.richart@gmail.com", password:"XXXXXX"}});
        		console.log("event sent");
        		document.dispatchEvent(event);
        		document.addEventListener("ScrapChromeResult", function(event){
        			console.log(event.detail.msg);
        			postHandler.post(
        					"FilterScrap", 
        					{
        						scrapjson : JSON.stringify({Chrome:event.detail.msg})
        					},
        					function(){},
        					function(res){
        						res = JSON.parse(res);
        						res = res.Chrome;
        						for(var i in res){
        							postHandler.post(
        								"AddClassicApp",
        								res[i],
        								function(){},
        								function(res){
        									console.log(res);
        								},
        								function(res){
        									console.log(res);
        								},
        								'text'
        							);
        						}
        					},
        					function(res){},
        					'text'
        			);
        		}, false);*/
        		event = new CustomEvent(message, json);
        		document.dispatchEvent(event);
        	}, function(retMsg) {
        		//easeTracker.trackEvent("App fail clicks");
        		showAlertPopup(retMsg, true);
        	}, 'text');
    }
	}
}

function checkForExtension() {
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
}
