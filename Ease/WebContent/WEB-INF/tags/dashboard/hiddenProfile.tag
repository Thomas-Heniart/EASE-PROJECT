<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/dashboard" prefix="dashboard" %>

<%@ attribute name="profile" type="com.Ease.Dashboard.Profile.Profile" required="true"%>

<div class="hiddenProfile" style="display:none;">
<div class="hiddenProfileContainer" id='${profile.getDBid()}'>
	<c:forEach items='${profile.getApps()}' var="app">
		<dashboard:app app='${app}'/>
	</c:forEach>
</div>
<div class="hiddenProfileHelper">
	<i class="fa fa-folder-open" aria-hidden="true"></i>
</div>
</div>