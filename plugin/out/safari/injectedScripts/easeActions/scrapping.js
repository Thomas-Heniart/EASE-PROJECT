if (window.location.href.indexOf("https://ease.space") == 0 || window.location.href.indexOf("http://localhost:8080") == 0 || window.location.href.indexOf("https://localhost:8443/*") == 0 || window.location.href.indexOf("http://51.254.207.91/*") == 0) {

    document.addEventListener("ScrapFacebook", function (event) {
        extension.sendMessage("ScrapFacebook", event.detail, function (res) {
            document.dispatchEvent(new CustomEvent("ScrapFacebookResult", {
                "detail": res
            }));
        });
    }, false);

    document.addEventListener("ScrapLinkedin", function (event) {
        extension.sendMessage("ScrapLinkedin", event.detail, function (res) {
            document.dispatchEvent(new CustomEvent("ScrapLinkedinResult", {
                "detail": res
            }));
        });
    }, false);

    document.addEventListener("ScrapChrome", function (event) {
        extension.sendMessage("ScrapChrome", event.detail, function (res) {
            document.dispatchEvent(new CustomEvent("ScrapChromeResult", {
                "detail": res
            }));
        });
    }, false);

    document.addEventListener("GetChromeUser", function (event) {
        extension.sendMessage("GetChromeUser", {}, function (res) {
            document.dispatchEvent(new CustomEvent("ChromeUserEmail", {
                "detail": res
            }));
        });
    }, false);

}
