<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="RightSideViewTab" id="AddSiteTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>

	<div>
		<div>
			<p>Add a website to Ease</p>
		</div>

		<div>
			<form method="post" id="addSiteForm" action="uploadWebsite"
				enctype="multipart/form-data">
				<input type="text" name="siteName" class="form-control"
					placeholder="Website folder" />
				<h4 style="margin-left: 25%; font-size: 1em">Json file</h4>
				<input id="jsonFile" type="file" class="form-control" accept=".json"
					name="uploadFile" />
				<h4 style="margin-left: 25%; font-size: 1em">Png file</h4>
				<input id="pngFile" type="file" class="form-control" accept=".png"
					name="uploadFile" /> <input type="submit"
					class="btn btn-default btn-primary" value="Upload" />
			</form>

		</div>

		<div style="margin-top: 5%">
			<form method="post" id="addSiteForm" action="addWebsite">
				<input type="text" name="siteUrl" class="form-control"
					placeholder="Website url" /> <input type="text" name="homePage" class="form-control"
					placeholder="Homepage url" /> <input type="text" name="siteName"
					class="form-control" placeholder="Website name" /> <input
					type="text" name="siteFolder" class="form-control"
					placeholder="Website folder. DO : 'Facebook' , DONT : 'resources/wesites/Facebook/'" />
				<div class="form-control"
					style="margin-left: 25%; margin-top: 1%; width: 50%; text-align: center; position: relative;">
					<input
						style="width: 20px; height: 20px; padding: 0; margin: 0; vertical-align: bottom; position: relative;"
						type="checkbox" name="haveLoginButton" />Have loginWith button
				</div>
				<div class="form-control"
					style="margin-left: 25%; margin-top: 1%; width: 50%; position: relative;">
					Have login button : <input
						style="width: 20px; height: 20px; padding: 0; margin: 0; vertical-align: bottom; position: relative;"
						type="checkbox" name="haveLoginWith" value="7" />Facebook <input
						style="width: 20px; height: 20px; padding: 0; margin: 0; vertical-align: bottom; position: relative;"
						type="checkbox" name="haveLoginWith" value="28" />Linkedin
				</div>
				<input type="submit" class="btn btn-default btn-primary"
					value="Send to database" />
			</form>
		</div>
		
		<div class="movable-websites">
			<c:forEach items="${siteManager.getSitesList()}" var="item" varStatus="loop">
				<c:if test="${!item.isHidden()}">
					<div style="margin: 10px 0;">
						<img width="50" src="${item.getFolder()}logo.png" />
						<span style="margin-left: 10px;">${item.getName()}</span>
						<span>position : ${item.getPosition()}</span>
						<button class="test-move" siteId="${item.getId()}">Move to top</button>
					</div>
				</c:if>
			</c:forEach>
			<form action="ChangeSitePosition">
				<input type="hidden" name="position" />
			</form>
			<script>
				$(".test-move").click(function() {
					var siteId = $(this).attr("siteId");
					var itemToPrepend = $(this).parent();
					$.post(
						'ChangeSitePosition',
						{
							siteId: siteId
						},
						function(data) {
							$(".movable-websites").prepend(itemToPrepend);
						}
					);
				});
			</script>
		</div>
	</div>
</div>