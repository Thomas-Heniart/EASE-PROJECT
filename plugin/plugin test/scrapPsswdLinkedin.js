function waitLoading(callback){
    if($("p.instructions").length == 0){
        setTimeout(function(){
            waitLoading(callback);
        }, 250);
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
    text1.innerText = "Collecting apps where you signed in with Linkedin";
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
    progressLoading(((Math.random()*0.5)+0.5)*progressTotal, 4000);
    
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

var accountsToSend = [];
    
waitLoading(function(){
    //text3.innerText = "Collecting ...";
    $("span.fsl").click();
    var accounts = $("p.third-party-apps-name");
    var nbAccounts = accounts.length;
    checkAccount(accounts, 0);
    function checkAccount(accounts, i){
        //accountsToSend.push($(accounts[i]).text());
        console.log($(accounts[i]).text());
        i++;
        if (i<nbAccounts)
            checkAccount(accounts, i);
        else {
            endScrap();
        }
    }
        
    function endScrap(){
        setTimeout(function(){
            popup.removeChild(text3);
            text2.innerText = "Redirecting to Ease.";   
            //window.location.href = "https://ease.space";
        }, 500);
        progressLoading(progressTotal, 300);
    }
});

