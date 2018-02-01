<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class='RightSideViewTab <c:if test="${param.verifyEmails}">show</c:if>' id="GrowthHackingTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	<div class="presentation">
		<h1>How to use the module</h1>
		<p>First, you must fill <strong>email</strong> and <strong>password</strong> (Number of emails to test / 5)</p>
		<p>Then upload your csv file with email list which needs to be clean and click on upload (You can't upload more than 58 emails to test per GMail address)</p>
		<p>Once the page reloads, wait to receive an email which say the list is ready to be clean</p>
		<p>To finish, on right side, you must enter emails you used to send emails one by one</p>
		<p>Finally copy the list of emails</p>
	</div>
	<div class="flex-row">
		<form class="centered-row" method="post" id="uploadEmailList" action="MailListCleaner" enctype="multipart/form-data">
				<h2>Upload and send emails</h2>
				<div class='email-inputs'>
					<div>
						<input type="email" name="email" class="input" placeholder="Email" />
						<input type="password" name="password" class="input" placeholder="Password" />
					</div>
					<div>
						<input type="email" name="email" class="input" placeholder="Email" />
						<input type="password" name="password" class="input" placeholder="Password" />
					</div>
					<div>
						<input type="email" name="email" class="input" placeholder="Email" />
						<input type="password" name="password" class="input" placeholder="Password" />
					</div>
					<div>
						<input type="email" name="email" class="input" placeholder="Email" />
						<input type="password" name="password" class="input" placeholder="Password" />
					</div>
					<div>
						<input type="email" name="email" class="input" placeholder="Email" />
						<input type="password" name="password" class="input" placeholder="Password" />
					</div>
				</div>
				<div>
					<a href="#" id="addEmail">+ Add email</a>				
				</div>
				<input type="submit" value="Upload" />
		</form>
		<div class="centered-row">
			<div id="loading"></div>
			<h2>Clean emails</h2>
			<form id="verifyEmailForm">
				<input type="email" id="emailToVerify" class="input" placeholder="example@gmail.com" />
				<input type="submit" value="Verify" /> 
			</form>
			<button id="signout-button" style="display: none;">Sign Out</button>
			<p id="email-count" class="hidden">
				<span id="currentNumber"></span>/<span id="totalNumber"></span>
			</p>
			<div id="results"></div>
			<script type="text/javascript">
				$.get("MailListCleaner").success(function(data) {
					serverEmails = []
					JSON.parse(data.substring(4)).forEach(function(email) {
						if (email.charCodeAt(email.length - 1) == 13)
							email = email.slice(0, -1);
						serverEmails.push(email);
					});
					$("#totalNumber").text(serverEmails.length);
				});
				$("#GrowthHackingTab #addEmail").click(function(e) {
					e.preventDefault();
					$("#GrowthHackingTab .email-inputs").append("<div>"
							+ "<input type='email' name='email' class='input' placeholder='Email' />"
							+ "<input type='password' name='password' class='input' placeholder='Password' />"
							+ "</div>");
				});
				// Enter an API key from the Google API Console:
				//   https://console.developers.google.com/apis/credentials
				var apiKey = 'AIzaSyBoaxdj0lp_SoLR0JsJK86aQJA_ExxHeeI';
				// Enter the API Discovery Docs that describes the APIs you want to
				// access. In this example, we are accessing the People API, so we load
				// Discovery Doc found here: https://developers.google.com/people/api/rest/
				var discoveryDocs = [
						"https://people.googleapis.com/$discovery/rest?version=v1",
						"https://www.googleapis.com/discovery/v1/apis/gmail/v1/rest" ];
				// Enter a client ID for a web application from the Google API Console:
				//   https://console.developers.google.com/apis/credentials?project=_
				// In your API Console project, add a JavaScript origin that corresponds
				//   to the domain where you will be running the script.
				var clientId = '337155500789-1fkropmq0n38ologumpu15ftln8v55ed.apps.googleusercontent.com';
				// Enter one or more authorization scopes. Refer to the documentation for
				// the API or https://developers.google.com/people/v1/how-tos/authorizing
				// for details.
				//var scopes = 'gmail.readonly';
				var signoutButton = document.getElementById('signout-button');
				var loading = $("#loading");
				var emails = [];
				var serverEmails = [];
				var senders = [];
				var currentEmail;
				function handleClientLoad() {
					// Load the API client and auth2 library
					gapi.load('client:auth2', initClient);
				}
				function initClient() {
					gapi.client.init(
							{
								apiKey : apiKey,
								discoveryDocs : discoveryDocs,
								clientId : clientId,
								scope : 'https://www.googleapis.com/auth/gmail.readonly'
					}).then(function() {
						// Listen for sign-in state changes.
						gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);
						// Handle the initial sign-in state.
						updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
						$("#verifyEmailForm").submit(function(e) {
							e.preventDefault();
							currentEmail = $("#emailToVerify", this).val();
							gapi.auth2.getAuthInstance().signIn();
						})
						signoutButton.onclick = handleSignoutClick;
					});
				}
				function updateSigninStatus(isSignedIn) {
					if (isSignedIn) {
						signoutButton.style.display = 'block';
						makeApiCall();
					} else {
						signoutButton.style.display = 'none';
					}
				}
				function handleAuthClick(event) {
					gapi.auth2.getAuthInstance().signIn();
				}
				function handleSignoutClick(event) {
					gapi.auth2.getAuthInstance().signOut();
				}
				// Load the API and make an API call.  Display the results on the screen.
				function makeApiCall() {
					var userId = currentEmail;
					var query = "to:"+currentEmail;
					gapi.client.gmail.users.threads.list({
						userId : userId,
						q : query
					}).then(function(resp) {
						var threads = JSON.parse(resp.body).threads;
						if (threads == undefined)
							return;
						
						threads.forEach(function(thread) {
							var snippet = thread.snippet;
							if (!(snippet.indexOf("blocked") > -1 || snippet.indexOf("bloqué") > -1)) {
								var atIndex = snippet.indexOf("@");
								var email = "@";
								var i;

								for (var i = atIndex - 1; snippet[i] != " " && i >= 0; i--)
										email = snippet[i] + email;

								for (i = atIndex + 1; snippet[i] != "." && i < snippet.length; i++)
									email = email + snippet[i];
								email = email + ".";
							
								if (email[email.length - 1] == ".") {
									for (i = i + 1; snippet[i] != " " && snippet[i] != "." && snippet[i] != "!" && snippet[i] != ","; i++)
										email = email + snippet[i];
									emails.push(email);
								}
							}
						});
						setTimeout(function() {
							$("#GrowthHackingTab #results div").remove();
							emails.forEach(function(email) {
								if (email.charCodeAt(email.length - 1) == 13)
									email = email.slice(0, -1);
								var index = serverEmails.indexOf(email);
								if (index > - 1)
									serverEmails.splice(index, 1);
							});
							$("#currentNumber").text(serverEmails.length);
							$("#email-count").removeClass("hidden");
							gapi.auth2.getAuthInstance().signOut();
							serverEmails.forEach(function(email) {
								$("#GrowthHackingTab #results").append("<div>" + email + "</div>");
							});
						}, 200);
					});
				}
			</script>
			<script async defer src="https://apis.google.com/js/api.js"
				onload="this.onload=function(){};handleClientLoad()"
				onreadystatechange="if (this.readyState === 'complete') this.onload()">
				gapi.auth2.getAuthInstance().signOut();
				
			</script>
		</div>
	</div>
</div>