<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
	<title>Welcome on ease !</title>
	<script src="/jsMinified.v00006/jquery1.12.4.js"></script>
	<script src="/js/postHandler.js"></script>
	<link rel="icon" type="image/png" href="resources/icons/account.png" />
	<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
</head>
<script>
</script>
<BODY>
	<style type="text/css">
		#search {
			margin-bottom: 10px;
		}
		p {
			display: inline-block;
			margin: 0;
		}
		.result .image {
			width: 40px;
			height: 40px;
			margin-right: 5px;
		}
		.result {
			margin-bottom: 10px;
		}
	</style>
	<input type="text" id="search" placeholder="search something..." />
	<noscript id="resultHelper">
		<div class="result">
			<img class="image" src=""/>
			<div style="display: inline-block;vertical-align:top;">
				<div><p>name : </p><p class="websiteName"></p></div>
				<div><p>url : </p><p class="url"></p></div>
			</div>
		</div>
	</noscript>
	<div id="results" style="display:block;"></div>

	<script type="text/javascript">
			$('#search').on('input', function(){
				var self = $(this);
				if (!(self.val().length)){
					$('#results').text('');
					return;
				}
				$.get('https://autocomplete.clearbit.com/v1/companies/suggest?query=' + self.val(), function(data){

					var json = data;
					var results = $('#results');
					$('#results').text('');
					for (var i = 0; i < json.length; i++) {
						var tmp = $($('#resultHelper').text());
						tmp.find('img').attr('src',json[i].logo);
						tmp.find('.websiteName').text(json[i].name);
						tmp.find('.url').text(json[i].domain);
						results.append(tmp);
					}
				});
			});
	</script>
</BODY>
