<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>
	$(document).ready(function(){
		document.addEventListener("LogoutFrom", function(event){
		    if(event.detail.length != 0){
		    	var nb = event.detail.length;
		    	for(var i in event.detail){
		    		var divLogo = '<img src="'+ event.detail[i].siteSrc +'logo.png" idx="'+ event.detail[i].siteId +'"></img>';
		    		$(".logos-logout-container").append(divLogo);
		    	}
		    	$(".logout-overlay").show();
		    	setTimeout(function(){
    				$(".logout-overlay").css("opacity", 0);
    				setTimeout(function(){
    					$(".logout-overlay").hide();
    				}, 800);
		    	}, 10000)
		    	document.addEventListener("LogoutDone", function(event2){
		    		if($('.logos-logout-container img[idx="'+ event2.detail.siteId +'"]')){
		    			console.log("disappear "+ event2.detail);
		    			nb--;
		    			$('.logos-logout-container img[idx="'+ event2.detail.siteId +'"]').css("opacity", 0);
		    			setTimeout(function(){
		    				$('.logos-logout-container img[idx="'+ event2.detail.siteId +'"]').hide();
		    				if(nb == 0) {
		    					$(".logout-overlay").css("opacity", 0);
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
	</script>
	<div class='logout-overlay' style="display:none;">
		<h3>We are logging you out from your websites</h3>
		<div class="sk-fading-circle">
			<div class="sk-circle1 sk-circle"></div>
  			<div class="sk-circle2 sk-circle"></div>
  			<div class="sk-circle3 sk-circle"></div>
  			<div class="sk-circle4 sk-circle"></div>
  			<div class="sk-circle5 sk-circle"></div>
  			<div class="sk-circle6 sk-circle"></div>
  			<div class="sk-circle7 sk-circle"></div>
  			<div class="sk-circle8 sk-circle"></div>
  			<div class="sk-circle9 sk-circle"></div>
  			<div class="sk-circle10 sk-circle"></div>
  			<div class="sk-circle11 sk-circle"></div>
  			<div class="sk-circle12 sk-circle"></div>
		</div>
		<div class="logos-logout-container"></div>
	</div>