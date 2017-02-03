<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<div class="easePopup" id="addBookmarkPopup">
	<div class="title">
		<p>Add bookmark link</p>
	</div>
	<div class="bodysHandler">
		<div class="popupBody show" id="addBookmark">
			<div class="handler">
				<div class="row text-center">
					<div class="logo" id="appLogo">
						<img src="/resources/websites/Facebook/logo.png"/>
					</div>
				</div>
				<div class="row text-center">
					<div class="input appName">
						<input id="appName" type="text" name="name" placeholder="App name" />
						<i class="fa fa-pencil placeholderIcon" aria-hidden="true"></i>
					</div>
				</div>
				<div class="row infoRow">
				<p class="simpleText">
					We are currently working on <span>fully integrating</span> this app.	
				</p>
				<p class="simpleText">
					Add it as a bookmark and we will integrate it <span>faster</span> !
				</p>
				</div>
				<div class="row text-center orDelimiter lineBehind">
					<p class="post-title text-underlined">URL</p>
				</div>
				<div class="row">
					<input type="text" name="url" placeholder="Your url..." />
				</div>
				<div class="row text-center errorText errorHandler">
					<p>
					</p>
				</div>
				<div class="row text-center">
					<button class="btn" type="submit">Add as shortcut</button>
				</div>
				<div class="row text-center">
					<a id="goBack" class="liteTextButton">Go back</a>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="js/popups/addBookmarkPopup.js"></script>