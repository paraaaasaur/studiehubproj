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

</style>

<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">

<title>線上測驗區</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
var obj = null; 
// 全部值
var size = 0; 
// 總題數
var counter = 0; 
// 目前題目

window.addEventListener('load', function(){
	
	var dataArea = document.getElementById("dataArea");
	var next = document.getElementById("next");
	var back = document.getElementById("back");
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "<c:url value='/question.controller/sendRandomExam' />", true);
	xhr.onreadystatechange = function(){
		if (xhr.readyState == 4 && xhr.status == 200 ){
			console.log(xhr.responseText);
			dataArea.innerHTML = showFirstData(xhr.responseText);
		}
	};
	xhr.send();
	
	

	next.addEventListener('click', function(){
		if(counter < size-1){
			counter += 1;
			back.style.display = '';
			dataArea.innerHTML = showFirstData(xhr.responseText);
		}else{
			alert("已經是最後一頁了!");

// 		next.style.display = 'none';
			}
		});
		
	back.addEventListener('click', function(){
		if(counter > 1){
			counter -= 1;
			back.style.display = '';
			dataArea.innerHTML = showFirstData(xhr.responseText);
		}else if(counter == 1){
			counter -= 1;
			back.style.display = 'none';
			dataArea.innerHTML = showFirstData(xhr.responseText);
		}else{
			back.style.display = 'none';
			dataArea.innerHTML = showFirstData(xhr.responseText);
			}
		
		});
	})

 function showFirstData(textObj){
	
	obj = JSON.parse(textObj);
	size = obj.size;
	let questions = obj.list;
	let segment = "";
	
	if (size == 0){
		segment += "<h4>很抱歉，目前系統無相關試題</h4>";
	} else {
		segment += "<h4>測驗共" + size + "題</h4><br>";
	    
		   	let question = questions[counter];
	   		let number = counter+1;
	     	
		   	segment += "<h4>第&ensp;" + number + "&ensp;題</h4>";
			segment += "<div><img width='400' height='260' src='" + question.q_pictureString + "' ></div>"; 	
			segment += "<div><audio controls src='" + question.q_audioString + "' ></div>"; 	
	
			segment += "<h3>問題：" + question.q_question + "</h3><br>"; 
			segment += "<div><input type='radio' value='A' name='userAnswer' id='A' /><label for='A'>"+ "A &emsp; " + question.q_selectionA +"</label><br>";
			segment += "<input type='radio' value='B' name='userAnswer' id='B' /><label for='B'>"+ "B &emsp; " + question.q_selectionB +"</label><br>";
			segment += "<input type='radio' value='C' name='userAnswer' id='C' /><label for='C'>"+ "C &emsp; " + question.q_selectionC +"</label><br>";
			segment += "<input type='radio' value='D' name='userAnswer' id='D' /><label for='D'>"+ "D &emsp; " + question.q_selectionD +"</label></div><hr><br>";

	   }
			return segment;
	}
	


</script>


</head>
<body class="is-preload">

	<!-- Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>

<div align='center'>
<h2>線上測驗區</h2>



<!-- <hr> -->
<%-- <font color='red'>${successMessage}</font>&nbsp; --%>
<!-- <hr> -->
   

<div align='left'  id='dataArea'>
</div>

<div>
<button id='back' style="display: none">上一題</button>
<button id='next'>下一題</button>
&emsp;<button id='submit'>提交</button>
</div><br>
<!-- <br> -->
<%-- <br><a href="<c:url value='/question.controller/turnQuestionIndex'/> " >回前頁</a> --%>
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