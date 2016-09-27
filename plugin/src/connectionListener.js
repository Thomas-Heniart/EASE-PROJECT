//Override .submit() function to make it fire an event
var scriptEl = document.createElement( "script" );
scriptEl.innerHTML = "HTMLFormElement.prototype._nativeSubmit = HTMLFormElement.prototype.submit;"
    + "HTMLFormElement.prototype.submit = function submit() {"
    + "var submitEvent = document.createEvent('HTMLEvents');"
    + "submitEvent.initEvent('submit', true, true);"
    + "if (this.dispatchEvent(submitEvent)) {"
    + "    this._nativeSubmit.apply(this, arguments);"
    + "}"
    +"};";
scriptEl.async = false;
document.head.appendChild(scriptEl);

var scriptFB = document.createElement("script");
scriptFB.innerHTML = "FB._nativeGetLoginStatus = FB.getLoginStatus;"
    + "FB.getLoginStatus = function getLoginStatusFB(){"
    + "var fbEvent = document.createEvent('HTMLEvents');"
    + "fbEvent.initEvent('fbConnect', true, true);"
    + "if(this.dispatchEvent(fbEvent)){"
    + "    this._nativeGetLoginStatus.apply(this, arguments);"
    + " }"
    + "};";
scriptFB.async = false;
document.body.appendChild(scriptEl);

var allForms = [];
listenToForms();

document.body.addEventListener('fbConnect', function(){
    console.log("FACEBOOK CONNECT DUDE !");
});

document.body.addEventListener('DOMSubtreeModified', function (res) {
    if(!equalArrays(document.forms, allForms)){
        listenToForms();
    }
}, false);

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
    allForms = [];
    console.log("forms loaded");
    for (var i = 0; i < document.forms.length; i++) {
        allForms.push(document.forms[i]);
        listenToSubmit(document.forms[i]);
        listenToClick(document.forms[i]);
    }
}

function listenToSubmit(form){
     form.addEventListener("submit", function(res){
        var fields = res.target.getElementsByTagName("input");
        checkFields(res.target.getElementsByTagName("input"));
    });
}

function listenToClick(form){
    var buttons = form.getElementsByTagName("button");
    for (var i=0;i<buttons.length;i++){
        buttons[i].addEventListener("click", function(res){
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
        inputSubmit[k].addEventListener("click", function (res){
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
        }
        if(hasEmail && hasPassword){
            chrome.runtime.sendMessage({'name':'newConnectionToRandomWebsite', 'message':{'website':window.location.host, 'username':email}}, function(){
                console.log("Connection for email " + email + " on website " + window.location.host + " sent to Ease");
            });
        break;
        }
    }
}
                                       
function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}