if(window === window.top){

var allForms = [];
var allButtonsLogWith = [];
var handledLogWith = [{name:"facebook", short:"fb"}, {name:"linkedin", short:"lnkdn"}];
listenToForms();
checkForms();

function checkForms(){
    setTimeout(function(){
        listenToForms();
        checkForms();
    },500);
}

function equalArrays (array1, array2) {
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
    for (var i = 0; i < document.forms.length; i++) {
        var id = document.forms[i].getAttribute("efi-attribute");
        if(id == null){
            document.forms[i].addEventListener("submit", function easeSubmitListener(res){
                checkFields(res.target.getElementsByTagName("input"));
            });
            allForms.push(document.forms[i]);
            var att = document.createAttribute("efi-attribute");
            att.value = "secured-form";
            document.forms[i].setAttributeNode(att);         
        }        
    }
}

function checkFields(fields){
    var hasPassword = false;
    var hasLogin = false;
    var passIdx = null;
    for (var j = 0; j<fields.length; j++){
        if(fields[j].type=="password"){
            hasPassword = true;
            var password = fields[j].value;
            passIdx = j;
            break;
        } 
    }
    if(hasPassword){
        for (var j = passIdx-1; j >= 0; j--) {
            if((fields[j].type=="text" || fields[j].type=="email" || fields[j].type==null) && (validateEmail(fields[j].value) || isLoginField(fields[j].name))){
                hasLogin = true;
                var username = fields[j].value;
                break;
            }
        }
        if(hasLogin){
            extension.runtime.sendMessage('newConnectionToRandomWebsite', {'website':window.location.host, 'username':username, 'password':password}, function(){});
        }
    }
}
                                       
function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function isLoginField(name){
    if(name.indexOf("login")==0 || name.indexOf("user")==0)
        return true;
    return false;
}
    
}