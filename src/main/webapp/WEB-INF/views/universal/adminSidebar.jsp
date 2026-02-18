<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="assets/css/main.css">
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
					<li><a href=".">使用者首頁</a></li>
					<li id='loginHref'><a href="gotoAdminLogin.controller">管理員登入</a></li>
					<li><a href="gotoShowAllUser.controller">會員資訊</a></li>
					<li><span class="opener">課程資訊</span>
						<ul>
							<li><a href="queryProduct">所有課程</a></li>
							<li><a href="findAllProductPending">待審核課程</a></li>
						</ul>
					</li>


					<li><span class="opener">交易管理</span>
						<ul>
							<li><a href="order.controller/adminSelect">訂單</a></li>
							<li><a href="cart.controller/adminSelect">購物車</a></li>
						</ul>
					</li>
					<li><a href="goSelectAllChatAdmin">討論區</a></li>
					<li><span class="opener">題庫</span>
						<ul>
							<li><a href="question.controller/queryQuestion">查詢、編輯試題資料(後端)</a></li>
							<li><a href="question.controller/intoVerifyQuestion">試題審核區(後端)</a></li>
						</ul>
					</li>

					<li><span class="opener">活動</span>
					    <ul>
					        <li><a href="managerAllEvent">活動審核(管理者)</a></li>
                  <li><a href="adminAllEvent">管理者後台(管理者)</a></li>
					        		        
					    </ul>
					</li>
					<li><a href="credits">⭐️SPECIAL THANKS⭐️</a></li>
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