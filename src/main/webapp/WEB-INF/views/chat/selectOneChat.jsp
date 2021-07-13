<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<title>討論區</title>
<script>
	var u_id = "${loginBean.u_id}";
	var userPicString = "${loginBean.pictureString}";
	var c_ID = "${c_ID}";
	
	window.onload = function() {
		
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "<c:url value='/selectOneChat/" + c_ID + "' />", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var user_content = "<tablestyle='width: 20%;'>";
				var users = JSON.parse(xhr.responseText);
				for (var i = 0; i < users.length; i++) {
					user_content += "<tr><td>"
							+ users[i].u_ID
							+ "</td></tr>";
				}
				user_content += "</table>";
				
				var content = "<table align='right' style='width: 80%;'>";
				for (var i = 0; i < users.length; i++) {
					content += "<tr><td style='text-align: left;'><div style='min-height: 200px;'>"
							+ users[i].c_Conts
							+ "</div></td></tr>";
				}
				content += "<tr><td><textarea style='min-height: 100px;' placeholder='請輸入文章內容...'></textarea></td></tr>"
						+ "<tr><td><button type='button' class='primary' id='sendData'>送出</button> &nbsp;"
						+ "<input type='reset' value='清除'></td></tr>"
						+ "</table>";
				var selectAll = document.getElementById("selectAll");
				selectAll.innerHTML = user_content + content;
			}
			
		}
		
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
		
		var adminId = "${adminId}";
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
</script>
</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<div align='center'>
					<%@include file="../universal/header.jsp"%>
					<br>
					<h2><span id='selectSingle' style='display: block; text-align: left;'></span></h2>
					<div align='center' id='selectAll'></div>
				</div>
				<p />
			</div>
		</div>
		<%@include file="../universal/sidebar.jsp"%>
	</div>
	<script	src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>