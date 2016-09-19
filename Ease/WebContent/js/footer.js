function showMediaTab() {
  $(".footer").show("slide", { direction: "down" }, 500);
}

function hideMediaTab() {
  $(".footer").hide("slide", { direction: "down" }, 500);
}

$(document).ready(function() {
  $("#buttonShow i").hover(function(e) {
    showMediaTab();
    $("#buttonShow").hide("slide", { direction: "down" }, 500);
  });
  $(".mediaTab").mouseleave(function() {
    hideMediaTab();
    $("#buttonShow").show("slide", { direction: "down" }, 500);
  })
});
