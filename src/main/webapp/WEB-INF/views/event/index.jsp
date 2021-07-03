<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%-- <link rel='stylesheet' href='${pageContext.request.contextPath}/css/style.css' /> --%>
<!-- 可以連到 -->

<link rel='stylesheet' href="<c:url value='/' />css/style.css" />

<link rel='stylesheet' href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
	

<%-- <c:url value='/' 他會去找 文件跟目錄下的專案名稱  /springapp/ --%>
<!-- 可以連到 -->



<!-- <link rel='stylesheet' href='/css/style.css' /> -->
<!-- jsp這樣找不到 HTML才可以 --> 
<!-- tomcat的文件跟目錄在C:\_JSP\tomcat9\webapps\  這裡  /css/style.css /開頭會被解釋成 文件根目錄 也就是說 webapps資料夾裡面會有一個資料夾跟專案同名 然後css 放在同名專案資料夾裡面 -->

<!-- <link rel='stylesheet' href='/springapp/css/style.css' /> -->
<!-- 絕對路徑 -->

<meta charset="UTF-8">
<title>Spring Boot</title>
</head>
<body>
	<div align='center'>
    <h2>Spring Boot 範例</h2>
    <hr>
    

<button type="button" class="btn btn-primary">（首选项）Primary</button>
<BR>

    <a href="<c:url value='/queryAllEvent' />">活動後台</a><br> 
    <a href="<c:url value='/insertEvent' />">新增活動表單</a><br> 
    <a href="<c:url value='/eventindex' />">使用者活動頁</a><br> 
    <hr>
    
    <a href="<c:url value='/insertType' />">新增類型資料</a><br> 
    <a href="<c:url value='/queryType' />">查詢類型資料</a><br> 
    
 
    <hr>
		context path = ${pageContext.request.contextPath} 
<!-- 		context path = /springapp -->
		<br> 
		context path2 =<c:url value="/hello" />
<!-- 		context path2 =/springapp/hello -->
		<br> 
		context path3 =<c:url value="/" />
<!-- 		context path3 =/springapp/ -->
		<hr>
		<img src='${pageContext.request.contextPath}/images/PDF.png'>
		
<%-- 		<img src='<c:url value="/" />images/PDF.png'> --%>
		<img src='<c:url value="/" />aaaab.png'>
		<img src='<c:url value="/" />/images/MemberImage_111.jpg'>
<!-- 		<img src="images/aaa.jpg"   alt=""/> -->
		<video src='<c:url value="/" />2021-06-21 22-21-06.mp4' controls="controls"></video>
		
	</div>
</body>
</body>
</html>