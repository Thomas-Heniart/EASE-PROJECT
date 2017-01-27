if (window.location.href.indexOf("https://ease.space") == 0 || window.location.href.indexOf("http://localhost:8080") == 0 || window.location.href.indexOf("https://localhost:8443/*") == 0 || window.location.href.indexOf("http://51.254.207.91/*") == 0) {

    document.addEventListener("Test", function (event) {
        extension.sendMessage("TestConnection", {
            detail: event.detail
        }, function (response) {});
    }, false);

    document.addEventListener("MultipleTests", function (event) {
        extension.sendMessage("TestMultipleConnections", {
            detail: event.detail
        }, function (response) {});
    }, false);

    extension.onMessage.addListener("printResults", function (results) {
        var response = {};
        response.detail = results;
        var event = new CustomEvent("PrintTestResult", response);
        document.dispatchEvent(event);
    });

}
