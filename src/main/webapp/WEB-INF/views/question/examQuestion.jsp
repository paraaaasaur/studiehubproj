<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
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
		counter += 1;
		dataArea.innerHTML = showFirstData(xhr.responseText);
	})
	
	back.addEventListener('click', function(){
		counter -= 1;
		dataArea.innerHTML = showFirstData(xhr.responseText);
	})
	
	
})

 function showFirstData(textObj){
	
	obj = JSON.parse(textObj);
	size = obj.size;
	let questions = obj.list;
	let segment = "";
	
	if (size == 0){
		segment += "<div>很抱歉，目前系統無相關試題</div>";
	} else {
		segment += "<div>測驗共" + size + "題</div><br>";
	    
		   	let question = questions[counter];
	   		let number = counter+1;
	     	
		   	segment += "<div>第" + number + "題</div>";
			segment += "<div><img width='400' height='260' src='" + question.q_pictureString + "' ></div>"; 	
			segment += "<div><audio controls src='" + question.q_audioString + "' ></div>"; 	
	
			segment += "<div>問題:" + question.q_question + "</div><br>"; 
			segment += "<div><input type='radio' value='A' name='userAnswer' id='A' />"+ "A " + question.q_selectionA +"</div>"
			segment += "<div><input type='radio' value='B' name='userAnswer' id='B' />"+ "B " + question.q_selectionB +"</div>"
			segment += "<div><input type='radio' value='C' name='userAnswer' id='C' />"+ "C " + question.q_selectionC +"</div>"
			segment += "<div><input type='radio' value='D' name='userAnswer' id='D' />"+ "D " + question.q_selectionD +"</div><br><hr><br>"

			
	   }
			return segment;
	}
	




</script>
<meta charset="UTF-8">
<title>線上測驗區</title>
</head>
<body>
<div align='center'>
<h2>線上測驗區</h2>

<!-- <hr> -->
<%-- <font color='red'>${successMessage}</font>&nbsp; --%>
<!-- <hr> -->

<div align='left'  id='dataArea'>
</div>

<button id='back'>上一題</button>
<button id='next'>下一題</button>
<button id='submit'>提交</button>


<br><br><a href="<c:url value='/question.controller/turnQuestionIndex'/> " >回前頁</a>
</div>
</body>
</html>