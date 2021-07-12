<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>會員註冊</title>

<script>
var hasError = false;
var pswHasError = false;
window.onload = function(){
	var alink = document.getElementById("accountCheck");
	var sendData = document.getElementById("sendData");
	var loading = document.getElementById("loadingGif");
	//檢查帳號是否重複
	alink.onclick = function(){
		var u_id = document.getElementById("u_id").value;
		var span = document.getElementById("result0c");
		if(!u_id){
			span.innerHTML = "<font color='red' size='-1'>請輸入帳號</font>";
			return;
		}
		var xhr = new XMLHttpRequest();
		// 待寫後端判斷(done)
		xhr.open("POST", "<c:url value='/checkUserId' />", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xhr.send("u_id=" + u_id);
		var message = "";
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var result = JSON.parse(xhr.responseText);
				if(result.u_id.length == 0){
					message = "<font color='green' size='-2'>帳號可用!</font>";
				}
				else if ( result.u_id.startsWith("Error") ) {
				message = "<font color='red' size='-2'>發生錯誤: 代號" + result.u_id + "</font>";
				}
				else {
					message = "<font color='red' size='-2'>此帳號已被註冊!</font>";
				}
				span.innerHTML = message;
			}
		}
	}
    
	//確認送出資料
	sendData.onclick = function(){
		//抓欄位資料
		var u_id = document.getElementById("u_id").value; //帳號
		var u_psw = document.getElementById("u_psw").value; //密碼
		var ck_psw = document.getElementById("ck_psw").value; //確認密碼
		var u_lastname = document.getElementById("u_lastname").value; //姓
		var u_firstname = document.getElementById("u_firstname").value; //名
		var u_email = document.getElementById("u_email").value; //信箱
		var span0 = document.getElementById('result0c'); //帳號span
		var span1 = document.getElementById('result1c'); //密碼span
		var span2 = document.getElementById('result2c'); //姓span
		var span3 = document.getElementById('result3c'); //名span
		var span4 = document.getElementById('result4c'); //信箱span
		var spanCheckPsw = document.getElementById('checkPsw'); //確認密碼
		
// 		var spanResult = document.getElementById('resultMsg');
		if(!u_id){
			setErrorFor(span0, "請輸入帳號");
		} else{
			span0.innerHTML = "";
		}
		if(!u_psw){
			setErrorFor(span1, "請輸入密碼");
		} else{
			span1.innerHTML = "";
		}
		if(!ck_psw){
			setErrorFor(spanCheckPsw, "請再次輸入密碼!")
		} else{
			spanCheckPsw.innerHTML = "";
		}
		if(!u_lastname){
			setErrorFor(span2, "請輸入姓氏");
		} else{
			span2.innerHTML = "";
		}
		if(!u_firstname){
			setErrorFor(span3, "請輸入名字");
		} else{
			span3.innerHTML = "";
		}
		if(!u_email){
			setErrorFor(span4, "請輸入信箱");
		} else{
			span4.innerHTML = "";
		}
		//檢查email格式
// 		if(!(u_email.includes('@'))){
// 			setErrorFor(span4, "信箱格式錯誤");
// // 			hasError = true;
// 		} else{
// 			span4.innerHTML = "";
// 		}
		
		// 檢查密碼是否一致
		if(u_psw==ck_psw && ck_psw!=""){
			spanCheckPsw.innerHTML = "";
			pswHasError = false;
		}else{
			spanCheckPsw.innerHTML = "<font color='red' size='-2'>密碼不同，請再次確認!</font>";
			pswHasError = true;
		}
		if (hasError || pswHasError){
			return false;
		}
		var xhr1 = new XMLHttpRequest();
		xhr1.open("POST", "<c:url value='/userSignup' />");
		var jsonSignupData = {
			"u_id" : u_id,
			"u_psw" : u_psw,
			"u_lastname" : u_lastname,
			"u_firstname" : u_firstname,
			"u_email" : u_email
		}
		xhr1.setRequestHeader("Content-Type", "application/json");
		xhr1.send(JSON.stringify(jsonSignupData));
		// loading GIF 秀出來
		loading.src = "<c:url value='images/user/loading.gif' />";
		xhr1.onreadystatechange = function() {
			if (xhr1.readyState == 4 && xhr1.status == 200){
				result = JSON.parse(xhr1.responseText);
				//判斷回傳
				if(result.fail){
					loading.src = ""; //loading GIF 隱藏
					loading.hidden = true;
// 					spanResult.innerHTML = "<font color='red' >" + result.fail + "</font>";
					span0.innerHTML = "<font color='red' >" + result.fail + "</font>";
				}else if(result.success){
					loading.src = ""; //loading GIF 隱藏
					loading.hidden = true;
					alert(result.success + "! 系統已寄送註冊成功之信件至您的信箱, 請盡早完成會員資料...");
					top.location='<c:url value='/gotologin.controller' />';
				}else if(result.formatError){
					loading.src = ""; //loading GIF 隱藏
					loading.hidden = true;
					alert(result.formatError);
				}
			}
		}
	}
	
	function setErrorFor(input, message){
		input.innerHTML = "<font color='red' size='-2'>" + message + "</font>";
		hasError = true;
	}
	
	//一鍵帶入
