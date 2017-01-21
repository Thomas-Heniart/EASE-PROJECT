var listenerFactory = {
    addListener: function (easeFct, chromeFct, userFct, createdFct) {
        if (!easeFct.listeners) {
            easeFct.listeners = [];
        }
        var list = easeFct.listeners;
        var store = {
            "userFct": userFct,
            "createdFct": createdFct,
        }
        list.push(store);
        chromeFct.addListener(createdFct);
    },
    removeListener: function (easeFct, chromeFct, userFct) {
        if (easeFct.listeners) {
            var list = easeFct.listeners;
            for (var i in list) {
                if (list[i].userFct == userFct) {
                    chromeFct.removeListener(list[i].createdFct);
                    list.splice(i, 1);
                    break;
                }
            }
        }

    }
};