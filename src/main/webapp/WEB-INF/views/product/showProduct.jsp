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
    
    var dataArea = document.getElementById("dataArea");
	var query = document.getElementById("query");
	var productname = document.getElementById("productname");
    let xhr = new XMLHttpRequest();
    xhr.open("GET","<c:url value='/findAllProduct' />",true);
    xhr.send();
    xhr.onreadystatechange = function(){

        if(xhr.readyState == 4 && xhr.status == 200){
            var result = JSON.parse(xhr.responseText);
			dataArea.innerHTML = showData(result);
        }
    }

	query.addEventListener('click',function(){
		let pname = productname.value;
		if(!pname){
			alert('請輸入關鍵字');
			return
		}

		let xhr2 = new XMLHttpRequest();
		xhr2.open('GET',"<c:url value='/queryByProductName' />?pname="+pname);
		xhr2.send();
		xhr2.onreadystatechange = function(){
			if(xhr2.readyState == 4 && xhr2.status == 200){
				var result = JSON.parse(xhr2.responseText)
				dataArea.innerHTML = showData(result);
			}
		}
	})
    
}

function showData(textObj) {
    let obj = JSON.parse(JSON.stringify(textObj));
    let size = obj.size;
    let products = obj.list;
	console.log(obj);
	console.log(size);
	console.log(products);
    let segment = "<table border='1' style = 'width:1300px;text-align: center;'>";
        if (size == 0) {
			segment += "<tr><th colspan='8'>查無資料</th></tr>";
		} else {
            segment += "<tr><th colspan='8'>共計" + size + "筆資料</th></tr>";

			segment += "<tr><th>課程圖片</th><th>課程名稱</th><th>課程類別</th><th>課程價格</th><th>課程介紹</th><th width:50px;>功能</th></tr>";
			for (n = 0; n < products.length; n++) {
				let product = products[n];
    			let tmp0 = "<c:url value = '/updateProduct/'/>"+ product.p_ID;
    			let tmp1 = "<c:url value = '/deleteProduct/'/>"+ product.p_ID;
    			console.log(tmp0);
				segment += "<tr>";
                segment += "<td><img width='100' height='60' src='" + product.pictureString + "' ></td>";
				segment += "<td>" + product.p_Name + "</td>";
				segment += "<td style='width: 100px;'>" + product.p_Class + "</td>";
				segment += "<td style='width: 100px;'>" + product.p_Price + "</td>";
				segment += "<td>" + product.p_DESC + "</td>";
				segment += "<td><input type='button'value='更新'onclick=\"window.location.href='"+tmp0+"'\"'/>";
				segment += "<input type='button'value='刪除'onclick=\"window.location.href='"+tmp1+"'\" /></td>";
				segment += "</tr>";
                }
        }
        segment += "</table>";
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
				<%@include file="../universal/header.jsp"%>
				<h2 align='center'>課程資訊</h2>
				<hr>
				<div style="text-align: center;">
					<input type="text" id="productname" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入課程關鍵字">
					<button id="query" style="display: inline;">搜尋</button>
					<br>
				<br>
				</div>
				
				<div id='dataArea'></div>
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