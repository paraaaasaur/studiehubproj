<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<style>
	tr {
		text-align: center;
	}

	.top-post-box {} /* <todo@1.1.0>: reserved for each top-post container (looping <tr>) */
	.top-post-box__item {
		text-align: center;
		vertical-align: middle;
	}
	.top-post-box__class {
		text-align: center;
	}
	.top-post-box__title {
		text-align: left;
	}
	.top-post-box__delete-user-link {
		width: 36px;
		height: 36px;
	}
</style>
<title>討論區</title>
<script type="application/json" id="bootstrap-data">
	{
		"adminId": "${fn:escapeXml(adminId)}"
	}
</script>
<script src="assets/js/purify.js/"></script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { adminId } = bootstrapData;

	window.onload = function() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "selectAllChatAdmin", true);
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
				for (const user of users) {
					content +=
							"<tr>"
							+ deleteUserIconCell(user.c_ID).outerHTML
							+ tdText(user.c_ID).outerHTML
							+ classCell(user.c_Class).outerHTML
							+ titleCell(user.c_Title).outerHTML
							+ contentMarkupCell(user.c_Conts).outerHTML
							+ tdText(user.u_ID).outerHTML
							+ tdText(user.c_Date).outerHTML
							+ "</tr>";
				}
				content += "</table>";
				var selectAll = document.getElementById("selectAll");
				selectAll.innerHTML = content;
			}
		}

		//如果有登入，隱藏登入標籤
		var logoutHref = document.getElementById('logoutHref');
		if (adminId) {
			logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
		}
	}

	function deleteUserIconCell(c_ID) {
		const td = document.createElement("td");
		td.align = 'center';

		const a = document.createElement("a");
		a.href = 'goDeleteChatAdmin/' + c_ID;
		{
			const img = document.createElement("img");
			img.src = 'images/user/d_user.svg';
			img.classList.add("top-post-box__delete-user-link");

			a.appendChild(img);
		}

		td.appendChild(a);

		return td;
	}
	function tdText(text) {
		const td = document.createElement("td");
		td.classList.add('top-post-box__item');
		td.textContent = text;

		return td;
	}
	function classCell(c_Class) {
		const td = document.createElement("td");
		td.textContent = c_Class;
		td.classList.add(".top-post-box__class");

		return td;
	}
	function titleCell(c_Title) {
		const td = document.createElement("td");
		td.textContent = c_Title;
		td.classList.add(".top-post-box__title");

		return td;
	}
	function contentMarkupCell(c_Conts) {
		const td = document.createElement("td");
		td.classList.add('top-post-box__item');
		td.innerHTML = DOMPurify.sanitize(c_Conts);

		return td;
	}

</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/adminHeader.jsp"%>


				<div align='center'>
					<br>
					<div align='center' id='selectAll'></div>
				</div>


			</div>
		</div>
		<%@include file="../universal/adminSidebar.jsp"%>
	</div>

	<script	src="assets/js/jquery.min.js"></script>
	<script	src="assets/js/browser.min.js"></script>
	<script	src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>