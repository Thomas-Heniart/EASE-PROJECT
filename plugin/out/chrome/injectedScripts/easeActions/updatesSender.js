extension.getStorage("sessionId", function (oldSessionId) {
    var newSessionId = "";
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        if (cookies[i][0] = " ") {
            cookies[i] = cookies[i].substring(1, cookies[i].length);
        }
        if (cookies[i].indexOf("sId") == 0) {
            newSessionId = cookies[i].substring(cookies[i].indexOf("=") + 1, cookies[i].length);
            break;
        }
    }

    extension.setStorage("sessionId", newSessionId, function () {});
    if (newSessionId != "" && newSessionId != oldSessionId) {
        extension.getStorage("storedUpdates", function (storedUpdates) {
            if (storedUpdates != undefined && storedUpdates.length > 0) {
                $.post("https://ease.space/FilterUpdates", {
                    "sessionId": newSessionId,
                    "scrap": JSON.stringify(storedUpdates),
                    "extensionId": extensionId
                }, function (resp) {
                    var res = resp.split(" ");
                    if (res[0] == "200") {
                        var indices = res;
                        indices.splice(0, 1);
                        var toStore = [];
                        for (var i = 0; i < storedUpdates; i++) {
                            if (!indices.includes(i))
                                toStore.push(storedUpdates[i]);
                        }
                        extension.setStorage("storedUpdates", toStore, function () {});
                    }
                });
            }
        });
    } else if(newSessionId == ""){
        extension.setStorage("websitesToLogout", [], function(){});
    }
});
