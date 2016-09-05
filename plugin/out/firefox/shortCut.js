extension.addShortCut(function(command){
    if(command == "open-ease") {
        window.open('http://ease.space');
        //nothing to say
    }
});

extension.onWindowCreated(function() {
	if (extension.storage.get("NewTab") == "1"){
 	   window.open('http://ease.space');
	}
});