function refresh(){postHandler.post("CheckConnection",{email:$("#userEmail").data("content")},function(){},function(){},function(){window.location="/"},"text"),setTimeout(refresh,15e3)}$(document).ready(function(){refresh()});