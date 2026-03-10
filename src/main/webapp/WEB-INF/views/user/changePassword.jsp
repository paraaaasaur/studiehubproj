<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}",
		"pswInOGBean": "${fn:escapeXml(loginBean.u_psw)}",
		"errorMessageOfChangingPassword": "${fn:escapeXml(errorMessageOfChangingPassword)}"
	}
</script>
<title>修改密碼</title>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { u_id, userPicString, pswInOGBean, errorMessageOfChangingPassword } = bootstrapData;

	if (errorMessageOfChangingPassword) {
		alert(errorMessageOfChangingPassword);
	}

	var hasError = false;
	window.onload = function() {
		var save = document.getElementById("save");
		
		var old_pswBox = document.getElementById("old_psw");
		old_pswBox.onchange = function() {
			var old_psw = document.getElementById("old_psw").value;
			if (!(pswInOGBean == old_psw)) {
				alert("目前的密碼輸入錯誤! 請再試一次");
				hasError = true;
				save.disabled = true;
			} else {
				save.disabled = false;
			}
			if (hasError) {
				return false;
			}
		}

		//universal
	    //如果有登入，隱藏登入標籤
	    var loginHref = document.getElementById('loginHref');
	    var signupHref = document.getElementById('signupHref');
		document.getElementById('user-info-actions').toggleAttribute('hidden', false);
	    var logoutHref = document.getElementById('logoutHref');
	    var userId = document.getElementById('userId');
	    var userPic = document.getElementById('userPic');
		var loginEvent = document.getElementById('loginEvent');
		var loginEvent1 = document.getElementById('loginEvent1');
	    var loginALLEvent1 = document.getElementById('loginALLEvent1');

	    if (u_id) {
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

				<hr>
				<div style="text-align: center;">
					<div style="display: inline-block; text-align: left;">
						<form:form method="POST" action="changePassword.controller" modelAttribute="userBean">
      						目前的密碼: <input type="password" name="old_psw" id='old_psw' placeholder="Current password">
							<br>
							新密碼: <form:password path="u_psw" name="u_psw" id="u_psw" placeholder="New password" />
							<br>
      						再次輸入新密碼: <input type="password" name="cfm_psw" id='cfm_psw' placeholder="Confirm new password" required>
							<br>
							<div align='center'>
								<input type="submit" id="save" value="儲存變更">
							</div>
						</form:form>
					</div>
					<hr>
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