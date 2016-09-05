if (!document.getElementById("ease_extension_mamene")){
    var div = document.createElement('div');
    div.id = "ease_extension_mamene";
    document.body.appendChild(div);
	chrome.runtime.sendMessage({name:"reloaded"}, function(){});
}