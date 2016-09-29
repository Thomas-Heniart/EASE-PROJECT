<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="loggedBody">
    <div class="col-left show" style="width: 100%; float:left">
		<%@ include file="ProfileView.jsp"%>
		<%@ include file="ProfileEditView.jsp" %>
		<div id="downloadExtension" class="centeredItem" style="display:none;">
			<p class="title">You are almost done ...<i class="fa fa-heart" aria-hidden="true"></i></p>
			<p class="info">Now, you need our addon for Ease to automagically work on this computer. (thank god it takes just 1 second  to download)</p>
			<p><i class="fa fa-angle-down" aria-hidden="true"></i></p>
			<button id="install-button">Get Ease Addon</button>
		</div>
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
	setTimeout(function(){sendConnectionStatus(true);},500);
});

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
