if (window.self == window.top)
{
    console.log("Ca part");

    setTimeout(function() {

        easeMsgManager.sendMessage("toto", "salut toto", function(response) {
            console.log("response: " + response);
            easeMsgManager.sendMessage("toto", "salut fifi", function(response) {
                console.log("response: " + response);
            });
        });

    }, 1000);
}