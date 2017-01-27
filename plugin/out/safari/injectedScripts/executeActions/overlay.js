if (window.location.href.indexOf("https://ease.space") != 0 && window.location.href.indexOf("http://localhost:8080") != 0 && window.location.href.indexOf("https://localhost:8443/*") != 0 && window.location.href.indexOf("http://51.254.207.91/*") != 0) {

    var overlay = {
        elements: {},
        types: {
            "checkAlreadyLogged": {
                "color": "D4C60F",
                "text": "Analysing"
            },
            "connect": {
                "color": "266A2E",
                "text": "Logging in"
            },
            "logout": {
                "color": "ec555b",
                "text": "Logging out"
            },
            "switch": {
                "color": "ec555b",
                "text": "Switching"
            },
        },
        create: function () {
            this.elements.main = document.createElement('div');
            this.elements.main.id = "ease_overlay_mm";
            this.elements.main.className = "overlayEase";
            document.body.appendChild(this.elements.main);

            this.elements.spinner = document.createElement('div');
            this.elements.spinner.id = "div-loading-ease-mm";
            this.elements.spinner.className = "spinnerElementEase";
            this.elements.spinner.style = "background-color: #6C7FA0";
            this.elements.main.appendChild(this.elements.spinner);

            this.elements.close = document.createElement('a');
            this.elements.close.href = "javascript:void(0)";
            this.elements.close.className = "closeEaseOverlay";
            this.elements.close.setAttribute('onClick', "document.getElementById('ease_overlay_mm').style='display: none';");
            this.elements.close.innerHTML = '&times;';
            this.elements.spinner.appendChild(this.elements.close);

            this.elements.text = document.createElement('div');
            this.elements.text.id = "loading-text-ease-mm";
            this.elements.text.className = "spinnerElementEase";
            this.elements.text.innerHTML = "Loading";
            this.elements.spinner.appendChild(this.elements.text);

            this.elements.spaceship = document.createElement('div');
            this.elements.spaceship.id = "loading-background-ease-mm";
            this.elements.spaceship.className = "spinnerElementEase";
            this.elements.spinner.appendChild(this.elements.spaceship);
        },
        set: function (type) {
            this.elements.spinner.style = "background-color: #" + this.types[type].color;
            this.elements.text.innerHTML = this.types[type].text;
        },
        remove: function (type) {
            this.elements.main.style = "display: none";
        }
    }
}
