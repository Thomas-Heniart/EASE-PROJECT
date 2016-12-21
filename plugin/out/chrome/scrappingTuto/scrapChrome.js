extension.runtime.onMessage("checkChromeCo", function(msg, sendResponse){
    if($("#Passwd").length!=0){
        sendResponse(false);
    } else {
        sendResponse(true);
    }
});

extension.runtime.onMessage("typePasswordChrome", function(msg, sendResponse){
    $("#Passwd").val(msg.pass);
    $("#signIn").click();
});

extension.runtime.onMessage("connectToChrome", function(msg, sendResponse){
    $("#Email").val(msg.login);
    $("#next").click();
    function waitForPass(){
        if($(".form-error").length>0){
            sendResponse(false);
        } else if ($("#Passwd").length!=0){
            $("#Passwd").val(msg.pass);
            $("#signIn").click();
            sendResponse(true);
        } else {
            setTimeout(waitForPass,100);
        }
    }
    waitForPass();
});

extension.runtime.onMessage("scrapChrome", function(msg, sendResponse){
  console.log("start scrap");
    function waitload(callback){
        if($(".z-Of.cba.z-op").length != 0){
            console.log("start scrap");
            callback();
        } else if($(".gga").length != 0){
            console.log("no pass");
            sendResponse([]);
        } else {
            console.log("wait pass");
            setTimeout(function(){waitload(callback);},100);
        }
    }

    waitload(function(){
        var loadingString = "";
        var waitingTime = 5;
        console.log($(".z-Of.cba.z-op").length);
        var nbOfPass = $(".z-Of.cba.z-op").length;
        var results = [];
        console.log(nbOfPass);
        function getPass(index){
            console.log(index);
            var element = $(".z-Of.cba.z-op").get(index);
            var field = $(element).find(".bba.EW.a-Fa");
            function waitPass(){
                if($(field).val()!="••••••••" && $(field).val()!=loadingString){
                    if(waitingTime == 5){//Get the string "Chargement en cours" whatever language
                        loadingString = $(field).val();
                        waitingTime = 50;
                        setTimeout(waitPass, waitingTime);
                    } else {
                        if($(element).find(".CW").text()!=""){
                             results.push({website:$(element).find(".Zaa").text(),login:$(element).find(".CW").text(), pass:$(field).val()});
                        }
                        if(index+1 < nbOfPass)
                            getPass(index+1);
                        else{
                            $(element).find(".Vaa.AW.aga").click();
                            sendResponse(results);
                        }
                    }
                } else {
                    setTimeout(waitPass, waitingTime);
                }
            }
            if($(element).find(".Vaa.AW.BW").length!=0){
                $(element).find(".Vaa.AW.BW").click();
                waitPass();
            } else {
                if(index+1 < nbOfPass)
                    getPass(index+1);
                else{
                    $(element).find(".Vaa.AW.aga").click();
                    sendResponse(results);
                }   
            }
            
        }
        getPass(0);
    });
});