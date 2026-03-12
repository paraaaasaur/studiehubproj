<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="assets/css/main.css">

<title>登入</title>
<script type="application/json" id="bootstrap-data">
	{
		"fail": "${fn:escapeXml(fail)}"
	}
</script>
<script>
const fail = JSON.parse(document.getElementById('bootstrap-data').textContent).fail;
	if (fail) {
		alert(fail);
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../../fragments/admin/header.jsp"%>

				<div align='center'>
					<div id='resultMsg' style="height: 18px; font-weight: bold;"></div>
					<hr>
				</div>

				<div style="text-align: center;">
					<div style="display: inline-block; text-align: left;">
						<form action="AdminLogin.controller" method="POST">
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
		<%@include file="../../fragments/sidebar.jsp"%>
	</div>

	<script	src="assets/js/jquery.min.js"></script>
	<script	src="assets/js/browser.min.js"></script>
	<script	src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>