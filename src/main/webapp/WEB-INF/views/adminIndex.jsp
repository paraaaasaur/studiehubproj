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
<title>Studie Hub(管理員)</title>
<script type="application/json" id="bootstrap-data">
	{
		"success": "${fn:escapeXml(success)}",
		"adminId": "${fn:escapeXml(adminId)}"
	}
</script>
<script>
const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
const { success, adminId } = bootstrapData;
if (success) {
	alert(success);
}

window.onload = function(){
    var logoutHref = document.getElementById('logoutHref');
    if(adminId) {
    	logoutHref.style.visibility = "visible";
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
				<!-- adminHeader include -->
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


			</div>
		</div>
		<!-- adminSidebar include -->
		<%@include file="universal/adminSidebar.jsp" %>
	</div>

	<!-- Scripts -->
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>