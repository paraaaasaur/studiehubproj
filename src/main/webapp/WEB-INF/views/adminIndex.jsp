<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub(管理員)</title>

<script>

if("${success}"=="管理員登入成功"){alert("${"管理員登入成功!"}")}

var adminId = "${adminId}";
window.onload = function(){
// console.log(adminId);
    
    //如果有登入，隱藏登入標籤
    var loginHref = document.getElementById('loginHref');
    var logoutHref = document.getElementById('logoutHref');
    var userId = document.getElementById('userId');
    var userPic = document.getElementById('userPic');
    if(adminId){
    	loginHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    }
    
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
								<%@include file="universal/adminHeader.jsp" %>
								<section id="banner">
									<div class="content">
										<header>
											<h1>Studie Hub</h1>
											<p>影片學習，生活化主題影片，樂趣學習英文！</p>
										</header>
										<p>你知道語言不應該用學的，而是要用練的嗎？南加大語言學教授 Stephen Krashen 主張，學習外語不是累積學科知識，而更像是技能訓練。因此<strong> Studie Hub </strong>採用大量真實的情境的影片，搭配互動練習，幫助您快速活用英文。</p>
										<ul class="actions">
											<li><a href="#" class="button big">Learn More</a></li>
										</ul>
									</div>
									<span class="image object">
										<img src="images/adminIndexImage.jpg" alt="" />
									</span>
									<hr>
								</section>
								<section></section>
						</div>
					</div>
				<!-- Sidebar -->
				<!-- 這邊把side bar include進來 -->
				<%@include file="universal/adminSidebar.jsp" %>
			</div>

		<!-- Scripts -->
			<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

	</body>
</html>