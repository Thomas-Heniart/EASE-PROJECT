var Dashboard = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.columns = this.rootEl.find('.dashboardColumn');
	this.isEditMode = false;
	this.adder = new profileAdder(this.rootEl.find('#profileAdder'));

	this.enterEditMode = function(){
		self.rootEl.addClass('editMode');
		self.isEditMode = true;
	}
	this.leaveEditMode = function(){
		self.rootEl.removeClass('editMode');
		self.adder.close();
		self.isEditMode = false;
	}
	this.reinitColumns = function(){
		this.columns.each(function(){
			if (!($(this).find('.item').length))
				$(this).css('width', '0px');
		});
	}
	if ($('.ProfilesHandler .item').length >= 15){
		this.adder.qRoot.css('display', 'none');
	}
	//profileAdder click event
	if (this.adder.qRoot){
		this.adder.submitButton.on('click', function(){
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
					self.adder.qRoot.css('display', 'none');
				}
				var Name = self.adder.getName();
				var Color = self.adder.choosenColor;
				var newProfile = new Profile($(profile).find('.ProfileBox'));
				newProfile.setName(Name);
				newProfile.setColor(Color);
				profiles.push(newProfile);	
				$(self.columns[columnIdx]).css('width', '');
				$(self.columns[columnIdx]).append($(profile));
				self.adder.close();
				postHandler.post(
					'AddProfile', 
					{
						name : Name,
						color : Color
					},
					function(){},
					function(retMsg){
						newProfile.setId(retMsg);
						easeTracker.trackEvent('AddProfile', {"profieName":Name, "profileColor":Color});
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
	}
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
				$('.ProfileBox .ProfileContent').css('pointer-events', 'none');
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
				$('.ProfileBox .ProfileContent').css('pointer-events', '');
				self.columns.each(function(){
					if (!($(this).find('.item').length)){
						$(this).width(0);
					}
				});
				postHandler.post(
					"MoveProfile",
					{
						columnIdxDest: (item.parent().index() + 1),
						positionDest: item.index(),
						profileId: item.attr('id')
					},
					function(){},
					function(retMsg){
						easeTracker.trackEvent("MoveProfile");
					},
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
	var urlParams = window.location.search.substring(1,window.location.search.length).split("&");
	for(var i in urlParams){
		var param = urlParams[i].split("=")[0];
		var value = urlParams[i].split("=")[1];
		if(param=="openCatalog" && value=="true"){
			enterEditMode();
		}
	}
});


var profileAdder = function(elem){
	var self = this;
	this.qRoot = $(elem);

	this.inputNameHandler = this.qRoot.find("input[name='name']");
	this.inputNameDiv = this.qRoot.find('.input');
	this.openButton = this.qRoot.find('.opener');
	this.closeButton=  this.qRoot.find('.closer');
	this.adderBody = this.qRoot.find('.adder');
	this.submitButton = this.qRoot.find("button[type='submit']");
	if (!elem.length){
		this.qRoot == null;
		return;
	}
	/* ------COLOR CHOOSE------- */
	this.colors = [];/* obj.color obj.qRoot */
	this.choosenColor = null;
	this.inputNameHandler.keyup(function(e){
		if (e.which == 13){
			self.submitButton.click();
		}
	});
	this.qRoot.find('.colorHolder').each(function(index, elem){
		var tmp = new Object();
		tmp.color = $(elem).find('.color').attr('color');
		tmp.qRoot = $(elem);
		tmp.qRoot.click(function(){
			self.scaleInInput();
			self.setColor(tmp.color);
		});
		self.colors.push(tmp);
	});
	this.setColor = function(color){
		self.choosenColor = color;
		self.inputNameHandler.css('background-color', color);
	}
	/* ------COLOR CHOOSE END----- */
	/* ------SUGGESTIONS----- */
	this.suggestionsDivHandler = this.qRoot.find('.suggestionsRow .suggestions');
	this.greyColors = ["#E2E2E2", "#D5D5D5", "#D4D0D0"];
	this.suggestions = [
	"Me",
	"Work",
	"Internship",
	"School",
	"Shopping",
	"Team work",
	"Freelance",
	"Love it",
	"Travel",
	"Discover",
	"Learning",
	"Tools"];
	this.suggestionsDiv = [];
	this.createSuggestionDiv = function(name, color){
		var div = $(
			'<div class="suggestion">'
			+'<p class="name" style="background-color:'+color+';">'+name+'</p>'
			+'</div>'
			);
		div.find('p').click(function(){
			self.inputNameHandler.focus();
			self.inputNameHandler.val(name);
			self.scaleInInput();
			self.setColor(color);
		});
		return div;
	}
	for (var i = 0; i < self.suggestions.length; i++) {
		var tmp = self.createSuggestionDiv(self.suggestions[i], self.colors[i % self.colors.length].color);
		this.suggestionsDiv.push(tmp);
		self.suggestionsDivHandler.append(tmp);		
	}
	/* ------SUGGESTIONS END----- */

	this.isValid = function(){
		if (self.inputNameHandler.val().length && self.choosenColor)
			return true;
		easeAnimations.animateOnce(self.inputNameDiv, 'shake-anim');
		return	false;
	}
	this.scaleInInput = function(){
		easeAnimations.animateOnce(self.inputNameDiv, 'scaleinAnimation');
	}
	this.getName = function(){
		return self.inputNameHandler.val();
	}
	this.getColor = function(){
		return self.choosenColor;
	}
	this.reset = function(){
		self.inputNameHandler.val('');
		self.choosenColor = "#373B60";
		self.inputNameHandler.css('background-color','');
	}
	this.open = function(){
		self.reset();
		self.openButton.addClass('hide');
		self.adderBody.addClass('show');
		$('.col-left').stop().animate({
			scrollTop: $('.col-left')[0].scrollHeight
		}, 800);
	}
	this.close = function(){
		self.openButton.removeClass('hide');
		self.adderBody.removeClass('show');
	}
	self.openButton.click(function(){
		self.open();
	});
	self.closeButton.click(function(){
		self.close();
	});
}
