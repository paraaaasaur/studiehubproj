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
<%-- <link rel='stylesheet' href="<c:url value='/css/style.css' />" type="text/css" /> --%>
</head>
<body>  
<div align="center">
<fieldset style="width:960px;">
	<legend >維護試題資料</legend> 
<%-- 	    <form:form method="POST" modelAttribute="place" > --%>
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
	        <td  width='360'>
		       <form:select path="q_class" >
                   <form:option label="請挑選" value="" />
                   <form:options  items="${classList}" />
               </form:select>
                  <br>&nbsp;
               <form:errors path="q_class"  cssClass="error"/>		      
		   </td>
		  
		  
	     	<td>題目類型：<br>&nbsp;</td>
	        <td  width='360'>
		       <form:select path="q_type" >
                   <form:option label="請挑選" value="" />
                   <form:options  items="${typeList}" />
               </form:select>
                  <br>&nbsp;
               <form:errors path="q_type"  cssClass="error"/>		      
		   </td>
	   </tr>
		
	   <tr>
	      <td>問題<br>&nbsp;</td>
	      <td  width='360'>
	      	<form:input path="q_question"/><br>&nbsp;	
		      <form:errors path='q_question' cssClass="error"/>
		  </td>
		   <td>選項A：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="q_selectionA"/><br>&nbsp;	
		      <form:errors path='q_selectionA' cssClass="error"/>
		  </td>
	   </tr>	   
	   
	   	<tr>
	      <td>選項B<br>&nbsp;</td>
	      <td  width='360'>
	      	<form:input path="q_selectionB"/><br>&nbsp;	
		      <form:errors path='q_selectionB' cssClass="error"/>
		  </td>
		   <td>選項C：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="q_selectionC"/><br>&nbsp;	
		      <form:errors path='q_selectionC' cssClass="error"/>
		  </td>
	   </tr>	
	   
	  <tr>
	      <td>選項D<br>&nbsp;</td>
	      <td  width='360'>
	      	<form:input path="q_selectionD"/><br>&nbsp;	
		      <form:errors path='q_selectionD' cssClass="error"/>
		  </td>
		   <td>正解：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="q_answer"/><br>&nbsp;	
		      <form:errors path='q_answer' cssClass="error"/>
		  </td>
	   </tr>	
	   
	   <tr>
	      <td>題目照片<br>&nbsp;</td>
	      <td  width='360'>
	   	  	 <form:input path="multipartFilePic" type='file'/><br>&nbsp;
		      <form:errors path='multipartFilePic' cssClass="error"/>
		  </td>
		   <td>題目音檔：<br>&nbsp;</td>
	   	  <td>
	      	<form:input path="multipartFileAudio" type='file'/><br>&nbsp;	
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
	
</fieldset>
<br>
<a href="<c:url value='/queryQuestion'/> " >回前頁</a>
</div>
</body>
</html>