var extensionLight = {
    runtime:{
      sendMessage:function(name, msg, callback){
      safari.self.tab.dispatchMessage(name, msg);
      safari.self.addEventListener("message", function waitResponse(event){
                if(event.name==name+" response"){
                    safari.self.removeEventListener(waitResponse);
                    callback(event.message);
                }
            }, false);
    },
    onMessage:function(name, fct){
      safari.self.addEventListener("message", function(event){
        if(event.name==name){
                    function sendResponse(response){
                        safari.self.tab.dispatchMessage(event.name+" response from tab "+event.message.tab, response);
                    }
          fct(event.message.msg, sendResponse);
        }
      }, false);
    }
    }        
}