if (window.location.href.indexOf("https://ease.space") != 0 && window.location.href.indexOf("http://localhost:8080") != 0 && window.location.href.indexOf("https://localhost:8443/*") != 0 && window.location.href.indexOf("http://51.254.207.91/*") != 0) {

    var allForms = [];
    checkForms();
    var openedByEase = false;

    function checkForms() {
        listenToForms();
        setTimeout(function () {
            checkForms();
        }, 500);
    }

    function listenToForms() {
        for (var i = 0; i < document.forms.length; i++) {
            var id = document.forms[i].getAttribute("efi-attribute");
            if (id == null) {
                document.forms[i].addEventListener("submit", function easeSubmitListener(res) {
                    checkFields(res.target.getElementsByTagName("input"));
                });
                allForms.push(document.forms[i]);
                var att = document.createAttribute("efi-attribute");
                att.value = "secured-form";
                document.forms[i].setAttributeNode(att);
            }
        }
    }

    function checkFields(fields) {
        var hasPassword = false;
        var hasLogin = false;
        var passIdx = null;
        for (var j = 0; j < fields.length; j++) {
            if (fields[j].type == "password") {
                hasPassword = true;
                var password = fields[j].value;
                passIdx = j;
                break;
            }
        }
        if (hasPassword && !openedByEase) {
            for (var j = passIdx - 1; j >= 0; j--) {
                if ((fields[j].type == "text" || fields[j].type == "email" || fields[j].type == null) && (validateEmail(fields[j].value) || isLoginField(fields[j].name))) {
                    hasLogin = true;
                    var username = fields[j].value;
                    break;
                }
            }
            if (hasLogin) {
                extension.sendMessage('newFormSubmitted', {
                    'update': {
                        'website': window.location.host,
                        'username': username,
                        'password': password
                    }
                }, function () {});
            }
        }
    }


    function validateEmail(email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    }

    function isLoginField(name) {
        if (name.indexOf("login") == 0 || name.indexOf("user") == 0)
            return true;
        return false;
    }

    $("[class*='facebook']").click(function () {
        if (!openedByEase) {
            extension.sendMessage("logWithButtonClicked", {
                "button": $(this),
                "logWithWebsite": "Facebook",
                "hostLogWithWebsite": "www.facebook.com"
            }, function (response) {});
        }
    });

    $("[class*='linkedin']").click(function () {
        if (!openedByEase) {
            extension.sendMessage("logWithButtonClicked", {
                "button": $(this),
                "logWithWebsite": "Linkedin",
                "hostLogWithWebsite": "www.linkedin.com"
            }, function (response) {});
        }
    });

}
