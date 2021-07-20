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
					<li><span class="opener">課程資訊</span>
						<ul>
							<li><a href="/studiehub/queryProductForUser">所有課程</a></li>
							<li style='display:none' id='loginEvent'><a href="/studiehub/insertProduct">新增課程</a></li>
						</ul>
					</li>
					<li id="cartHref" hidden><a href="<c:url value='/cart.controller/cartIndex' />">購物車</a></li>
					<li><a href="<c:url value='/goSelectAllChat' />">討論區</a></li>
					
					<li><span class="opener">活動</span>
					    <ul>
					        <li><a href="<c:url value='/eventindex' />">所有活動</a></li>
					        <li style='display:none' id='loginEvent'><a href="<c:url value='/insertEvent' />">新增活動</a></li>
					        <li style='display:none' id='loginALLEvent'><a href="<c:url value='/userAllEvent' />">使用者後臺</a></li>
<%-- 					        <li><a href="<c:url value='/Eventindex' />">所有活動(測試)</a></li>			         --%>
					        			        
					    </ul>
					</li>

					<li><span class="opener">題庫</span>
						<ul>
							<li><a href="<c:url value='/question.controller/guestQueryQuestion' />">所有試題</a></li>
							<li><a href="<c:url value='/question.controller/turnQuestionIndex' />">線上測驗區</a></li>
							<li><a href="<c:url value='/question.controller/insertQuestion' />">申請新增試題</a></li>
						</ul>
					</li>
					
					<li><a href="<c:url value='/goSelectAllChat' />">討論區</a></li>

					<li><a href="<c:url value='/gotoAdminIndex.controller' />"><b style="color:#f56a6a;">管理者頁面</b></a></li>
<%-- 					<li><a href="<c:url value='/gotoAdminLogin.controller' />"><b style="color:#f56a6a;">管理者頁面</b></a></li> --%>
				</ul>
			</nav>
			<!-- Section -->
			<section>
				<header class="major">
					<h2>精選課程</h2>
				</header>
				<div class="mini-posts">
					<article>
						<a href="<c:url value = '/takeClass/2'/>" class="image"><img src="${pageContext.request.contextPath}/images/productImages/【基礎英文文法第一課】7分鐘學會5大句型，從這裡開始_2.png"
							alt="" /></a>
						<p>
							除了go Dutch，還有什麼俚語包括國家名稱？<br /> 一起學習 8
							種國家俚語，學習用「荷蘭」形容藉酒壯膽、用「法國」形容不告而別！
						</p>
					</article>
					<article>
						<a href="<c:url value = '/takeClass/3'/>" class="image"><img src="${pageContext.request.contextPath}/images/productImages/【從零開始學日文】日語50音的發音和寫法簡單教學_3.png"
							alt="" /></a>
						<p>
							正式英文 email 究竟該怎麼寫？ 該怎麼稱呼對方？ 又該如何有禮貌地結束信件？<br /> 幫你搞定商業
							email，讓你職場應對超得體！
						</p>
					</article>
					<article>
						<a href="<c:url value = '/takeClass/4'/>" class="image"><img src="${pageContext.request.contextPath}/images/productImages/如何有效練習英文口說 _ Avis經驗談 _ 突破心魔才能學好英文口語_4.png"
							alt="" /></a>
						<p>別再說 “I am boring”！這十種最常見的英文錯誤你中了幾個？</p>
					</article>
				</div>
				<ul class="actions">
					<li><a href="#" class="button">More</a></li>
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