<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ckeditor.css">
<title>新增回覆</title>
<script>
window.onload = function(){
	$('#autoInput').on('click', function(){
    	$('#c_Conts').val("我也想知道，同問");
    })
}
</script>
</head>
<body>
<div id="wrapper">
		<div id="main">
			<div class="inner">
				<div align='center'>
					<%@include file="../universal/adminHeader.jsp"%>
					<br>
					<form:form method="POST" modelAttribute="chatReply" enctype='multipart/form-data'>
					<table style="line-height:20px;">
					  <tr>
					    <td align='left'>文章編號: </td>
						<td colspan='2' align='center'><form:input path="c_IDr" readonly="true"/><br><form:errors path="c_IDr" cssClass="error"/></td>
					  </tr>
					  <tr>
					    <td align='left'>日期: </td>
						<td colspan='2' align='center'><form:input path="c_Date" readonly="true"/><br><form:errors path="c_Date" cssClass="error"/></td>
					  </tr>
					  <tr>
					    <td align='left'>帳號: </td>
						<td colspan='2' align='center'><form:input path="U_ID" readonly="true"/><br><form:errors path="U_ID" cssClass="error"/></td>
					  </tr>
					  <tr>
					    <td align='left'>內容: </td>
						<td colspan='2' align='center'><form:textarea path="c_Conts"/><br><form:errors path="c_Conts" cssClass="error"/></td>
					  </tr>
					  <tr>
						<td colspan='3' align='center'><button type="button" id="autoInput">一鍵</button> &nbsp;<input class="primary" type='submit' value="新增"></td>
					  </tr>
					</table>
					</form:form>
					<div align='center'>
						<hr>
						<a href="<c:url value='/goSelectAllChat' />">上一頁</a>
					</div>
				</div>
				<p />
			</div>
		</div>
	</div>
	<script	src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script	src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
	<script src="${pageContext.request.contextPath}/build/ckeditor.js"></script>
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