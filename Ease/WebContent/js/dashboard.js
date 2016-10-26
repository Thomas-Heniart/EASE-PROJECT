var Dashboard = function(rootEl){
	var self = this;
	this.rootEl = rootEl;
	this.columns = this.rootEl.find('.dashboardColumn');

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
						$(this).width('25%');
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