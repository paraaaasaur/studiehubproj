<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<link rel="stylesheet" href="assets/css/ckeditor.css">
<title>編輯回覆</title>
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
		//universal
		var loginHref = document.getElementById('loginHref');
		var signupHref = document.getElementById('signupHref');
		var logoutHref = document.getElementById('logoutHref');
		var userId = document.getElementById('userId');
		var userPic = document.getElementById('userPic');
		var loginEvent = document.getElementById('loginEvent');
		var loginEvent1 = document.getElementById('loginEvent1');
		var loginALLEvent1 = document.getElementById('loginALLEvent1');

		if (u_id) {
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
			$('#c_Conts').val("推一個");
		})
	}
</script>

</head>

<body>
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../../fragments/header.jsp"%>


				<div align='center'>
					<br>
					<form:form method="POST" modelAttribute="chatReply" enctype='multipart/form-data'>
						<table style="line-height:20px;">
							<tr>
								<td align='left'>文章編號: </td>
								<td colspan='2' align='center'>
									<form:input path="c_IDr" readonly="true"/>
									<br>
									<form:errors path="c_IDr" cssClass="error"/>
								</td>
							</tr>
							<tr>
								<td align='left'>日期: </td>
								<td colspan='2' align='center'>
									<form:input path="c_Date" readonly="true"/>
									<br>
									<form:errors path="c_Date" cssClass="error"/>
								</td>
							</tr>
							<tr>
								<td align='left'>帳號: </td>
								<td colspan='2' align='center'>
									<form:input path="U_ID" readonly="true"/>
									<br>
									<form:errors path="U_ID" cssClass="error"/>
								</td>
							</tr>
							<tr>
								<td align='left'>內容: </td>
								<td colspan='2' align='center'>
									<form:textarea path="c_Conts"/>
									<br>
									<form:errors path="c_Conts" cssClass="error"/>
								</td>
							</tr>
							<tr>
								<td colspan='3' align='center'>
									<button type="button" id="autoInput">一鍵</button>
									<input class="primary" type='submit' value="編輯">
								</td>
							</tr>
						</table>
					</form:form>

					<div align='center'>
						<hr>
						<a href="goSelectAllChat">上一頁</a>
					</div>
				</div>


			</div>
		</div>
		<%@include file="../../fragments/sidebar.jsp"%>
	</div>

	<script	src="assets/js/jquery.min.js"></script>
	<script	src="assets/js/browser.min.js"></script>
	<script	src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	<script src="build/ckeditor.js"></script>
	<script>
		ClassicEditor.create( document.querySelector( '#c_Conts' ), {
			// 這裡可以設定 plugin
		})
			.then( editor => {
				console.log( 'Editor was initialized', editor );
			 })
			 .catch( err => {
				console.error( err.stack );
			 });
	</script>

</body>
</html>