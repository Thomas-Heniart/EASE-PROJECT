var constructorInput = function (rootEl, parent) {
	var self = this;
	this.qInput = rootEl;
	this.oParent = parent;
	this.qInput.on('focus', function(e) {
		$(this).prop('readonly', false);
	});
	this.qInput.on('change', function() {
		self.val($(this).val());
	});
	this.qInput.prop('readonly', true);
	this.qInput.prop("autocomplete", "off");
	this.listeners = [];
	this.listenBy = function (qBy) {
		self.listeners.push(qBy);
	}
	this.isValid = false;
	this.validate = function () {
		return true;
	};
	this.onEnter = function(callback) {
		console.log(self.qInput);
		self.qInput.keypress(function(e) {
			if (e.which == 13)
				callback(e);
		});
	};
	this.focus = function() {
		self.qInput.select();
		self.qInput.click();
		self.qInput.focus();
	}
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
		$(this).prop('readonly', false);
		self.qInput.val(value);
		var tmp = self.validate();
		if (tmp != self.isValid) {
			self.isValid = tmp;
			self.onStateChanged();
		}
		this.qInput.prop('readonly', true);
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
	HiddenInput : function (rootEl) {
		constructorInput.apply(this, arguments);
		var self = this;
		this.isValid = true;
		self.val($(self.qInput).val());
		self.onStateChanged();
	},
	BasicInput : function (rootEl) {
		constructorInput.apply(this, arguments);
		var self = this;
		this.isValid = true;
		this.reset = function() {
			self.qInput.val("");
			self.qInput.removeClass("valid");
			self.qInput.removeClass("unValid");
		}
		self.val($(self.qInput.val()));
		self.onStateChanged();
	},
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
			var reg = /^[-A-Za-z0-9$@$!%.*#?&'()\[\]{}_<>=+\\/:;,]{8,}$/;
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

