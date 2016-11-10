if($("#email").length>0){
    extension.runtime.sendMessage("fbDisconnected",{},function(){
        console.log("Disconnected from FaceBook");
    });
}