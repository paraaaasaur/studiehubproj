<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>會員資訊</title>
</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%><!-- 帳號，密碼，確認密碼，姓，名，信箱 -->
				
				<div align='center'>
<%-- 				id: ${loginBean.u_id } --%>
				
				
				<form:form method="POST" action="/updateUserinfo.controller" modelAttribute="userBean" enctype='multipart/form-data'>
						<!-- 測試 -->
						<table>
							<tr>
								<td>帳號:</td>
								<td>${userBean.u_id}</td>
							</tr>
							<tr>
								<td>名字:</td>
								<td>${userBean.u_firstname}</td>
							</tr>
							<tr>
								<td>姓氏:</td>
								<td>${userBean.u_lastname}</td>
							</tr>
							<tr>
								<td>地址:</td>
								<td>${userBean.u_address}</td>
							</tr>
							<tr>
								<td>信箱:</td>
								<td>${userBean.u_email}</td>
							</tr>



						</table>
						<!-- 測試 -->





					</form:form>
				
				
				
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