<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<c:if test="${language ne 'en' and language ne 'fr_FR'}">
    <c:set var="language" value="en" scope="session"/>
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
    <link rel="stylesheet" type="text/css" href="/cssMinified.v00009/lib/fonts/museo-font.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/default_style.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/bootstrap.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/landingPage.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="/cssMinified.v00009/securityPage.css" />

    <link rel="manifest" href="manifest.json">
</head>
<body id="landingBody">
<nav id="mainNav" class="navbar navbar-default navbar-custom navbar-fixed-top">
    <div class="container">
        <div class="navbar-header page-scroll">
            <a class="navbar-brand page-scroll" href="/"><img src="resources/landing/ease-white-logo.svg" /></a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li class="hidden">
                    <a href="#page-top"></a>
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
                    <a href="/contact">
                        <fmt:message key="landing.header.contact-link" />
                    </a>
                </li>
                <li>
                    <a href="/?skipLanding=true" id="connexionButton">
                        <fmt:message key="landing.header.connexion-link" />
                    </a>
                </li>
                <li>
                    <%@ include file="templates/LanguageChooser.jsp" %>
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
                Nous prenons la sécurité au sérieux. Et ceci pour de bonnes raisons: chaque personne utilisant notre service, attend de ses données quelles soient sécurisées et confidentielles. Nous comprenons l’importance de cette mission que vous nous confiez, et travaillons à garder cette relation. C’est pour cela que EASE.SPACE souhaite être transparent sur les règles de sécurité et de confidentialité.
            </div>
        </div>
    </div>
