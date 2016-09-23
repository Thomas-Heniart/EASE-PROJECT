if (window.top === window) {

    if (!document.getElementById("ease_overlay_mamene")){
        var overlay = document.createElement('div');
        overlay.id = "ease_overlay_mamene";
        var popup = document.createElement('div');
        document.body.appendChild(overlay);
        chrome.runtime.sendMessage({"name":'reloaded', "message":{}}, function(){});
        startOverlay();
    }
    
}

function startOverlay(){
    
    var overlay = document.getElementById("ease_overlay_mamene");
    
    overlay.className = "overlayEase";
    
    var spinner = document.createElement('div');
    spinner.id = "div-loading-ease-mamene";
    spinner.className = "spinnerElementEase";
    spinner.style = "background-color: #6C7FA0";
    
    var spaceship = document.createElement('div');
    spaceship.id = "loading-background-ease-mamene";
    spaceship.className = "spinnerElementEase";
    
    var text = document.createElement('div');
    text.id = "loading-text-ease-mamene";
    text.className = "spinnerElementEase";
    text.innerHTML = "Loading";
    
    spinner.appendChild(spaceship);
    spinner.appendChild(text);
    
    overlay.appendChild(spinner);
    
    /*var spinner = document.createElement('div');
    spinner.id = "ease_spinner_mamene";
    spinner.className = "spinnerEase";
    
    popup.appendChild(spinner);*/
}

function checkConnectionOverlay(msg){
    spinner = document.getElementById("div-loading-ease-mamene");
    text = document.getElementById("loading-text-ease-mamene");
    spinner.style = "background-color: #F26430";
    text.innerHTML = "Analysing";
    
}

function loginOverlay(msg){ //quand todo = login
    spinner = document.getElementById("div-loading-ease-mamene");
    text = document.getElementById("loading-text-ease-mamene");
    spinner.style = "background-color: #266A2E";
    text.innerHTML = "Logging in";
}

function logoutOverlay(msg){ //quand todo = logout
    spinner = document.getElementById("div-loading-ease-mamene");
    text = document.getElementById("loading-text-ease-mamene");
    spinner.style = "background-color: #ec555b";
    spinner.setAttribute('data-back-color', '#ec555b');
    text.innerHTML = "Logging out";
    
}

function endOverlay(msg){
    setTimeout(function(){overlay.style = "display: none"}, 500);
}

function errorOverlay(msg){ //quand grave ou fin waitfor
    spinner = document.getElementById("div-loading-ease-mamene");
    text = document.getElementById("loading-text-ease-mamene");
    spinner.style = "background-color: #ec555b";
    text.innerHTML = "Error";
    setTimeout(function(){overlay.style = "display: none"},600);
}

chrome.runtime.onMessage.addListener(function(event, sender, sendResponse){
    if(event.name == "rmOverlay"){
        endOverlay({});
    }
});