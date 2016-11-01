		<div id="downloadExtension" class="centeredItem" style="display:none;">
		<div class="popupContent">
			<p class="title classicContent">You are almost done ...<i class="fa fa-heart" aria-hidden="true"></i></p>
			<p class="title safariUpdate">Your addon is too old !<i class="fa fa-recycle" aria-hidden="true"></i></p>
			<p class="info classicContent">Download the addon to make Ease automagically work on this computer</br>For now, it only works on Chrome and Safari.</p>
			<p class="info safariUpdate">Update it to make Ease automagically work on this computer.</p>
			<p><i class="fa fa-angle-down" aria-hidden="true"></i></p>
			<button class="install-button classicContent">Get Ease Addon</button>
			<button class="install-button safariUpdate">Update Ease Addon</button>
			<p id="moreInfoButton" style="margin-top: 5px;">Why is the addon necessary.</p>
		</div>
		<div class="safariHelper" style="display: none;">
			<h1 style="margin-top: 0px;">Final step</h1>
			<p>You have downloaded our addon (called "EaseExtension.safariextz"). Simply double click on it in your downloads to install it.</p>
			<div style="width: 55%;margin:0px auto 5px auto;"><img style="width: 100%" src="resources/other/safari-addon-example.png"/></div>
			<p style="font-size: 1.3em;">Once it's done, just refresh the page :)</p>
		</div>
		<div class="popupHelper" style="display: none;">
			<div class="firstLine" style="margin-bottom: 10px;">
				<div class="imageHandler">
					<img src="resources/other/extension_example.png" />
				</div>

				<div class="textHelper">
					<p style="font-size: 20px; font-weight: 600;">It looks like this !</p>
					<p>Ease uses an Ä<span>Addon</span>Äù. It is an additional feature that you add to a browser, to customize its capabilities.
					</p>
				</div>
			</div>
			<div style="text-align:left;">
			<p>This small piece of software only appears on your <span>browser</span>. It is the tool that we use to <span>log you in and out</span>.
			</p>
			<p>Downloading the addon is a <span>1 step</span> process directly from Ease.</p>
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
