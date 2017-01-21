(function(open) {

    XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {
        document.dispatchEvent(new CustomEvent("newRequest", {"detail":url}));
        open.call(this, method, url, async, user, pass);
    };

})(XMLHttpRequest.prototype.open);