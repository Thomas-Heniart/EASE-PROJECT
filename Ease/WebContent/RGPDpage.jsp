<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="languages/text" />
<!DOCTYPE html>
<head>
    <title> Ease.space | RGPD</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1" />
    <!-- Description shown in Google -->
    <meta name="description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
    <!-- Facebook metadata -->
    <meta property="og:url" content="https://ease.space/" />
    <meta property="og:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés." />
    <meta property="og:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
    <meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png" />
    <meta property="og:type" content="website" />
    <!-- Twitter metadata -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:site" content="@Ease_app" />
    <meta name="twitter:creator" content="@Ease_app" />
    <meta name="twitter:title" content="Ease.space | Le meilleur moyen de se connecter à ses sites préférés." />
    <meta name="twitter:description" content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement." />
    <meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png" />
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1" />
    <meta property="og:image"
          content="https://ease.space/resources/other/fb_letsgo_icon.jpg" />
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png" />

    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00040/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00040/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00040/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00040/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00040/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00040/securityPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00040/productPage.css"/>
    <link rel="stylesheet" href="semantic/dist/semantic.min.css">


    <link rel="manifest" href="manifest.json">
</head>
<body id="landingBody" style="margin:0;">
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
    <div class="container">
        <div class="navbar-header page-scroll">
            <a id="ease-logo" class="navbar-brand page-scroll" href="/discover"></a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li class="hidden">
                    <a href="#page-top"></a>
                </li>
                <li>
                    <a href="/product">
                        <fmt:message key="landing.footer.section-product.title" />
                    </a>
                </li>
                <li>
                    <a href="/security">
                        <fmt:message key="landing.header.security-link" />
                    </a>
                </li>
                <li>
                    <a href="/pricing">
                        <fmt:message key="landing.header.price-link" />
                    </a>
                </li>
                <li>
                    <a href="/?skipLanding=true" id="connexionButton">
                        <fmt:message key="landing.header.connexion-link" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified.v00023/jquery1.12.4.js"></script>
<script src="/jsMinified.v00023/jquery.complexify.js"></script>
<script src="/jsMinified.v00023/bootstrap.js" async></script>
<script src="/jsMinified.v00023/ajaxHandler.js" async></script>
<script src="/jsMinified.v00023/landingPage.js" async></script>
<script src="/jsMinified.v00023/basic-utils.js" async></script>
<script src="/jsMinified.v00023/languageChooser.js" async></script>
<script src="/jsMinified.v00023/tracker.js" async></script>
<script src="semantic/dist/semantic.min.js"></script>
<script>
  if (window.matchMedia("(max-width: 768px)").matches) {
    $('#tableHello').removeClass('product-tab');
  }
</script>
<script type="text/javascript">
  $(document).ready(function(){
    $('.ui.menu').on('click', function() {
      $('#more').toggleClass('hidden');
      $('.ui.tab').toggleClass('active');
      return false;
    });
    $('#table-general').popup({
      on        : 'hover',
      position  : 'bottom center',
    });
    $('#table-switch').popup({
      on        : 'hover',
      position  : 'bottom center'
    });
    $('#table-notification').popup({
      on        : 'hover',
      position  : 'bottom center',
    });
    $('#table-activeUser').popup({
      on        : 'hover',
      position  : 'bottom center'
    });
  });
