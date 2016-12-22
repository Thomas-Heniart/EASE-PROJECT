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

