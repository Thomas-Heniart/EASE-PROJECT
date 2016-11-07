var profileAdder = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.isOpen = false;
	this.colorList = [];
	this.colorChoosen = null;
	this.colorChooser = this.rootEl.find('.colorChooser');
	this.colorHolders = this.rootEl.find('.colorHolder');
	this.colorButtons = this.colorChooser.find('.colorHolder .color');
	this.headerPreview = this.rootEl.find('.profileHeaderPreview');
	this.profileNameInputDiv = this.headerPreview.find('.nameInput');
	this.nameInput = this.profileNameInputDiv.find('input');
	this.confirmButton = this.headerPreview.find('.confirm');
	this.triggerButton = this.rootEl.find('.opener');

	this.colorButtons.each(function(){
		var color = $(this).attr('color');
		$(this).attr('color', '');
		$(this).click(function(){
			self.colorChoosen = color;
			self.profileNameInputDiv.css('background-color', color);
			self.headerPreview.addClass('show');
			self.colorChooser.find('.choosen').removeClass('choosen');
			$(this).addClass('choosen');
			setTimeout(function(){self.nameInput.focus();}, 100);
		});
	});
	this.getName = function(){
		return self.nameInput.val();
	};
	this.isValid = function(){
		if (this.colorChoosen != null && this.getName().length)
			return true;
		self.profileNameInputDiv.addClass('shake-anim');
  
    self.profileNameInputDiv.one('webkitAnimationEnd oanimationend msAnimationEnd animationend',   
    	function(e) {
	    self.profileNameInputDiv.removeClass('shake-anim');
	    });
    self.nameInput.focus();
		return false;
	};
	this.open = function(){
		this.isOpen = true;
		this.colorChoosen = null;
		this.nameInput.val('');
		this.triggerButton.addClass('show');
		this.colorChooser.addClass('show');
		self.colorHolders.each(function(){
			$(this).css('right', ((7 - $(this).index()) * 2.5) + "vw");
		});
		return true;
	}
	this.close = function(){
		this.isOpen = false;
		this.colorChoosen = null;
		this.nameInput.val('');
		this.colorChooser.removeClass('show');
		this.triggerButton.removeClass('show');
		this.headerPreview.removeClass('show');
		this.colorChooser.find('.choosen').removeClass('choosen');
		self.colorHolders.each(function(){
			$(this).css('right', '');
		});
		return true;
	}
	$(document).click(function(event){
		self.isOpen && !($(event.target).closest('.profileAdder').length) && self.close();
	});
	this.profileNameInputDiv.find('input').on('keyup', function(e){
		if (e.which == 13) {
			self.confirmButton.click();
		}
	});
	this.triggerButton.click(function(){
		self.isOpen && self.close() || self.open();
	});
};

var Dashboard = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.columns = this.rootEl.find('.dashboardColumn');
	this.isEditMode = false;
	this.adder = new profileAdder(this.rootEl.find('.profileAdder'));

	this.enterEditMode = function(){
		self.rootEl.addClass('editMode');
		self.isEditMode = true;
	}
	this.leaveEditMode = function(){
		self.rootEl.removeClass('editMode');
		self.isEditMode = false;
	}
	this.reinitColumns = function(){
		this.columns.each(function(){
			if (!($(this).find('.item').length))
				$(this).css('width', '0px');
		});
	}
	if ($('.ProfilesHandler .item').length >= 15){
		this.adder.rootEl.css('display', 'none');
	}
	//profileAdder click event

	this.adder.confirmButton.on('click', function(){
		if (self.adder.isValid()){
			var profile = $($('#profileHelper').html());
			var a = 1000;
			var columnIdx = 0;
			for (var i = 0; i < self.columns.length; i++) {
				var tmp = 0;			
				$(self.columns[i]).find('.item').each(function(){
					var appsNb = $(this).find('.siteLinkBox').length;
					tmp += (appsNb < 7) ? 2 : (appsNb + 2) / 3;
				});
				if (tmp < a){
					a = tmp;
					columnIdx = i;
				}
			}
			$(profile).addClass('scaleOut12');
			setTimeout(function(){
				$(profile).removeClass('scaleOut12');
			}, 500);
			if ((profiles.length + 1) >= 15){
				self.adder.rootEl.css('display', 'none');
			}
			var Name = self.adder.getName();
			var Color = self.adder.colorChoosen;
			var newProfile = new Profile($(profile).find('.ProfileBox'));
			newProfile.setName(Name);
			newProfile.setColor(Color);
			profiles.push(newProfile);	
			$(self.columns[columnIdx]).css('width', '');
			$(self.columns[columnIdx]).append($(profile));
			self.adder.close();
			postHandler.post(
				'addProfile', 
				{
					name : Name,
					color : Color
				},
				function(){},
				function(retMsg){
					newProfile.setId(retMsg);
					easeTracker.trackEvent('Profile added');
				},
				function(retMsg){
					newProfile.remove();
					delete newProfile;
					self.adder.rootEl.css('display', '');
				},
				'text'
				);
		}
	});
	//drag and drop initialization
	this.columns.each(function(){
		if (!($(this).find('.item').length)){
			$(this).width(0);
		}
		$(this).sortable({
			animation: 300,
			group:"profiles",
			handle: ".ProfileName",
			filter: ".ProfileSettingsButton",
			forceFallback: true,
			onStart: function(evt){
				var item = $(evt.item);

				item.css('transition', 'transform 0s')
				$('body').css('cursor', 'move');
				self.columns.each(function(){
					if (!($(this).find('.item').length)){
						$(this).width('24.8%');
					}
				});
			},
			onEnd: function(evt){
				var item = $(evt.item);

				item.css('transition', '')
				$('body').css('cursor', '');
				self.columns.each(function(){
					if (!($(this).find('.item').length)){
						$(this).width(0);
					}
				});
				postHandler.post(
					"moveProfile",
					{
						columnIdx: item.parent().index() + 1,
						profileIdx: item.index(),
						profileId: item.attr('id')
					},
					function(){},
					function(retMsg){},
					function(retMsg){},
					'text'
					);
			}
		});
	});
}

var easeDashboard;
$(document).ready(function(){
	easeDashboard = new Dashboard($('.ProfilesHandler'));
});