<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">

<title>全部會員資料</title>
<script>
window.onload = function() {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "<c:url value='/showAllUser.controller' />", true);
	xhr.send();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var content = "<table border='1'>";
// 			var content = "<table border='1'>";
			content += "<tr><th>刪除</th><th width='100'>帳號</th><th width='100'>密碼</th>"
					+ "<th width='80'>姓</th><th width='80'>名</th><th width='100'>生日</th><th width='100'>電子郵件</th>"
					+ "<th width='100'>電話</th><th width='50'>性別</th><th width='150'>地址</th><th width='50'>圖片</th></tr>";
			var users = JSON.parse(xhr.responseText);
			for(var i=0; i < users.length; i++){
				//待寫刪除
				var gotoDeleteUser = "<c:url value='/gotoDeleteUser.controller/' />";
			    content += 	"<tr><td align='center'><a href='" + gotoDeleteUser + users[i].u_id + "'>" + 
    			"<img width='25' height='25' src='<c:url value='/images/user/d_user.svg' />' ></a>" + 
			    			"<td align='center'>" + users[i].u_id + "</td>" + 
			                "<td align='center'>" + users[i].u_psw + "</td>" +
		        	       	"<td align='center'>" + users[i].u_lastname + "</td>" +
		            	   	"<td align='center'>" + users[i].u_firstname + "</td>" +
							"<td align='center'>" + users[i].u_birthday + "</td>" +
							"<td align='center'>" + users[i].u_email + "</td>" +
							"<td align='center'>" + users[i].u_tel + "</td>" +
							"<td align='center'>" + users[i].u_gender + "</td>" +
							"<td align='center'>" + users[i].u_address + "</td>" +
							"<td align='center'>" + users[i].u_img + "</td>" +
		               		"</tr>";
			}
			content += "</table>";
			var showUser = document.getElementById("showUser");
			showUser.innerHTML = content;
		}
	}
		
}
</script>
</head>
<!-- <body> -->
<body class="is-preload">
<div id="wrapper">
<div id="main">
<div class="inner">
<div align='center'>
<%@include file="../universal/header.jsp" %> 
	<h3>會員資料</h3>
	<hr>
	<div class='center'  id='showUser'></div>
</div>
	
<p/>
<div align='center'>
<hr>
<a href="<c:url value='/gotoUserIndex.controller' />">上一頁</a>
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