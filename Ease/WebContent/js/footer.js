$(document).ready(function() {
  $("#buttonShow i").hover(function(e) {
	$(".footer").show("slide", { direction: "down" }, 500);
    $(".controls").css({"bottom":"45px"});
    $("#buttonShow").hide("slide", { direction: "down" }, 500);
    $(".homepageOnoffContainer").hide(0);

  });
  $(".footer").not(".credits").mouseleave(function() {
	$(".footer").hide("slide", { direction: "down" }, 500);
    $(".controls").css({"bottom":""});
    $("#buttonShow").show("slide", { direction: "down" }, 500);
    $(".homepageOnoffContainer").show(0);
  })
});
