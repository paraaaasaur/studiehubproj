<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<style>
	.question-box {} /* <todo@1.1.0>: reserved for each question container (looping <tr>) */
	.question-box__action-link {
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
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<title>所有試題資料</title>
<script src="assets/js/utility/dom.js"></script>
<script type="application/json" id="bootstrap-data">
	{
		"adminId": "${fn:escapeXml(adminId)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { adminId } = bootstrapData;

	let dataArea = null;
	let questionName = null;
	let questionQuery = null;

	window.onload = function() {
		var logoutHref = document.getElementById('logoutHref');
		if(adminId) {
			logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
		}

		questionName = document.getElementById("questionName");
		questionQuery = document.getElementById("question-query");
		dataArea = document.getElementById("dataArea");

		let xhr = new XMLHttpRequest();
		xhr.open('GET', "question.controller/findAllQuestions", true);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200 ) {
				dataArea.replaceChildren(showData(xhr.responseText));
			}
		};
		xhr.send();

		questionQuery.addEventListener('click', function() {
			let qname = questionName.value;
			if (!qname) {
				alert('請輸入問題內容，可輸入部分內容');
				return;
			}

			let xhr2 = new XMLHttpRequest();
			xhr2.open('GET', "question.controller/queryByName?qname=" + qname);
			xhr2.send();
			xhr2.onreadystatechange=function() {
				if (xhr2.readyState == 4 && xhr2.status == 200) {
					dataArea.replaceChildren(showData(xhr2.responseText));
				}
			}
		});
	};

	function showData(textObj) {
		let obj = JSON.parse(textObj);
		let size = obj.size;
		let questions = obj.list;
		const container = document.createElement("table");

		if (size == 0) {
			const noRows = htmlToFragment("<tr><th colspan='1'>查無資料</th></tr>");
			container.appendChild(noRows);
			return container;
		} else {
			const dataSizeMessage = dataSizeRow(size);
			const header = htmlToFragment("<tr><th>編輯</th><th>&ensp;刪除</th><th>題目編號</th><th>課程分類</th><th>題目類型</th><th>問題</th><th>題目照片</th><th>題目音檔</th></tr>");

			container.append(dataSizeMessage, header);

			for (const question of questions) {
				const questionBox = document.createElement("tr");
				// questionBox.classList.add('question-box'); // <todo@1.1.0>

				questionBox.append(
						editIconCell(question.q_id),
						deleteIconCell(question.q_id),
						tdTextWide(question.q_id),
						tdTextWide(question.q_class),
						tdTextWide(question.q_type),
						tdText(question.q_question),
						imgCell(question.q_pictureString),
						audioCell(question.q_audioString)
				)

				container.appendChild(questionBox);
		   }
		}

		return container;
	}
	function dataSizeRow(dataSize) {
		const tr = document.createElement("tr");

		const th = document.createElement("th");
		th.textContent = "共計" + dataSize + "筆資料";
		th.colSpan = 8;

		tr.appendChild(th);

		return tr;
	}
	function editIconCell(q_id) {
		const td = document.createElement("td");
		td.classList.add('question-box__item--wide');

		const a = document.createElement("a");
		a.href = 'question.controller/modifyQuestion/' + q_id;
		{
			const img = document.createElement("img");
			img.src = 'images/question/edit.png';
			img.classList.add('question-box__action-link');

			a.appendChild(img);
		}

		td.appendChild(a);

		return td;
	}
	function deleteIconCell(q_id) {
		const td = document.createElement("td");
		td.classList.add('question-box__item--wide');

		const a = document.createElement("a");
		a.href = 'javascript:void(0);';
		a.onclick = (e) => onClickDelete(e, q_id);
		// console.log(a.onclick, q_id);
		{
			const img = document.createElement("img");
			img.src = 'images/question/delete.png';
			img.classList.add('question-box__action-link');

			a.appendChild(img);
		}

		td.appendChild(a);

		return td;
	}
	function onClickDelete(e, q_id) {
		e.preventDefault();

		const cfm = confirm('是否確定刪除編號：' + q_id);
		if (cfm) {
			window.location.href = 'question.controller/deleteQuestion/' + q_id;
		}
	}
	function tdTextWide(text) {
		const td = document.createElement("td");
		td.textContent = text;
		td.classList.add('question-box__item--wide');

		return td;
	}
	function tdText(text) {
		const td = document.createElement("td");
		td.textContent = text;
		td.classList.add('question-box__item');

		return td;
	}
	function imgCell(q_pictureString) {
		const td = document.createElement("td");

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
				<%@include file="../universal/adminHeader.jsp"%>


				<div align='center'>
					<h2>所有試題資料</h2>
					<hr>
					<font color='red'>${fn:escapeXml(successMessage)}</font>&nbsp;
					<hr>

					<div style="text-align: center;">
						<input type="text" id="questionName" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入部分問題內容">
						<button id="question-query" style="display: inline;">搜尋</button>
						<br><br>
					</div>

					<div id='dataArea'></div>
				</div>


			</div>
		</div>
		<%@include file="../universal/adminSidebar.jsp"%>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	
</body>
</html>