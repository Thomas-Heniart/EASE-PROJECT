var postHandler={get:function(n,t,o,i,e,a){$.get(n,t,function(n){o();var t=n.split(" ")[0],a=n.substring(n.indexOf(" ")+1,n.length);/^\d+$/.test(t)||(a="Sorry, you're facing of a unknown bug. We'll fix it asap !"),"200"==t?i(a):"0"==t||"5"==t?document.location.reload(!0):e(a,!0)},a)},post:function(n,t,o,i,e,a){$.post(n,t,function(n){o();var t=n.split(" ")[0],a=n.substring(n.indexOf(" ")+1,n.length);/^\d+$/.test(t)||(a="Sorry, you're facing of a unknown bug. We'll fix it asap !"),"200"==t?i(a):"0"==t||"5"==t?document.location.reload(!0):e(a,!0)},a)}};