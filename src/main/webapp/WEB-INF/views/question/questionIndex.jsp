<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>

<%-- <link  rel='stylesheet' href="<c:url value='/css/style.css'  />" />	  --%>
<meta charset="UTF-8">
<title>Question Index</title>
</head>
<body>
<div align='center'>
    <h2>題庫首頁</h2>
    <hr>
<%--     <a href="<c:url value='/readfile/excel/adc' />">匯入初始資料</a><br>  --%>
    <a href="<c:url value='/queryQuestion' />">查詢、修改試題資料</a><br> 
    <a href="<c:url value='/insertQuestion' />">新增試題資料</a><br>
      
    <hr>
<%-- <img  src='${pageContext.request.contextPath}/images/PDF.png' > --%>

<!-- 注意下一行 -->
<%-- <img  src="<c:url value='/images/PDF.png'  />" > --%>
 </div>   
</body>
</body>
</html>