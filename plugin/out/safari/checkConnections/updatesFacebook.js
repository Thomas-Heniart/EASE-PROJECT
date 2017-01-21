var xhr=new XMLHttpRequest();

xhr.open("POST","https://www.facebook.com/settings/applications/fetch_apps?tab=all&container_id=u_0_8&dpr=1",true);
xhr.onreadystatechange = function (aEvt) {
    if (xhr.readyState == 4) {
        if(xhr.status == 200){
            var apps = xhr.response.split("ellipsis");
            var updates = [];
            for(var i = 1; i<apps.length;i++){
                updates.push(unescape(JSON.parse('"' + decodeURI(apps[i].split(">")[1].split("\\u003C")[0]) + '"')));
            }
            if(updates.length > 0){
                extension.runtime.sendMessage('newFacebookUpdates', updates, function(){});
            }
        }
    }
};
xhr.withCredentials = true;
xhr.setRequestHeader("accept-language","q=0.8,en-US");
xhr.setRequestHeader("content-type","application/x-www-form-urlencoded");
xhr.setRequestHeader("accept","*/*");
xhr.setRequestHeader("authority","www.facebook.com");
var user=0;
var cookies = document.cookie.split(";");
for(var i=0;i<cookies.length;i++){
    if(cookies[i].indexOf("c_user")!=-1){
        user = cookies[i].split("=")[1];
    }
}
if(document.getElementsByName("fb_dtsg").length > 0){
    var fbdtsg = document.getElementsByName("fb_dtsg")[0].getAttribute("value").replace(":","%3A");
    xhr.send("__user="+ user + "&__a=1&__dyn=7AmajEzUGByA5Q9UrEwlg9pEbFbGAdy8Z9LFwxBxCbzEeAq2i5U4e2CEaUgxebkwy6UnGiex2uVWxeUW2y4GDg4bDBxe6rxCLGqu58nyokz8lxu1iyECUym8yUgx66EK3O69L-6Z1im7WAxx4AyBzEW2qayoO9CBQm5EgAxmnBw&__af=i0&__req=1&__be=-1&__pc=PHASED%3ADEFAULT&fb_dtsg="+fbdtsg);
}
