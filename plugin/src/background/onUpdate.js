extension.runtime.onUpdate(function(){
    
    extension.reloadEaseTabs();
    
    extension.storage.get("extensionId", function(id){
        if(id == null){
            id = generateId(32);
            extension.storage.set("extensionId", id, function(){});
        }
    });
});



function reqFb(){
    /*var req = new XMLHttpRequest();
    req.open('GET', 'https://www.facebook.com/settings?tab=applications', false);
    req.withCredentials = true;
    extension.storage.get("FbCookies", function(res){
        req.setRequestHeader('Cookie', res);
        req.send(null);
        console.log(req);
    });*/
    /*var req = new XMLHttpRequest();
    req.open('POST', 'https://www.facebook.com/settings/applications/fetch_apps?tab=all&container_id=u_0_8&dpr=1', false);
    req.withCredentials = true;
    extension.storage.get("FbCookies", function(res){
        req.setRequestHeader('Cookie', res);
        req.setRequestHeader("Content-type", "application/x-javascript");
        console.log(req);
    });*/
    
}

function generateId(len){
    var text = "";
    var alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < len; i++ )
        text += alphabet.charAt(Math.floor(Math.random() * alphabet.length));

    return text;
}