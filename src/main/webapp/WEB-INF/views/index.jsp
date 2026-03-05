<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>


<script type="application/json" id="bootstrap-data">
	{
		"successMessageOfChangingPassword": "${fn:escapeXml(successMessageOfChangingPassword)}",
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}"
	}
</script>

<script>
const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
const { successMessageOfChangingPassword, u_id, userPicString } = bootstrapData;

if (successMessageOfChangingPassword) {
	alert(successMessageOfChangingPassword);
}

window.onload = function(){
    var logout = document.getElementById("logout");
    logout.onclick = function(){
        var xhr = new XMLHttpRequest();
        xhr.open("GET", 'logout.controller', true);
        xhr.send();
        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4 && xhr.status == 200){
                var result = JSON.parse(xhr.responseText);
                if(result.success){
                    alert(result.success);
                    top.location = '';
                }else if(result.fail){
                    alert(result.fail);
                    top.location = '';
                }
            }
        }
    }
    
    
//universal
    //如果有登入，隱藏登入標籤
    var loginHref = document.getElementById('loginHref');
    var signupHref = document.getElementById('signupHref');
    var logoutHref = document.getElementById('logoutHref');
    var userId = document.getElementById('userId');
    var userPic = document.getElementById('userPic');
	var loginEvent = document.getElementById('loginEvent');
	var loginEvent1 = document.getElementById('loginEvent1');
    var loginALLEvent1 = document.getElementById('loginALLEvent1');
    if(u_id){
    	loginHref.hidden = true;
    	signupHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
    	userId.textContent = u_id;
    	loginEvent.style.display = "block";
    	loginEvent1.style.display = "block";
    	loginALLEvent1.style.display = "block";
    }
	// 有登入才會顯示購物車sidebar
	let cartHref = document.querySelector('#cartHref');
	cartHref.hidden = (u_id)? false : true;
	cartHref.style.visibility = (u_id)? 'visible' : 'hidden';
//universal
}
</script>
</head>

<body class="is-preload">
	<!-- Wrapper -->
	<div id="wrapper">
		<!-- Main -->
		<div id="main">
			<div class="inner">
				<!-- header include -->
				<%@include file="universal/header.jsp" %>

				<!-- Banner -->
				<section id="banner">
					<div class="content">
						<header>
							<h1>Studie Hub</h1>
							<p>影片學習，生活化主題影片，樂趣學習英文！</p>
						</header>
						<p>你知道語言不應該用學的，而是要用練的嗎？南加大語言學教授 Stephen Krashen 主張，學習外語不是累積學科知識，而更像是技能訓練。因此<strong> Studie Hub </strong>採用大量真實的情境的影片，搭配互動練習，幫助您快速活用英文。</p>
						<ul class="actions">
							<li><a href="#popularCourse" class="button big primary">Learn More</a></li>
						</ul>
					</div>
					<span class="image object">
						<img src="images/demopic4.jpg" alt="" />
					</span>
				</section>


				<section>
					<header class="major" id="popularCourse">
						<h2>熱門課程</h2>
					</header>
					<div class="posts">
						<article>
							<a href="#" class="image"><img src="images/productImages/sample-image-03.png" alt="" /></a>
							<h3>🐒 猴子互動口說課（Monkey Interactive English）</h3>
							<p>高互動設計，保持學習動能：讓好奇心成為學習的引擎。</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/productImages/sample-image-04.png" alt="" /></a>
							<h3>🦊 狐狸導師思考日文課（Fox Mentor Logic Japanese）</h3>
							<p>重視思考脈絡的教學方式：不是快，而是看得深。</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>

						<article>
							<a href="#" class="image"><img src="images/productImages/sample-image-05.png" alt="" /></a>
							<h3>🐺 狼群溝通實戰課（Wolf Team Communication）</h3>
							<p>團隊導向的互動設計、建立信任，提升群體表達效率，清楚的角色與程度分級。</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/productImages/sample-image-06.png" alt="" /></a>
							<h3>🐻 棕熊穩健基礎英文課（Brown Bear Foundation English）</h3>
							<p>慢而扎實的學習設計，建立長期自信，而非短期刺激。同程度學員安心學習，不追求花樣，重視理解。</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/productImages/sample-image-07.png" alt="" /></a>
							<h3>🦌 鹿系自然表達英文課（Deer Natural Expression）</h3>
							<p>學習過程能感到進步，建立信心，就不怕開口犯錯：台灣人口說不好的一大原因就是害怕開口跟犯錯，所以課程首要條件就是先建立信心與興趣</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
						<article>
							<a href="#" class="image"><img src="images/productImages/sample-image-01.png" alt="" /></a>
							<h3>🐱 貓咪英文口說課（Cat Speaking Program）</h3>
							<p>專為內向型學員設計的安心學習環境：理解「慢熟型人格」的學習節奏，從觀察、模仿到自然開口，不強迫、不比較。</p>
							<ul class="actions">
								<li><a href="#" class="button">More</a></li>
							</ul>
						</article>
					</div>
				</section>

			</div>
		</div>

		<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="universal/sidebar.jsp" %>

	</div>

	<!-- Scripts -->
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>