deleteAccountPopup = function (rootEl) {
    var self = this;
    this.qRoot = $(rootEl);
    this.parentHandler = this.qRoot.closest('#easePopupsHandler');

    this.errorRowHandler = this.qRoot.find('.errorHandler');

    this.passwordInputHander = this.qRoot.find("input[name='password']");

    this.formHandler = this.qRoot.find("#deleteAccountForm");
    this.submitButton = this.qRoot.find("button[type='submit']");
    this.goBackButtonHandler = this.qRoot.find("#goBack");

    this.passwordInputHander.keyup(function (e) {
        if (e.which == 13) {
            self.submitButton.click();
        }
    });
    this.goBackButtonHandler.click(function () {
        self.close();
    });
    this.passwordInputHander.on('input', function () {
        if ($(this).val().length)
            self.submitButton.removeClass('locked');
        else
            self.submitButton.addClass('locked');
    });
    this.formHandler.submit(function (e) {
        e.preventDefault();
        if (!(self.passwordInputHander.val().length))
            return;
        self.errorRowHandler.removeClass('show');
        self.submitButton.addClass('loading');
        ajaxHandler.post("/api/v1/common/DeleteAccount", {
            password: cipher(self.passwordInputHander.val())
        }, function () {
            self.submitButton.removeClass('loading');
        }, function (data) {
            easeTracker.trackEvent('DeleteAccount');
            location.reload();
        }, function (msg) {
            self.errorRowHandler.find('p').text(msg);
            self.errorRowHandler.addClass('show');
        });
        /* postHandler.post(
            'DeleteAccount',
            {
                password: self.passwordInputHander.val()
            },
            function () {
                self.submitButton.removeClass('loading');
            },
            function (msg) {
                easeTracker.trackEvent('DeleteAccount');
                location.reload();
            },
            function (msg) {
                self.errorRowHandler.find('p').text(msg);
                self.errorRowHandler.addClass('show');
            },
            'text'
        );*/
    });
    this.currentProfile = null;
    this.resetPasswordShows = function () {
        self.qRoot.find("input[name='password']").attr('type', 'password');
        self.qRoot.find('.showPassDiv.show').removeClass('show');
    }
    this.reset = function () {
        self.passwordInputHander.val('');
        self.resetPasswordShows();
        self.submitButton.addClass('locked');
        self.errorRowHandler.removeClass('show');
    }
    this.open = function () {
        currentEasePopup = self;
        self.reset();
        self.parentHandler.addClass('myshow');
        self.qRoot.addClass('show');
    }
    this.close = function () {
        self.qRoot.removeClass('show');
        self.parentHandler.removeClass('myshow');
    }
}

var easeDeleteAccountPopup;
$(document).ready(function () {
    easeDeleteAccountPopup = new deleteAccountPopup($('#deleteAccountPopup'));
});