var webSocketClient = new function(){
    var self = this;
    this.webSocketServerConnectionUrl = document.location.toString().indexOf("localhost") > -1 ? "localhost:8443" : "ease.space";
    this.socket = null;

    this.keepAlive = function () {
        if (self.socket.readyState === undefined || self.socket.readyState > 1) {
            console.log("webSocket connection lost, trying to reconnect...");
            self.socket = new WebSocket("wss://" + window.location.host + "/webSocketServer");
            self.socket.onopen = self.onOpen;
            self.socket.onmessage = self.onMessage;
            self.socket.onerror = self.onError;
            self.socket.onclose = self.onClose;
        }
    };
    this.initialize = function () {
        console.log('connection to webSocket server.');
        self.socket = new WebSocket("wss://" + window.location.host + "/webSocketServer");
        self.socket.onopen = self.onOpen;
        self.socket.onmessage = self.onMessage;
        self.socket.onerror = self.onError;
        self.socket.onclose = self.onClose;
        setInterval(self.keepAlive, 30000);
    };
    this.onOpen = function (event) {
        console.log("connection to webSocket server initialized.");
    };
    this.onError = function (event) {
        console.log("webSocketClient encountered an error.");
        console.log("connection to webSocket server closed.");
    };
    this.onClose = function (event) {
        console.log("connection to webSocket server closed.");
    };
    this.onMessage = function (event) {
        var mess = JSON.parse(event.data);
        console.log(mess.type);
        console.log(mess.data);
    };
};

window.addEventListener('load', webSocketClient.initialize);
