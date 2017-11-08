var server = {
    serverUrl: "https://192.168.0.18:8443",
    post: function (url, json, successCallback, errorCallback, async, cipher) {
        if (cipher) {
            if (serverPublicKey !== "")
                map(json, this.cipher);
        }
        var self = this;
        $.ajax({
            type: "POST",
            url: this.serverUrl + url,
            data: JSON.stringify(json),
            contentType: "application/JSON; charset=utf-8",
            dataType: "json",
            async: ((async === null) ? true : async),
            success: function (data) {
                if (cipher) {
                    if (serverPublicKey !== "")
                        map(data, self.decipher);
                }
                successCallback(data);
            },
            error: errorCallback
        });
    }
};