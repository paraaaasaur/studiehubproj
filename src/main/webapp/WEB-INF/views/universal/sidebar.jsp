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
			<section id="search" class="alt">
				<form method="post" action="#">
					<input type="text" name="query" id="query" placeholder="Search" />
				</form>
			</section>

			<!-- Menu -->
			<nav id="menu">
				<header class="major">
					<h2>Menu</h2>
				</header>
				<ul>
					<li><a href="<c:url value='/' />">首頁</a></li>
					<li id='loginHref'><a href="<c:url value='/gotologin.controller' />">登入</a></li>
					<li id='signupHref'><a href="<c:url value='/gotosignup.controller' />">註冊</a></li>
					<li><span class="opener">會員資訊</span>
						<ul>
							<li><a href="<c:url value='/gotoChangePassword.controller' />">更改密碼</a></li>
							<li><a href="<c:url value='/gotoUpdateUserinfo.controller' />">編輯個人資料</a></li>
						</ul></li>
					<li><a href="#">課程</a></li>
					<li><a href="<c:url value='/' />">購物車</a></li>
					<li><a href="#">討論區</a></li>
					<li><a href="#">題庫</a></li>
					<li><a href="<c:url value='/Eventindex' />">活動</a></li>
				</ul>
			</nav>

			<!-- Section -->
			<section>
				<header class="major">
					<h2>精選課程</h2>
				</header>
				<div class="mini-posts">
					<article>
						<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/demopic1.jpg"
							alt="" /></a>
						<p>
							除了go Dutch，還有什麼俚語包括國家名稱？<br /> 一起學習 8
							種國家俚語，學習用「荷蘭」形容藉酒壯膽、用「法國」形容不告而別！
						</p>
					</article>
					<article>
						<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/demopic2.jpg"
							alt="" /></a>
						<p>
							正式英文 email 究竟該怎麼寫？ 該怎麼稱呼對方？ 又該如何有禮貌地結束信件？<br /> 幫你搞定商業
							email，讓你職場應對超得體！
						</p>
					</article>
					<article>
						<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/demopic3.png"
							alt="" /></a>
						<p>別再說 “I am boring”！這十種最常見的英文錯誤你中了幾個？</p>
					</article>
				</div>
				<ul class="actions">
					<li><a href="#" class="button">More(這個連結去商品選單)</a></li>
				</ul>
			</section>

			<!-- Section -->
			<section>
				<header class="major">
					<h2>聯絡我們</h2>
				</header>
				<p>你知道語言不應該用學的，而是要用練的嗎？南加大語言學教授 Stephen Krashen
					主張，學習外語不是累積學科知識，而更像是技能訓練。因此 Studie Hub
					採用大量真實的情境的影片，搭配互動練習，幫助您快速活用英文。</p>
				<ul class="contact">
					<li class="icon solid fa-envelope"><a
						href="mailto:i3t5128@gmail.com">i3t5128@gmail.com</a></li>
					<li class="icon solid fa-phone">(886) 987-12345</li>
					<li class="icon solid fa-home">中壢教室：桃園市中壢區中大路300號<br />
						國立中央大學(工程二館側面 / 資策會大樓)
					</li>
				</ul>
			</section>

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