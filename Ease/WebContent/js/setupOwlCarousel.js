function setupOwlCarousel(){
		$('.owl-carousel').owlCarousel({
			items : 3,
			itemsCustom : false,
			itemsDesktop : [ 1199, 3 ],
			itemsDesktopSmall : [ 980, 3 ],
			itemsTablet : [ 768, 3 ],
			itemsTabletSmall : false,
			itemsMobile : [ 479, 1 ],
			singleItem : false,
			itemsScaleUp : false,
			pagination : false,
			touchDrag : false,
			mouseDrag : false,
			afterInit: function () {
				$('.owl-carousel').find('.owl-wrapper').each(function () {
					var w = $(this).width() / 2;
					$(this).width(w);
					$(this).css('margin', '0 auto');
				});
			},
			afterUpdate: function () {
				$('.owl-carousel').find('.owl-wrapper').each(function () {
					var w = $(this).width() / 2;
					$(this).width(w);
					$(this).css('margin', '0 auto');
				});
			}		
		});
		$('.owl-wrapper').sortable({
			animation: 300,
			group:"profiles",
			handle: ".ProfileName",
			forceFallback: true,
			onStart: function(evt){
				var item = $(evt.item);
				$('body').css('cursor', 'move');
				item.css({
					'pointer-events': 'none',
					'opacity': '0'
				});
			},
			onEnd: function(evt){
				var item = $(evt.item);
				$('body').css('cursor', '');
				item.css({
					'pointer-events': '',
					'opacity': ''
				});
				if (evt.oldIndex != evt.newIndex){
					postHandler.post(
						"moveProfile",
						{
							profileId: item.find('.item').attr('id'),
							index: item.index() + 1
						},
						function(){},
						function(retMsg){},
						function(retMsg){},
						'text'
					);
				}
			}
		});
	}
	$(document).ready(function() {
		setupOwlCarousel();
		$('.owl-carousel').on('mousewheel', '.owl-stage', function(e) {
			if (e.deltaY > 0) {
				$('.owl-carousel').trigger('next.owl');
			} else {
				$('.owl-carousel').trigger('prev.owl');
			}
			e.preventDefault();
		});
	});