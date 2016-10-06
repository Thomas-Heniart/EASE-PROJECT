
var postHandler = {
		
		email: $('#userEmail').attr('data-content'),
				
		isCorrectUser:function(email){
			if(email==null){
				return true;
			}
			var cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
			 for (var i in cookies) {
				 var cookie = cookies[i];
			   if (cookie.getName().equals("email")) {
				  if(cookie.getValue()==email){
					  return true;
				  }
				  return false;
			    }
			  }
			}
			return false;
		},
		
		post:function(name, parameters, alwaysDo, successCallback, errorCallback, type){
			if(this.isCorrectUser(this.email)){
				$.post(
					name,
					parameters,
					function(data) {
						alwaysDo();
						var retCode = data.split(" ")[0];
						var retMsg = data.substring(data.indexOf(" ")+1, data.length);
						if(!/^\d+$/.test(retCode)) 
							retMsg = "Sorry, you're facing of a unknown bug. We'll fix it asap !";    
						if (retCode == '200')
							successCallback(retMsg);
						else if (retCode == '0' || retCode == '5')
							document.location.reload(true);
						else 
							errorCallback(retMsg, true);
					}, 
					type);
			} else {
				document.location.reload(true);
			}			
		}
}