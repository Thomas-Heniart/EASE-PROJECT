function refresh() {
  $.ajax({
    url: 'index.jsp'
  }).success(function() {
    setTimeout(refresh, 5*1000);
  });
}

$(document).ready(function() {
  refresh();
})
