var popups = [];

var closeAllPopups = function () {
	console.log("test");
	popups.map(function(aPopup) {
		aPopup.close();
	});
};

var constructorPopup = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.oForm;
	this.qCloseButton;
	this.qCloseButtons;
	self.qCloseButtons = [];
	this.qRoot.find('form').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oForm = new Form[oClass]($(elem), self);
		}
	});
	this.qRoot.find('button').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass == "CloseButton") {
			var tmpButton = $(elem, self);
			self.qCloseButtons.push(tmpButton);
		}
	});
	self.qCloseButtons.forEach(function(element) {
		element.click(function() {
			self.close();
		})
	});
	popups.push(self);
	this.open = function () {
		self.oForm.reset();
		self.setVal(arguments);
		self.qRoot.addClass('md-show');
		setTimeout(self.postOpen, 100);
	};
	this.close = function () {
		self.oForm.reset();
		self.qRoot.removeClass('md-show');
	};
	this.postOpen = function(){

	};
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
		this.postOpen = function(){
			self.oForm.oInputs[1].qInput.focus();			
		}
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
	},
	AddUpdatePopup : function() {
		constructorPopup.apply(this, arguments);
		var self = this;
		this.title = self.qRoot.find('.title');
		this.text = self.qRoot.find('.text');
		this.error = self.qRoot.find('.error');
		this.setVal = function (arg) {
			self.oForm.oInputs[4].val($(arg[1]).attr("siteName"));
			self.qRoot.find("img").attr("src", $(arg[1]).find('img').attr("src"));
			postHandler.post("checkVerifiedEmail", {"email": $(arg[1]).attr("login")},
				function () {}, 
				function (msg) {
					self.oForm.oInputs[0].val($(arg[1]).attr("siteId"));
					self.updateIndex = $(arg[1]).attr("index");
					console.log(self.updateIndex);
					self.oForm.oInputs[1].val(arg[0].id);
					self.oForm.oInputs[2].val($(arg[1]).attr("cryptedPassword"));
					self.oForm.oInputs[3].val($(arg[1]).attr("login"));
					var ret = msg.split(" ")[0];
					if (ret == "1"){
						self.title.html($(arg[1]).attr("login") + "<br>has not been confirmed.");
						self.text.html("An email was sent to your inbox.<br>Updates will work for this email after confirmation.");
					} else if (ret == "2") {
						self.title.html($(arg[1]).attr("login") + "<br>has been confirmed.");
						self.text.html("Click OK to add this website.");
					} else {
						self.text.html("An error has occured, please retry in a few minutes.");
					}
				}, 
				function (msg) {
					self.text.html("An error has occured, please retry in a few minutes.");
				});
		};
		this.close = function () {
			self.oForm.reset();
			self.title.html("Verifiying email...");
			self.text.html("");
			self.error.html("");
			self.qRoot.removeClass('md-show');
		};
	},
	RegisterPopup : function() {
		constructorPopup.apply(this, arguments);
		var self = this;
		this.open = function () {
			self.oForm.reset();
			self.setVal(arguments);
			self.qRoot.addClass('md-show');
			$(".md-overlay", self.qRoot.parrent).addClass("md-show");
		};
		
		this.close = function () {
			this.close = function () {
				self.oForm.reset();
				self.qRoot.removeClass('md-show');
				$(".md-overlay", self.qRoot.parrent).removeClass("md-show");
			};
		}
	}
}