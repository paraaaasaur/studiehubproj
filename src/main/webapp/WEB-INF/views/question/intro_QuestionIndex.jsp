<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>


<style type="text/css">
   span.error {
	color: red;
	display: inline-block;
	font-size: 5pt;
}

.spinner {
    width: 70px;
    height: 70px;
    background-color: #5b99de;
    margin: 50px auto 50px auto;
  }
  .spin {
    animation: RotatePlane 1.5s infinite ease-in-out;
  }
  .text {
    text-align: center;
    font-weight: bolder;
    font-size: 2rem;
    color: #5b99de;
  }
  @keyframes RotatePlane {
    0%   { transform: perspective(120px) rotateX(0deg) rotateY(0deg); }
    50%  { transform: perspective(120px) rotateX(-180.1deg) rotateY(0deg); }
    100% { transform: perspective(120px) rotateX(-180deg) rotateY(-179.9deg); }
  }
  
  img:hover{                             
    filter: invert(0%);
    -webkit-filter: opacity(0.7);                       
}

</style>

<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">

<title>線上測驗區</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
window.addEventListener('load', function(){
	var dataArea = document.getElementById("dataArea");
	var dataArea2 = document.getElementById("dataArea2");
	dataArea.innerHTML = showData();
	dataArea2.innerHTML = showData2();
});

function showData(){
  	let tmp1 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp0 = "<a href='" + tmp1 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/n1.png' />'" + "</a>";
 	
 	let tmp3 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp2 = "<a href='" + tmp3 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/n2.png' />'" + "</a>";
 	
 	let tmp5 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp4 = "<a href='" + tmp5 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/n3.png' />'" + "</a>";
 	
 	let tmp7 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp6 = "<a href='" + tmp7 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/n4.png' />'" + "</a>";
 	
 	let tmp9 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp8 = "<a href='" + tmp9 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/n5.png' />'" + "</a>";
 	
 	
	let content  = "<div>" + tmp0 +"&ensp;"+ tmp2 +"&ensp;"+ tmp4 +"&ensp;"+ tmp6 +"&ensp;"+ tmp8 + "</div>"; 


	return content;
};

function showData2(){
	let tmp1 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp0 = "<a href='" + tmp1 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/e1.png' />'" + "</a>";
 	
 	let tmp3 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp2 = "<a href='" + tmp3 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/e2.png' />'" + "</a>";
 	
 	let tmp5 = "<c:url value='/question.controller/startRandomMixExam/'  />";
 	let tmp4 = "<a href='" + tmp5 + "' >" + "<img width='180' height='55' src='<c:url value='/images/question/e3.png' />'" + "</a>";
 	
 	
	let content  = "<div>" + tmp0 +"&ensp;"+ tmp2 +"&ensp;"+ tmp4 + "</div>"; 


	return content;
};

</script>


</head>
<body class="is-preload">

	<!-- Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>


<div>
<h1 align='center'>線上測驗區</h1>

<div >
<h2>✓ 挑戰一下試題範例吧</h2>
</div><br>
<h3>〻請選擇 日語測驗程度：</h3>

<ul>
　<li>線上測驗-共10題，作答時間為5分鐘</li>
  <li>聽力題:5題，多選題:3題，單選題:3題</li>
</ul>
  
<div align='left'  id='dataArea'>
</div><br>
<br><h3>〻請選擇 英語測驗類型：</h3>
<ul >
　<li>線上測驗-共10題，作答時間為5分鐘</li>
  <li>聽力題:5題，多選題:3題，單選題:3題</li>
  <li>透過迷你版-體驗英檢線上測驗，先行瞭解考古題的模式與題型</li>
</ul>
<div align='left'  id='dataArea2'>
</div><br>
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