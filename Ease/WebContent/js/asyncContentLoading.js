asyncLoading = {
	loadHtml :  function(param){
		var pages = param.urls;
		var appendTo = param.appendTo;

		for (var i = 0; i < pages.length; i++) {
			$.get(pages[i]).done(function(data){
				$(appendTo).append(data);
			});
		}
	},
	loadScripts : function(param){
		var scripts = param.urls;

		for (var i = 0; i < scripts.length; i++) {
			var script = document.createElement('script');
			if (param.async)
				script.async = param.async;
			if (param.onload)
				script.onload = param.onload;
			if (param.appendTo)
				param.appendTo.appendChild(script);
			else
				document.head.appendChild(script);
			script.src = scripts[i];
		}
	}
}