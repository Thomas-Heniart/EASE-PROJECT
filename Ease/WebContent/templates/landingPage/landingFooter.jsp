<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<section id="footer" class="notranslate">
	<div class="container">
		<div class="row">
			<div class="left-part left">
				<div class="left-menu ui row menu-column">
					<div class="links">
						<a href="/discover">
							<fmt:message key="landing.footer.section-product.title"/>
						</a>
						<a href="/security">
							<fmt:message key="landing.header.security-link"/>
						</a>
						<a href="/pricing">
							<fmt:message key="landing.header.price-link"/>
						</a>
						<a href="/rgpd">
							<fmt:message key="landing.header.gdpr"/>
						</a>
						<a href="https://blog.ease.space/">
							<fmt:message key="landing.header.blog" />
						</a>
						<a href="https://easespace.welcomekit.co" target="_blank">
							<fmt:message key="landing.footer.hiring"/>
						</a>
					</div>
				</div>
				<div class="right-menu ui row menu-column">
					<div class="links">
						<a href="/?skipLanding=true">
							<fmt:message key="landing.header.connexion-link"/>
						</a>
						<a href="/#/teamCreation?plan_id=0">
							<fmt:message key="landing.footer.link.create-account"/>
						</a>
						<a href="/contact">
							<fmt:message key="landing.header.contact-link"/>
						</a>
						<a href="/legalNotice">
							<fmt:message key="landing.footer.legal-notice"/>
						</a>
						<a href="/resources/CGU_Ease.pdf" target="_blank">
							<fmt:message key="landing.footer.link.terms"/>
						</a>
						<div><%@ include file="../../templates/LanguageChooser.jsp" %></div>
					</div>
				</div>
			</div>
			<div class="ui row easeExplore right">
				<div class="logo">
					<img class="ui small image" src="/resources/images/ease_logo_blue.svg"/>
				</div>
				<div class="social_links">
					<div class="link">
						<a href="https://www.linkedin.com/company/ease.space"  target="_blank"><i class="fa fa-linkedin"></i></a>
					</div>
					<div class="link">
						<a href="https://twitter.com/ease_space"  target="_blank"><i class="fa fa-twitter"></i></a>
					</div>
					<div class="link">
						<a href="https://www.facebook.com/YourEaseSpace/"  target="_blank"><i class="fa fa-facebook"></i></a>
					</div>
					<div class="link">
						<a href="https://unsplash.com/@ease_space/collections"  target="_blank"><i class="fa fa-camera"></i></a>
					</div>
					<div class="link">
						<a href="https://www.instagram.com/ease.space/"  target="_blank"><i class="fa fa-instagram"></i></a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Facebook Pixel Code -->
	<script>
	  !function(f,b,e,v,n,t,s)
	  {if(f.fbq)return;n=f.fbq=function(){n.callMethod?
	  n.callMethod.apply(n,arguments):n.queue.push(arguments)};
	  if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';
	  n.queue=[];t=b.createElement(e);t.async=!0;
	  t.src=v;s=b.getElementsByTagName(e)[0];
	  s.parentNode.insertBefore(t,s)}(window, document,'script',
	  'https://connect.facebook.net/en_US/fbevents.js');
	  fbq('init', '1771793116375287');
	  fbq('track', 'PageView');
	</script>
	<noscript><img height="1" width="1" style="display:none"
	  src="https://www.facebook.com/tr?id=1771793116375287&ev=PageView&noscript=1"
	/></noscript>
	<!-- End Facebook Pixel Code -->

	<!-- Twitter universal website tag code -->
	<script>
	!function(e,t,n,s,u,a){e.twq||(s=e.twq=function(){s.exe?s.exe.apply(s,arguments):s.queue.push(arguments);
	},s.version='1.1',s.queue=[],u=t.createElement(n),u.async=!0,u.src='//static.ads-twitter.com/uwt.js',
	a=t.getElementsByTagName(n)[0],a.parentNode.insertBefore(u,a))}(window,document,'script');
	// Insert Twitter Pixel ID and Standard Event data below
	twq('init','nz51g');
	twq('track','PageView');
	</script>
	<!-- End Twitter universal website tag code -->

	<!-- Linkedin tag -->
	<script type="text/javascript">
	_linkedin_data_partner_id = "273508";
	</script><script type="text/javascript">
	(function(){var s = document.getElementsByTagName("script")[0];
	var b = document.createElement("script");
	b.type = "text/javascript";b.async = true;
	b.src = "https://snap.licdn.com/li.lms-analytics/insight.min.js";
	s.parentNode.insertBefore(b, s);})();
	</script>
	<noscript>
	<img height="1" width="1" style="display:none;" alt="" src="https://dc.ads.linkedin.com/collect/?pid=273508&fmt=gif" />
	</noscript>

</section>

