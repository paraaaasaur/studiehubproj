<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<style>
	td {
		white-space:nowrap;
		overflow:hidden;
		text-overflow:ellipsis;
	}

	table {
		table-layout: fixed;
		word-wrap: break-word;
	}

	.search-bar {
		text-align: center;
	}

	.event-row {}
	.event-row__thumbnail {
		width: 70px;
		height: 60px;
	}
	.event-row__action {
		height: 50px;
		font-size: 10px;
		border-radius: 10px;
	}
</style>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>
<script type="application/json" id="bootstrap-data">
	{
		"adminId": "<c:out value="${adminId}" />",
		"successMessage": "<c:out value="${successMessage}" />"
	}
</script>
<script src="assets/js/utility/dom.js"></script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { adminId, successMessage } = bootstrapData;

	window.onload = function() {
		var logoutHref = document.getElementById('logoutHref');
		if (adminId) {
			logoutHref.style.visibility = "visible";
		}
	}

	let dataArea = null;
	let restname = null;
	let query = null;
	let queryall = null;

	window.addEventListener("load", function() {
		dataArea = document.getElementById("dataArea");
		restname = document.getElementById("restname");
		query = document.getElementById("query");
		queryall = document.getElementById("queryall");

		let xhr = new XMLHttpRequest();
		xhr.open("GET", "admin/events?approved=false&includeEntryforms=false", true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status === 200) {
				if(successMessage){
					alert(successMessage);
				}
				renderEventsTable(xhr.responseText);
			}
		};

		query.addEventListener("click", function() {
			let rname = restname.value;
			if (!rname) {
				alert('請輸入活動名稱,可輸入部分');
				return;
			}

			let xhr2 = new XMLHttpRequest();
			xhr2.open('GET', "admin/events?rname=" + rname + "&approved=false&includeEntryforms=false");
			xhr2.send();
			xhr2.onreadystatechange = function() {
				if (xhr2.readyState == 4 && xhr2.status == 200) {
					renderEventsTable(xhr2.responseText);
				}
			}
		});

		queryall.onclick = async function() {
			const json = await fetch('admin/events?approved=false&includeEntryforms=false')
					.then(res => res.text());

			renderEventsTable(json);
		}
	});

	function renderEventsTable(textobj) {
		const obj = JSON.parse(textobj);
		const events = obj.list;
		const size = events.length;

		const eventsTable = document.createElement("table");

		if (size == 0) {
			eventsTable.appendChild(createNoResultMessage());
		} else {
			eventsTable.appendChild(createResultSizeMessage(size));
			eventsTable.appendChild(createTableHeader());


			for (const event of events) {
				const eventRow = document.createElement("tr");

				eventRow.appendChild(createTextCell(event.a_uid, event.a_uid));
				eventRow.appendChild(createTextCell(event.uidname,event.uidname));
				eventRow.appendChild(createTextCell(event.a_type, event.a_type));
				eventRow.appendChild(createTextCell(event.a_name, event.a_name));
				eventRow.appendChild(createTextCell(event.a_startTime, event.a_startTime));
				eventRow.appendChild(createTextCell(event.a_endTime, event.a_endTime));
				eventRow.appendChild(createTextCell(event.a_address, event.a_address));
				eventRow.appendChild(createThumbnailCell(event.a_picturepath));
				eventRow.appendChild(createActionGroup(event));

				eventsTable.appendChild(eventRow);
			}
		}

		dataArea.replaceChildren(eventsTable);
	}
	function createNoResultMessage() {
		const row = document.createElement("tr");

		const msgEl = document.createElement("th");
		msgEl.colSpan = 1;
		msgEl.textContent = '查無資料';

		row.appendChild(msgEl);

		return row;
	}
	function createResultSizeMessage(size) {
		const row = document.createElement("tr");

		const msgEl = document.createElement("th");
		msgEl.textContent = '共計' + size + '筆資料';
		msgEl.colSpan = 8;

		row.appendChild(msgEl);

		return row;
	}
	function createTableHeader() {
		return htmlToFragment(
				"<tr>" +
					"<th>會員帳號</th>" +
					"<th>會員姓名</th>" +
					"<th>活動類型</th>" +
					"<th>活動名稱</th>" +
					"<th>活動開始時間</th>" +
					"<th>活動結束時間</th>" +
					"<th>活動地址</th>" +
					"<th>活動照片</th>" +
				"</tr>"
		);
	}
	function createTextCell(text, title = '') {
		const cell = document.createElement("td");
		cell.textContent = text;
		cell.title = title;

		return cell;
	}
	function createThumbnailCell(imgSrc) {
		const cell = document.createElement("td");

		const thumbnailEl = document.createElement("img");
		thumbnailEl.src = imgSrc;
		thumbnailEl.classList.add("event-row__thumbnail");

		cell.appendChild(thumbnailEl);

		return cell;
	}
	function createActionGroup(event) {
		const cells = document.createDocumentFragment();

		cells.append(
			createApprovalCell(event.a_aid),
			createDenialCell(event.a_aid, event.a_name),
			createReviewCell(event.a_aid)
		);

		return cells;
	}
	function createApprovalCell(a_aid) {
		const cell = document.createElement("td");

		const approvalButton = document.createElement("input");
		approvalButton.type = 'button';
		approvalButton.value = '驗證發布';
		approvalButton.classList.add("event-row__action");
		approvalButton.onclick = e => onclickApproval(e, a_aid);

		cell.appendChild(approvalButton);

		return cell;
	}
	function onclickApproval(e, a_aid) {
		e.preventDefault();
		window.location.href = 'verification/' + a_aid;
	}
	function createDenialCell(a_aid, a_name) {
		const cell = document.createElement("td");

		const denialButton = document.createElement("input");
		denialButton.type = 'button';
		denialButton.value = '駁回';
		denialButton.classList.add("event-row__action");
		denialButton.onclick = e => onclickDenial(e, a_aid, a_name);

		cell.appendChild(denialButton);

		return cell;
	}
	function onclickDenial(e, a_aid, a_name) {
		e.preventDefault();
		const cfm = confirm('是否確定駁回(' + a_name + ')');
		if (cfm) {
			window.location.href = 'deleteverification/' + a_aid;
		}
	}
	function createReviewCell(a_aid) {
		const cell = document.createElement("td");

		const reviewButton = document.createElement("input");
		reviewButton.type = 'button';
		reviewButton.value = '活動內容';
		reviewButton.classList.add("event-row__action");
		reviewButton.onclick = e => onclickReview(e, a_aid);

		cell.appendChild(reviewButton);

		return cell;
	}
	function onclickReview(e, a_aid) {
		e.preventDefault();
		window.location.href = 'Selecteventcontent/' + a_aid;
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/adminHeader.jsp"%>


				<h2 align='center'>活動驗證後台</h2>

				<search class="search-bar">
					<input id="restname" type="text" style="display: inline; width: 300px; "placeholder="請輸入活動關鍵字">
					<button id="query" style="height:50px;font-size:10px;border-radius:10px;" >搜尋</button>&nbsp;
					<button id="queryall" style="height:50px;font-size:10px;border-radius:10px;" >搜尋全部</button>
					<br><br>
				</search>

				<div id='dataArea'></div>


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