if (window.location.href.indexOf("https://ease.space") == 0 || window.location.href.indexOf("http://localhost:8080") == 0 || window.location.href.indexOf("https://localhost:8443/*") == 0 || window.location.href.indexOf("http://51.254.207.91/*") == 0) {

    var extensionId;
    extension.getStorage("extensionId", function (eId) {
        extensionId = eId;
        $(document).ready(function () {
            $('body').prepend('<div id="ease_extension" extensionId="' + eId + '" safariversion="2.1.3" style="dislay:none;">');
        });
    });

}
