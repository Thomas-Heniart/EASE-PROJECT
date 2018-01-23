asyncLoading = {
	loadHtml :  function(param){
		var pages = param.urls;
		var appendTo = param.appendTo;
		var callback = param.callback;
		var pagesSize = pages.length;

		for (var i = 0; i < pages.length; i++) {
			$.get(pages[i]).done(function(data){
				$(appendTo).append(data);
				pagesSize--;
				if (!pagesSize && callback)
					callback();
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
	},
	loadSingleScript : function(url, callback){
		var sc = document.createElement('script');
		sc.async = true;
		sc.onload = function(){
			if (callback)
				callback();
		};
		sc.src = url;
		(document.getElementsByTagName('head')[0]||document.getElementsByTagName('body')[0]).appendChild(sc);
	},
	loadScriptsOneByOne : function(urls, callback){
		var currentScriptIndex = 0;
		if (!urls.length){
			if (callback)
				callback();
			return;
		}
		function singleScriptLoading(){
			var sc = document.createElement('script');
			sc.async = true;
			sc.onload = function(){
				currentScriptIndex++;
				if (currentScriptIndex == urls.length){
					if (callback)
						callback();
				}
				else
					singleScriptLoading();
			}
			sc.src = urls[currentScriptIndex];
			(document.getElementsByTagName('head')[0]||document.getElementsByTagName('body')[0]).appendChild(sc);
		}
		singleScriptLoading();
	}
}