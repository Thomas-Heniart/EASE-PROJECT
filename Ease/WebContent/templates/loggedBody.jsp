<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="loggedBody">
    <div class="col-left show" style="width: 100%; float:left">
		<%@ include file="ProfileView.jsp"%>
		<%@ include file="ProfileEditView.jsp" %>
		<div id="downloadExtension" class="centeredItem" style="display:none;">
		<div class="popupContent">
			<p class="title">You are almost done ...<i class="fa fa-heart" aria-hidden="true"></i></p>
			<p class="info">Now, you need our addon for Ease to automagically work on this computer. (thank god it takes just 1 second  to download)</p>
			<p><i class="fa fa-angle-down" aria-hidden="true"></i></p>
			<button id="install-button">Get Ease Addon</button>
			<p id="moreInfoButton" style="margin-top: 5px;">Know more about the Addon.</p>
		</div>
		<div class="popupHelper" style="display: none;">
			<div class="firstLine" style="margin-bottom: 10px;">
				<div class="imageHandler">
					<img src="resources/other/extension_example.png" />
				</div>

				<div class="textHelper">
					<p style="font-size: 20px; font-weight: 600;">It looks like this !</p>
					<p>Ease uses an “<span>Addon</span>”. It is like an additional feature that you add to a browser, to customize its capabilities.
					</p>
				</div>
			</div>
			<div style="text-align:left;">
			<p>This small piece of software only appears on your <span>browser</span>. It is the tool that we use to <span>log you in and out</span>.
			</p>
			<p>Downloading the addon is a <span>1 step</span> process directly from Ease.</p>
			<p><span>We made it a button !</span> To help you get shit done faster. Clic on the addon and reach https://ease.space directly.</p>
			</div>
			<button id="returnButton">OK, got it.</button>
		</div>
		</div>
		<script type="text/javascript">
			$('#downloadExtension #moreInfoButton').click(function(){
				$('#downloadExtension .popupContent').css('display', 'none');
				$('#downloadExtension .popupHelper').css('display', 'block');
			});
			$('#downloadExtension #returnButton').click(function(){
				$('#downloadExtension .popupContent').css('display', 'block');
				$('#downloadExtension .popupHelper').css('display', 'none');				
			});
		</script>
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
