/*!
 * Start Bootstrap - Agency v3.3.7+1 (http://startbootstrap.com/template-overviews/agency)
 * Copyright 2013-2016 Start Bootstrap
 * Licensed under MIT (https://github.com/BlackrockDigital/startbootstrap/blob/gh-pages/LICENSE)
 */
 ! function(t) {
    "use strict";
    t("a.page-scroll").bind("click", function(a) {
        var o = t(this);
        t("html, body").stop().animate({
            scrollTop: t(o.attr("href")).offset().top - 50
        }, 1250, "easeInOutExpo"), a.preventDefault()
    }), t(".navbar-collapse ul li a").click(function() {
        
    }), t("#mainNav").affix({
        offset: {
            top: 100
        }
    })
}(jQuery);

$(document).ready(function() {
            // Get media - with autoplay disabled (audio or video)
            var media = $('video').not("[autoplay='autoplay']");
            var tolerancePixel = 200;

            function checkMedia(){
                // Get current browser top and bottom
                var scrollTop = $(window).scrollTop() + tolerancePixel;
                var scrollBottom = $(window).scrollTop() + $(window).height() - tolerancePixel;

                media.each(function(index, el) {
                    var yTopMedia = $(this).offset().top;
                    var yBottomMedia = $(this).height() + yTopMedia;

                    if(scrollTop < yBottomMedia && scrollBottom > yTopMedia){ //view explaination in `In brief` section above
                        $(this).get(0).play();
                    } else {
                        $(this).get(0).pause();
                    }
                });

                //}
            }
            $(document).on('scroll', checkMedia);
        });