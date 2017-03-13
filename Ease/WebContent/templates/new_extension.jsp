<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="popupHandler" id="extension">
	<div class="easePopup show" id="step1">
		<div class="title">
			<p>Make Ease.space work ! <img src="/resources/emojis/lightning.png" /></p>
		</div>
		<div class="bodysHandler">
			<div class="popupBody show" id="download">
				<div class="handler">
					<div class="row text-center">
						<p style="font-size:1.5vw;" id="line1">This computer doesn’t have the Ease Extension yet.</p>
					</div>
					<div class="row text-center">
						<p style="font-size:1.4vw" id="line2">It needs to be installed on your browser in order for Ease to fully work.</p>
					</div>
					<div class="row text-center">
						<button class="btn" type="submit">Download Ease Extension</button>
					</div>
					<div class="row text-center">
						<p id="showExtensionInfo" style="text-decoration:underline;">Why is the extension necessary</p>
					</div>
				</div>
			</div>
			<div class="popupBody" id="chrome">
				<div class="handler">
					<div style="padding:1vw 0;" class="row text-center">
						<p>Click on "Add extension"</p>
					</div>
					<div class="row text-center">
						<button class="btn" type="submit">Then click here!</button>
						<p id="store_download">If nothing happens, <a target="_blank" href="https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm">click here</a> to add the extension through the web store</p>
					</div>
				</div>
			</div>
			<div class="popupBody" id="safari">
				<div class="handler">
					<div class="row">
						<p style="width:10%;">1.</p><p style="width:78%;">Complete the step above :)</p>
					</div>
					<div class="row">
						<p style="width:10%;">2.</p><p style="width:78%;">Click "Trust" in the extension settings of Safari</p>
					</div>
					<div class="row text-center">
						<button class="btn" type="submit">Then click here!</button>
					</div>
				</div>
				<div class="installSafariHelper">
					<div style="margin-bottom:0.5vw" class="row">
						<p>Click on the icon <img style="height:1.8vw" src="resources/icons/safariDownloadButton.png"/> over here.</p>
					</div>
					<div class="row">
						<p>Then double-click on EaseExtension.safariextz</p>
					</div>
					<div class="crt">
						<i class="fa fa-chevron-up" aria-hidden="true"></i>
					</div>
				</div>
			</div>
			<div class="popupBody" id="other">
				<div class="handler">
					<div class="row">
						<p>Ease.space works on Chrome and Safari for now.</p>
					</div>
					<div class="row">
						<p>We are working on bringing it to all browsers soon !</p>
					</div>
					<div class="row">
						<p>To continue using Ease.space, you can download <a href="#">Chrome</a> or use Safari now.</p>
					</div>
				</div>
			</div>			
		</div>
	</div>
	<div class="easePopup" id="extensionInfo">
		<div class="title">
			<p>Our Extension <img src="/resources/emojis/loupe.png" /></p>
		</div>
		<div class="bodysHandler">
			<div class="popupBody show">
				<div class="handler">
					<div class="row">
						<p>An extension is a small piece of software that you add to your browser in order to customize it's capabilities.</p>
					</div>
					<div class="row">
						<p>The Ease extension allows us to log you in and out of your favorite website.</p>
					</div>
					<div class="row">
						<p>It also keeps your personnal information secured. Downloading it takes about 5 seconds.</p>
					</div>
					<div class="row text-center">
						<button class="btn" type="submit">Got it !</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var NavigatorName = getUserNavigator();
	$("#chrome button[type='submit'], #safari button[type='submit']").click(function(){
		window.location.replace("index.jsp");
	});
	$("#extension #download #showExtensionInfo").click(function(){
		$('#extension #step1').removeClass('show');
		$('#extension #extensionInfo').addClass('show');
	});
	$("#extension #extensionInfo button").click(function(){
		$('#extension #extensionInfo').removeClass('show');		
		$('#extension #step1').addClass('show');
	});
	$("#extension #download button[type='submit']").click(function(){
		$("#extension #step1 #download").removeClass('show');
		if (NavigatorName == "Chrome"){
			$("#extension #step1 #chrome").addClass('show');
			chrome.webstore.install(
				'https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm',
				function() {
					//do nothing
				},
				function() {
					//do nothing
				});
		} else if (NavigatorName == "Safari"){
			$("#extension #step1 #safari").addClass('show');
			window.location.replace(location.protocol + '//' + location.hostname+"/safariExtension/EaseExtension.safariextz");
		} else {
			$("#extension #step1 #other").addClass('show');
		}
	});
</script>