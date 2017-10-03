<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="fr_FR" scope="session"/>
</c:if>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="com.Ease.Languages.text" />
<html>
<head>
    <title> Ease.space | Security</title>
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
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00016/lib/fonts/museo-font.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/default_style.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/bootstrap.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/landingPage.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/cssMinified.v00016/securityPage.css"/>

    <link rel="manifest" href="manifest.json">
</head>
<body id="landingBody">
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
<header>
    <div class="container">
        <div class="intro-text">
            <div class="intro-heading">
                La sécurité chez Ease.space
            </div>
            <div class="intro-lead-in">
                Nous prenons votre sécurité très au sérieux. Notre outil protège votre anonymat et met en place les meilleures pratiques de sécurité pour vous et votre entreprise.
            </div>
        </div>
    </div>
</header>
<section style="background-color:white;padding:0 0 170px 0;">
    <div class="container">
        <ul class="docs-nav">
            <li><a href="#1" class="cc-active">1. Anonymat</a></li>
            <li><a href="#2" class="cc-active">2. Sécurité pour l'utilisateur</a></li>
            <li><a href="#3" class="cc-active">3. Sécurité pour l'entreprise</a></li>
            <li><a href="#4" class="cc-active">4. Nos technologies de sécurité</a></li>
            <li><a href="#5" class="cc-active">5. Politique interne de sécurité</a></li>
            <li><a href="#6" class="cc-active">6. F.A.Q.</a></li>
            <li><a href="#7" class="cc-active">7. Notre Security White paper</a></li>
        </ul>
        <div id="docs-content" class="docs-content">
            <div class="doc_section" id="1" style="padding-top:70px;">
                <h1 class="doc_header">1. Anonymat</h1>
                <p>Nous vous facilitons la vie sans la connaître.</p>
                <p><strong>Il y a seulement deux informations personnelles que nous connaissons sur vous</strong> : une
                    adresse email et un pseudo.</p>
                <p>Les autres données (identifiants de comptes, mots de passe, etc.) sont stockées chiffrées et 100% anonymes. Voir explications techniques sur le chiffrement plus bas.</p>
                <p>Nous ne vendons pas vos données. D’abord car cela ne correspond pas à nos valeurs et ensuite car notre mode de revenu est la sécurisation des données pour les entreprises.</p>
            </div>
            <div class="doc_section" id="2" style="padding-top:70px;">
                <h1 class="doc_header">2. Sécurité pour l’utilisateur</h1>
                <p>Augmentez le niveau de sécurité de vos mots de passe personnels.</p>
                <p>Vos mots de passe ne sont plus stockés sur votre ordinateur mais dans les serveurs d’un leader du cloud. C’est un atout si votre ordinateur casse, et une protection supplémentaire s’il se fait pirater.</p>
                <p>Vos mots de passe sont maintenant stockés chiffrés 24h/24 7J/7. Fini les fichiers excel et les post-its.</p>
                <p>Grâce à Ease.space, vous restez à jour sur les dernières technologies de sécurité informatique.</p>
                <p>Système de prévention à la fraude : au bout de 10 mots de passe erronés, votre compte est bloqué pendant 5 minutes.</p>
                <p>Il existe la possibilité de bloquer la mémorisation des mots de passe sur navigateur pour ne plus laisser des mots de passe sensibles sur des ordinateurs dont vous n’êtes pas propriétaire.</p>
                <p>Lorsque vous souhaitez choisir des mots de passe compliqués et différents pour chaque compte (majuscules, minuscules, chiffres etc), Ease.space s’en souvient pour vous.</p>
                <p>Grâce à des procédés de chiffrement avancés (Cf. White paper), nous ne connaissons pas votre mot de passe maître. Si vous le perdez, votre compte sera renouvelé et vous devrez entrer à nouveau les mots de passe des comptes que vous aviez intégrés.</p>
            </div>
            <div class="doc_section" id="3" style="padding-top:70px;">
                <h1 class="doc_header">3. Sécurité pour l’entreprise</h1>
                <p>Mettez en place des technologies de pointe et des bonnes pratiques.</p>
                <p>Principe de base : grâce à notre système de sécurité, vos collaborateurs reçoivent des accès (sous la forme d’app). Ils n’ont plus besoin de connaître les mots de passe des comptes de l’entreprise. Un collaborateur qui n’a plus besoin de connaître de mots de passe ne peut plus les oublier et n’a plus besoin de les noter sur des post its.</p>
                <p>Un meilleur contrôle : Grâce à une interface d’organisation simple et customisable, les accès ne sont distribués qu’aux personnes en ayant réellement besoin.</p>
                <p>Pour empêcher que des mots de passe sensibles se retrouvent sur les ordinateurs personnels des collaborateurs, notre extension peut bloquer la mémorisation des mots de passe sur le navigateur. Les mots de passe ne peuvent donc pas se retrouver mémorisés sur des machines inconnues.</p>
                <p>Vous pouvez contrôler la durée de partage des accès à un collaborateur, visualiser l’historique d'activité des outils de la société, programmer le départ d’un collaborateur et supprimer ses accès automatiquement.</p>
                <p>Mettez facilement en place une politique de changement des mots de passe de vos outils sensibles.</p>
                <p>Partage sécurisé de comptes entre collaborateurs : les envois de mots de passe sont 100% chiffrés de
                    l’envoie à la réception. Ce n’est pas le mot de passe qui est transféré, c’est l’accès (l’app).<br/>Programmez
                    qui, quand et pour combien de temps, chaque collaborateur aura accès à un compte.</p>
                <p>Le renouvellement du mot de passe Ease.space en cas de perte. Le mot de passe Ease.space est la porte
                    d’entrée aux outils de la société, son renouvellement par un collaborateur doit donc être vérifié.
                    Pour prévenir les fraudes, il sera confirmé par l’administrateur. Dans le cas de la perte du mot de
                    passe Ease.space de l’administrateur, notre équipe s’occupe de contacter personnellement ce dernier
                    pour s’assurer de son identité.</p>
                <p>Pour en savoir plus, <a href="/resources/documents/Ease.space_Security-Whitepaper.pdf" target="_blank">téléchargez notre Security White paper.</a></p>
            </div>
            <div class="doc_section" id="4" style="padding-top:70px;">
                <h1 class="doc_header">4. Nos technologies de securité</h1>
                <p>Cette partie est expliquée plus en détail dans notre White paper.</p>
                <p>Sécurité des mots de passe de vos différents comptes web.</p>
                <p>Nous sécurisons vos mots de passe avec un chiffrement asymétrique, qui crée 2 clés, une privée et une publique. la clé publique nous permet de vous envoyer des données chiffrées que vous seul pouvez déchiffrer avec votre clé privée uniquement.</p>
                <p>Nous protégeons chaque clé privée en la chiffrant une nouvelle fois (symétriquement). Le chiffrement symétrique crée une nouvelle clé unique qui sert autant à chiffrer que déchiffrer la clé privée (qui elle sert à déchiffrer vos mots de passe et vous connecter).</p>
                <p>Cette clé symétrique est une nouvelle fois chiffrée (par votre mot de passe Ease.space et une variable inconnue appelée un “salt”).</p>
                <p class="sub_header">Sécurité de votre mot de passe Ease.space.</p>
                <p>Nous ne connaissons pas votre mot de passe maître. La méthode utilisée pour le stocker de manière anonyme et sécurisée est appelée “hashage”.</p>
                <p>Hasher (différent de chiffrer) un mot de passe fonctionne dans un seul sens et ne crée pas de clé. C’est une fonction mathématique (comme le chiffrement) sauf qu’elle n’a pas d’inverse. Il n’y a pas de moyen (pas de clé) de retrouver la valeur initiale (le mot de passe).</p>
                <p>Lorsque vous créez un compte Ease.Space, vous rentrez pour la première fois votre <strong>mot de
                    passe maître</strong>, nous le hashons et le stockons hashé dans notre base de données. Le jour
                    suivant, vous rentrez de nouveau votre mot de passe pour vous connecter. Nous le hashons à nouveau
                    et comparons vos deux hashs (celui de la création de votre compte, et celui que vous venez de
                    rentrer). Si ces deux hashs sont identiques, nous savons qu’il s’agit bien de vous sans connaître
                    votre mots de passe.</p>
                <p>Afin d’augmenter encore votre sécurité, nous ajoutons un <strong>«sel»</strong> à votre mot de passe Ease.Space avant de le hasher. La technique du <strong>sel</strong> consiste à ajouter une chaîne aléatoire de caractères derrière votre mot de passe afin de le rendre encore plus complexe et prévenir certains types d’attaques.</p>
                <p class="sub_header">HTTPS</p>
                <p>Toutes les communications entre nos utilisateurs et nos serveurs sont sécurisées en HTTPS - SSL/TLS.
                    Nous avons choisi le certificat Let’s Encrypt.</p>
                <p>HTTPS permet 2 choses : la sécurité des données “pendant le voyage”, la vérification de
                    l’authenticité de l’envoyeur et du receveur.</p>
                <p>Chiffrement : Tous les transferts de data qui surviennent entre l’ordinateur de l’utilisateur et nos serveurs doivent être chiffrés, afin d’éviter que n’importe qui puisse accéder à vos informations sensibles pendant l’envoi des données.</p>
                <p>Authentification : Cela signifie que le bon ordinateur communique avec le bon serveur, et que le serveur est “digne de confiance”. </p>
                <p class="sub_header">Serveurs</p>
                <p>L’ensemble du trafic vers nos serveurs passe par un firewall. C’est-à-dire que le trafic d’origine inconnu est filtré et bloqué.</p>
                <p>Des tests automatiques de vulnérabilité sont réalisés quotidiennement. De plus un test de pénétration interne est réalisé 4 fois par an à l’aide de l’outil Qualys (Qualys est une entreprise de sécurité utilisée par la plupart des entreprise de Forbes 100). D’autres tests réalisés par des tiers sont faits de manière régulière et notre objectif est de constamment augmenter leur récurrence.</p>
                <p>Enfin, nous récoltons de manière anonyme un ensemble de rapports d’erreurs provenant de l’activité de nos utilisateurs. Aucune information personnelle n'est collectée lors de ces rapports automatiques d'erreurs. Ils sont réalisés pour augmenter la qualité et la sécurité du produit.</p>
                <p>Pour en savoir plus, <a href="/resources/documents/Ease.space_Security-Whitepaper.pdf"
                                           target="_blank">téléchargez notre Security White paper.</a></p>
            </div>
            <div class="doc_section" id="5" style="padding-top:70px;">
                <h1 class="doc_header">5. Politique interne de sécurité</h1>
                <p>En interne, nous utilisons Ease.space pour sécuriser, gérer et organiser les accès de la société.</p>
                <p>Une charte de confidentialité est signée par les membres de l’équipe dont l’activité nécessite
                    l’accès à des données sensibles.</p>
                <p>Au départ d’un collaborateur, ses accès sont supprimés et les mots de passe partagés de la société
                    auxquels il avait accès sont changés.</p>
                <p>Une politique de modification récurrente des mots de passe est en place sur l’intégralité des outils de la société.</p>
                <p>Nos serveurs sont chez un leader Européen du Cloud, et sont Infogérés.</p>
            </div>
            <div class="doc_section" id="6" style="padding-top:70px;">
                <h1 class="doc_header">6. FAQ</h1>
                <p class="sub_header">Si un hacker cherche à trouver mon mot de passe maître ?</p>
                <p>Nous vous encourageons à utiliser un mot de passe «compliqué» pour sécuriser votre plateforme. Choisir un mot de passe avec 8 caractères (minuscules, majuscules et chiffres) implique 280
                    000 milliards de possibilités de combinaisons. Des dizaines de milliers d’années seraient donc nécessaires si un pirate souhaitait réaliser des tests pour trouver votre mot de passe (à raison d’un milliard
                    de tests par jour cela représenterait environ 750 années). Pour d’avantage de sécurité nous bloquons
                    le compte pendant plusieurs minutes suite à 10 échecs de connexion.</p>
                <p class="sub_header">Si je souhaite accéder à mes mots de passe sur un ordinateur qui n’est pas le mien ?</p>
                <p>Ease.space est disponible depuis n’importe quel ordinateur. Il vous suffit de vous connecter avec vos identifiants sur https://ease.space pour retrouver vos comptes.</p>
                <p class="sub_header">Si mon ordinateur se fait voler ?</p>
                <p>Aucune donnée n’est stockée sur votre ordinateur. Seul vous, détenteur du mot de passe principal, pouvez accéder à votre compte Ease.space.</p>
                <p class="sub_header">Si je perds mon mot de passe maître ?</p>
                <p>Pour des raisons de sécurité, Ease.space ne stocke pas votre mot de passe maître, car c’est ce mot de
                    passe maître qui sécurise l’ensemble de vos données. S’il est perdu, et que vous le réinitialisez,
                    l’ensemble des données sensibles stockées sur Ease.space aura disparu, il vous faudra les entrer
                    à nouveau comme le jour de votre inscription.</p>
                <p class="sub_header">Si un hacker accède à la base de données d’Ease.space ?</p>
                <p>Les données stockées sur Ease.space sont entièrement chiffrées. Personne d’autre que vous ne peut accéder à vos informations personnelles. Si un hacker accède à la base de données, il aura accès à des données illisibles.</p>
            </div>
            <div class="doc_section" id="7" style="padding-top:70px;">
                <h1 class="doc_header">7. Notre Whitepaper</h1>
                <p>Pour obtenir toutes les informations relatives à la sécurité, <a
                        href="/resources/documents/Ease.space_Security-Whitepaper.pdf" target="_blank">téléchargez notre
                    Security Whitepaper.</a></p>
            </div>
            <div style="text-align: center">
                <a href="/teams#/registration" class="btn btn-xl signUpButton">
                    Essayez Ease.space gratuitement
                </a>
            </div>
        </div>
    </div>
</section>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
<script src="/jsMinified.v00021/jquery1.12.4.js"></script>
<script src="/jsMinified.v00021/jquery.complexify.js"></script>
<script src="/jsMinified.v00021/bootstrap.js" async></script>
<script src="/jsMinified.v00021/ajaxHandler.js" async></script>
<script src="/jsMinified.v00021/landingPage.js" async></script>
<script src="/jsMinified.v00021/basic-utils.js" async></script>
<script src="/jsMinified.v00021/languageChooser.js" async></script>
<script src="/jsMinified.v00021/tracker.js" async></script>
<script type="text/javascript">
    if (window.matchMedia("(max-width: 768px)").matches) {
        $('#docs-content').removeClass('docs-content');
        $('ul.docs-nav').addClass('docs-without-nav');
        $('ul.docs-nav').removeClass('docs-nav');
    }
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
<script type="text/javascript">
    window.addEventListener('load',function(){
        (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
            r.async = true;
            r.src = "/jsMinified.v00021/amplitude-analytics.js";
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