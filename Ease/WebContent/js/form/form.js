var constructorForm = function(rootEl, parent) {
	var self = this;
	this.qRoot = rootEl;
	this.oParent = parent;
	this.oInputs = [];
	this.param = {};
	this.qRoot.find('input').each(function(index, elem) {
	this.oErrorMsg;
	
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oInputs.push(new Input[oClass]($(elem), self));
		}
	});
	this.qRoot.find('div').each(function (index, elem) {
		var oClass = $(elem).attr('oClass');
		if (oClass != null) {
			self.oErrorMsg = new ErrorMsg[oClass]($(elem), self);
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
	this.submit = function(e) {
		e.preventDefault();
		self.oInputs.forEach(function (elem) {
			self.params[elem.qInput.attr('name')] = elem.getVal();
		});
		console.log(self.qRoot.attr('action'));
		self.beforeSubmit();
		postHandler.post(self.qRoot.attr('action'), params, self.afterSubmit, self.successCallback, self.errorCallback);
	};
	this.beforeSubmit = function () {
		
	};
	this.afterSubmit = function () {
		
	};
	this.successCallback = function () {
		
	};
	this.errorCallback = function () {
		
	}
}

var Form = {
	EditUserNameForm : function(rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
	},
	EditUserPasswordForm : function(rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
	},
	AddAppForm : function(rooEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.newAppItem = null;
		this.appsContainer = null;
		this.helper = null;
		this.site_id = null;
		this.profile_id = null;
		this.app_id = null;
		this.postName = 'addApp';
		this.helper = null;
		this.setHelper = function(jqHelper) {
			self.helper = jqHelper;
			self.site_id = self.helper.attr("idx");
		}
		this.setAppsContainer = function(qObject) {
			self.appsContainer = qObject;
			self.profile_id = self.appsContainer.closest('.item').attr('id');
		}
		this.setNewAppItem = function(qObject) {
			self.newAppItem = qObject;
		}
		this.beforeSubmit = function() {
			self.oParent.close();
			self.appsContainer.append(self.newAppItem);
			self.params = {
					profileId : self.profile_id,
					siteId : self.site_id,
					name : self.oInputs[0].getVal(),
					login : self.oInputs[1].getVal(),
					password : self.oInputs[2].getVal(),
					appId : self.app_id
				};
		}
		this.afterSubmit = function() {
			
		}
		this.successCallback = function() {
			self.newAppItem.find('.linkImage').addClass('scaleOutAnimation');
			setTimeout(function() {
				self.newAppItem.find('.linkImage').removeClass('scaleOutAnimation');
			}, 1000);
			self.newAppItem.find('.linkImage').attr('onclick', "sendEvent(this)");
			self.newAppItem.attr('login', self.oInputs[1].getVal());
			self.newAppItem.attr('webId', self.helper.attr('idx'));
			self.newAppItem.attr('name', self.oInputs[0].getVal());
			self.newAppItem.attr('logwith', (self.app_id == null) ? 'false' : self.app_id);
			self.newAppItem.find('.siteName p').text(
			self.oInputs[0].getVal());
			self.newAppItem.attr('id', retMsg);
			self.newAppItem.attr('ssoid', self.helper.attr('data-sso'));
			setupAppSettingButtonPopup(self.newAppItem.find('.showAppActionsButton'));
			$("#email-suggestions").append("<p class='email-suggested>@ <span>" + self.oInputs[1].getVal() + "</span>");
			self.reset();
		}
		this.errorCallback = function(retMsg) {
			self.newAppItem.remmove();
			self.reset();
			$(parent).find('.alertDiv').addClass('show');
    	  	showAlertPopup(retMsg, true);
		}
		this.setPostName = function(postName) {
			self.postName = postName;
			self.qRoot.attr("action", self.postName);
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
	},
	DeleteProfileForm : function (rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.beforeSubmit = function () {
			$('#loading').addClass('la-animate');
		}
		this.afterSubmit = function () {
			$('#loading').removeClass("la-animate");
		}
        this.successCallback = function(retMsg) {
			window.location.replace("index.jsp");
		};
		this.errorCallback = function(retMsg) {
			$(parent).find('.alertDiv').addClass('show');
  	  		$(parent).find('#password').val('');
    	  	showAlertPopup(retMsg, true);
		}
	},
	ModifyAppForm : function(rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.oPopup = null;
		this.app = null;
		this.password = null;
		this.appId = null;
		this.aId = null;
		this.setApp = function(jObject) {
			self.app = jObject;
			self.oInputs[0].val(self.app.attr("name"));
			self.oInputs[1].val(self.app.attr("login"));
			self.appId = self.app.attr("id");
			self.aId = self.app.attr("aid");
		}
		this.submit = function(e) {
			e.preventDefault();
			var AppToLoginWith = rootEl.find('.AccountApp.selected');
			console.log(AppToLoginWith);
			if (AppToLoginWith.length) {
				self.aId = AppToLoginWith.attr('aid');
			}
			self.login = self.oInputs[1].getVal();
			self.password = self.oInputs[2].getVal();
			$.post(
					'editApp',
					{
						name : self.oInputs[0].getVal(),
						appId : self.appId,
						lwId : self.aId,
						login : self.login,
						wPassword : self.password
					},
					function(data) {
						var retMsg = data.substring(4);
						var image = self.app.find('.linkImage');
						self.oParent.close();
						image.addClass('scaleOutAnimation');
						setTimeout(function() {
							image.removeClass('scaleOutAnimation');
						}, 1000);
						self.app.attr('login', self.oInputs[1].getVal());
						self.app.attr('name', self.oInputs[0].getVal());
						self.app.attr('logwith', (self.oInputs[1].getVal().length || self.aId) == null ? 'false' : self.aId);
						self.app.find('.siteName p').text(self.oInputs[0].getVal());
						self.app.find('.emptyAppIndicator').remove();
						self.app.removeClass('emptyApp');
					});
		}
	},
	DeleteAppForm : function (rootEl) {
		constructorForm.apply(this, arguments);
		var self = this;
		this.beforeSubmit = function () {
			$(self.oParent.app).find('.linkImage').addClass('easyScaling');
		}
		this.afterSubmit = function () {
			self.oParent.close();
			$(self.oParent.app).find('.linkImage').removeClass('easyScaling');
		}
        this.successCallback = function(retMsg) {
			$(self.oParent.app).find('.linkImage').addClass('deletingApp');
			setTimeout(function() {
				self.oParent.app.remove();
			}, 500);
		};
		this.errorCallback = function(retMsg) {
    	  	showAlertPopup(retMsg, true);
		}
	}
}