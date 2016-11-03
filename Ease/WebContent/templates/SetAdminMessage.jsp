<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="RightSideViewTab" id="AdminMessageTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	
	<div>
		<div>
			<p>Set up admin message</p>
		</div>
	
		<div>
			<form method="post" id="setAdminMessage" action="updateAdminMessage">
				<input type="text" name="message" class="form-control" placeholder="Message" value="${adminMessage.getMessage()}"/>
				<input type="text" name="color" class="form-control" placeholder="Hex code of color (ex : #363B60)" value="${adminMessage.getColor()}"/>
				<div class="form-control" style="margin-left: 25%; margin-top: 1%; width: 50%; text-align: center; position: relative;">
					<c:if test="${adminMessage.visible()}">
						<input style="width: 20px; height: 20px; padding: 0; margin: 0; vertical-align: bottom; position: relative;" 
						type="checkbox" name="visible" checked/>
					</c:if>
					<c:if test="${!adminMessage.visible()}">
						<input style="width: 20px; height: 20px; padding: 0; margin: 0; vertical-align: bottom; position: relative;" 
						type="checkbox" name="visible"/>
					</c:if>
					Visible
				</div>
				<input type="submit" class="btn btn-default btn-primary"
					value="Send to database" />
			</form>
		</div>
	</div>
</div>