function sendEvent(email, password, userSelector, passSelector, buttonSelector, urlLogin, urlHome){ //dispatch an Event to warn the plugin it has to connect to the websit\e
    var event = new CustomEvent("NewConnection",
     {'detail':{"user": email,
     "pass":password,
     "userField":userSelector,
      "passField":passSelector,
      "button": buttonSelector,
       "urlLogin": urlLogin,
        "urlHome": urlHome}});
    document.dispatchEvent(event);
    console.log("Ease : go to new page");
    console.log(event.detail);
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}