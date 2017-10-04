<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<?xml version="1.0" encoding="UTF-8" ?>
<html xmlns="http://w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1"/>
    <title>Team space</title>
    <!-- Description shown in Google -->
    <!-- Facebook metadata -->
    <meta name="description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta property="og:url" content="https://ease.space/"/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="Ease.space"/>
    <meta property="og:logo" content="https://ease.space/resources/icons/APPEASE.png"/>
    <meta property="og:description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta property="og:image" content="https://ease.space/resources/images/fbmeta-fr.png"/>
    <!-- Twitter metadata -->
    <meta name="twitter:card" content="summary_large_image"/>
    <meta name="twitter:site" content="@Ease_app"/>
    <meta name="twitter:creator" content="@Ease_app"/>
    <meta name="twitter:title" content="Ease.space"/>
    <meta name="twitter:description"
          content="Ease est la homepage qui vous débarrasse des mots de passe. En 1 clic, soyez connecté à vos sites web automatiquement."/>
    <meta name="twitter:image" content="https://ease.space/resources/images/fbmeta-en.png"/>
    <link rel="icon" type="image/png" href="resources/icons/APPEASE.png"/>
    <link rel="stylesheet" href="/cssMinified.v00016/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:500,700" />
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00016/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/semantic/dist/semantic.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/lib/fonts/untitled-font-4/styles.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/team.css"/>
    <script src="https://js.stripe.com/v3/"></script>
    <script src="/jsMinified.v00021/jsencrypt.js"></script>
    <script src="/jsMinified.v00021/moment.js"></script>
    <script type="text/javascript">
        (function (e, t) {
            var n = e.amplitude || {_q: [], _iq: {}};
            var r = t.createElement("script")
            ;r.type = "text/javascript";
            r.async = true
            ;r.src = "https://d24n15hnbwhuhn.cloudfront.net/libs/amplitude-3.7.0-min.gz.js"
            ;r.onload = function () {
                if (e.amplitude.runQueuedFunctions) {
                    e.amplitude.runQueuedFunctions()
                } else {
                    console.log("[Amplitude] Error: could not load SDK")
                }
            }
            ;var i = t.getElementsByTagName("script")[0];
            i.parentNode.insertBefore(r, i)
            ;

            function s(e, t) {
                e.prototype[t] = function () {
                    this._q.push([t].concat(Array.prototype.slice.call(arguments, 0)));
                    return this
                }
            }

            var o = function () {
                    this._q = [];
                    return this
                }
            ;var a = ["add", "append", "clearAll", "prepend", "set", "setOnce", "unset"]
            ;
            for (var u = 0; u < a.length; u++) {
                s(o, a[u])
            }
            n.Identify = o;
            var c = function () {
                    this._q = []
                    ;
                    return this
                }
            ;var l = ["setProductId", "setQuantity", "setPrice", "setRevenueType", "setEventProperties"]
            ;
            for (var p = 0; p < l.length; p++) {
                s(c, l[p])
            }
            n.Revenue = c
            ;var d = ["init", "logEvent", "logRevenue", "setUserId", "setUserProperties", "setOptOut", "setVersionName", "setDomain", "setDeviceId", "setGlobalUserProperties", "identify", "clearUserProperties", "setGroup", "logRevenueV2", "regenerateDeviceId", "logEventWithTimestamp", "logEventWithGroups", "setSessionId"]
            ;

            function v(e) {
                function t(t) {
                    e[t] = function () {
                        e._q.push([t].concat(Array.prototype.slice.call(arguments, 0)))
                    }
                }

                for (var n = 0; n < d.length; n++) {
                    t(d[n])
                }
            }

            v(n);
            n.getInstance = function (e) {
                e = (!e || e.length === 0 ? "$default_instance" : e).toLowerCase()
                ;
                if (!n._iq.hasOwnProperty(e)) {
                    n._iq[e] = {_q: []};
                    v(n._iq[e])
                }
                return n._iq[e]
            }
            ;e.amplitude = n
        })(window, document);

        amplitude.getInstance().init("5f012a5e604acb0283ed11ed8da5414f");
    </script>
    <script src="/jsMinified.v00021/tracker.js"></script>
</head>
<BODY>
<div id="app">
</div>
<script src="teams_bundle.js?00000007"></script>
<script type="text/javascript">
  window.addEventListener('load', function () {
    $crisp = [];
    CRISP_WEBSITE_ID = "6e9fe14b-66f7-487c-8ac9-5912461be78a";
    (function () {
      d = document;
      s = d.createElement("script");
        s.src = "/jsMinified.v00021/thirdParty/crisp.js";
      s.async = 1;
      d.getElementsByTagName("head")[0].appendChild(s);
    })();
  });
</script>
</BODY>
