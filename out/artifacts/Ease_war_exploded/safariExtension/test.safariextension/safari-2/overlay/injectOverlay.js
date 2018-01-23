var easeOverlay = null;

extensionLight.runtime.onMessage("strtOverlay", function(message, sendResponse){
if (window.top === window) {
   if (!document.getElementById("ease_overlay_mamene")){
        var overlay = document.createElement('div');
        overlay.id = "ease_overlay_mamene";
        var popup = document.createElement('div');
        document.body.appendChild(overlay);
        extensionLight.runtime.sendMessage('reloaded',{}, function(){});
        startOverlay();

        var Overlay = function(elem){
            var self = this;
            this.qRoot = document.getElementById('ease-overlay');
            this.parent = document.getElementById('ease-overlay-holder');

            this.infoTextHolder = document.getElementById('ease-overlay-status-text');
            this.imgHandler = document.getElementById('ease-overlay-image');

            this.closer = document.getElementById('ease-closer');

            this.closer.onclick = function (){
                self.hide();
            }
            this.setImage = function(src){
                self.imgHandler.setAttribute('src', src);
            }
            this.setInfoText = function(str){
                self.infoTextHolder.innerHTML = str;
            }
            this.show = function(){
                self.parent.classList.add('show');
            }
            this.hide = function(){
                self.parent.classList.remove('show');
            }
        }
        easeOverlay = new Overlay();
        easeOverlay.show();
    }
}
});


function startOverlay(){
    document.getElementById("ease_overlay_mamene").innerHTML =
    "<div id='ease-overlay-holder'>" +
        "<div id='ease-overlay'>" +
            "<div id='ease-closer'>" +
                "<img src='https://ease.space/resources/icons/cancel.png'/>" +
            "</div>" +
            "<div class='header'>" +
                "<div class='logoImg'>" +
                    "<img src='https://ease.space/resources/icons/Ease_logo_blue_little.png'/>" +
                "</div>" +
                "<div class='status' id='ease-overlay-status-text'>" +

                "</div>" +
            "</div>" +
            "<div class='overlay-body'>" +
                "<div class='loaderHelper'>" +

                "</div>" +
                "<div class='websiteImgHandler'>" +
                    "<img id='ease-overlay-image' src='https://ease.space/resources/icons/grey_square.png'/>" +
                "</div>" +
            "</div>" +
        "</div>" +
    "</div>";
}

function checkConnectionOverlay(msg){
    easeOverlay.setInfoText("Analysing...");
    easeOverlay.setImage(msg.detail[msg.bigStep].website.img);
    easeOverlay.show();
}

function loginOverlay(msg){
    easeOverlay.setInfoText("Login...");
    easeOverlay.setImage(msg.detail[msg.bigStep].website.img);
    easeOverlay.show();
}

function logoutOverlay(msg){
    easeOverlay.setInfoText("Logout...");
    easeOverlay.setImage(msg.detail[msg.bigStep].website.img);
    easeOverlay.show();
}

function endOverlay(msg){
    easeOverlay.hide();
    easeOverlay.setInfoText("Analysing...");
    easeOverlay.setImage(msg.detail[msg.bigStep].website.img);
}

function errorOverlay(msg){ //quand grave ou fin waitfor
    easeOverlay.setInfoText("Error !");
    easeOverlay.setImage(msg.detail[msg.bigStep].website.img);
    easeOverlay.show();
}

extensionLight.runtime.onMessage( "rmOverlay", function(message, sendResponse){
    endOverlay({});
});