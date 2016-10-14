
var constructorForm = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.oInputs = [];
	this.oErrorMsg;
	this.qRoot.find('input').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oInputs.push(new Input[oClass]($(elem)));
		}
	});
	this.qRoot.find('div').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oErrorMsg = new ErrorMsg[oClass]($(elem));
		}
	});
	this.qButton = this.qRoot.find("button[type='submit']");
	this.qButton.click(function (e) {
		self.submit(e);
	});
	this.isEnabled = false;
	for (var i = 0; i < this.oInputs.length; ++i) {
		this.oInputs[i].listenBy(this.qRoot);
	}
	self.isEnabled = false;
	self.qButton.prop('disabled', true);
	self.qButton.removeClass("Active");
	
	
	this.enable = function () {
		self.isEnabled = true;
		self.qButton.prop('disabled', false);
		self.qButton.addClass("Active");
	};
	this.disable = function () {
		self.isEnabled = false;
		self.qButton.prop('disabled', true);
		self.qButton.removeClass("Active");
	};
	this.qRoot.on("StateChanged", function () {
		if (self.checkInputs())
			self.enable();
		else
			self.disable();
	});
	this.checkInputs = function () {
		for (var i = 0; i < self.oInputs.length; ++i) {
			if (self.oInputs[i].isValid == false) {
				return false;
			}
		}
		return true;
	};
	this.reset = function () {
		self.disable();
		for (var i = 0; i < self.oInputs.length; ++i) {
			self.oInputs[i].reset();
		}
	};
	this.submit = function () {
		
	}
}

var Form = {
	EditUserNameForm : function (rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.submit = function (e) {
			e.preventDefault();
			console.log("Change name");
		}
	},
	EditUserPasswordForm : function (rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.submit = function (e) {
			e.preventDefault();
			console.log("Change password");
		}
	},
	DeleteProfileForm : function (rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.submit = function (e) {
			e.preventDefault();
			console.log("Profile deleted");
		}
	},
	DeleteAppForm : function (rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.submit = function (e) {
			e.preventDefault();
			console.log("App deleted");
		}
	}
}