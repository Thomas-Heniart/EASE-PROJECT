$(document).ready(function(){
	document.addEventListener("LogoutFrom", function(event){
		if(event.detail.length != 0){
			var nb = event.detail.length;
			for(var i in event.detail){
				var divLogo = '<img src="'+ event.detail[i].siteSrc +'logo.png" idx="'+ event.detail[i].siteId +'"></img>';
				$(".logos-logout-container").append(divLogo);
			}
			$(".logout-overlay").show();
			$(".logout-overlay").removeClass('hide');
			setTimeout(function(){
				$(".logout-overlay").addClass('hide');
				setTimeout(function(){
					$(".logout-overlay").hide();
				}, 800);
			}, 10000)
			document.addEventListener("LogoutDone", function(event2){
				if($('.logos-logout-container img[idx="'+ event2.detail.siteId +'"]')){
		    			//console.log("disappear "+ event2.detail);
		    			nb--;
		    			$('.logos-logout-container img[idx="'+ event2.detail.siteId +'"]').css("opacity", 0);
		    			setTimeout(function(){
		    				$('.logos-logout-container img[idx="'+ event2.detail.siteId +'"]').hide();
		    				if(nb == 0) {
		    					$(".logout-overlay").addClass('hide');
		    					setTimeout(function(){
		    						$(".logout-overlay").hide();
		    					}, 800);
		    				}
		    			},500);
		    		}
		    	}, false);
		}
	}, false);
});
