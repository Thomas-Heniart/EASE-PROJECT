extension.onInstalled.addListener(function(){
    
    extension.ease.reload();
    
    extension.storage.get("extensionId", function(id){
        if(id == null){
            id = generateId(32);
            extension.storage.set("extensionId", id, function(){});
        }
    });
});

function generateId(len){
    var text = "";
    var alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < len; i++ )
        text += alphabet.charAt(Math.floor(Math.random() * alphabet.length));

    return text;
}