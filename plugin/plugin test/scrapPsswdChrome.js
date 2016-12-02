function waitLoading(callback){
    if($("tr.z-Of.cba.z-op").length == 0){
        setTimeout(function(){
            waitLoading(callback);
        }, 100);
    }
    else
        callback();
}

if (window.top === window) {

    if ($(".ease_overlay_scrap_facebook").length == 0){
    
    var overlay = document.createElement('div');
    overlay.className = "ease_overlay_scrap_facebook";
    document.body.appendChild(overlay);
   
    var popup = document.createElement('div');
    popup.className = "popup";
    overlay.appendChild(popup);
    
    var logo = document.createElement('img');
    logo.src = "https://ease.space/resources/images/logo.png"
    logo.className = "ease_logo";
    popup.appendChild(logo);
    
    var text1 = document.createElement('p');
    text1.innerText = "Collecting credentials stored in chrome";
    text1.className ="big";
    popup.appendChild(text1);
    
    var progress_wrap = document.createElement('div');
    progress_wrap.className = "ease_progress_wrap";
    popup.appendChild(progress_wrap);
        
    var progress_bar = document.createElement('div');
    progress_bar.className = "ease_progress_bar";
    progress_wrap.appendChild(progress_bar);
    var progressTotal = $('.ease_progress_wrap').width();
    function progressLoading(progress, length){
        $('.ease_progress_bar').stop().animate({
            left: progress
        }, length);
    }
    progressLoading(((Math.random()*0.5)+0.5)*progressTotal, 10000);
    
    var text2 = document.createElement('p');
    text2.innerText = "Magic processing ...";
    text2.className = "little bold";
    popup.appendChild(text2);
    
    var text3 = document.createElement('p');
    text3.innerText = "You'll select the datas you want to keep on Ease in the next step";
    text3.className = "little";
    popup.appendChild(text3);
    
    }
}

waitLoading(function(){
    var loadingString = "";
    var waitingTime = 10;
    var accounts = $("tr.z-Of.cba.z-op");
    var nbAccounts = accounts.length;
    checkAccount(accounts, 0);
    
    function checkAccount(accounts, i){
        var elmt = $(accounts[i]);
        elmt.find("div.Vaa.AW.BW").click();
        getPass(elmt ,function(website, login, pass){
            console.log("website : "+website+", login : "+login+", pass : "+pass);
            i++;
            if (i<nbAccounts)
                checkAccount(accounts, i);
            else {
                endScrap();
            }
        });
    }
    
    function getPass(elmt, callback){
        if(elmt.find("input.bba.EW.a-Fa").val()=="••••••••" || elmt.find("input.bba.EW.a-Fa").val()==loadingString){
            setTimeout(function(){
                getPass(elmt, callback);
            }, waitingTime);
        } else if(loadingString==""){
            loadingString = elmt.find("input.bba.EW.a-Fa").val();
            waitingTime = 100;
            getPass(elmt, callback);
        } else {
            callback(elmt.find("span.Zaa").text(),elmt.find("span.hba").text(),elmt.find("input.bba.EW.a-Fa").val());
        }
    }
    
    function endScrap(){
        setTimeout(function(){
            popup.removeChild(text3);
            text2.innerText = "Redirecting to Ease.";   
            window.location.href = "https://ease.space";
        }, 500);
        progressLoading(progressTotal, 300);
    }
    
});

