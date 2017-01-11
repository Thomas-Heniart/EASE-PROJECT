<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="popupHandler" id="extension">
	<div class="easePopup show" id="step1">
		<div class="title">
			<p>Download our extension</p>
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
				</div>
			</div>
		</div>
	</div>
	<div class="easePopup" id="extensionInfo">
		<div class="title">
			<p>Our Extension</p>
		</div>
		<div class="bodysHandler">
			<div class="popupBody show">
				<div class="handler">
					<div class="row">
						<p>Ease uses an <span>extension</span>. It will <span>log you</span> on <span>your favorite websites.</span></p>
					</div>
					<div class="row">
						<p>It is a <span>small piece</span> of software that you add to your browser in order to <span>customize its capabilities.</span></p>
					</div>
					<div class="row">
						<p>Without it, Ease cannot <span>fill the blanks</span> and log you in to your websites.</p>
					</div>
					<div class="row">
						<p>Downloading the extension is a <span>1 click process</span> directly from Ease.</p>
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
		if (NavigatorName == "Chrome"){
			$("#extension #step1 #download").removeClass('show');
			$("#extension #step1 #chrome").addClass('show');
			chrome.webstore.install(
				'https://chrome.google.com/webstore/detail/echjdhmhmgildgidlcdlepfkaledeokm',
				function() {
//					window.location.replace("index.jsp");
				},
				function() {
//					window.location.replace("index.jsp");
				});
		} else if (NavigatorName == "Safari"){
			$("#extension #step1 #download").removeClass('show');
			$("#extension #step1 #safari").addClass('show');
			window.location.replace(location.protocol + '//' + location.hostname+"/safariExtension/EaseExtension.safariextz");
		}
	});
</script>