<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ attribute name="app" type="com.Ease.Dashboard.App.App" required="true" %>
<%@ attribute name="informations" type="java.util.Map" required="false" %>
<c:choose>
    <c:when test="${app.getType() eq 'WebsiteApp'}">
        <div class="siteLinkBox emptyApp <c:if test='${app.isDisabled()}'>app_disabled</c:if>"
        login=""
        webId="${app.getSite().getDb_id()}"
        name="${app.getName()}"
        move="true"
        id="${app.getDBid()}"
        ssoId="${app.getSite().getSsoId()}"
        logwith=""
        data-type="${app.getType()}">
        <div class="linkImage">
            <div class="emptyAppIndicator">
                <span></span>
                <img src="resources/other/raise-your-hand-to-ask.svg"/>
            </div>
            <div class="showAppActionsButton">
                <i class="fa fa-cog"></i>
            </div>
            <img class="logo" src="/resources/helpers/1x1_grey_square.jpg"
                 lazy-src="<c:out value='${app.getSite().getLogo()}'/>"/>
        </div>
    </c:when>
    <c:when test="${app.getType() eq 'LinkApp'}">
        <div class="siteLinkBox <c:if test='${app.isDisabled()}'>app_disabled</c:if>"
        name="${app.getName()}"
        id="${app.getDBid()}"
        url="${app.getLinkAppInformations().getLink()}"
        move="true"
        logwith=""
        data-type="${app.getType()}">
        <div class="linkImage">
            <c:if test='${app.isPinned() eq false}'>
                <div class="showAppActionsButton">
                    <i class="fa fa-cog"></i>
                </div>
            </c:if>
            <c:if test='${app.isDisabled()}'>
                <img class="logo app_disabled" src="/resources/icons/app_disabled.png"/>
            </c:if>
            <img class="logo" src="/resources/helpers/1x1_grey_square.jpg"
                 lazy-src="${app.getLinkAppInformations().getImgUrl()}"/>
        </div>
    </c:when>
    <c:otherwise>
        <c:if test="${app.getType() eq 'ClassicApp'}">
            <div class="siteLinkBox <c:if test='${app.isDisabled()}'>app_disabled</c:if>"
            <c:forEach items="${app.getAccount().getAccountInformationsWithoutPassword()}" var="entry">
                ${entry.getInformationName()}="${entry.getInformationValue()}"
            </c:forEach>
            webId="${app.getSite().getDb_id()}"
            name="${app.getName()}"
            id="${app.getDBid()}"
            ssoId="${app.getSite().getSsoId()}"
            move="true"
            logwith=""
            account='${app.getAccount().getJson()}'
            data-type="${app.getType()}">
        </c:if>
        <c:if test="${app.getType() eq 'LogwithApp'}">
            <div class="siteLinkBox"
            webId="${app.getSite().getDb_id()}"
            name="${app.getName()}"
            id="${app.getDBid()}"
            ssoId="${app.getSite().getSsoId()}"
            move="true"
            logwith="${app.getLogwith().getDBid()}"
            data-type="${app.getType()}"
            logwithname="${app.getLogwith().getSite().getName()}">
        </c:if>
        <div class="linkImage">
            <c:if test="${app.isPinned() eq false}">
                <div class="showAppActionsButton">
                    <i class="fa fa-cog"></i>
                </div>
            </c:if>
            <c:if test='${app.isDisabled()}'>
                <img class="logo app_disabled" src="/resources/icons/app_disabled.png"/>
            </c:if>
            <img class="logo" src="/resources/helpers/1x1_grey_square.jpg"
                 lazy-src="<c:out value='${app.getSite().getLogo()}'/>"/>
        </div>
    </c:otherwise>
</c:choose>
<div class="siteName">
    <p>${app.getName()}</p>
</div>
</div>
