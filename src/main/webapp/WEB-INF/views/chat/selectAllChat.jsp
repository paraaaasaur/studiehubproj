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
#createBtn{
  padding: 30px 20px;
  position:fixed;
  top:85%;
  left:90%;
  z-index:1;
  font-size: 100%;
}
#iconPos{
  position:relative;
  bottom: 20px;
  font-size:30px;
}
</style>
<title>討論區</title>
<script>
	window.onload = function() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "<c:url value='/selectAllChat' />", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var content = "<table border='1'>";
				content += "<tr><th style='text-align: center; width: 60px;'>類別</th>"
						+ "<th style='text-align: center; width: 360px;'>標題</th>"
						+ "<th style='text-align: center; width: 60px;'>帳號</th>"
						+ "<th style='text-align: center; width: 120px;'>日期</th></tr>";
				var users = JSON.parse(xhr.responseText);
				for (var i = 0; i < users.length; i++) {
					var goDeleteChat = "<c:url value='/goDeleteChat/' />";
					content += "<tr><td style='vertical-align: middle;'>"
							+ users[i].c_Class
							+ "</td>"
							+ "<td style='text-align: left;'>"
							+ "<a href=\"#\">" + users[i].c_Title + "</a>"
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
					<%@include file="../universal/header.jsp"%>
					<br>
					<div align='center' id='selectAll'></div>
				</div>
				<p />
			</div>
		</div>
		<button id="createBtn" type="button" onclick="location.href='<c:url value="/goInsertChat" />'"><i id="iconPos" class="fas fa-pen"></i></button>
		<%@include file="../universal/sidebar.jsp"%>
	</div>
	<script	src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>