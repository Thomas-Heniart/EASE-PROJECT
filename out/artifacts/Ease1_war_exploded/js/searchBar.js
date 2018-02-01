function search(value) {
	value = value.replace('&', '%26');
	window.open('https://www.google.fr/search?q=' + value, '_blank');
	$("#searchBar input").val('');
}

$(document).ready(function(){
	$("#searchBar input").keyup(function (e) {
		$('#searchBarButton').addClass('active');
    	if (e.keyCode == 13) {
    		if ($("#searchBar input").val() != "")
				search($("#searchBar input").val());
    	}
	});
	$("#searchBarButton").click(function() {
		if ($("#searchBar input").val() != "")
			search($("#searchBar input").val());
	});
	$("#searchBar input").focus(function(){
		$('#searchBarButton').addClass('active');
	});
	$("#searchBar input").blur(function(){
		$('#searchBarButton').removeClass('active');
	});
});