<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">

<title>登入</title>
<script>
var hasError = false;
window.onload = function(){
	var login = document.getElementById("login");
	
	login.onclick = function(){
		var u_id = document.getElementById("u_id").value;
		var u_psw = document.getElementById("u_psw").value;
		var result0c = document.getElementById("result0c");
		var result1c = document.getElementById("result1c");
		var resultMsg = document.getElementById("resultMsg");
		var rememberMeButton = document.getElementById("rememberMe");
		var rememberMe;
		if(rememberMeButton.checked){
			rememberMe = "rememberMe";
		}else{
			rememberMe = null;
		}
// 		alert("alert測試");
		if(!u_id){
			setErrorFor(result0c, "請輸入帳號");
		} else{
			result0c.innerHTML = "";
		}
		if(!u_psw){
			setErrorFor(result1c, "請輸入密碼");
		} else{
			result1c.innerHTML = "";
		}
		if (hasError){
			return false;
		}
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "<c:url value='/login.controller' />");
		var jsonLoginData = {
			"u_id" : u_id,
			"u_psw" : u_psw,
			"rememberMe" : rememberMe
		}
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(JSON.stringify(jsonLoginData));
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200){
				result = JSON.parse(xhr.responseText);
				//判斷回傳
				if(result.fail){
// 					resultMsg.innerHTML = "<font color='red' >" + result.fail + "</font>";
					alert(result.fail);
					var u_idc = document.getElementById("u_id");
					var u_pswc = document.getElementById("u_psw");
					u_idc.value = "";
					u_pswc.value = "";
				}else if(result.success){
// 					alert(result.loginBean.u_id+ ", " + result.success + "! 點擊跳轉至首頁...");
					alert(result.loginBean.u_id+ ", " + result.success);
// 					top.location='<c:url value='/gotoUserIndex.controller' />';
					top.location='<c:url value='/' />';
				}
			}
		}
	}
	
	function setErrorFor(input, message){
		input.innerHTML = "<font color='red' size='-2'>" + message + "</font>";
		hasError = true;
	}
	
	
}
</script>

</head>
<!-- <body> -->
<body class="is-preload">
<div id="wrapper">
<div id="main">
<div class="inner">
<%@include file="../universal/header.jsp" %> 

<div align='center'>
<!--   <h3>登入</h3> -->
  <div id='resultMsg' style="height: 18px; font-weight: bold;"></div>
  <hr>
</div>

<div style="text-align: center;">
  <div style="display: inline-block; text-align: left;">
      帳號: <input type="text" name="u_id" id='u_id'>
      <div id='result0c' style="height: 10px;"></div><br>
      密碼: <input type="password" name="u_psw" id='u_psw'>
      <div id='result1c' style="height: 10px;"></div><br>
  </div>
  <div align='center'>
	  <div class="form-check">
		  <input type="checkbox" value="rememberMe" id="rememberMe">
		  <label for="rememberMe"> 記住帳號 </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <span><a href="<c:url value='/gotoForgetPassword.controller' />">忘記密碼</a></span>
	  </div>
	  <button id="login" class='primary' style="width: 210px;">登入</button>
	  <hr>
	<%--     <a href="<c:url value='/gotoUserIndex.controller' />">上一頁</a> --%>
</div>
</div>


</div>
</div>
<%@include file="../universal/sidebar.jsp" %>  
</div>
<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>