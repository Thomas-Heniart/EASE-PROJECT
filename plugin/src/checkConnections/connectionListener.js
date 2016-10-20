var allForms = [];
listenToForms();
checkForms();

function checkForms(){
    setTimeout(function(){
        if(!equalArrays(document.forms, allForms)){
            listenToForms();
        }
        checkForms();
    },500);
}

function equalArrays (array1, array2) {
    // compare lengths - can save a lot of time 
    if (array2.length != array1.length)
        return false;

    for (var i = 0, l=array2.length; i < l; i++) {
        // Check if we have nested arrays
        if (array2[i] instanceof Array && array1[i] instanceof Array) {
            // recurse into the nested arrays
            if (!array2[i].equals(array1[i]))
                return false;       
        }           
        else if (array2[i] != array1[i]) { 
            // Warning - two different object instances will never be equal: {x:20} != {x:20}
            return false;   
        }           
    }       
    return true;
}

function listenToForms (){
    console.log("listen : "+(new Date()).getTime());
    for (var i = 0; i < document.forms.length; i++) {
        var listen = true;
        for (var j = 0; j < allForms.length; j++){
            if(equalArrays(document.forms[i], allForms[j])){
                listen = false;
                break;
            }
        }
        if(listen){
            listenToSubmit(document.forms[i]);
            listenToClick(document.forms[i]);
            allForms.push(document.forms[i]);
            console.log("Add form : ");
            console.log(document.forms[i]);
        }
        
    }
}

function listenToSubmit(form){
     form.addEventListener("submit", function easeSubmitListener(res){
        var fields = res.target.getElementsByTagName("input");
        checkFields(res.target.getElementsByTagName("input"));
    });
}

function listenToClick(form){
    var buttons = form.getElementsByTagName("button");
    for (var i=0;i<buttons.length;i++){
        buttons[i].addEventListener("click", function easeClickListener(res){
            checkFields(form.getElementsByTagName("input"));
        });
    }
    var inputs = form.getElementsByTagName("input");
    var inputSubmit = [];
    for (var j=0; j<inputs.length; j++){
        if(inputs[j].type == "submit"){
            inputSubmit.push(inputs[j]);
        }
    }
    for (var k=0;k<inputSubmit.length;k++){
        inputSubmit[k].addEventListener("click", function easeClickListener(res){
            checkFields(form.getElementsByTagName("input"));
        });
    }
}

function checkFields(fields){
    var hasPassword = false;
    var hasEmail = false;
    for (var j = 0; j<fields.length; j++){
        if(validateEmail(fields[j].value)){
            hasEmail = true;
            var email = fields[j].value;
         }
         if(fields[j].type=="password"){
            hasPassword = true;
             var password = fields[j].value;
        }
        if(hasEmail && hasPassword){
            extensionLight.runtime.sendMessage('newConnectionToRandomWebsite', {'website':window.location.host, 'username':email, 'password':"???"}, function(){});
        break;
        }
    }
}
                                       
function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}