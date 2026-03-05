<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<style>
	tr {
		text-align: center;
	}

	#createBtn {
	  padding: 30px 20px;
	  position:fixed;
	  top:85%;
	  left:90%;
	  z-index:1;
	  font-size: 100%;
	}

	#iconPos {
	  position:relative;
	  bottom: 20px;
	  font-size:30px;
	}

	.top-post-box {} /* <todo@1.1.0>: reserved for each top-post container (looping <tr>) */
	.top-post-box__item {
		text-align: center;
		vertical-align: middle;
	}
	.top-post-box__title {
		text-align: left;
		vertical-align: auto;
	}
</style>
<title>討論區</title>
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { u_id, userPicString } = bootstrapData;

	window.onload = function() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "selectAllChat", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var content = "<table border='1'>";
				content += "<tr><th style='text-align: center; width: 60px;'>類別</th>"
						+ "<th style='text-align: center; width: 360px;'>標題</th>"
						+ "<th style='text-align: center; width: 60px;'>帳號</th>"
						+ "<th style='text-align: center; width: 120px;'>日期</th></tr>";
				var users = JSON.parse(xhr.responseText);
				for (const user of users) {
					content += "</tr>"
							+ tdText(user.c_Class).outerHTML
							+ titleCell(user.c_ID, user.c_Title).outerHTML
							+ tdText(user.u_ID).outerHTML
							+ tdText(user.c_Date).outerHTML
							+ "</tr>";
				}
				content += "</table>";
				var selectAll = document.getElementById("selectAll");
				selectAll.innerHTML = content;
			}
		}
		function tdText(text) {
			const td = document.createElement("td");
			td.classList.add('top-post-box__item');
			td.textContent = text;

			return td;
		}
		function titleCell(c_ID, c_Title) {
			const td = document.createElement("td");
			td.classList.add('top-post-box__title');

			const a = document.createElement("a");
			a.textContent = c_Title;
			a.href = 'goSelectOneChat/' + c_ID;

			td.appendChild(a);

			return td;
		}

		var logout = document.getElementById("logout");
	    logout.onclick = function(){
	        var xhr = new XMLHttpRequest();
	        xhr.open("GET", "logout.controller", true);
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
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>


				<div align='center'>
					<br>
					<div align='center' id='selectAll'></div>
					<button id="createBtn" type="button" onclick="location.href='goInsertChat'"><i id="iconPos" class="fas fa-pen"></i></button>
				</div>


			</div>
		</div>
		<%@include file="../universal/sidebar.jsp"%>
	</div>

	<script	src="assets/js/jquery.min.js"></script>
	<script	src="assets/js/browser.min.js"></script>
	<script	src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>