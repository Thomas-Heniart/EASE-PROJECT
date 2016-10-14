
var constructorPopup = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.oForm;
	this.qCloseButton;
	this.qRoot.find('form').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oForm = new Form[oClass]($(elem));
		}
	});
	this.qRoot.find('button').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass == "CloseButton") {
			self.qCloseButton = $(elem);
		}
	});
	this.open = function () {
		self.oForm.reset();
		self.setVal(arguments);
		self.qRoot.addClass('md-show');
	};
	this.close = function () {
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
		this.setVal = function (profileIndex) {
			self.oForm.oInputs[0].val(profileIndex);
		}
	},
	AddAppPopup : function () {
		constructorPopup.apply(this,arguments);
		var self = this;
		this.setForm = function(anObject) {
			self.oForm = anObject;
			self.oForm.setPopup(self);
		}
		this.setVal = function (name) {
			self.oForm.oInputs[0].val(name);
		};
		this.close = function() {
			self.qRoot.removeClass('md-show');
			self.reset();
			$('.classicLogin').css('display', 'block');
		}
	},
	DeleteAppPopup : function () {
		constructorPopup.apply(this,arguments);
		var self = this;
		this.setVal = function (appId) {
			self.oForm.oInputs[0].val(appId);
		};
	}
}