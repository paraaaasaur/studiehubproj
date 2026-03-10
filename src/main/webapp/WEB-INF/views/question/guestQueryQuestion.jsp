<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<title>所有試題資料</title>
<style>
	.question-box {} /* <todo@1.1.0>: reserved for each question container (looping <tr>) */

	.question-box__check-detail-link {
		width: 37px;
		height: 37px;
	}

	.question-box__item {
		vertical-align: middle;
	}

	.question-box__item--wide {
		vertical-align: middle;
		width: 7%;
	}

	.question-box__image {
		width: 100px;
		height: 60px;
	}
</style>
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { u_id, userPicString } = bootstrapData;

	let dataArea = null;
	let questionName = null;
	let query = null;

	window.addEventListener('load', function(){

		//universal
		//如果有登入，隱藏登入標籤
		var loginHref = document.getElementById('loginHref');
		var signupHref = document.getElementById('signupHref');
		document.getElementById('user-info-actions').toggleAttribute('hidden', false);
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
			userId.textContent = u_id;
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
		xhr.open('GET', "question.controller/findAllQuestions", true);
		xhr.onreadystatechange = function(){
			if (xhr.readyState == 4 && xhr.status == 200 ) {
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
			xhr2.open('GET', "question.controller/queryByName?qname=" + qname);
			xhr2.send();
			xhr2.onreadystatechange = function() {
				if (xhr2.readyState == 4 && xhr2.status == 200) {
					dataArea.innerHTML = showData(xhr2.responseText);
				}
			}
		});
	})

	function showData(textObj) {
		let obj = JSON.parse(textObj);
		let size = obj.size;
		let questions = obj.list;
		let segment = "<table>";

		if (size == 0) {
			segment += "<tr><th colspan='8'>查無資料</th><tr>";
		} else {
			segment += dataSizeRow(size).outerHTML;
			segment += "<tr><th>查看試題</th><th>題目編號</th><th>課程分類</th><th>題目類型</th><th>問題</th><th>題目照片</th><th>題目音檔</th></tr>";

			for (const question of questions) {
				segment += "<tr>";
				segment += checkIconCell(question.q_id).outerHTML;
				segment += tdTextWide(question.q_id).outerHTML;
				segment += tdTextWide(question.q_class).outerHTML;
				segment += tdTextWide(question.q_type).outerHTML;
				segment += tdText(question.q_question).outerHTML;
				segment += imgCell(question.q_pictureString).outerHTML;
				segment += audioCell(question.q_audioString).outerHTML;
				segment += "</tr>";
		   }
		}
		segment += "</table>";
		return segment;
	}

	function dataSizeRow(dataSize) {
		const tr = document.createElement("tr");

		const th = document.createElement("th");
		th.textContent = "共計" + dataSize + "筆資料";
		th.colSpan = 8;

		tr.appendChild(th);

		return tr;
	}
	function checkIconCell(q_id) {
		const td = document.createElement("td");
		td.classList.add('question-box__item');

		const a = document.createElement("a");
		a.href = 'question.controller/guestOneQuestion/' + q_id;
		{
			const img = document.createElement("img");
			img.src = 'images/question/check.png';
			img.classList.add('question-box__check-detail-link');

			a.appendChild(img);
		}

		td.appendChild(a);

		return td;
	}
	function tdTextWide(text) {
		const td = document.createElement("td");
		td.textContent = text;
		td.classList.add('question-box__item--wide');

		return td;
	}
	function tdText(text) {
		const td = document.createElement("td");
		td.classList.add('question-box__item');
		td.textContent = text;

		return td;
	}
	function imgCell(q_pictureString) {
		const td = document.createElement("td");
		td.classList.add('question-box__item');

		const img = document.createElement("img");
		img.src = q_pictureString;
		img.classList.add('question-box__image');

		td.appendChild(img);

		return td;
	}
	function audioCell(q_audioString) {
	const td = document.createElement("td");
	td.classList.add('question-box__item');

	const audio = document.createElement("audio");
	audio.toggleAttribute('controls', true);
	audio.src = q_audioString;

	td.appendChild(audio);

	return td;
}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>


				<div align='center'>
					<h2>所有試題資料</h2>
					<hr>
					<font color='red'>${fn:escapeXml(successMessage)}</font>&nbsp
					<hr>

					<div style="text-align: center;">
						<input type="text" id="questionName" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入部分問題內容">
						<button id="query" style="display: inline;">搜尋</button>
						<br>
						<br>
					</div>

					<div id='dataArea'></div>
				</div>


			</div>
		</div>
		<%@include file="../universal/sidebar.jsp"%>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	
</body>
</html>