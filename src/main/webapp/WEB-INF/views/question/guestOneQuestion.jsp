<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
<style type="text/css">
   span.error {
	color: red;
	display: inline-block;
	font-size: 5pt;
}

</style>
<script>
var u_id = "${loginBean.u_id}";
var userPicString = "${loginBean.pictureString}";

window.onload = function(){

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

}

</script>


<meta charset="UTF-8">
</head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>

</head>

<body class="is-preload">
	

	<!-- Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>
				
<div align="center">
	<h2 align='center'>查看詳細試題</h2>
	
	<form:form method="POST" modelAttribute="Q1" enctype='multipart/form-data'>
	
	
	<Table>
	<c:choose>
		<c:when test='${Q1.q_id == null}'>
		    <tr>
		    	<td>&nbsp;</td>
		    	<td>
	   	  		   &nbsp;
	   	  		</td>
		    </tr>
        </c:when>	   
    	<c:otherwise>
	 	 <tr>
	       <td>題目編號：<br>&nbsp;</td>
	   	   <td><form:hidden path="q_id"/>
	   	    	${Q1.q_id}<br>&nbsp;
	   	   </td>
	    </tr>
       </c:otherwise>   
		</c:choose>  
		
		<tr>
	      <td>課程分類：<br>&nbsp;</td>
	      <td><form:hidden path="q_class"/>${Q1.q_class}</td>
		</tr>
		
		<tr>
	      <td>題目類型：<br>&nbsp;</td>
	      <td><form:hidden path="q_type"/>${Q1.q_type}</td>
		</tr>	 	
		
	   <tr>
	      <td>問題：<br>&nbsp;</td>
	      <td><form:hidden path="q_question"/>${Q1.q_question}</td>
		</tr>
		
		<tr>
	      <td>選項A：<br>&nbsp;</td>
	      <td><form:hidden path="q_selectionA"/>${Q1.q_selectionA}</td>
		</tr>
		
		<tr>
	      <td>選項B：<br>&nbsp;</td>
	      <td><form:hidden path="q_selectionB"/>${Q1.q_selectionB}</td>
		</tr>
		
		<tr>
	      <td>選項C：<br>&nbsp;</td>
	      <td><form:hidden path="q_selectionC"/>${Q1.q_selectionC}</td>
		</tr>
		
		<tr>
	      <td>選項D：<br>&nbsp;</td>
	      <td><form:hidden path="q_selectionD"/>${Q1.q_selectionD}</td>
		</tr>
		
		<tr>
	      <td>選項E：<br>&nbsp;</td>
	      <td><form:hidden path="q_selectionE"/>${Q1.q_selectionE}</td>
		</tr>
		   
	   	<tr>
	      <td>正解：<br>&nbsp;</td>
	      <td><form:hidden path="q_answer"/>${Q1.q_answer}</td>
		</tr>
		
		<tr>
		<td  style='vertical-align: middle;'>題目音檔：<br>&nbsp;</td>
		<td><audio controls src='${Q1.q_audioString}' ></audio></td>
		</tr>	
	   
		<tr>
		<td  style='vertical-align: middle;'>題目照片：<br>&nbsp;</td>
		<td><img width='300' height='180' src= '${Q1.q_pictureString}'></td>
		</tr>	
		
		
		
		
	
	</Table>
	</form:form>
	
<br>

<a href="<c:url value='/question.controller/guestQueryQuestion'/> ">
   <button>回前頁</button>
</a>

<%-- <a href="<c:url value='/question.controller/guestQueryQuestion'/> " >回前頁</a> --%>
</div>
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