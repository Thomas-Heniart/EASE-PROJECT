extension.runtime.bckgrndOnMessage("changeHomepage", function(message, tab, sendResponse) {
    extension.tabs.update(tab, "https://ease.space", function(){});
});