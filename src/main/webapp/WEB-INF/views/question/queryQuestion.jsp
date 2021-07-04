<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<%-- <link  rel='stylesheet' href="<c:url value='/css/style.css'  />" /> --%>
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
	let segment = "<table border='1' style='width:1200px;'>";
	
	if (size == 0){
		segment += "<tr><th colspan='9'>查無資料</th><tr>";
	} else {
		segment += "<tr><th colspan='13'>共計" + size + "筆資料</th><tr>";
	    segment += "<tr><th>編輯</th><th>刪除</th><th>題目編號</th><th>課程分類</th><th>題目類型</th><th>問題</th><th>選項A</th><th>選項B</th><th>選項C</th><th>選項D</th><th>正解</th><th>題目照片</th><th>題目音檔</th></tr>";
	    
	    for(n = 0; n < questions.length ; n++){
		   	let question = questions[n];
	   		
// 			console.log("<td><input type='button'value='刪除'onclick=if(confirm('是否確定刪除編號：" + question.q_id + "'))location='<c:url value = '/question.controller/deleteQuestion/"+ question.q_id +"'/>' /></td>")


		   	let tmp1 = "<c:url value='/question.controller/modifyQuestion/'  />" + question.q_id;
	     	let tmp0 = "<a href='" + tmp1 + "' >" + "<img width='37' height='37' src='<c:url value='/images/question/edit.png' />'" + "</a>";
	     	
		   	let tmp3 = "<c:url value='/question.controller/queryQuestion/'  />" ;
// 	     	let tmp4 = "<a href='" + tmp3 + "'onclick=if(confirm('是否確定刪除編號：" + question.q_id + "'))location='<c:url value = '/question.controller/deleteQuestion/"+ question.q_id +"'/>' >" + "<img width='37' height='37' src='<c:url value='/images/question/delete.png' />'" + "</a>";
// 	     	井號:無作用連結
	     	let tmp4 = "<a href='#' onclick=if(confirm('是否確定刪除編號：" + question.q_id + "'))location='<c:url value = '/question.controller/deleteQuestion/"+ question.q_id +"'/>' >" + "<img width='37' height='37' src='<c:url value='/images/question/delete.png' />'" + "</a>";

	     	
	     	
			segment += "<tr>";
			segment += "<td>" + tmp0 + "</td>"; 	
			
// 			segment += "<td><input type='button'value='刪除'onclick=if(confirm('是否確定刪除編號：" + question.q_id + "'))location='<c:url value = '/question.controller/deleteQuestion/"+ question.q_id +"'/>' /></td>"
			segment += "<td>" + tmp4 + "</td>"; 	
			
			segment += "<td>" + question.q_id + "</td>"; 	
			segment += "<td>" + question.q_class + "</td>"; 	
			segment += "<td>" + question.q_type + "</td>"; 	
			segment += "<td>" + question.q_question + "</td>"; 	
			segment += "<td>" + question.q_selectionA + "</td>"; 	
			segment += "<td>" + question.q_selectionB + "</td>"; 	
			segment += "<td>" + question.q_selectionC + "</td>";
			segment += "<td>" + question.q_selectionD + "</td>"; 	
			segment += "<td>" + question.q_answer     + "</td>"; 	

			segment += "<td><img width='100' height='60' src='" + question.q_pictureString + "' ></td>"; 	
			segment += "<td><audio controls src='" + question.q_audioString + "' ></td>"; 	
			segment += "</tr>"; 	
	   }
	}
	segment += "</table>"; 
	return segment;
}
</script>
<meta charset="UTF-8">
<title>Check All Questions</title>
</head>
<body>
<div align='center'>
<h2>所有試題資料</h2>
<hr>
<font color='red'>${successMessage}</font>&nbsp;

<hr>


試題搜尋：<input id="questionName" type='text' placeholder="請輸入部分問題內容" />
<button id='query'>提交</button>


<div  id='dataArea'>
</div>
<a href="<c:url value='/question.controller/turnQuestionIndex'/> " >回前頁</a>
</div>
</body>
</html>