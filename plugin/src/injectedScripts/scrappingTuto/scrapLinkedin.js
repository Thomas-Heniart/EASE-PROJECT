if (window.location.href.indexOf("https://www.linkedin.com") == 0) {
    extension.onMessage.addListener("scrapLnkdn", function (msg, sendResponse) {
        wait(function (res) {
            if (!res) {
                sendResponse([]);
            } else {
                var results = [];
                var nbOfApps = $(".third-party-apps-name").length;
                if (nbOfApps == 0)
                    sendResponse(results);
                else {
                    $(".third-party-apps-name").each(function (index) {
                        results.push($(this).text());
                        if (index + 1 == nbOfApps) {
                            sendResponse(results);
                        }
                    });
                }
            }
        });

    });

    function wait(callback) {
        var iteration = 0;

        function waitforelem() {
            if (iteration % 20 == 0) {
                console.log("-- Waiting for accounts --");
            }
            if (iteration > 100) {
                callback(false);
            } else if ($("p.instructions").length == 0) {
                setTimeout(function () {
                    iteration++;
                    waitforelem();
                }, 100);
            } else {
                callback(true);
            }
        }
        waitforelem(callback);
    }
}
