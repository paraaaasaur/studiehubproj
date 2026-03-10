<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<title>發表文章</title>
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { u_id, userPicString } = bootstrapData;

	var hasError = false;

	window.onload = function() {
		var sendData = document.getElementById("sendData");
		sendData.onclick = function() {
			//抓欄位資料
			var today = new Date();
			var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
			if(today.getHours()<12){
			var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds() + "AM";
			}else{
				var time = today.getHours()-12 + ":" + today.getMinutes() + ":" + today.getSeconds() + "PM";
			}
			var dateTime = date+' '+time;
			var c_Date = dateTime;
			var c_Class = document.getElementById("c_Class").value;
			var c_Title = document.getElementById("c_Title").value;
			var c_Conts = document.getElementById("c_Conts").value;
			var u_ID = u_id;
			var span1 = document.getElementById('result1c');
			var span2 = document.getElementById('result2c');

			if(!c_Title){
				setErrorFor(span1, "請輸入標題");
			} else{
				span1.innerHTML = "";
			}

			if (hasError) {
				return false;
			}
			var xhr1 = new XMLHttpRequest();
			xhr1.open("POST", "insertChat", true);
			var jsonInsertData = {
				"c_Date" : c_Date,
				"c_Class" : c_Class,
				"c_Title" : c_Title,
				"c_Conts" : c_Conts,
				"u_ID" : u_ID
			}
			xhr1.setRequestHeader("Content-Type", "application/json");
			xhr1.send(JSON.stringify(jsonInsertData));
			xhr1.onreadystatechange = function() {
				if (xhr1.readyState == 4 && xhr1.status == 200) {
					const result = JSON.parse(xhr1.responseText);
					if (result.fail) {
						const font = document.createElement('font');
						font.color = 'red';
						font.textContent = result.fail;

						span1.replaceChildren(font);
					} else if (result.success) {
						alert(result.success + "! 為您導回上一頁...");
						top.location = 'goSelectAllChat';
					}
				}
			}
		};

		function setErrorFor(input, message) {
			const font = document.createElement('font');
			font.textContent = message;
			font.color = 'red';
			font.size = '-2';

			input.replaceChildren(font);
			hasError = true;
		}

		var logout = document.getElementById("logout");
		logout.onclick = function() {
			var xhr = new XMLHttpRequest();
			xhr.open("GET", "logout.controller", true);
			xhr.send();
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4 && xhr.status == 200) {
					var result = JSON.parse(xhr.responseText);
					if (result.success) {
						alert(result.success);
						top.location = '';
					} else if (result.fail) {
						alert(result.fail);
						top.location = '';
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
			document.getElementById('user-info-actions').toggleAttribute('hidden', false);
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



		$('#autoInput').on('click', function(){
			$('#c_Title').val("請問德文的你好怎麼說？");
			$('#c_Class').val("德文");
			$('#c_Conts').val("德文新手請教各位大大");
		});
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>


				<div align='center'>
					<div class="container">
						<br>
						<table style="width: 750px;">
							<tr>
								<td style="width:60px;">標題:</td>
								<td style="width:650px;">
									<input type="text" name="c_Title" id="c_Title" style="width:650px;" placeholder="請輸入文章標題...">
									<span id="result1c">&nbsp;</span>
								</td>
							</tr>
							<tr>
								<td style="width:60px;">類別:</td>
								<td style="width:650px;">
									<select name="c_Class" id="c_Class" style="width:650px;">
										<option value="英文">英文</option>
										<option value="西文">西文</option>
										<option value="法文">法文</option>
										<option value="德文">德文</option>
										<option value="俄文">俄文</option>
										<option value="日文">日文</option>
										<option value="韓文">韓文</option>
										<option value="其他">其他</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:60px;">內容:</td>
								<td style="width:650px;">
									<textarea name="c_Conts" id="c_Conts" placeholder="請輸入文章內容..."></textarea>
									<span id="result2c">&nbsp;</span>
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center" style="table-layout: fixed">
									<button type="button" id="autoInput">一鍵</button> &nbsp;
									<input type="submit" class="primary" id="sendData"> &nbsp;
									<input type="reset" value="清除">
								</td>
							</tr>
						</table>
					</div>
				</div>


			</div>
		</div>
		<%@include file="../universal/sidebar.jsp"%>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>