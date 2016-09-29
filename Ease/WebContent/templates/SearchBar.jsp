
<div>
	<input autofocus="autofocus" id="searchBar">
	<button id="searchBarButton"><span>Search</span></button>
	<a id="googleLink" href="" style="display: none" target="_blank"></a>
</div>

<script>

function search(value) {
	$("#googleLink").attr('href', 'https://www.google.fr/search?q=' + value); 
	$("#googleLink")[0].click();
	$("#searchBar").val("");
}

$(document).ready(function(){
	$("#searchBar").keyup(function (e) {
    	if (e.keyCode == 13) {
    		if ($("#searchBar").val() != "")
				search($("#searchBar").val());
    	}
	});
	$("#searchBarButton").click(function() {
		if ($("#searchBar").val() != "")
			search($("#searchBar").val());
	});
});

</script>