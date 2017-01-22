var xhr=new XMLHttpRequest();
xhr.open("GET","https://www.linkedin.com/psettings/third-party-applications",true);
xhr.onreadystatechange = function (aEvt) {
    if (xhr.readyState == 4) {
        if(xhr.status == 200){
            var apps = xhr.response.split("third-party-apps-name");
            var updates = [];
            for(var i=1;i<apps.length;i++){
                updates.push(apps[i].replace("\">","").split("</p>")[0]);
            }
            if(updates.length > 0){
                extension.runtime.sendMessage('newLinkedinUpdates', updates, function(){});
            }
        }
    }
};
xhr.send(null);