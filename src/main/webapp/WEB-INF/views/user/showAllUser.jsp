<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<style>
td {
	text-align: center;
}

tr {
	text-align: center;
}
</style>
<title>會員資料</title>
<script>


var segments = [];
var pageNum = 0;
var rowPerPage = 5;


	window.onload = function() {
		
		let pageHref = $('#pageHref');
		
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "<c:url value='/showAllUser.controller' />", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var headArea = $('#headArea');
				var bodyArea = $('#bodyArea');
				
				headArea.html ( "<tr><th style='text-align: center; width: 100px;'>帳號</th>"
							  + "<th style='text-align: center; width: 100px;'>密碼</th>"
							  + "<th style='text-align: center; width: 120px;'>姓</th>"
							  + "<th style='text-align: center; width: 120px;'>名</th>"
							  + "<th style='text-align: center; width: 120px;'>生日</th>"
							  + "<th style='text-align: center; width: 100px;'>電子郵件</th>"
							  + "<th style='text-align: center; width: 100px;'>電話</th>"
							  + "<th style='text-align: center; width: 50px;'>性別</th>"
							  + "<th style='text-align: center; width: 150px;'>地址</th>"
							  + "<th style='text-align: center; width: 100px;'>圖片</th></tr>");
				var users = JSON.parse(xhr.responseText);
				
				for (var i = 0; i < users.length; i++) {
				var contentBody = ""; 
					var gotoDeleteUser = "<c:url value='/gotoDeleteUser.controller/' />";
					contentBody += "<tr><td style='vertical-align: middle;'>" + users[i].u_id + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_psw + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_lastname + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_firstname + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_birthday + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_email + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_tel + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_gender + "</td>"
								 + "<td style='vertical-align: middle;'>" + users[i].u_address + "</td>"
							// 							"<td><img width='50' height='50' style='border-radius: 50%;' src='" + users[i].pictureString + "'></td>" +
								 + "<td style='vertical-align: middle;'><img width='100%' style='border-radius: 10%;' src='" + users[i].pictureString + "'></td>" + "</tr>";
					segments.push(contentBody);
				}
				
				let htmlStuff = "";
				for(let i = 0; i < 5; i++){
					htmlStuff += segments[i];
				}
				bodyArea.html(htmlStuff);
			}
			
			pageNum = Math.ceil((segments.length) / rowPerPage);
			let temp0 = "";
			let tempPageNum = (pageNum > rowPerPage)? rowPerPage : pageNum;
			for(let i=0 ; i<tempPageNum ; i++){
				temp0 += "<button class='pageBtn primary' data-index='" + i + "' type='button' id='btnPage'>" + (i + 1) + "</button>&nbsp;&nbsp;&nbsp;";
			}
			pageHref.html(temp0);
			
			$('.pageBtn').on('click', function(){
				let pageIndex = $(this).attr('data-index');
				switchPage(pageIndex);
			});
			
			function switchPage(pageIndex){
				let htmlStuff = "";
				counter = pageIndex * rowPerPage;
				let tempCounter0 = (counter + rowPerPage > segments.length)? segments.length : counter + rowPerPage;
				for(let i = counter; i < tempCounter0; i++){
					htmlStuff += segments[i];
				}
				bodyArea.html(htmlStuff);
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
<!-- <body> -->
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<div align='center'>
					<%@include file="../universal/adminHeader.jsp"%>
					<br>
<!-- 					<h3>會員資料</h3> -->
					<!-- 	<hr> -->
					<table border='1'>
<!-- 					<div align='center' id='showUser'></div> -->
					<thead id="headArea"></thead>
					<tbody id="bodyArea"></tbody>
					</table>
				</div>

				<p />
				<div align='center'>
				<div id="pageHref"></div>
				<br>
<!-- 					<hr> -->
					<a href="<c:url value='/gotoAdminIndex.controller' />">上一頁</a>
				</div>

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