function refresh() {
  $.ajax({
    url: 'index.jsp'
  }).success(function() {
    setTimeout(refresh, 110 * 60 * 1000);
  });
}

$(document).ready(function() {
  refresh();
})
