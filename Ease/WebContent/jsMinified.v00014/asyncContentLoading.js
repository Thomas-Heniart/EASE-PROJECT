asyncLoading={loadHtml:function(n){for(var e=n.urls,a=n.appendTo,t=n.callback,o=e.length,d=0;d<e.length;d++)$.get(e[d]).done(function(n){$(a).append(n),!--o&&t&&t()})},loadScripts:function(n){for(var e=n.urls,a=0;a<e.length;a++){var t=document.createElement("script");n.async&&(t.async=n.async),n.onload&&(t.onload=n.onload),n.appendTo?n.appendTo.appendChild(t):document.head.appendChild(t),t.src=e[a]}},loadSingleScript:function(n,e){var a=document.createElement("script");a.async=!0,a.onload=function(){e&&e()},a.src=n,(document.getElementsByTagName("head")[0]||document.getElementsByTagName("body")[0]).appendChild(a)},loadScriptsOneByOne:function(n,e){function a(){var o=document.createElement("script");o.async=!0,o.onload=function(){++t==n.length?e&&e():a()},o.src=n[t],(document.getElementsByTagName("head")[0]||document.getElementsByTagName("body")[0]).appendChild(o)}var t=0;if(!n.length)return void(e&&e());a()}};