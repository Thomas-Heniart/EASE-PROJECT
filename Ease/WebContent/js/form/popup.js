var constructorPopup = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.oForm;
	this.qCloseButton;
	this.qRoot.find('form').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oForm = new Form[oClass]($(elem), self);
		}
	});
	this.qRoot.find('button').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass == "CloseButton") {
			self.qCloseButton = $(elem, self);
		}
	});
	this.open = function () {
		self.oForm.reset();
		self.setVal(arguments);
		self.qRoot.addClass('md-show');
	};
	this.close = function () {
		self.oForm.reset();
		self.qRoot.removeClass('md-show');
	};
	this.qCloseButton.click(function () {
		self.close();
	});
	this.setVal = function () {
		
	};
}

var Popup = {
	DeleteProfilePopup : function () {
		constructorPopup.apply(this,arguments);
		var self = this;
		self.targetProfile;

		this.setVal = function (arg) {
			self.oForm.oInputs[0].val(arg[0].id);
			self.targetProfile = arg[0];
		}
	},
	AddAppPopup : function () {
		constructorPopup.apply(this,arguments);
		var self = this;
		this.setVal = function(name) {
			self.oForm.oInputs[0].val(name);
		};
		this.close = function() {
			self.qRoot.removeClass('md-show');
			$('.classicLogin').addClass("show");
			$('.suggested-emails').removeClass("show");
		}
		this.setHelper = function(jqObj) {
			self.oForm.setHelper(jqObj);
		}
		this.setAppsContainer = function(jqObj) {
			self.oForm.setAppsContainer(jqObj);
		}
		this.setNewAppItem = function(item) {
			self.oForm.setNewAppItem(item);
		}
		this.appId = function(id) {
			self.oForm.app_id = id;
			if (id == null)
				self.oForm.setPostName('addApp');
		}
		this.setPostName = function(aString) {
			self.oForm.setPostName(aString);
		}
	},
	ModifyAppPopup : function() {
		constructorPopup.apply(this,arguments);
		var self = this;
		this.close = function() {
			self.qRoot.removeClass('md-show');
			$('.classicLogin').addClass("show");
			$('.suggested-emails').removeClass("show");
		}
		this.setApp = function(jObj) {
			self.oForm.setApp(jObj);
		}
	},
	DeleteAppPopup : function () {
		constructorPopup.apply(this,arguments);
		var self = this;
		this.app;
		this.setVal = function (arg) {
			self.oForm.oInputs[0].val(arg[0].attr("id"));
			self.app = arg[0];
		};
	},
	DeleteEmailPopup : function () {
		constructorPopup.apply(this, arguments);
		var self = this;
		this.setEmail = function(email) {
			self.oForm.oInputs[0].val(email);
			self.oForm.qRoot.trigger("StateChanged");
		}
	},
	PopupDeleteAccount : function() {
		constructorPopup.apply(this, arguments);
		var self = this;
		this.setForm = function(jForm) {
			var oClass = jForm.attr('oClass');
			if (oClass != null) {
				self.oForm = new Form[oClass]($(elem), self);
			}
		}
		self.qRoot.find("button[type='submit']").click(function(e) {
			e.preventDefault();
			self.close();
			self.oForm.submit(e);
		})
	}
}