// 	var thisAutoInput = document.getElementById('autoInput');
// 	var thisId = document.getElementById('u_id');
// 	var thisPsw = document.getElementById('u_psw');
// 	var thisCkPsw = document.getElementById('ck_psw');
// 	var thisLastname = document.getElementById('u_lastname');
// 	var thisFirstname = document.getElementById('u_firstname');
// 	var thisEmail = document.getElementById('u_email');
	$('#autoInput').on('click', function(){
		$('#u_id').val("demoid");
		$('#u_psw').val("demopsw");
		$('#ck_psw').val("demopsw");
		$('#u_lastname').val("孫");
		$('#u_firstname').val("若安");
		$('#u_email').val("iiidemo0723@gmail.com");
	})
	
	
		
}
</script>

</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%><!-- 帳號，密碼，確認密碼，姓，名，信箱 -->
				
				<div align='center'>
					<div class="container">
					<br>
						<form>
							<table style="width: 750px;">
								<tr>
									<td>帳號:</td>
									<td style="width:300px;"><input type="text" name="u_id" id="u_id" style="width:250px;" placeholder="Account"></td>
									<td style="font-size: small; width:80px; text-align:left;"><a href="#" id='accountCheck' style="color:blue;">檢查帳號</a></td>
									<td style="width:150px;"><span id="result0c">&nbsp;</span></td>
								</tr>
								<tr>
									<td>密碼:</td>
									<td style="width:300px;"><input type="password" name="u_psw" id="u_psw" style="width:250px;" placeholder="Password"></td>
									<td style="width:80px;"></td>
									<td style="width:150px;"><span id="result1c">&nbsp;</span></td>
								</tr>
								<tr>
									<td>確認密碼:</td>
									<td style="width:300px;"><input type="password" name="ck_psw" id="ck_psw" style="width:250px;" placeholder="Confirm Password"></td>
									<td style="width:80px;"></td>
									<td style="width:150px;"><span id="checkPsw">&nbsp;</span></td>
								</tr>
								<tr>
									<td>姓氏:</td>
									<td style="width:300px;"><input type="text" name="u_lastname" id="u_lastname" style="width:250px;" placeholder="Last name"></td>
									<td style="width:80px;"></td>
									<td style="width:150px;"><span id="result2c">&nbsp;</span></td>
								</tr>
								<tr>
									<td>名字:</td>
									<td style="width:300px;"><input type="text" name="u_firstname" id="u_firstname" style="width:250px;" placeholder="First name"></td>
									<td style="width:80px;"></td>
									<td style="width:150px;"><span id="result3c">&nbsp;</span></td>
								</tr>
								<tr>
									<td>信箱:</td>
									<td style="width:300px;"><input type="text" name="u_email" id="u_email" style="width:250px;" placeholder="E-mail"></td>
									<td style="width:80px;"></td>
									<td style="width:150px;"><span id="result4c">&nbsp;</span></td>
								</tr>
								<tr>
									<td colspan="4" align="center" style="table-layout: fixed">
										<button type="button" id="autoInput">一鍵</button>&nbsp;
										<input type="reset" value="清除">&nbsp;
										<button type="button" class="primary" id="sendData">送出</button>
<%-- 										<img style="visibility:hidden;" id="loadingGif" alt="" src="<c:url value='images/user/loading.gif' />" width='80px'> --%>
									</td>
								</tr>
							</table>
						</form>
										<img id="loadingGif" alt="" src="" width='100px'>
					</div>
				</div>

			</div>
		</div>
		<%@include file="../universal/sidebar.jsp"%>
	</div>
	<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>