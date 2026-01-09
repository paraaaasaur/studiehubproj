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
										<h1>致謝</h1>
										<p>我們的網站製作使用了來自多方無償提供的多媒體資源，在此列舉以略表謝意！</p>
										<h2>使用者</h2>
										<h3>大頭貼</h3>
										<ol>
											<li><a href="https://www.flaticon.com/free-icons/dog" title="dog icons">Dog icons created by Freepik - Flaticon</a></li>
											<li><a href="https://www.flaticon.com/free-icons/squirrel" title="squirrel icons">Squirrel icons created by Freepik - Flaticon</a></li>
											<li><a href="https://www.flaticon.com/free-icons/dolphin" title="dolphin icons">Dolphin icons created by Freepik - Flaticon</a></li>
											<li><a href="https://www.flaticon.com/free-icons/crab" title="crab icons">Crab icons created by Flat Icons - Flaticon</a></li>
											<li><a href="https://www.flaticon.com/free-icons/cool" title="cool icons">Cool icons created by BZZRINCANTATION - Flaticon</a></li>
											<li><a href="https://www.flaticon.com/free-icons/ghost" title="ghost icons">Ghost icons created by Freepik - Flaticon</a></li>
											<li><a href="https://www.flaticon.com/free-icons/clover" title="clover icons">Clover icons created by Freepik - Flaticon</a></li>
											<li><a href="https://www.flaticon.com/free-icons/dinosaur" title="dinosaur icons">Dinosaur icons created by Freepik - Flaticon</a></li>
										</ol>
										<p>The default user avatars are provided by amazing creators on the Flaticon website.</p>
										<p>Thank you very much!</p>
										<hr>
										<h2>課程</h2>
										<h3>影片/圖片</h3>
										<p><a href="https://www.youtube.com/watch?v=KaHypeOcui0">Animals Stock Footage | No Copyright Wildlife Shots | Royalty free animals | free stock videos</a></p>
										<p>The video/image materials in course/index sections have been kindly provided by HikingFex.com.</p>
										<p>Thank you so much!</p>
										<hr>
										<h2>試題</h2>
										<h3>圖片</h3>
										<p><a href="https://www.irasutoya.com/p/terms.html">いらすとや</a></p>
										<ul>
											<li><a href="https://www.irasutoya.com/2018/10/blog-post_886.html" title="cold-classroom-uniform">暖房のない寒い教室のイラスト（制服）</a></li>
											<li><a href="https://www.irasutoya.com/2013/11/blog-post_4923.html" title="train-give-out-seat">お婆さんに席を譲る人のイラスト</a></li>
											<li><a href="https://www.irasutoya.com/2018/03/happy-birthday.html" title="birthday">「Happy Birthday」の文字と誕生日を祝う人たちのイラスト</a></li>
											<li><a href="https://www.irasutoya.com/2015/12/blog-post_351.html" title="consultation-with-teacher">男性の先生に相談をしている男子生徒のイラスト
											</a></li>
										</ul>
										<p>素敵なイラストをありがとうございます！個人学習のために活用させていただきます🙏</p>
										<br>
										<h3>音檔</h3>
										<p>AI-generated by <a href="https://www.minimax.io/audio/text-to-speech">MiniMax</a></p>
										<p>Amazing service, much appreciated!</p>
									</div>
								</section>

							
								<section>
									<header class="major" id="popularCourse">
										<h2>熱門課程</h2>
									</header>
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