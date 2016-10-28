var Dashboard = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.columns = this.rootEl.find('.dashboardColumn');
	this.profileAdder = this.rootEl.find('.profileAdder');

	this.enterEditMode = function(){
		self.rootEl.addClass('editMode');
	}
	this.leaveEditMode = function(){
		self.rootEl.removeClass('editMode');
	}
	//profileAdder click event
	this.profileAdder.on('click', function(){
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
		$(self.columns[columnIdx]).css('width', '');
		$(self.columns[columnIdx]).append($(profile));
		$(profile).addClass('scaleOut12');
		setTimeout(function(){
			$(profile).removeClass('scaleOut12');
		}, 500);
		postHandler.post(
			'addProfile', 
			{
				name : 'Profile name',
				color : '#35a7ff'
			},
			function(){},
			function(retMsg){
				$(profile).attr('id', retMsg);
				$(profile).find('.SitesContainer').attr('id', retMsg);
				profiles.push(new Profile($(profile).find('.ProfileBox')));
			},
			function(retMsg){},
			'text'
			);
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
			forceFallback: true,
			onStart: function(evt){
				var item = $(evt.item);
				$('body').css('cursor', 'move');
				self.columns.each(function(){
					if (!($(this).find('.item').length)){
						$(this).width('24.8%');
					}
				});
			},
			onEnd: function(evt){
				var item = $(evt.item);
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