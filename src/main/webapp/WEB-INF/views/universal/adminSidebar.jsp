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
<title>sidebar</title>

<body>

	<div id="sidebar">
		<div class="inner">

			<!-- Search -->
<!-- 			<section id="search" class="alt"> -->
<!-- 				<form method="post" action="#"> -->
<!-- 					<input type="text" name="query" id="query" placeholder="Search" /> -->
<!-- 				</form> -->
<!-- 			</section> -->

			<!-- Menu -->
			<br>
			<br>
			<br>
			<nav id="menu">
				<header class="major">
					<h2>Menu</h2>
				</header>
				<ul>
					<li><a href="<c:url value='/' />">使用者首頁</a></li>
					<li id='loginHref'><a href="<c:url value='/gotoAdminLogin.controller' />">管理員登入</a></li>
					<li><a href="<c:url value='/gotoShowAllUser.controller' />">會員資訊</a></li>
					<li><span class="opener">課程資訊</span>
						<ul>
							<li><a href="<c:url value='queryProductForUser' />">所有課程</a></li>
							<li><a href="<c:url value='queryProduct' />">所有課程(後端)</a></li>
						</ul>
					</li>
					<li><a href="<c:url value='/' />">購物車</a></li>
					<li><a href="#">討論區</a></li>
					<li><a href="#">題庫</a></li>
					<li><a href="<c:url value='/Eventindex' />">活動</a></li>
				</ul>
			</nav>


			<!-- Footer -->
			<footer id="footer">
				<p class="copyright">
					&copy; Untitled. All rights reserved. Demo Images: <a
						href="https://unsplash.com">Unsplash</a>. Design: <a
						href="https://html5up.net">HTML5 UP</a>.
				</p>
			</footer>

		</div>
	</div>
	<!-- </div> -->
</body>

</html>