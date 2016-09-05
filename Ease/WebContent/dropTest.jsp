<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<html xmlns="http://w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" contentType="text/html; charset=UTF-8"/>
    <title>Welcome on ease !</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
    <link rel="stylesheet" href="css/default_style.css" />
    <link href='https://fonts.googleapis.comcss?family=Source+Sans+Pro' rel='stylesheet' type='textcss' />
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/owl.carousel.css" />
    
    <link rel="stylesheet" href="css/lib/vicons-font/vicons-font.css">
    <link rel="stylesheet" href="css/lib/vicons-font/buttons.css">
	<link rel="stylesheet" href="css/lib/textInputs/set1.css">
	<link rel="stylesheet" href="css/lib/borderLoading/component.css">
	<link rel="stylesheet" href="css/lib/niftyPopupWindow/component.css">
	
	<script src="js/classie.js"></script>
   	<script src="js/owl.carousel.js"></script>
    <script src="js/basic-utils.js" ></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"> </script>
    <script src="js/jquery.mousewheel.min.js"></script>
    
		<link rel="stylesheet" type="text/css" href="css/lib/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
		<link rel="stylesheet" type="text/css" href="css/lib/dropDownMenu/dropdown.css" />
		<script src="js/snap.svg-min.js"></script>
    	<script src="js/modalEffects.js"></script>
    	<script src="http://code.interactjs.io/v1.2.6/interact.min.js"></script>
</head>
<script>
</script>
<BODY>
<div id="newuser">
			<input  id="email" name="email" type="email" placeholder="email"/>
</div>
       <button id="go">GO !</button> 
</BODY>
<script type="text/javascript">

$(document).ready( function() {
/*	$('#go').click(function(){
			var fname = $('#fname').val();
			var lname = $('#lname').val();
			var email = $('#email').val();
			var password = $('#password').val();
			var confirmPassword = $('#confirmPassword').val();
			$.post(
				'ease.space/newUser',
				{
					fname: fname,
					lname: lname,
					email: email,
					password: password,
					confirmPassword:  confirmPassword
				},
				function(msg){
					alert(msg);
				},
				'text'
			);		
	});*/
	
	
	$('#go').click(function(){
		$.post(
		'createInvitation',
		{
			email: $('#email').val()
		},
		function(data){
			alert(data);
		},
		'text'
		);
	});
});
/*$(document).ready( function() {
	$('#go').click(function(){
		var contactsLengt = 0;
		var c;
		var splited;

		var fname;
		var lname;
		var email;
		var password;

		while (contactsLengt < contacts.length){
			c = contacts[contactsLengt];

			splited = c.split("\t");
			fname = splited[1];
			lname = splited[2];
			email = splited[0];
			password = splited[3];
			$.post(
				'newUser',
				{
					fname: fname,
					lname: lname,
					email: email,
					password: password,
					confirmPassword: password 
				},
				function(msg){
					if (msg[0] == 's'){
					var   nb = parseInt($('.currentNb').text());

					var   nbr = nb + 1;
					$('.currentNb').text(nbr);
				}
				},
				'text'
			);		
			contactsLengt++;
		}
	});
});*/
</script>