<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">

<title>登入</title>
<script>
	if ("${fail}" == "帳號或密碼錯誤") {
		alert('帳號或密碼錯誤, 請再試一次!');
	}
</script>

</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/adminHeader.jsp"%>

				<div align='center'>
					<div id='resultMsg' style="height: 18px; font-weight: bold;"></div>
					<hr>
				</div>

				<div style="text-align: center;">
					<div style="display: inline-block; text-align: left;">
						<form action="<c:url value='/AdminLogin.controller' />"
							method="POST">
							帳號: <input type="text" name="id">
							<br> 密碼: <input type="password" name="psw" id='u_psw'>
							<br>
							<div align='center'>
								<input type="submit" id="login" class='primary' value="登入" />
								<hr>
							</div>
						</form>
					</div>
				</div>


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