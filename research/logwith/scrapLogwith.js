console.log("I'am here biatch");
console.log($("[class*='facebook']").length);
$("[class*='facebook']").click(function() {
	console.log($(this));
	chrome.runtime.sendMessage({"name":"clickBtn", "msg":{"btn":$(this), "url":window.location.host}}, function(response) {
  		console.log("Message sended.");
	});
});