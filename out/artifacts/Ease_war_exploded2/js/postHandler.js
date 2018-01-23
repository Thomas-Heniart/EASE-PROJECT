
var postHandler = {
	get: function(url, parameters, alwaysDo,successCallback, errorCallback, type){
		$.get(
			url,
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
	},
	post:function(name, parameters, alwaysDo, successCallback, errorCallback, type){
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
	}
};