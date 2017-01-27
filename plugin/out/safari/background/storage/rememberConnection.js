function rememberClassicConnection(username, website) {
    storage.lastConnections.set(website, {
        "user": username
    });
    console.log("-- Connection for email " + username + " on website " + website + " remembered --");
}

function rememberDirectLogWithConnection(website, logWithDatas) {
    storage.lastConnections.set(website, logWithDatas);
    console.log("-- Connection with " + logWithDatas.logWith + " on website " + website + " remembered --");
}

function rememberLogWithConnection(website, hostLogWithWebsite) {
    if (storage.lastConnections.get(hostLogWithWebsite)) {
        storage.lastConnections.set(website, {
            "user": storage.lastConnections.get(hostLogWithWebsite).user,
            "logWith": hostLogWithWebsite
        });
    }
    console.log("-- Connection with " + logWithWebsite + " on website " + website + " remembered --");
}

function storeUpdate(update) {
    removeUpdate(update);
    var nDate = new Date();
    update.expiration = nDate.getTime() + (2 * 604800000); //expiration en 2 semaines
    storage.storedUpdates.push(update);
}

function removeUpdate(update) {
    var toDelete = -1;
    for (var i = 0; i < storage.storedUpdates.length; i++) {
        var oldUpdate = storage.storedUpdates.get(i);
        if (oldUpdate.type == update.type && oldUpdate.website == update.website) {
            if (update.type == "logwith" && oldUpdate.logwith == update.logwith && oldUpdate.username == update.username) {
                toDelete = i;
                break;
            }
            if (update.type == "classic" && oldUpdate.username == update.username) {
                toDelete = i;
                break;
            }
        }
    }
    if (toDelete > -1) {
        storage.storedUpdates.remove(toDelete);
    }
}

function cleanStoredUpdates() {
    for (var i = 0; i < storage.storedUpdates.length; i++) {
        if (storage.storedUpdates.get(i).expiration < (new Date()).getTime()) {
            storage.storedUpdates.remove(i);
            i--;
        }
    }
    setTimeout(cleanStoredUpdates, 1000 * 60 * 60 * 4);
}

extension.runtime.onMessage.addListener("fbDisconnected", function () {
    rememberClassicConnection("disconnected", "www.facebook.com");
});
