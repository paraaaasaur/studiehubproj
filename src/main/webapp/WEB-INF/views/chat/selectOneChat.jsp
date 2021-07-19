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
<style>
#iconPos{
  width: 20px;
  position: relative;
  bottom: 8px;
  font-size:20px;
  color: #ADADAD;
}
</style>
<title>討論區</title>
<script>
	var u_id = "${loginBean.u_id}";
	var userPicString = "${loginBean.pictureString}";
	var c_ID = "${c_ID}";
	var hasError = false;
	
	window.onload = function() {
		
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "<c:url value='/selectOneChat/" + c_ID + "' />", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var users = JSON.parse(xhr.responseText);
				var content = "<table align='right'>";
				for (var i = 0; i < users.length; i++) {
					var goUpdateChat = "<c:url value='/goUpdateChat/' />";
					content += "<tr><td style='text-align: center;' width=20%><div>"
							+ "<br>"
							+ "<img width='80%' style='border-radius: 10%;' src='"
							+ users[i][1].pictureString
							+ "'>"
							+ "<br>"
							+ users[i][0].u_ID
							+ "</div></td>"
							+ "<td style='text-align: left;' width=80%><div style='min-height: 180px;'><p align='right'>"
							+ users[i][0].c_Date
							+ "<hr style='margin: -20px'></p>"
							+ users[i][0].c_Conts
							+ "</div><span>";
							if(users[i][0].u_ID==u_id){
								content += "<a href='"
								+  goUpdateChat + users[i][0].c_ID
								+ "'><i id='iconPos' class='fas fa-ellipsis-v'></i></a>";
							}
					content += "</span></td></tr>";
					console.log(users[i]);
				}
				content += "</table>";
				var selectAll = document.getElementById("selectAll");
				selectAll.innerHTML = content;
			}
			
		}
		
		var sendData = document.getElementById("sendData");
		sendData.onclick = function(){
			//抓欄位資料
			var today = new Date();
			var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
			if(today.getHours()<12){
			var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds() + "AM";
			}else{
				var time = today.getHours()-12 + ":" + today.getMinutes() + ":" + today.getSeconds() + "PM";
			}
			var dateTime = date+' '+time;
			var c_IDr = "${c_ID}";
			var c_Date = dateTime;
			var c_Conts = document.getElementById("c_Conts").value;
			var u_ID = "${loginBean.u_id}";
			var span1 = document.getElementById('result1c');
			
			//if(!c_Conts){
			//	setErrorFor(span2, "請輸入內容");
			//} else{
			//	span2.innerHTML = "";
			//}
			
			if (hasError){
				return false;
			}
			if(u_id != ""){
				var xhr1 = new XMLHttpRequest();
				xhr1.open("POST", "<c:url value='/insertChatReply' />");
				var jsonInsertData = {
					"c_IDr" : c_IDr,
					"c_Date" : c_Date,
					"c_Conts" : c_Conts,
					"u_ID" : u_ID
				}
				xhr1.setRequestHeader("Content-Type", "application/json");
				xhr1.send(JSON.stringify(jsonInsertData));
				xhr1.onreadystatechange = function() {
					if (xhr1.readyState == 4 && xhr1.status == 200){
						result = JSON.parse(xhr1.responseText);
						//判斷回傳
						if(result.fail){
							span1.innerHTML = "<font color='red' >" + result.fail + "</font>";
						}else if(result.success){
							alert(result.success);
							history.go(0);
						}
					}
				}
			}else{
				top.location='<c:url value='/gotologin.controller' />';
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
					<div style='text-align: center'>
					<table align='right' style='width: 80%;'>
						<tr>
							<td>
							<textarea id='c_Conts' style='min-height: 100px;' placeholder='請輸入回覆內容...'></textarea>
							<span id='result1c'>&nbsp;</span>
							</td>
						</tr>
						<tr>
							<td>
							<button type='button' class='primary' id='sendData'>送出</button>
							</td>
						</tr>
					</table>
					</div>
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