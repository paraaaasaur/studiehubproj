<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>

<script>

if("${successMessageOfChangingPassword}"=="修改成功"){alert('密碼修改成功!');}

var u_id = "${loginBean.u_id}";
var userPicString = "${loginBean.pictureString}";

window.onload = function(){
    var logout = document.getElementById("logout");
    logout.onclick = function(){
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "<c:url value='/logout.controller' />", true);
        xhr.send();
        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4 && xhr.status == 200){
                var result = JSON.parse(xhr.responseText);
                if(result.success){
                    alert(result.success);
                    top.location = '<c:url value='/' />';
                }else if(result.fail){
                    alert(result.fail);                    
                    top.location = '<c:url value='/' />';
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
    	userId.innerHTML = u_id;
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

							<!-- Header -->
							<!-- 這邊把header include進來 -->
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
											<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/productImages/【從零開始】第一期：學英語，從入門到摔門！_1.jpg" alt="" /></a>
											<h3>從0開始學英文！</h3>
											<p>國際自編教材與全球同步更新，學員可自由選擇想上的主題：主題涵蓋職場、社交、旅遊、文化及日常生活等情境，可依據個人興趣及需求，自由選擇</p>
											<ul class="actions">
												<li><a href="#" class="button">More</a></li>
											</ul>
										</article>
										<article>
											<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/productImages/英文閒聊：我克服拖延症的秘密是什麼？ Chen Lily_6.png" alt="" /></a>
											<h3>拯救拖延症</h3>
											<p>真實的英語接觸，面對面學習更有感：情境互動教學，用全身細胞掌握學習的節奏，讓你不只被動學英語，而是主動用英語，「開口說」才是關鍵</p>
											<ul class="actions">
												<li><a href="#" class="button">More</a></li>
											</ul>
										</article>
										
										<article>
											<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/productImages/如何有效練習英文口說 _ Avis經驗談 _ 突破心魔才能學好英文口語_4.png" alt="" /></a>
											<h3>如何提升英文口說？</h3>
											<p>分級制度嚴謹，跟著類似程度的夥伴一起互動交流：沒有嚴謹的程度分級，會讓班上同學發言量不均、很難彼此交流，也會打擊程度低學員的自信</p>
											<ul class="actions">
												<li><a href="#" class="button">More</a></li>
											</ul>																				
										</article>
										<article>
											<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/productImages/如何開始自學日文？日文學習步驟分享！_5.png" alt="" /></a>
											<h3>自學日文從哪裡開始？</h3>
											<p>課程不流於聊天或背誦，運用英文邏輯理解與表達：結構性教法，讓你用英文邏輯思考去理解、學會如何運用句子，每堂課都能帶走很多新的學習</p>
											<ul class="actions">
												<li><a href="#" class="button">More</a></li>
											</ul>
										</article>
										<article>
											<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/productImages/【從零開始學日文】日語50音的發音和寫法簡單教學_3.png" alt="" /></a>
											<h3>50音簡單教學</h3>
											<p>學習過程能感到進步，建立信心，就不怕開口犯錯：台灣人口說不好的一大原因就是害怕開口跟犯錯，所以課程首要條件就是先建立信心與興趣</p>
											<ul class="actions">
												<li><a href="#" class="button">More</a></li>
											</ul>
										</article>
										<article>
											<a href="#" class="image"><img src="${pageContext.request.contextPath}/images/productImages/【基礎英文文法第一課】7分鐘學會5大句型，從這裡開始_2.png" alt="" /></a>
											<h3>英文基礎文法第一課</h3>
											<p>100%國際認證師資，不只English speaker而是English teacher：認證教師才能真正激發潛力&自信，英協教師具備平均8年以上教學經驗及劍橋認證</p>
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
			<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

	</body>
</html>