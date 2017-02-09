<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<div class="ProfilesView show">
	<div class="ProfilesHandler <c:if test="${param.catalogOpen}">editMode</c:if>">
		<c:forEach items='${dashboardColumns}' var='column' varStatus="loop">
		<c:if test="${loop.index != 0}">
		<div class="dashboardColumn" style="${column.size() == 0 ? 'width:0;': ''}">
			<c:forEach items='${column}' var="profile">
			<dashboard:profile profile="${profile}"/>
		</c:forEach>

	</div>
</c:if>
</c:forEach>
<div id="profileAdder">
	<span class="opener fa-stack fa-lg">
		<i class="fa fa-circle fa-stack-1x" aria-hidden="true"></i>
		<i class="fa fa-plus-circle fa-stack-1x" aria-hidden="true"></i>
	</span>
	<div class="adder">
		<div class="row inputRow">
			<div class="input">
				<span class="closer fa-stack fa-lg">
					<i class="fa fa-circle fa-stack-1x" aria-hidden="true"></i>
					<i class="fa fa-plus-circle fa-stack-1x" aria-hidden="true"></i>
				</span>			
				<input type="text" name="name" placeholder="New category" />
			</div>
			<button class="btn" type="submit">Ok</button>
		</div>
		<div class="handler">
			<div class="row suggestionsRow">
				<p class="sectionName">Suggestions</p>
				<div class="suggestions">
				</div>
			</div>
			<div class="row colorChooseRow">
				<p class="sectionName">Change color to :</p>
				<div class="colors">
					<c:forEach items="${colors}" var="color" varStatus="loop">
					<c:if test="${loop.index < 8}">
					<div class="colorHolder">
						<div class="color" color="${color}" style="background-color: ${color}">
						</div>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>
</div>
</div>
</div>
<%@ include file="ObjectHelpers.jsp"%>
</div>
<p class="shortcutInfo" style="text-shadow: 0 0 30px black, 0 0 20px black, 0 0 10px black; color: white; font-size:12px; font-weight:500; position: fixed; bottom: 1.5%; left:2%; display:none;">Hold ctrl and click to open multiple apps.</p>
