<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="loggedBody">
    <div class="col-left show" style="width: 100%; float:left">
		<%@ include file="ProfileView.jsp"%>
		<%@ include file="extension.jsp" %>		
		<%@ include file="catalogView.jsp"%>
	</div>
	<%@ include file="SettingsView.jsp" %>
	<%@ include file="PopupModifyProfile.jsp" %>
	<%@ include file="PopupDeleteProfile.jsp" %>
	<%@ include file="PopupModifyUser.jsp" %>
	<%@ include file="PopupDeleteApp.jsp" %>
	<%@ include file="PopupAddApp.jsp" %>	
	<%@ include file="PopupModifyApp.jsp" %>
	<div class="md-overlay"></div>
</div>

<script>
$(document).ready(function(){
	$('.md-overlay').click(function(){
		$('.md-show').removeClass('md-show');
	});
	$('.popupClose').click(function () {
		$('.md-show').removeClass('md-show');
	});
});
</script>
		<script>
$(document).ready(function(){
	$('.cookiesInfo').css('display', 'none');
});
		(function() {
				if (!String.prototype.trim) {
					(function() {
						// Make sure we trim BOM and NBSP
						var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
						String.prototype.trim = function() {
							return this.replace(rtrim, '');
						};
					})();
				}

				[].slice.call( document.querySelectorAll( 'input.input__field' ) ).forEach( function( inputEl ) {
					// in case the input is already filled..
					if( inputEl.value.trim() !== '' ) {
						classie.add( inputEl.parentNode, 'input--filled' );
					}

					// events:
					inputEl.addEventListener( 'focus', onInputFocus );
					inputEl.addEventListener( 'blur', onInputBlur );
				} );
			})();
		</script>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-75916041-5', 'auto');
  ga('send', 'pageview');
</script>