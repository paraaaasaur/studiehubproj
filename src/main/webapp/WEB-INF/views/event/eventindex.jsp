<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">

<title>Studie Hub</title>
<style>
.ellipsis {
overflow:hidden;
white-space: nowrap;
text-overflow: ellipsis;
}
</style>

<script>
let div1 = null;
var u_id = "${loginBean.u_id}";
var userPicString = "${loginBean.pictureString}";
window.addEventListener("load", function() {
	//window.addEvenListener 網頁監聽器 
	//當瀏覽器從第一行到最後一行載完畢後才執行 function() 

	
	
	var logout = document.getElementById("logout");
		logout.onclick = function() {
			var xhr1 = new XMLHttpRequest();
			xhr.open("GET", "<c:url value='/logout.controller' />", true);
			xhr.send();
			xhr.onreadystatechange = function() {
				if (xhr1.readyState == 4 && xhr1.status == 200) {
					var result = JSON.parse(xhr1.responseText);
					if (result.success) {
						alert(result.success);
						top.location = '<c:url value='/' />';
					} else if (result.fail) {
						alert(result.fail);
						top.location = '<c:url value='/' />';
					}
				}
			}
		}

		var loginHref = document.getElementById('loginHref');
	    var signupHref = document.getElementById('signupHref');
	    var logoutHref = document.getElementById('logoutHref');
	    var userId = document.getElementById('userId');
	    var userPic = document.getElementById('userPic');
	    if(u_id){
	    	loginHref.hidden = true;
	    	signupHref.hidden = true;
	    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
	    	userPic.src = userPicString;	//有登入就秀大頭貼
	    	userId.innerHTML = u_id;
	    	loginEvent.style.display = "block";
	    	loginALLEvent.style.display = "block";

	    }
	
	
	
	
	
	
	
	
	
	dataArea = document.getElementById("div1");
// 	restname = document.getElementById("restname");
// 	query = document.getElementById("query");
	//抓到 Id 叫 dataArea 能對這個地方做修改 或 對他做監聽事件
	let xhr = new XMLHttpRequest();
	xhr.open("GET", "<c:url value='/EventfindAll' />", true);
	//他會送出請求去/findAll 然後 controller 去接收 /findAll 執行方法
	//說明請求的內容 fales 就是同步 true 就是非同步 
	xhr.send();
	//真正送出請求
	xhr.onreadystatechange = function() {
		//當屬性發生變化的時候執行方法	
		if (xhr.readyState == 4 && xhr.status === 200) {
			            console.log(xhr.responseText);

			dataArea.innerHTML = showData(xhr.responseText);
			//執行方法 將 jsoe字串  轉為 jsoe物件 
		}
	};

});


function showData(textobj) {
	let obj = JSON.parse(textobj)
	let size = obj.size;
	let events = obj.list

    let segment="" ;
   

	
	

		for (n = 0; n < events.length; n++) {
			let event = events[n];
					
           if(event.verification=="Y"){
			
                let tmp0 = "<c:url value='/' />"  + event.a_picturepath;
                let tmpx = "<c:url value='/Selecteventcontent/' />" + event.a_aid;
//              let tmpx = "<c:url value='/Selecteventcontent' />";
                let tmpxx = "<a href= '"+tmpx+"'>"+event.a_name+"</a>";
                let tmp1 = event.a_name ;
                let tmp2 = event.a_startTime;
                let tmp3 = event.a_endTime;
                let tmp4 = event.a_address;
                let tmp5 = "https://www.google.com/maps?q="+event.a_address ;
				console.log(tmp5);
// 				class='image'
			    segment += "<article  class='container'>";
			    segment += "<a href='"+tmpx+"' class='image'><img src='"+tmp0+"' alt=''  height='300' /></a>"
			    
			    
			    //判斷活動過期了沒有 有在</h3>裡加上已過期
                if(event.expired=="未過期"){
			    	
        			segment += "<h3 class='ellipsis'>"+tmpxx+"</h3>"

			    }else{
			    	segment += "<h3 class='ellipsis'>"+tmpxx+"("+event.expired+")"+ "</h3>"
					
			    }
				
				segment += "<p  class='ellipsis'>報名時間:"+event.a_registration_starttime+"<span>至"+event.a_registration_endrttime+"</span>"+"</p>"
				segment += "<p  class='ellipsis'>活動時間:"+tmp2+"<span>至"+tmp3+"</span>"+"</p>"
				segment += "<p  class='ellipsis'>活動地點:"+tmp4+"</p>"
				segment += "<ul class='actions'>"
				segment += "<li><a href="+tmpx+" class='button'>詳細資訊</a></li>"
				segment += "<li><a href="+tmp5+" class='button'>詳細地址</a></li>"
				segment += "</ul>"
				segment += "</article>"
           }else{
        	   segment +=""
           }

// 					<article>
// 				    <a href="#" class="image"><img src="images/pic05.jpg" alt="" /></a>
// 				    <h3>Feugiat lorem aenean</h3>
// 				    <p>Aenean ornare velit lacus, ac varius enim lorem ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed nulla amet lorem feugiat tempus aliquam.</p>
// 				    <ul class="actions">
// 					<li><a href="#" class="button">More</a></li>
// 				    </ul>
// 			        </article>
		
	}
	

	return segment;
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
								<%@include file="../universal/header.jsp" %>  

							<!-- Banner -->
<!-- 								以刪 -->

							<!-- Section -->
<!-- 								以刪 -->

							<!-- Section -->
								<section>
									<header class="major">
										<h2>活動頁面</h2>
									</header>
									<div class="posts" id="div1">

									
										
										
									</div>
								</section>

						</div>
					</div>

				<!-- Sidebar -->
				<!-- 這邊把side bar include進來 -->
				<%@include file="../universal/sidebar.jsp" %>  

			</div>

		<!-- Scripts -->
			<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

	</body>
</html>