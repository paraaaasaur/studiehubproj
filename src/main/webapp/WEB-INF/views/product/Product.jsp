<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>
<style type="text/css">

.product{
    border: 1px rgb(153, 149, 149) solid;
    padding: 30px;
    margin: 50px;
    border-radius: 50px;
    text-align: center;
    display: inline-block;
    width:300px;
    height:300px;
}
.image{
	text-align: center;
}

</style>

<script>
var u_id = "${loginBean.u_id}";

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
    
    //如果有登入，隱藏登入標籤
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
    } 
    
}

function showData(textObj) {
    let obj = JSON.parse(JSON.stringify(textObj));
    let size = obj.size;
    let products = obj.list;
	console.log(obj);
	console.log(size);
	console.log(products);
    let segment = "";
        if (size == 0) {
			segment += "<tr><th colspan='8'>查無資料</th></tr>";
		} else {
			
			for(n=0;n<products.length;n++){
				let product = products[n];
				segment += "<div class='product'>";
				segment += "<a href='"+"<c:url value = '/takeClass/"+ product.p_ID +"'/>" +"'class='image'style='height:270px'>";
				segment += "<img src='"+ product.pictureString +"' width='230px' height='120px'>";
				segment += "<br>";
				segment += "<h3>"+ product.p_Name +"</h3>"
			    segment += "</a>";
			    segment += "</div>";
			}
			
			
			
        }
       
        return segment;
}
console.log(${product.pictureString});
console.log(${product.videoString});
</script>
</head>

<body class="is-preload">
	<!-- Wrapper -->
	<div id="wrapper">
		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>
				<h2 align='center'>課程資訊</h2>
				<hr>
				<div style="text-align: center;">
					<input type="text" id="productname" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入課程關鍵字">
					<button id="query" style="display: inline;">搜尋</button>
				<br>
				<br>
				
				</div>
				<div id='dataArea'>
				
				
				<h2>${product.p_Name}</h2>
				<video controls style="width: 1500px; height: 700px;">
					<source type="${product.video_mimeType}" src="${product.videoString}">

				</video>
				<hr>
				<h2>關於課程</h2>
				<div>${product.p_DESC}</div>
				</div>
			</div>
		</div>
		<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="../universal/sidebar.jsp"%>

	</div>

	<!-- Scripts -->
	<script
		src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>