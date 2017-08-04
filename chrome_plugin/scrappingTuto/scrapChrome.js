extension.runtime.onMessage("checkChromeCo", function(msg, sendResponse){
    if($("div#password input[name='password'], #identifierId").length!=0){
        sendResponse(false);
    } else {
        sendResponse(true);
    }
});

extension.runtime.onMessage("typePasswordChrome", function(msg, sendResponse){
  $("div#password input[name='password']").val(msg.pass);
  $("#passwordNext").click();
});

extension.runtime.onMessage("connectToChrome", function(msg, sendResponse){
    $("#identifierId").val(msg.login);
    $("#identifierNext").click();
    function waitForPass(){
        console.log($("div#password input[name='password']"));
        if($("#identifierId[aria-invalid='true']").length>0){
            sendResponse(false);
        } else if ($("div#password input[name='password']").length!=0){
            $("div#password input[name='password']").val(msg.pass);
            $("#passwordNext").click();
            sendResponse(true);
        } else {
            setTimeout(waitForPass,100);
        }
    }
    waitForPass();
});

extension.runtime.onMessage("scrapChrome", function(msg, sendResponse){
    function waitload(callback){
        if($("div[jscontroller='VXdfxd']").length != 0){
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

    function waitPass(field, loadingString, waitingTime){
        console.log("Field val: " + field.val());
        if(field.val()!="••••••••" && field.val() != loadingString){
            if(waitingTime == 5){//Get the string "Chargement en cours" whatever language
                loadingString = field.val();
                waitingTime = 50;
                setTimeout(waitPass, waitingTime);
            } else {
              console.log("pass: " + field.val());
                /* if($(element).find(".CW").text()!=""){
                     results.push({website:$(element).find(".Zaa").text(),login:$(element).find(".CW").text(), pass:$(field).val()});
                } */
                /* if(index+1 < nbOfPass)
                    getPass(index+1);
                else{
                    $(element).find(".Vaa.AW.aga").click();
                    sendResponse(results);
                } */
            }
        } else {
            setTimeout(function() {
              waitPass(field, loadingString, waitingTime);
            }, waitingTime);
        }
    }

    /* it works */
    var passwordEyes = $(".mUbCce.fKz7Od.cXmCRb[role='button'][jsaction*='click:cOuCgd']");
    function clickAndGetPassword(index) {
      var eyeElem = $(passwordEyes[index]);
      eyeElem.click();
      var field = $("input", eyeElem.parent().parent());
      while (field.val() == "••••••••") {}
      setTimeout(function() {
        /* Get login, website and put in results */
        console.log(field.val());
      }, 200);
      if (index < passwordEyes.length) {
        setTimeout(function() {
          clickAndGetPassword(index + 1);
        }, 1000);
      }
    }

    waitload(function(){
        var loadingString = "";
        var waitingTime = 5;
        var passwordEyes = $(".mUbCce.fKz7Od.cXmCRb[role='button'][jsaction*='click:cOuCgd']");
        var nbOfPass = passwordEyes.length;
        var results = [];
        $(".mUbCce.fKz7Od.cXmCRb[role='button'][jsaction*='click:cOuCgd']").each(function() {
          $(this).click();
          var field = $("input", $(this).parent().parent());
          waitPass(field, loadingString, waitingTime);
          console.log(field.val() + " and " + field.text());
        });
        setTimeout(function() {
          sendResponse(results);
        }, 4000);
        /* function getPass(index){
            //console.log(index);
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
                if(index+1 < nbOfPass) {
                  setTimeout(2000, function() {
                    getPass(index+1);
                  });
                }
                else{
                    $(element).find(".Vaa.AW.aga").click();
                    sendResponse(results);
                }
            }

        }
        getPass(0); */
    });
});
