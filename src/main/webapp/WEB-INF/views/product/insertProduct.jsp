<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>

<style type = "text/css">
	span.error{
		color:red;
		display: inline-block;
		font-size:5pt;
	
	}
</style>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>

<script>
var u_id = "${loginBean.u_id}";

window.onload = function(){
    var logout = document.getElementById("logout");
    logout.onclick = function(){
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "<c:url value='/logout.controller' />", true);
        xhr.send();
        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4 && xhr.status == 200){
                var result = JSON.parse(xhr.responseText);
                if(result.success){
                    alert(result.success);
                    top.location = '<c:url value='/' />';
                }else if(result.fail){
                    alert(result.fail);                    
                    top.location = '<c:url value='/' />';
                }
            }
        }
    }
    
    //如果有登入，隱藏登入標籤
    var loginHref = document.getElementById('loginHref');
    var signupHref = document.getElementById('signupHref');
    var logoutHref = document.getElementById('logoutHref');
    var userId = document.getElementById('userId');
    var userPic = document.getElementById('userPic');
    var loginEvent = document.getElementById('loginEvent');
    if(u_id){
    	loginHref.hidden = true;
    	signupHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
    	userId.innerHTML = u_id;
        loginEvent.style.display = "block";
    	loginALLEvent.style.display = "block";
    } 
    
}
</script>

</head>

<body class="is-preload">

		<!-- Wrapper -->
			<div id="wrapper">

				<!-- Main -->
					<div id="main">
						<div class="inner">
						<%@include file="../universal/header.jsp" %>  
						
						<h2 align='center'>請輸入課程資訊</h2>
						<hr>

							<form:form method="POST" modelAttribute="productInfo" enctype='multipart/form-data'>
								<table border="1">
                                    <form:hidden path="u_ID"/>${loginBean.u_id}
                                    <tr>
                                        <td>課程名稱:</td>
                                        <td><form:input path="p_Name"/>
                                        	<form:errors path='p_Name' cssClass="error"/>
                                        </td>
                                        
                                    </tr>
                                    <tr>
                                        <td>課程類別:</td>
                                        <td><form:select path="p_Class">
                                        		<form:option label="請挑選" value="-1"/>
                                        		<form:option label="英文" value="en"/>
                                        		<form:option label="日文" value="ja"/>
                                        	</form:select>
                                        	<form:errors path='p_Class' cssClass="error"/>
                                        	</td>
                                    </tr>
                                    <tr>
                                        <td>課程價錢:</td>
                                        <td><form:input path="p_Price"/>
                                        	<form:errors path='p_Price' cssClass="error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>課程介紹:</td>
                                    	<td><textarea style="resize:none" rows="10" cols="100" name="descString" id="descString"></textarea>
                                    		<form:errors path='descString' cssClass="error"/>
                                    	</td>
                                    </tr>
                                    <tr>
                                    	<td>課程圖片:</td>
                                        <td><form:input path="imgFile" type="file" />
                                        	<form:errors path='imgFile' cssClass="error"/>
                                        </td>
                                    </tr>
                                    <tr>
                                    	<td>課程影片:</td>
                                        <td><form:input path="videoFile" type="file" />
                                        	<form:errors path='videoFile' cssClass="error"/>
                                        </td>
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
				<%@include file="../universal/sidebar.jsp" %>  

			</div>

		<!-- Scripts -->
			<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

	</body>
</html>