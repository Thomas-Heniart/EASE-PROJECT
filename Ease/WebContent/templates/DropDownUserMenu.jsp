<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="com.Ease.session.User" %> 
<nav id="menu" class="menu">
<button class="menu__label"><i class="fa fa-fw fa-user"></i><span><%= ((User)(session.getAttribute("User"))).getFirstName().substring(0, (((User)(session.getAttribute("User"))).getFirstName().length() > 8) ? 8 : ((User)(session.getAttribute("User"))).getFirstName().length()) %></span></button>
	<ul class="menu__inner">
<!--	<li><a><span>home page</span></a></li>-->arg0
	<li><a href="#" id="ModifyUserButton"><i class="fa fa-fw fa-cogs"></i><span>Parameters</span></a></li>
	</ul>
</nav>
<script>
$(document).ready(function(){	
	$('#ModifyUserButton').click(function(){
		$('.SettingsView').addClass('show');
		$('.col-left').removeClass('show');
		$('.MenuButtonSet').removeClass('show');
	});
});
/*$(document).ready(function(){	
	$('#menu').click(
		function(){
			$(this).addClass('menu--open');
		},
		function(){
			$(this).removeClass('menu--open');
		}
	);
});*/
(function() {

				function SVGDDMenu( el, options ) {
					this.el = el;
					this.init();
				}

				SVGDDMenu.prototype.init = function() {
					this.isOpen = false;

					this.initEvents();
				};

				SVGDDMenu.prototype.initEvents = function() {
					this.el.addEventListener( 'click', this.toggle.bind(this) );
						
				};

				SVGDDMenu.prototype.toggle = function() {
					var self = this;

					if( this.isOpen ) {
						classie.remove( self.el, 'menu--open' );
					}
					else {
						classie.add( self.el, 'menu--open' );
					}

					this.isOpen = !this.isOpen;	
				};

				new SVGDDMenu( document.getElementById( 'menu' ) );

			})();
</script>
