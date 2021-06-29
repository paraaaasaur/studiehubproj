<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
</style>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>




</head>

<body class="is-preload">
	屬性

	<!-- Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>
                
				<h2 align='center'>請輸入修改活動</h2>
				<hr>

				<form:form method="POST" modelAttribute="EventInfo" enctype='multipart/form-data'>
					
					<table border="1">

						<c:choose>

							<c:when test='${EventInfo.a_aid == null}'>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>活動編號：<br>&nbsp;
									</td>
									<td><form:hidden path='a_aid' /> <%-- form:hidden 這個欄位不能被修改  在修改的時候才會出現--%>
										${EventInfo.a_aid}<br>&nbsp;</td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr>
						<tr>
							<td>活動名稱:</td>
							<td><form:input path="a_name" /></td>

						</tr>
						<tr>
							<td>活動類別:</td>
							<td><form:input path="a_type" /></td>
						</tr>
						<tr>
							<td>活動開始時間:</td>
							<td><form:input path="a_startTime" /></td>
						</tr>
						<tr>
							<td>活動結束時間</td>
							<td><form:input path="a_endTime" /></td>
						</tr>
						<tr>
							<td>活動地址:</td>
							<td><form:input path="a_address" /></td>
						</tr>
						<tr>
							<td>活動說明:</td>
							<td><form:textarea path="comment" cols="100"
									rows="10" /></td>
						</tr>
						<tr>
							<td>活動圖片:</td>
							<td><form:input path="eventImage" type="file" /></td>
						</tr>

						<tr>
							<td><input type="submit"></td>
						</tr>
					</table>
				</form:form>

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