</header>
<section style="background-color: white">
    <div class="container">
        <ul class="docs-nav">
            <li><a href="#1" class="cc-active">1. Anonymat : on vous facilite la vie sans la connaitre</a></li>
            <li><a href="#2" class="cc-active">2. Sécurité pour l'utilisateur</a></li>
            <li><a href="#3" class="cc-active">3. Sécurité pour l'entreprise</a></li>
            <li><a href="#4" class="cc-active">4. Nos technologies de sécurité</a></li>
            <li><a href="#5" class="cc-active">5. Politique interne de sécurité</a></li>
        </ul>
        <div class="docs-content">
            <div class="doc_section">
                <h1 class="doc_header" id="1">1. Anonymat : on vous facilite la vie sans la connaître</h1>
                <p>Il y a seulement deux informations personnelles que l’on connait sur vous : une adresse mail, un pseudo.</p>
                <p>Toutes les autres données (identifiants de comptes, mots de passe, etc.) sont stockées chiffrées. Elles sont donc 100% anonymes. Voir explications techniques sur le chiffrement plus bas.</p>
                <p>Nous ne vendons pas vos données personnelles. Notre mode de rémunération est la sécurisation des données chez les entreprises. De plus, cela ne correspond pas aux valeurs de la société.</p>
                <p>Ease.space vous permet d’être conforme aux dernières normes que les entreprises et les travailleurs indépendants doivent et devront bientôt respecter.</p>
            </div>
            <div class="doc_section">
                <h1 class="doc_header" id="2">2. Sécurité pour l’utilisateur : augmentez le niveau de sécurité pour vos mots de passe personnels</h1>
                <p class="sub_header">Stockez vos données de manière plus sécurisée</p>
                <p>Les mots de passe ne sont plus stockés sur votre ordinateur mais sur les serveurs d’un leader du cloud. Si votre ordinateur casse ou se fait pirater, aucun soucis.</p>
                <p>Vos mots de passe sont stockés chiffrés 24h/24 7J/7. Finis les fichiers excel et les post-its.</p>
                <p>Grâce à Ease.space, restez à jours sur les dernières technologies de chiffrement et les dernières méthodes de sécurité.</p>
                <p>Système de prévention à la fraude : au bout de 10 mots de passe Ease.space eronés, votre compte est bloqué 5 minutes.</p>
                <p>Option : bloquer la mémorisation des mots de passe sur navigateur pour ne plus laisser des mots de passe sensibles sur des ordinateurs inconnus ou dont vous n’êtes pas propriétaire.</p>
                <p>Vous voulez choisir des mots de passe compliqués et différents pour chaque compte (majuscules, minuscules, chiffres etc) ? Ease s’en souvient pour vous.</p>
                <p>Grâce à des procédés de chiffrement avancés (hashage, Cf. white paper), nous ne connaissons pas votre mot de passe maître. Si vous le perdez, votre compte sera renouvelé et vous devrez entrer à nouveau les mots de passe des comptes que vous aviez intégré.</p>
            </div>
            <div class="doc_section">
                <h1 class="doc_header" id="3">3. Sécurité pour l’entreprise.</h1>
                <p>Mettez en place des technologies de pointe et de bonnes pratiques.</p>
                <p class="sub_header">Principe de base</p>
                <p>Grâce à notre système de sécurité, vos collaborateurs reçoivent des accès (sous la forme d’app). Ils n’ont plus besoin de connaître les mots de passe des comptes de l’entreprise.</p>
                <p>Un collaborateur qui n’a plus besoin de connaître ses mots de passe ne peut plus les oublier et n’a plus besoin de les noter sur des post its.</p>
                <p class="sub_header">Un meilleur contrôle</p>
                <p>Grâce à une interface d’organisation simple et customisable, les accès ne sont distribués qu’aux personnes en ayant réellement besoin.</p>
                <p>Notre extension peut bloquer la popup chrome permettant de mémoriser les mots de passe sur le navigateur. Les mots de passe ne peuvent donc pas se retrouver mémorisés sur des machines inconnues.</p>
                <p>Vous pouvez contrôler le temps de partage des accès à un collaborateur.</p>
                <p>Visualisez l’historique d'activité des employés sur les outils de la société.</p>
                <p>Programmez le départ d’un collaborateur et supprimez ses accès automatiquement.</p>
                <p>Mettez en place une politique de renouvellement récurrent des mots de passe pour vos outils sensibles.</p>
                <p class="sub_header">Partage sécurisé de comptes entre collaborateurs</p>
                <p>Les transferts de mots de passe sont 100% chiffrés de l’envoie à la réception.</p>
                <p>Ce n’est pas le mots de passe qui est transféré, c’est l’accès (l’app).</p>
                <p>Programmez qui, quand et combien de temps, chaque collaborateur aura accès à un compte.</p>
                <p class="sub_header">Le renouvellement du mot de passe Ease.space en cas de perte</p>
                <p>Le mot de passe Ease.space est la porte d’entrée aux outils de la société, son renouvellement par un collaborateur doit donc être vérifié.</p>
                <p>Pour prévenir les fraudes, le renouvellement du mot de passe maître d’un collaborateur sera confirmé par l’administrateur.</p>
                <p>Autre système de prévention à la fraude : au bout de 10 mots de passe Ease.space eronés, un compte est bloqué 5 minutes.</p>
                <p>Dans le cas de la perte du mot de passe Ease.space par l’administrateur, notre équipe s’occupe de contacter personnellement ce dernier pour s’assurer de son identité.</p>
                <p>Pour en savoir plus, <a>télechargez notre White Paper</a>.</p>
            </div>
            <div class="doc_section">
                <h1 class="doc_header" id="4">4. Nos technologies de securité</h1>
                <p>Intro : cette partie est expliquée plus en détail dans le document White paper.</p>
                <p class="sub_header">Sécurisation des mots de passe des comptes que vous nous fournissez</p>
                <p>Nous sécurisons vos mots de passe avec un chiffrement asymétrique, qui crée 2 clés, une privée et une publique. la clé publique nous permet de vous envoyer des données chiffrées que vous pouvez déchiffrer avec votre clé privée uniquement.</p>
                <p>Nous protégeons chaque clé privée en la chiffrant symétriquement. Le chiffrement symétrique crée une nouvelle clé unique qui sert autant à chiffrer que déchiffrer la clé privée (qui elle sert à déchiffrer vos mots de passe et vous connecter).</p>
                <p>Cette clé symétrique est elle aussi stockée chiffrée, cette fois par votre mots de passe Ease.space auquel on ajoute une variable au hasard appelé un “salt”.</p>
                <p class="sub_header">Sécurisation de votre Mots de passe Ease.space</p>
                <p>Nous ne connaissons pas votre mots de passe maître. La méthode utilisée pour le stocker de manière anonyme et sécurisé est appelée “hashage”.</p>
                <p>Hasher (différent de chiffrer) un mot de passe fonctionne dans un seul sens et ne crée pas de clé. C’est une fonction mathématique (comme le chiffrement) sauf qu’elle n’a pas d’inverse. Il n’y a pas de moyen (pas de clé) de retrouver la valeur initiale (le mot de passe).</p>
                <p>Lorsque vous créez un compte Ease.Space, vous rentrez pour la première fois votre mot de passe maître, nous le hashons et le stockons hashé dans notre base de donnée. Le jour suivant, vous rentrez de nouveau votre mot de passe pour vous connecter. Nous le hashons à nouveau et comparons vos deux hashs (celui de la création de votre compte, et celui que vous venez de rentrer). Si ces deux hashs sont identiques, nous savons qu’il s’agit bien de vous sans connaître votre mots de passe.</p>
                <p>Afin d’augmenter encore votre sécurité, nous ajoutons du «sel» à votre mot de passe Ease.Space avant de le hasher. La technique du sel consiste à ajouter une chaîne aléatoire de caractères derrière votre mot de passe afin de le rendre encore plus complexe et prévenir certains types d’attaques.</p>
                <p class="sub_header">HTTPS</p>
                <p>Toutes les communications entre nos utilisateurs et nos serveurs sont sécurisés en HTTPS - SSL/TLS. Pour cela, nous avons choisi le certificat Let’s Encrypt.<br/>
                HTTPS permets 2 choses : le chiffrement des données “pendant le voyage”, la vérification de l’authenticité de l’envoyeur et du receveur.</p>
                <p>Chiffrement : Tous les transferts de data qui surviennent entre l’ordinateur de l’utilisateur et nos serveurs doivent être chiffrés, afin d’éviter que n’importe qui puisse accéder à vos informations sensibles pendant l’envoi/la réception des données.</p>
                <p>Authentification : Le bon ordinateur communique avec le bon serveur, et le serveur est “digne de confiance”.</p>
                <p class="sub_header">Serveurs</p>
                <p>L’ensemble du trafic vers nos serveurs passe par un firewall. C’est à dire que le trafic inconnu est filtré et potentiellement bloqué suivant la politique de notre firewall.</p>
                <p>Des tests automatiques de vulnérabilité sont réalisés quotidiennement. De plus un test de pénétration interne est réalisé 4 fois par an à l’aide de l’outil Qualys(Qualys est une entreprise de sécurité utilisée par la plupart des entreprise de Forbes 100). D’autres tests réalisés par des tiers sont faits de manière régulière et notre objectif est de constamment augmenter leur récurrence.</p>
                <p>Enfin, nous récoltons de manière anonyme un ensemble de rapports d’erreurs provenant de l’activité de nos utilisateurs. Aucunes informations personnelles ne sont , et ne peuvent être collectées lors de ces rapports automatiques d'erreurs. Ils sont réalisés pour augmenter la qualité et la sécurité du produit.</p>
                <p>Pour en savoir plus, <a>télechargez notre White Paper</a>.</p>
            </div>
            <div class="doc_section">
                <h1 class="doc_header" id="5">5. Politique interne de sécurité</h1>
                <p>Nous utilisons Ease.space pour sécuriser, gérer et organiser les mots de passe de la société.</p>
                <p>Une charte de confidentialité est signée par toute personne travaillant pour la société dont l’activité nécessite l’accès à des données sensibles.</p>
                <p>Au départ d’un collaborateur, ses accès sont supprimés et les mots de passe partagés de la société auquel il/elle avait accès sont changés.</p>
                <p>Une politique de modification récurrente des mots de passe est en place sur l’intégralité des outils de la société.</p>
                <p>Nos serveurs sont chez un leader Européen du Cloud, ils sont Infogérés.</p>
                <p>Nous avons fait une déclaration (n°1667005) auprès de la Commission Nationale de l'Informatique et des Libertés (CNIL) qui nous interdit de diffuser vos emails et vous donne le droit de vous désinscrire ou de rectifier vos données à tout moment.</p>
            </div>
            <div style="text-align: center">
            <a href="#services" class="btn btn-xl signUpButton">
                Essayez Ease.space gratuitement
            </a>
            </div>
        </div>
    </div>
