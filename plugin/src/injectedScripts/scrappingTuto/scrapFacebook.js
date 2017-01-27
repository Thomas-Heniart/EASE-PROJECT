if (window.location.href.indexOf("https://www.facebook.com") == 0) {

    extension.onMessage.addListener("scrapFb", function (msg, sendResponse) {
        function waitload(callback) {
            if ($("._ikh._4na3").length == 0) {
                setTimeout(function () {
                    waitload(callback);
                }, 100);
            } else {
                callback();
            }
        }
        var results = [];
        waitload(function () {
            $(".fsl").click();
            var nbOfApps = $("._4n9u.ellipsis").length;
            $("._4n9u.ellipsis").each(function (index) {
                results.push($(this).text());
                if (index + 1 == nbOfApps) {
                    sendResponse(results);
                }
            });
        });
    });

}