</script>
<script type="text/javascript">
  $(document).ready(function () {
    var AFFIX_TOP_LIMIT = 560;
    var AFFIX_OFFSET = 80;

    var $menu = $("#menu"),
      $btn = $("#menu-toggle");

    $("#menu-toggle").on("click", function () {
      $menu.toggleClass("open");
      return false;
    });
    $(".docs-nav").each(function () {
      var $affixNav = $(this),
        $container = $affixNav.parent(),
        affixNavfixed = false,
        originalClassName = this.className,
        current = null,
        $links = $affixNav.find("a");

      function getClosestHeader(top) {
        var last = $links.first();

        if (top < AFFIX_TOP_LIMIT) {
          return last;
        }

        for (var i = 0; i < $links.length; i++) {
          var $link = $links.eq(i),
            href = $link.attr("href");

          if (href.charAt(0) === "#" && href.length > 1) {
            var $anchor = $(href).first();

            if ($anchor.length > 0) {
              var offset = $anchor.offset();

              if (top < offset.top - AFFIX_OFFSET) {
                return last;
              }

              last = $link;
            }
          }
        }
        return last;
      }


      $(window).on("scroll", function (evt) {
        var top = window.scrollY,
          height = $affixNav.outerHeight(),
          max_bottom = $container.offset().top + $container.outerHeight(),
          bottom = top + height + AFFIX_OFFSET;

        if (affixNavfixed) {
          if (top <= AFFIX_TOP_LIMIT) {
            $affixNav.removeClass("fixed");
            $affixNav.css("top", 0);
            affixNavfixed = false;
          } else if (bottom > max_bottom) {
            $affixNav.css("top", (max_bottom - height) - top);
          } else {
            $affixNav.css("top", AFFIX_OFFSET);
          }
        } else if (top > AFFIX_TOP_LIMIT) {
          $affixNav.addClass("fixed");
          affixNavfixed = true;
        }

        var $current = getClosestHeader(top);

        if (current !== $current) {
          $affixNav.find(".active").removeClass("active");
          $current.addClass("active");
          current = $current;
        }
      });
    });
  });
</script>
<script type="text/javascript">
  window.addEventListener('load',function(){
    $('.signUpButton').click(function(){
      easeTracker.trackEvent($(this).attr("trackEvent"));
      easeSignUpPopup.open();
    });
  });
</script>
<script type="text/javascript">
  window.addEventListener('load',function(){
    $crisp = [];
    CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
    (function () {
      d = document;
      s = d.createElement("script");
      s.src = "https://client.crisp.chat/l.js";
      s.async = 1;
      d.getElementsByTagName("head")[0].appendChild(s);
    })();
  });
</script>
<script type="text/javascript">
  window.addEventListener('load',function(){
    (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
      r.async = true;
      r.src = "https://cdn.amplitude.com/libs/amplitude-4.0.0-min.gz.js";
      r.onload=function(){e.amplitude.runQueuedFunctions()};var i=t.getElementsByTagName("script")[0];
      i.parentNode.insertBefore(r,i);function s(e,t){e.prototype[t]=function(){this._q.push([t].concat(Array.prototype.slice.call(arguments,0)));
        return this}}var o=function(){this._q=[];return this};var a=["add","append","clearAll","prepend","set","setOnce","unset"];
      for(var u=0;u<a.length;u++){s(o,a[u])}n.Identify=o;var c=function(){this._q=[];return this;
      };var p=["setProductId","setQuantity","setPrice","setRevenueType","setEventProperties"];
      for(var l=0;l<p.length;l++){s(c,p[l])}n.Revenue=c;var d=["init","logEvent","logRevenue","setUserId","setUserProperties","setOptOut","setVersionName","setDomain","setDeviceId","setGlobalUserProperties","identify","clearUserProperties","setGroup","logRevenueV2","regenerateDeviceId"];
      function v(e){function t(t){e[t]=function(){e._q.push([t].concat(Array.prototype.slice.call(arguments,0)));
      }}for(var n=0;n<d.length;n++){t(d[n])}}v(n);n.getInstance=function(e){e=(!e||e.length===0?"$default_instance":e).toLowerCase();
        if(!n._iq.hasOwnProperty(e)){n._iq[e]={_q:[]};v(n._iq[e])}return n._iq[e]};e.amplitude=n;
    })(window,document);

    if (location.hostname === "ease.space")
      amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
    else
      amplitude.getInstance().init("5f012a5e604acb0283ed11ed8da5414f");
    easeTracker.trackEvent("SecurityPageVisit");
  });
</script>
</body>
</html>
