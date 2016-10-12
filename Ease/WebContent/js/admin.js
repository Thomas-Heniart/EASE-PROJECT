/* Buttons behavior */

$(".admin-menu button").click(function(e) {
	$(".RightSideViewTab.show").removeClass("show");
	var target = $(e.target).closest("button").attr("target");
	console.log($("#" + target));
	$("#" + target).addClass("show");
});
$(".RightSideViewTab button .fa.fa-times").click(function(e) {
	$(".RightSideViewTab.show").removeClass("show");
});

/*  */