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

var u_id = "${loginBean.u_id}";
var userPicString = "${loginBean.pictureString}";

window.addEventListener('load', function(){
	
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
			segment += "<td style='vertical-align: middle;width:7%'>" + tmp0 + "</td>"; 	

			segment += "<td style='vertical-align: middle;width:7%'>" + question.q_id + "</td>"; 	
			segment += "<td style='vertical-align: middle;width:7%'>" + question.q_class + "</td>"; 	
			segment += "<td style='vertical-align: middle;width:7%'>" + question.q_type + "</td>"; 	
			segment += "<td style='vertical-align: middle;'>" + question.q_question + "</td>";
			
			segment += "<td style='vertical-align: middle;'><img  width='100' height='60' src='" + question.q_pictureString + "' ></td>"; 	
			segment += "<td style='vertical-align: middle;'><audio controls src='" + question.q_audioString + "' ></td>"; 	
			
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
<h2>所有試題資料</h2>
<hr>
<font color='red'>${successMessage}</font>&nbsp;
<hr>


<div style="text-align: center;">
<input type="text" id="questionName" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入部分問題內容">
<button id="query" style="display: inline;">搜尋</button>
<br>
<br>
</div>


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