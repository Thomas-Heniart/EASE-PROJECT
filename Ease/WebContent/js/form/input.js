

var constructorInput = function (rootEl) {
	var self = this;
	this.qInput = rootEl;
	this.qInput.on('focus', function(e) {
		$(this).prop('readonly', false);
	});
	this.qInput.on('blur', function(e) {
		$(this).prop('readonly', true);
	});
	this.qInput.prop('readonly', true);
	this.listeners = [];
	this.listenBy = function (qBy) {
		self.listeners.push(qBy);
	}
	this.isValid = false;
	this.validate = function () {
		return true;
	};
	this.onStateChanged = function () {
		for (var i = 0; i < self.listeners.length; ++i) {
			self.listeners[i].trigger("StateChanged");		
		}
		if (self.isValid) {
			self.qInput.removeClass("unValid");
			self.qInput.addClass("valid");
		} else {
			self.qInput.removeClass("valid");
			self.qInput.addClass("unValid");
		}
	};
	this.reset = function () {
		self.qInput.val("");
		self.qInput.removeClass("valid");
		self.qInput.removeClass("unValid");
		self.isValid = false;
	};
	this.val = function (value) {
		self.qInput.val(value);
		var tmp = self.validate();
		if (tmp != self.isValid) {
			self.isValid = tmp;
			self.onStateChanged();
		}
	};
	this.getVal = function () {
		return self.qInput.val();
	}
	this.qInput.on("keyup", function () {
		var tmp = self.validate();
		if (tmp != self.isValid) {
			self.isValid = tmp;
			self.onStateChanged();
		}
	});
}

var Input = {
	NoEmptyInput : function (rootEl) {
		constructorInput.apply(this,arguments);
		var self = this;
		this.validate = function () {
			return self.qInput.val().length > 0;
		}
	},
	EmailInput : function (rootEl) {
		constructorInput.apply(this,arguments);
		var self = this;
		this.validate = function () {
			var reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			return reg.test(self.qInput.val());
		}
	},
	PasswordInput : function (rootEl) {
		constructorInput.apply(this,arguments);
		var self = this;
		this.validate = function () {
			var reg = /^[A-Za-z0-9$@$!%.*#?&'()-_<>=+\\/:;,]{8,}$/;
			return reg.test(self.qInput.val());
		}
	},
	UrlInput : function (rootEl) {
		constructorInput.apply(this,arguments);
		var self = this;
		this.validate = function () {
			var reg = /^[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/;
			return reg.test(self.qInput.val());
		}
	}
}