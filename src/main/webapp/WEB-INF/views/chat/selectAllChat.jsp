<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>討論區</title>
<script>
	window.onload = function() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "<c:url value='/selectAllChat' />", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var content = "<table border='1'>";
				content += "<tr><th style='text-align: center; width: 60px;'>帳號</th>"
						+ "<th style='text-align: center; width: 60px;'>類別</th>"
						+ "<th style='text-align: center; width: 360px;'>標題</th>"
						+ "<th style='text-align: center; width: 120px;'>日期</th></tr>";
				var users = JSON.parse(xhr.responseText);
				for (var i = 0; i < users.length; i++) {
					var goDeleteChat = "<c:url value='/goDeleteChat/' />";
					content += "<tr><td style='vertical-align: middle;'>"
							+ users[i].c_ID
							+ "</td>"
							+ "<td style='vertical-align: middle;'>"
							+ users[i].c_Class
							+ "</td>"
							+ "<td style='vertical-align: middle;'>"
							+ users[i].c_Title
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
		<%@include file="../universal/sidebar.jsp"%>
	</div>
	<script	src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>