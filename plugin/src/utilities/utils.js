function getHost(url) {
    var getLocation = function (href) {
        var l = document.createElement("a");
        l.href = href;
        return l;
    };
    return getLocation(url).hostname;
}