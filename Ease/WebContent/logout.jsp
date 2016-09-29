<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EASE Logout</title>
<script src="js/jquery1.12.4.js"></script>
	<script src="js/jquery-ui-1.12.0.js"></script>
	<link rel="stylesheet" href="css/logout.css" />
</head>
<body class='logout'>
<script type="text/javascript">
	$(document).ready(function() {
		setTimeout(function() {
			window.location.replace("index.jsp");
		}, 4000);
	});
</script>

<div class='logout-overlay'>
	<h3>We are logging you out</h3>
	<div class="sk-fading-circle">
  <div class="sk-circle1 sk-circle"></div>
  <div class="sk-circle2 sk-circle"></div>
  <div class="sk-circle3 sk-circle"></div>
  <div class="sk-circle4 sk-circle"></div>
  <div class="sk-circle5 sk-circle"></div>
  <div class="sk-circle6 sk-circle"></div>
  <div class="sk-circle7 sk-circle"></div>
  <div class="sk-circle8 sk-circle"></div>
  <div class="sk-circle9 sk-circle"></div>
  <div class="sk-circle10 sk-circle"></div>
  <div class="sk-circle11 sk-circle"></div>
  <div class="sk-circle12 sk-circle"></div>
</div>
	<h3>automagically...</h3>
</div>
</body>
</html>