<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<style>
td {
	text-align: center;
}

tr {
	text-align: center;
}
</style>
<title>討論區</title>
<script>
	window.onload = function() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "<c:url value='/selectAllChatAdmin' />", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var content = "<table border='1'>";
				content += "<tr><th style='text-align: center; width: 60px;'>刪除</th>"
						+ "<th style='text-align: center; width: 60px;'>編號</th>"
						+ "<th style='text-align: center; width: 60px;'>類別</th>"
						+ "<th style='text-align: center; width: 120px;'>標題</th>"
						+ "<th style='text-align: center; width: 240px;'>內容</th>"
						+ "<th style='text-align: center; width: 60px;'>帳號</th>"
						+ "<th style='text-align: center; width: 120px;'>日期</th></tr>";
				var users = JSON.parse(xhr.responseText);
				for (var i = 0; i < users.length; i++) {
					var goDeleteChat = "<c:url value='/goDeleteChatAdmin/' />";
					content += "<tr><td align='center'><a href='"
							+ goDeleteChat + users[i].c_ID + "'>"
							+ "<img width='36' height='36' src='<c:url value='/images/user/d_user.svg' />' ></a>"
							+ "<td style='vertical-align: middle;'>"
							+ users[i].c_ID
							+ "</td>"
							+ "<td style='text-align: middle;'>"
							+ users[i].c_Class
							+ "</td>"
							+ "<td style='text-align: left;'>"
							+ users[i].c_Title
							+ "</td>"
							+ "<td style='text-align: left;'>"
							+ users[i].c_Conts
							+ "</td>"
							+ "<td style='vertical-align: middle;'>"
							+ users[i].u_ID
							+ "</td>"
							+ "<td style='vertical-align: middle;'>"
							+ users[i].c_Date
							+ "</td>"
							+ "</tr>";
				}
				content += "</table>";
				var selectAll = document.getElementById("selectAll");
				selectAll.innerHTML = content;
			}
		}
		
		var adminId = "${adminId}";
		//如果有登入，隱藏登入標籤
	    var loginHref = document.getElementById('loginHref');
	    var logoutHref = document.getElementById('logoutHref');
	    var userId = document.getElementById('userId');
	    var userPic = document.getElementById('userPic');
	    if(adminId){
	    	loginHref.hidden = true;
	    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
	    }
	    
	}
</script>
</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<div align='center'>
					<%@include file="../universal/adminHeader.jsp"%>
					<br>
					<div align='center' id='selectAll'></div>
				</div>
				<p />
			</div>
		</div>
		<%@include file="../universal/adminSidebar.jsp"%>
	</div>
	<script	src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>