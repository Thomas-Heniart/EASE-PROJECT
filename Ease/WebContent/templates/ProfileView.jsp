<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	List<com.Ease.session.Profile> pr = user.getProfiles();
	List<List> columns = new LinkedList<List>();
	columns.add(new LinkedList<com.Ease.session.Profile>());
	columns.add(new LinkedList<com.Ease.session.Profile>());
	columns.add(new LinkedList<com.Ease.session.Profile>());
	columns.add(new LinkedList<com.Ease.session.Profile>());
	int a = 0;
	for (com.Ease.session.Profile profile : pr) {
		columns.get(a).add(profile);
		a++;
	}
	pageContext.setAttribute("dashboardColumns", columns);
%>

<div class="ProfilesView show">
	<div class="MenuButtonSet">
		<button id="enterEditMode" state="off" class="button"><img src="resources/icons/menu_icon.png"/>
		<div class="openCatalogHelper"></div>
	</div>
	<c:if test="${user.getTuto() == '0'}">
		<%@ include file="Tutorial.jsp"%>
	</c:if>
<div class="ProfilesHandler">
	<c:forEach items='${dashboardColumns}' var='column'>
		<div class="dashboardColumn">
			<c:forEach items='${column}' var="profile">
				<dashboard:profile profile="${profile}"/>			
			</c:forEach>
		</div>
	</c:forEach>
</div>
<%@ include file="ObjectHelpers.jsp"%>
<p class="shortcutInfo" style="text-shadow: 0 0 30px black, 0 0 30px black, 0 0 30px black, 0 0 30px black; color: white; font-size:12px; font-weight:500; position: absolute; bottom: 1.5%; left:2%; display:none;">Hold ctrl and click to open multiple apps.</p>
</div>
