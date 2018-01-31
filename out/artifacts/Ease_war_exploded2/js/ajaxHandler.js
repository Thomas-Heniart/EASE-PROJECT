var ajaxHandler = {
    get: function (url, parameters, completeCallback, successCallback, errorCallback, async) {
        if (parameters === null || typeof parameters === "undefined")
            parameters = {};
        if (async === null || typeof async === "undefined")
            async = true;
        $.ajax({
            type: "GET",
            url: url,
            data: $.param(parameters),
            dataType: "json",
            complete: completeCallback,
            success: successCallback,
            statusCode: {
                400: function (error) {
                    errorCallback(error.responseText);
                }
            },
            async: async
        });
    },
    post: function (name, parameters, completeCallback, successCallback, errorCallback, async) {
        if (parameters === null || typeof parameters === "undefined")
            parameters = {};
        if (async === null || typeof async === "undefined")
            async = true;
        $.ajax({
            type: "POST",
            url: name,
            data: JSON.stringify(parameters),
            contentType: "application/JSON; charset=utf-8",
            dataType: "json",
            complete: completeCallback,
            success: successCallback,
            statusCode: {
                400: function (error) {
                    errorCallback(error.responseText);
                }
            },
            async: async
        });
    }
};