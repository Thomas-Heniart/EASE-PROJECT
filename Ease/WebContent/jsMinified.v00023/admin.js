function openSideViewTab(e){$(".RightSideViewTab.show").removeClass("show");var i=$(e.target).closest("button").attr("target");$("#"+i).addClass("show")}function closeSideViewTab(){$(".RightSideViewTab.show").removeClass("show")}currentAdminPopup=null,$(document).ready(function(){$("#easePopupsHandler").click(function(e){"easePopupsHandler"===$(e.target).attr("id")&&currentAdminPopup.close()}),$(".admin-menu button").click(openSideViewTab),$(".RightSideViewTab button .fa.fa-times").click(closeSideViewTab)});