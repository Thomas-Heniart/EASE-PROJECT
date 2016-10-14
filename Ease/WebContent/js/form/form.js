var constructorForm = function(rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.oInputs = [];
	this.qRoot.find('input').each(function(index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oInputs.push(new Input[oClass]($(elem)));
		}
	});
	this.qButton = this.qRoot.find("button[type='submit']");
	this.qButton.click(function(e) {
		self.submit(e);
	});
	this.isEnabled = false;
	for (var i = 0; i < this.oInputs.length; ++i) {
		this.oInputs[i].listenBy(this.qRoot);
	}
	self.isEnabled = false;
	self.qButton.prop('disabled', true);
	self.qButton.removeClass("Active");

	this.enable = function() {
		self.isEnabled = true;
		self.qButton.prop('disabled', false);
		self.qButton.addClass("Active");
	};
	this.disable = function() {
		self.isEnabled = false;
		self.qButton.prop('disabled', true);
		self.qButton.removeClass("Active");
	};
	this.qRoot.on("StateChanged", function() {
		if (self.checkInputs())
			self.enable();
		else
			self.disable();
	});
	this.checkInputs = function() {
		for (var i = 0; i < self.oInputs.length; ++i) {
			if (self.oInputs[i].isValid == false) {
				return false;
			}
		}
		return true;
	};
	this.reset = function() {
		self.disable();
		for (var i = 0; i < self.oInputs.length; ++i) {
			self.oInputs[i].reset();
		}
	};
	this.submit = function() {

	}
}

var Form = {
	EditUserNameForm : function(rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.submit = function(e) {
			e.preventDefault();
			console.log("Change name");
		}
	},
	EditUserPasswordForm : function(rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.submit = function(e) {
			e.preventDefault();
			console.log("Change password");
		}
	},
	AddAppForm : function(rooEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.submit = function(e) {
			e.preventDefault();
			e.stopPropagation();
			$.post(self.postName, {
				profileId : self.profile_id,
				siteId : self.site_id,
				name : self.oInputs[0].getVal(),
				login : self.oInputs[1].getVal(),
				password : self.oInputs[2].getVal(),
				appId : self.app_id
			}, function(data) {
				var retMsg = data.substring(4);
				self.appsContainer.append(self.newAppItem);
				self.newAppItem.find('.linkImage').addClass('scaleOutAnimation');
				setTimeout(function() {
					self.newAppItem.find('.linkImage').removeClass('scaleOutAnimation');
				}, 1000);
				self.newAppItem.find('.linkImage').attr('onclick', "sendEvent(this)");
				self.newAppItem.attr('login', self.oInputs[1].getVal());
				self.newAppItem.attr('webId', self.helper.attr('idx'));
				self.newAppItem.attr('name', self.oInputs[0].getVal());
				self.newAppItem.attr('logwith', 'false');
				self.newAppItem.find('.siteName p').text(self.oInputs[0].getVal());
				self.newAppItem.attr('id', retMsg);
				self.newAppItem.attr('ssoid', self.helper.attr('data-sso'));
				setupAppSettingButtonPopup(self.newAppItem.find('.showAppActionsButton'));
				self.reset();
				self.oPopup.close();
			});
		}
		this.newAppItem = null;
		this.oPopup = null;
		this.appsContainer = null;
		this.helper = null;
		this.site_id = null;
		this.profile_id = null;
		this.app_id = null;
		this.postName = 'addApp';
		this.setNewAppItem = function(qObject) {
			self.newAppItem = qObject;
		}
		this.setHelper = function(qObject) {
			self.helper = qObject;
		}
		this.setPopup = function(anObject) {
			self.oPopup = anObject;
		}
		this.setAppsContainer = function(qObject) {
			self.appsContainer = qObject;
		}
		this.siteId = function(id) {
			self.site_id = id;
		}
		this.profileId = function(id) {
			self.profile_id = id;
		}
		this.appId = function(id) {
			self.app_id = id;
			if (id == null) {
				self.postName = 'addApp';
				self.qRoot.trigger("StateChanged");
			}
		}
		this.setPostName = function(postName) {
			self.postName = postName;
			self.qRoot.trigger("StateChanged");
		}
		this.checkInputs = function() {
			if (self.postName != 'addApp')
				return true;
			for (var i = 0; i < self.oInputs.length; ++i) {
				if (self.oInputs[i].isValid == false) {
					return false;
				}
			}
			return true;
		}
	}
}