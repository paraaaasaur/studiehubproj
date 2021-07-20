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
var userPicString = "${loginBean.pictureString}";

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
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
    	userId.innerHTML = u_id;
    	loginEvent.style.display = "block";
    	loginEvent1.style.display = "block";
    	loginALLEvent1.style.display = "block";
    }
	// 有登入才會顯示購物車sidebar
	let cartHref = document.querySelector('#cartHref');
	cartHref.hidden = (u_id)? false : true;
	cartHref.style.visibility = (u_id)? 'visible' : 'hidden';
//universal

    $('#quickInsert').on('click',function(){
        $('#p_Name').val('日文教學_初級日語【日本人老師yuka教你日語】');
        $('#p_Class').val('日文');
        $('#p_Price').val('100');
        $('#descString').val('1. 100%國際認證師資，不只English speaker而是English teacher：認證教師才能真正激發潛力&自信，英協教師具備平均8年以上教學經驗及劍橋認證');
    });
    
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
                                <tr>
                                    <td>導師名稱:</td>
                                    
                                        <td><input type="hidden" name="u_ID" value="${loginBean.u_id}"/>${loginBean.u_id}
                                        </td>
                                        
                                    </tr>
                                    <tr>
                                        <td>課程名稱:</td>
                                        <td><form:input path="p_Name" id="p_Name"/>
                                        	<form:errors path='p_Name' cssClass="error"/>
                                        </td>
                                        
                                    </tr>
                                    <tr>
                                        <td>課程類別:</td>
                                        <td><form:select path="p_Class" id="p_Class">
                                        		<form:option label="請挑選" value="-1"/>
                                        		<form:option label="英文" value="英文"/>
                                        		<form:option label="日文" value="日文"/>
                                        	</form:select>
                                        	<form:errors path='p_Class' cssClass="error"/>
                                        	</td>
                                    </tr>
                                    <tr>
                                        <td>課程價錢:</td>
                                        <td><form:input path="p_Price" id="p_Price"/>
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
                                    <td><input type="submit">
                                    <button type="button" id="quickInsert">一鍵</button></td>
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