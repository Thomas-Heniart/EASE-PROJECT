extension.storage.get("sessionId", function (oldSessionId) {
    var newSessionId = "";
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        if (cookies[i][0] = " ") {
            cookies[i] = cookies[i].substring(1, cookies[i].length - 1);
        }
        if (cookies[i].indexOf("sId") == 0) {
            newSessionId = cookies[i].substring(cookies[i].indexOf("=") + 1, cookies[i].length - 1);
            break;
        }
    }

    extension.storage.set("sessionId", newSessionId, function () {});
    if (newSessionId != "" && newSessionId == oldSessionId) {
        extension.storage.get("storedUpdates", function (storedUpdates) {
            if (storedUpdates != undefined && storedUpdates.length > 0) {
                extension.storage.get("extensionId", function (eId) {
                    var xhr = new XMLHttpRequest();
                    xhr.open("POST", "https://ease.space/FiterUpdates", false);
                    xhr.onreadystatechange = function (aEvt) {
                        if (xhr.readyState == 4) {
                            var res = xhr.response.split(" ");
                            if (res[0] == "200") {
                                var indices = res[1].split(",");
                                var toStore = [];
                                for(var i=0;i<storedUpdates;i++){
                                    if(!indices.includes(i))
                                        toStore.push(storedUpdates[i]);
                                }
                                extension.storage.set("storedUpdates", toStore);
                            }
                        }
                    };
                    xhr.send("sessionId=" + newSessionId + "&extensionId=" + eId + "&updates=" + JSON.stringify(storedUpdates));
                });
            }
        });
    }
});



document.addEventListener("askForExtensionId", function (event) {
    extension.storage.get("extensionId", function (extensionId) {
        document.dispatchEvent(new CustomEvent("extensionId", {
            id: extensionId
        }));
    });
});