</section>
<%@ include file="templates/landingPage/landingFooter.jsp" %>
<%@ include file="templates/landingPage/registrationPopup.jsp" %>
<script src="/jsMinified.v00014/jquery1.12.4.js" ></script>
<script src="/jsMinified.v00014/jquery.complexify.js"></script>
<script src="/jsMinified.v00014/bootstrap.js" async></script>
<script src="/jsMinified.v00014/registrationPopup.js" async></script>
<script src="/jsMinified.v00014/landingPage.js" async></script>
<script src="/jsMinified.v00014/basic-utils.js" async></script>
<script src="/jsMinified.v00014/postHandler.js" async></script>
<script src="/jsMinified.v00014/languageChooser.js" async></script>
<script src="/jsMinified.v00014/tracker.js" async></script>
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
        $crisp=[];CRISP_WEBSITE_ID="6e9fe14b-66f7-487c-8ac9-5912461be78a";(function(){d=document;s=d.createElement("script");s.src="/jsMinified.v00014/thirdParty/crisp.js";s.async=1;d.getElementsByTagName("head")[0].appendChild(s);})();
    });
</script>
<script type="text/javascript">
    window.addEventListener('load',function(){
        (function(e,t){var n=e.amplitude||{_q:[],_iq:{}};var r=t.createElement("script");r.type="text/javascript";
            r.async=true;r.src="/jsMinified.v00014/amplitude-analytics.js";
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

        /* Prod */
        //amplitude.getInstance().init("74f6ebfba0c7743a0c63012dc3a9fef0");

        /* Test */
        amplitude.getInstance().init("73264447f97c4623fb38d92b9e7eaeea");
        easeTracker.trackEvent("SecurityPageVisit");
    });
</script>
</body>
</html>