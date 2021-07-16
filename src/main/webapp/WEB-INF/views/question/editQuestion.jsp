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
				<%@include file="../universal/adminHeader.jsp"%>
				
<div align="center">
	<h2 align='center'>維護試題資料</h2>
	
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
	        <td>
		       <form:select path="q_class" >
                   <form:option label="請挑選" value="" />
                   <form:options  items="${classList}" />
               </form:select>
               <form:errors path="q_class"  cssClass="error"/>		      
		   </td>
		   </tr>
		  	
		  	<tr>  
	     	<td>題目類型：<br>&nbsp;</td>
	        <td>
		       <form:select path="q_type" >
                   <form:option label="請挑選" value="" />
                   <form:options  items="${typeList}" />
               </form:select>
               <form:errors path="q_type"  cssClass="error"/>		      
		   </td>
	   		</tr>
		
	   <tr>
	      <td>問題：<br>&nbsp;</td>
	      <td>
	      	<form:input path="q_question"/>	
		      <form:errors path='q_question' cssClass="error"/>
		  </td>
		</tr>
		
		<tr>
		   <td>選項A：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="q_selectionA"/>
		      <form:errors path='q_selectionA' cssClass="error"/>
		  </td>
	   </tr>	   
	   
	   	<tr>
	      <td>選項B：<br>&nbsp;</td>
	      <td>
	      	<form:input path="q_selectionB"/>
		      <form:errors path='q_selectionB' cssClass="error"/>
		  </td>
		   </tr>
		  
		  
		  <tr>
		   <td>選項C：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="q_selectionC"/>
		      <form:errors path='q_selectionC' cssClass="error"/>
		  </td>
	   </tr>	
	   
	  <tr>
	      <td>選項D：<br>&nbsp;</td>
	      <td>
	      	<form:input path="q_selectionD"/>
		      <form:errors path='q_selectionD' cssClass="error"/>
		  </td>
	 </tr>	
	 
   	<tr>
	      <td>選項E：<br>&nbsp;</td>
	      <td>
	      	<form:input path="q_selectionE" placeholder="此欄位為多選題選項" />
		  </td>
	 </tr>	
	   
		<tr>   
	     	<td>正解：<br>&nbsp;</td>
	        <td>
		       <form:checkboxes items="${answerList}"  path="answers" />
		      <br><form:errors path='q_answer' cssClass="error"/>
		   </td>
	   </tr>	
	   
	   <tr>
	      <td>題目照片：<br>&nbsp;</td>
	      <td>
	   	  	 <form:input path="multipartFilePic" type='file'/>
		      <form:errors path='multipartFilePic' cssClass="error"/>
		  </td>
		  </tr>
		  
		 <tr> 
		   <td>題目音檔：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="multipartFileAudio" type='file'/>
		      <form:errors path='multipartFileAudio' cssClass="error"/>
		  </td>
	   </tr>	
		
		
		
		
	<tr height='50'>		
		<td colspan='6' align='center'>
	        <input type='submit'>
		</td>
	</tr>
	
	</Table>
	</form:form>
	
<br>
<a href="<c:url value='/question.controller/queryQuestion'/> " >回前頁</a>
</div>
	</div>
		</div>

	<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="../universal/adminSidebar.jsp"%>

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