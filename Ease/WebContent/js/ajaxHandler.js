var ajaxHandler = {
    get: function (url, parameters, completeCallback, successCallback, errorCallback) {
        $.ajax({
            type: "GET",
            url: url,
            data: JSON.stringify(parameters),
            dataType: "json",
            complete: completeCallback,
            success: successCallback,
            error: errorCallback
        });
    },
    post: function (name, parameters, completeCallback, successCallback, errorCallback) {
        $.ajax({
            type: "POST",
            url: name,
            data: JSON.stringify(parameters),
            contentType: "application/JSON; charset=utf-8",
            dataType: "json",
            compelte: completeCallback,
            success: successCallback,
            error: errorCallback
        });
    }
};