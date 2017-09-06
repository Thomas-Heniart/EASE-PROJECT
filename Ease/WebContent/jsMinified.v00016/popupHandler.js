var currentEasePopup = null;
$("#easePopupsHandler").click(function (e) {
    "easePopupsHandler" == $(e.target).attr("id") && currentEasePopup.close()
});