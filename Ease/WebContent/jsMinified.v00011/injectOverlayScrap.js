window.top===window&&extension.runtime.onMessage("scrapOverlay",function(e){if(!document.getElementById("ease_overlay_scrap")){var n=document.createElement("div");n.id="ease_overlay_scrap",document.body.appendChild(n),extension.runtime.sendMessage("scrapReloaded",{url:window.location.href},function(){});var t,a,o,i,c="safari-2/scrappingTuto/";"Linkedin"==e?(a="Importing accounts",o="you connected with",t="Sign in with Linkedin",c+="linkedin.png",i="3b5998"):"Facebook"==e?(a="Importing accounts",o="you connected with",t="Sign in with Facebook",c+="facebook.png",i="3b5998"):"Chrome"==e&&(a="Importing accounts saved in",o=!1,t="Google Chrome",c+="chrome.png",i=!1),n.className="overlayScrap";var r=document.createElement("div");r.className="containerScrap",n.appendChild(r);var d=document.createElement("img");d.src=safari.extension.baseURI+"safari-2/scrappingTuto/logo.png",d.className="logoEase",r.appendChild(d);var l=document.createElement("div");l.className="titleContainer",r.appendChild(l);var s=document.createElement("p");if(s.className="title",s.textContent=a,l.appendChild(s),o){var p=document.createElement("p");p.className="title",p.textContent=o,l.appendChild(p)}var m=document.createElement("div");m.className="websiteContainer",i&&(m.style="background-color:#"+i+" !important ; border-color:#"+i+" !important;"),r.appendChild(m);var u=document.createElement("img");u.src=safari.extension.baseURI+c,m.appendChild(u);var v=document.createElement("span");v.textContent=t,i&&(v.style="color:white;"),m.appendChild(v);var h=document.createElement("div");h.className="loader",r.appendChild(h);var g=document.createElement("div");g.className="infoContainer",r.appendChild(g);var C=document.createElement("p");C.textContent="You’ll select the ones you want to",g.append(C);var E=document.createElement("p");E.textContent="keep right after this.",g.append(E)}});