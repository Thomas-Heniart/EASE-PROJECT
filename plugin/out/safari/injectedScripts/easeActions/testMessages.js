document.addEventListener("Test", function(event){
    extension.sendMessage("TestConnection", {detail:event.detail}, function(response) {});
}, false);

document.addEventListener("MultipleTests", function(event){
    extension.sendMessage("TestMultipleConnections", {detail:event.detail}, function(response) {});
}, false);

extension.onMessage.addListener("printResults", function(results){
    var response = {};
    response.detail = results;
    var event = new CustomEvent("PrintTestResult", response);
    document.dispatchEvent(event);
});