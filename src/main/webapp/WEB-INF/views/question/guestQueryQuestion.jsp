<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>  
<!DOCTYPE html>
<html>
<head>


<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">

<title>所有試題資料</title>

</head>



<script>
let dataArea = null; 
let questionName = null; 
let query = null; 
window.addEventListener('load', function(){
	
	questionName = document.getElementById("questionName");
	query = document.getElementById("query");
	dataArea = document.getElementById("dataArea");
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "<c:url value='/question.controller/findAllQuestions' />", true);
	xhr.onreadystatechange = function(){
		if (xhr.readyState == 4 && xhr.status == 200 ){
			console.log(xhr.responseText);
			dataArea.innerHTML = showData(xhr.responseText);
		}
	};
	xhr.send();
	
	
	query.addEventListener('click', function(){
		let qname = questionName.value;
		if (!qname){
			alert('請輸入問題內容，可輸入部分內容');
			return;
		}
		
		let xhr2 = new XMLHttpRequest();
		xhr2.open('GET', "<c:url value='/question.controller/queryByName' />?qname=" + qname);
		xhr2.send();
		xhr2.onreadystatechange=function(){
			if (xhr2.readyState == 4 && xhr2.status == 200){
				dataArea.innerHTML = showData(xhr2.responseText);
			}
		}
		
		
	});
})

 function showData(textObj){
	
	let obj = JSON.parse(textObj);
	let size = obj.size;
	let questions = obj.list;
	let segment = "<table >";
	
	if (size == 0){
		segment += "<tr><th colspan='8'>查無資料</th><tr>";
	} else {
		segment += "<tr><th colspan='8'>共計" + size + "筆資料</th><tr>";
	    segment += "<tr><th>查看試題</th><th>題目編號</th><th>課程分類</th><th>題目類型</th><th>問題</th><th>題目照片</th><th>題目音檔</th></tr>";
	    
	    for(n = 0; n < questions.length ; n++){
		   	let question = questions[n];
	   		

		   	let tmp1 = "<c:url value='/question.controller/guestOneQuestion/'  />" + question.q_id;
	     	let tmp0 = "<a href='" + tmp1 + "' >" + "<img width='37' height='37' src='<c:url value='/images/question/check.png' />'" + "</a>";
	     	
	     	
			segment += "<tr>";
			segment += "<td width='7%'>" + tmp0 + "</td>"; 	

			segment += "<td width='7%'>" + question.q_id + "</td>"; 	
			segment += "<td width='7%'>" + question.q_class + "</td>"; 	
			segment += "<td width='7%'>" + question.q_type + "</td>"; 	
			segment += "<td>" + question.q_question + "</td>";
			
			segment += "<td><img  width='100' height='60' src='" + question.q_pictureString + "' ></td>"; 	
			segment += "<td><audio controls src='" + question.q_audioString + "' ></td>"; 	
			
			segment += "</tr>"; 	
	   }
	}
	segment += "</table>"; 
	return segment;
}
</script>


<body class="is-preload">

	<!-- Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>

<div align='center'>
<h2>所有試題資料</h2><br>



試題搜尋：<input type='text' id="questionName"  placeholder="請輸入部分問題內容" />
<button id='query'>提交</button>


<div  id='dataArea'>
</div>

<%-- <a href="<c:url value='/question.controller/turnQuestionIndex'/> " >回前頁</a> --%>
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