<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">

<title>忘記密碼</title>
<script>
var hasError = false;
window.onload = function(){
	var sendBtn = document.getElementById("send");
	
	sendBtn.onclick = function(){
		var email = document.getElementById("userEmail").value;
		
		if(!isEmail(email)){
			if(!email){
				alert("尚未輸入信箱!");
				hasError = true;
			}else{	
				alert("信箱格式錯誤, 請再試一次!");
				hasError = "true";
			}
		}
		if (hasError){
			return false;
		}
		
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "<c:url value='/sendRandomPasswordToRegisteredEmail.controller' />");
		var jsonData = {
			"u_email" : email
		}
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(JSON.stringify(jsonData));
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200){
				result = JSON.parse(xhr.responseText);
				//判斷回傳
// 				if(result.fail){
// 					alert("發生錯誤, 請再試一次!");
// 				}else if(result.success){
// 					alert("新的密碼已寄送到您的信箱, 請使用新密碼登入後立即修改!");
// 					top.location='<c:url value='/gotologin.controller' />';	//按下後，回登入畫面
// 				}
				alert(result.result);
			}
		}
	}
	
	
	function isEmail(email) {
	    return /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email);
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

<!-- <div align='center'> -->
<!--   <h3>登入</h3> -->
<!--   <div id='resultMsg' style="height: 18px; font-weight: bold;"></div> -->
<!--   <hr> -->
<!-- </div> -->

<div style="text-align: center;">
  <div style="display: inline-block; text-align: left;">
  <br>
  <br>
  <br>
      <input type="text" id="userEmail" style="display: inline; width: 500px; float: none;border-radius: 10px;" placeholder="請輸註冊信箱..." autofocus="autofocus">
	  <button id="send" style="display: inline;">送出</button>
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