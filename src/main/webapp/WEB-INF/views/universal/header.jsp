<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">
<title>header</title>

<body>

	<header id="header">
		<ul class="icons">
			<!-- <li><a href="#" class="icon brands fa-twitter"><span class="label">Twitter</span></a></li>
										<li><a href="#" class="icon brands fa-instagram"><span class="label">Instagram</span></a></li> -->
			<li style='vertical-align: middle;'><span class="label"><img id='userPic' width='45px' style='border-radius: 10%;' src=''></span></li>
			<li id='logoutHref' style="visibility:hidden; vertical-align: middle;"><a href="<c:url value='/logout.controller' />" id='logout'><span class="label">登出</span></a></li>
		</ul>
	</header>
	
</body>

</html>