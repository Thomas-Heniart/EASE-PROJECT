
var constructorPopup = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
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
		self.qRoot.addClass('open');
	};
	this.close = function () {
		self.qRoot.removeClass('open');
	};
	this.qCloseButton.onClick(function () {
		self.qRoot.close();
	});
	this.setVal = function () {
		
	};
}

var Popup = {
	DeleteProfilePopup : function () {
		constructorPopup.apply(this,arguments);
		var self = this;
		this.setVal = function (profileIndex) {
			self.qForm.oInputs[0].val(profileIndex);
		}
	},
	AddAppPopup : function () {
		constructorPopup.apply(this,arguments);
		var self = this;
		this.setVal = function () {
			
		};
	}
}