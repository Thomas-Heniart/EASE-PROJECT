function refresh() {
  $.ajax({
    url: 'index.jsp'
  }).success(function() {
    setTimeout(refresh, 60*60*1000);
  });
}

$(document).ready(function() {
  refresh();
})
