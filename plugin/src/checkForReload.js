if (window.top === window) {

    if (!document.getElementById("ease_extension_mamene")){
        var div = document.createElement('div');
        div.id = "ease_extension_mamene";
        document.body.appendChild(div);
	   extension.runtime.sendMessage("reloaded",{}, function(){});
    }
    
}