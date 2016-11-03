<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${adminMessage.visible()}">
	<div class="info-msg">
  		<i class="fa fa-info-circle"></i>
  		<p>${adminMessage.getMessage()}</p>
	</div>
</c:if>