var signUpPopup = function (elem) {
    var self = this;
    this.handler = $(elem).closest('.popupHandler');
    this.qRoot = $(elem);
    this.name;
    this.email;

    this.openDigitsConfirmation = function () {
        self.qRoot.find("#3").addClass("show");
        self.qRoot.find("#1").removeClass("show");
        self.qRoot.find("#2").removeClass("show");
        $("#3 #digits-email", self.qRoot).click(function (e) {
            e.preventDefault();
            ajaxHandler.post($("#1 form", self.qRoot).attr("action"),
                {
                    email: self.email,
                });
        });
        self.handler.addClass('myshow');
        $('body').css('overflow', 'hidden');
    }
    this.openRegistration = function () {
        self.qRoot.find('#2').addClass('show');
        self.qRoot.find("#3").removeClass("show");
        self.qRoot.find('#1').removeClass('show');
        self.handler.addClass('myshow');
        $('body').css('overflow', 'hidden');
    };
    this.open = function () {
        self.handler.addClass('myshow');
        $('body').css('overflow', 'hidden');
    };
    this.close = function () {
        self.handler.removeClass('myshow');
        $('body').css('overflow', '');
    };
    this.reset = function () {
        self.qRoot.find('#1').addClass('show');
        self.qRoot.find('#2').removeClass('show');
        self.qRoot.find("#3").removeClass("show")
        self.qRoot.find('.alert-message').removeClass('show');
        self.qRoot.find('button').removeClass('not-show');
    };
    $(document).click(function (e) {
        if ($(e.target).hasClass('popupHandler')) {
            self.close();
            self.reset();
            setTimeout(function () {
                $(e.target).css('display', '');
            }, 100);
        }
    });
    this.qRoot.find('#1 form').submit(function (e) {
        e.preventDefault();
        easeTracker.trackEvent("HomepageSignUp1");
        self.email = $(this).find("input[name='email']").val();
        self.name = $(this).find("input[name='name']").val();
        var loading = $(this).find('.loading');
        var submitButton = $(this).find(".submitButton");
        var alertMessage = $(this).find(".alert-message");

        if (!self.email.length || !self.name.length)
            return;

        loading.addClass('show');
        submitButton.addClass('not-show');
        ajaxHandler.post($(this).attr('action'),
            {
                email: self.email,
            },
            function () {
                loading.removeClass('show');
            },
            function () {
                self.openDigitsConfirmation();
            },
            function (retMsg) {
                alertMessage.text(retMsg);
                alertMessage.css('color', '#ec555b')
                alertMessage.addClass('show');
                setTimeout(function () {
                    alertMessage.removeClass('show');
                    submitButton.removeClass('not-show');
                }, 3000);
            });
    });
    this.qRoot.find("#3 form").submit(function (e) {
        e.preventDefault();
        self.digits = $(this).find("input[name='digits']").val();
        var loading = $(this).find('.loading');
        var submitButton = $(this).find(".submitButton");
        var alertMessage = $(this).find(".alert-message");

        if (!self.digits.length || !self.email.length)
            return;
        loading.addClass("show");
        submitButton.addClass("not-show");
        ajaxHandler.post($(this).attr("action"),
            {
                email: self.email,
                digits: self.digits
            }, function () {
                loading.removeClass("show");
            }, function () {
                self.openRegistration();
            }, function (msg) {
                loading.removeClass("show");
                alertMessage.text(msg);
                alertMessage.css('color', '#ec555b')
                alertMessage.addClass('show');
                setTimeout(function () {
                    alertMessage.removeClass('show');
                    submitButton.removeClass('not-show');
                }, 3000);
            })
    });
    this.qRoot.find('#2 form').submit(function (e) {
        e.preventDefault();
        var password = $(this).find("input[name='password']").val();
        var confirmPassword = $(this).find("input[name='confirmPassword']").val();

        var loading = $(this).find('.loading');
        var submitButton = $(this).find(".submitButton");
        var alertMessage = $(this).find(".alert-message");
        if ((!self.name.length || !self.email.length || (password !== confirmPassword) || self.digits.length) == false)
            return;
        loading.addClass('show');
        submitButton.addClass('not-show');
        ajaxHandler.post($(this).attr('action'),
            {
                email: self.email,
                username: self.name,
                password: password,
                digits: self.digits,
                registration_date: new Date().getTime(),
                newsletter: false
            },
            function () {
                loading.removeClass('show');
            },
            function () {
                alertMessage.text("Successfully registered");
                alertMessage.css('color', '#24d666');
                alertMessage.addClass('show');
                easeTracker.setUserId(self.email);
                easeTracker.trackEvent("HomepageSignUp2");
                easeTracker.trackEvent("Connect");
                setTimeout(function () {
                    window.location = "/";
                }, 750);
            },
            function (retMsg) {
                alertMessage.text(retMsg);
                alertMessage.css('color', '#ec555b');
                alertMessage.addClass('show');
                setTimeout(function () {
                    alertMessage.removeClass('show');
                    submitButton.removeClass('not-show');
                }, 3000);
            });
    });
    var regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/;
    $("input[name='password']").keyup(function (e) {
        var password_value = $("input[name='password']").val();
        $("#validatorPassword").css("display", "inline-block");
        if (!regex.test(password_value))
            $("#validatorPassword").removeClass('valid');
        else
            $("#validatorPassword").addClass('valid');
        if (!regex.test(password_value) || $("input[name='confirmPassword']").val() != password_value)
            $("#validatorConfirmPass").removeClass('valid');
        else
            $("#validatorConfirmPass").addClass('valid');
    });
    $("input[name='confirmPassword']").keyup(function (e) {
        $("#validatorConfirmPass").css("display", "inline-block");
        if (!regex.test($("input[name='password']").val()) || $("input[name='confirmPassword']").val() != $("input[name='password']").val())
            $("#validatorConfirmPass").removeClass('valid');
        else
            $("#validatorConfirmPass").addClass('valid');
    });
    $("#2 input[name='password']").complexify({
        strengthScaleFactor: 0.7
    }, function (valid, complexity) {
        $(".progress .progress-bar").css('width', complexity + "%");
        if (complexity < 20) {
            $(".progress .progress-bar").css('background-color', '#e4543b');
        } else if (complexity < 40) {
            $(".progress .progress-bar").css('background-color', '#e07333');
        } else if (complexity < 60) {
            $(".progress .progress-bar").css('background-color', '#ead94a');
        } else if (complexity < 80) {
            $(".progress .progress-bar").css('background-color', '#ddf159');
        } else {
            $(".progress .progress-bar").css('background-color', '#b0df33');
        }
    });
};
var easeSignUpPopup = null;
$(document).ready(function () {
    easeSignUpPopup = new signUpPopup($('#signUpPopup'));
});
