<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <title>Welcome on ease !</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
   	<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
</head>
<script>
</script>
<BODY>
<style type="text/css">
	.lala img{
		background-color: #eee;
		border-radius: 10px;
	}
</style>
<div class="lala" style="width:400px;height:400px;">
	<img  style="width:100%; height:100%;"/>
</div>
<script type="text/javascript">

	$('img').each(function(idx, elem){
		console.log('lala');
		var tmpImg = $('<img>');
		var self = $(elem);
		tmpImg.load(function(){
			console.log('qsd');
			self.attr('src', $(this).attr('src'));
		});
		tmpImg.attr('src', '/resources/backgrounds/photo.jpg');
	});
</script>
</BODY>
