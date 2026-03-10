<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>

<style type="text/css">
span.error {
	color: red;
	display: inline-block;
	font-size: 5pt;
}

textarea {
	resize: none;
}

</style>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>

<script type="application/json" id="bootstrap-data">
	{
		"adminId": "${fn:escapeXml(adminId)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { adminId } = bootstrapData;

	window.onload = function() {
		var logoutHref = document.getElementById('logoutHref');
		if (adminId) {
			logoutHref.style.visibility = "visible";
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
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/adminHeader.jsp"%>


				<h2 align='center'>請更改課程資訊</h2>
				<hr>
				<form:form method="POST" modelAttribute="productInfo" enctype='multipart/form-data'>
					<table border="1">
						<c:choose>
							<c:when test="${productInfo.p_ID == null }">
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>編號:<br>&nbsp;
									</td>
									<td><form:hidden path="p_ID" />
									${fn:escapeXml(productInfo.p_ID) }<br>&nbsp;</td>
								</tr>
							</c:otherwise>
						</c:choose>

						<tr>
							<td>課程名稱:</td>
							<td>
								<form:input path="p_Name" />
								<form:errors path='p_Name' cssClass="error" />
							</td>
						</tr>
						<tr>
							<td>課程類別:</td>
							<td>
								<form:select path="p_Class">
									<form:option label="請挑選" value="-1" />
									<form:option label="英文" value="英文" />
									<form:option label="日文" value="日文" />
								</form:select>
								<form:errors path='p_Class' cssClass="error" />
							</td>
						</tr>
						<tr>
							<td>課程價錢:</td>
							<td>
								<form:input path="p_Price" />
								<form:errors path='p_Price' cssClass="error" />
							</td>
						</tr>
						<tr>
							<td>課程介紹:</td>
							<td>
								<form:textarea path="descString" style="resize:none" rows="10" cols="100" />
								<form:errors path='descString' cssClass="error" />
							</td>
						</tr>
						<tr>
							<td>課程圖片:</td>
							<td>
								<form:input path="imgFile" type="file" />
								<form:errors path='imgFile' cssClass="error" />
							</td>
						</tr>
						<tr>
							<td>課程影片:</td>
							<td><form:input path="videoFile" type="file" />
								<form:errors path='videoFile' cssClass="error" />
							</td>
						</tr>
						<tr>
							<td><input type="submit"></td>
						</tr>
					</table>
				</form:form>


			</div>
		</div>
		<%@include file="../universal/adminSidebar.jsp"%>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>