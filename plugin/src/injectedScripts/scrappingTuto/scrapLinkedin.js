extension.runtime.onMessage("scrapLnkdn", function(msg, sendResponse){
    var results = [];
    var nbOfApps = $(".third-party-apps-name").length;
    if(nbOfApps == 0)
        sendResponse(results);
    else {
        $(".third-party-apps-name").each(function(index){
            results.push($(this).text());
            if(index+1 == nbOfApps){
                sendResponse(results);
            }
        });
    }
});

