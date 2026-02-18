<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<style type="text/css">
 td {
     white-space:nowrap; 
     overflow:hidden; 
     text-overflow:ellipsis; 
      } 
 table{
       table-layout:fixed;
       word-wrap:break-word;}

 .event-row {}
 .event-row__thumbnail {
	 width: 70px;
	 height: 60px;
 }
 .event-row__capacity {
	 text-align: center;
	 vertical-align: middle;
 }
 .event-row__current-attendance {
	 text-align: center;
	 vertical-align: middle;
 }
 .event-row__action {
	 width: 60px;
	 height: 50px;
	 font-size: 1px;
	 border-radius: 10px;
 }
 .event-row__edit-cell {
	 width: 10%;
 }

</style>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>

</head>




<script type="application/json" id="bootstrap-data">
	{
		"u_id": "<c:out value="${loginBean.u_id}" />",
		"userPicString": "<c:out value="${loginBean.pictureString}" />",
		"successMessage": "<c:out value="${successMessage}" />"
	}
</script>
<script src="assets/js/utility/dom.js"></script>
<script>
const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
const { u_id, userPicString, successMessage } = bootstrapData;
	let dataArea = null; //變數放在外面 空值(原始狀態)  放在方法裡 別的方法要用它會找不到 不要讓他被綁住
	let restname = null;
	let query = null;
	let queryall = null;

	window.addEventListener("load", function() {
		//window.addEvenListener 網頁監聽器
		//當瀏覽器從第一行到最後一行載完畢後才執行 function()


		var logout = document.getElementById("logout");
			logout.onclick = function() {
				var xhr1 = new XMLHttpRequest();
				xhr.open("GET", "logout.controller", true);
				xhr.send();
				xhr.onreadystatechange = function() {
					if (xhr1.readyState == 4 && xhr1.status == 200) {
						var result = JSON.parse(xhr1.responseText);
						if (result.success) {
							alert(result.success);
							top.location = '';
						} else if (result.fail) {
							alert(result.fail);
							top.location = '';
						}
					}
				}
			}

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
		    	userId.textContent = u_id;
		    	loginEvent.style.display = "block";
		    	loginEvent1.style.display = "block";
		    	loginALLEvent1.style.display = "block";
		    }
			// 有登入才會顯示購物車sidebar
			let cartHref = document.querySelector('#cartHref');
			cartHref.hidden = (u_id)? false : true;
			cartHref.style.visibility = (u_id)? 'visible' : 'hidden';
		//universal






















		dataArea = document.getElementById("dataArea");
		restname = document.getElementById("restname");
		query = document.getElementById("query");
		queryall = document.getElementById("queryall");

		//抓到 Id 叫 dataArea 能對這個地方做修改 或 對他做監聽事件
		let xhr = new XMLHttpRequest();
		xhr.open("GET", "Eventfindbyuid", true);
		//他會送出請求去/findAll 然後 controller 去接收 /findAll 執行方法
		//說明請求的內容 fales 就是同步 true 就是非同步
		xhr.send();
		//真正送出請求
		xhr.onreadystatechange = function() {
			//當屬性發生變化的時候執行方法
			if (xhr.readyState == 4 && xhr.status === 200) {
				    if(successMessage) {
						alert(successMessage);
				    }
				renderEventsTable(xhr.responseText);
				//執行方法 將 jsoe字串  轉為 jsoe物件
			}
		};

		query.addEventListener("click", function() {
			//當id= query 的DOM物件被按下後 執行此方法
			let rname = restname.value;
			//取得 id=rname 的DOM物件的值
			if (!rname) {
				alert('請輸入活動名稱,可輸入部分')
				return;
			}

			let xhr2 = new XMLHttpRequest();
			xhr2.open('GET', "queryEventByName?rname=" + rname);
			xhr2.send();
			xhr2.onreadystatechange = function() {
				if (xhr2.readyState == 4 && xhr2.status == 200) {



					renderEventsTable(xhr2.responseText);

				}
			}
		});

		queryall.addEventListener("click", function() {
					//當id= query 的DOM物件被按下後 執行此方法

					let xhr3 = new XMLHttpRequest();
					xhr3.open('GET', "Eventfindbyuid", true);
					xhr3.send();
					xhr3.onreadystatechange = function() {
						if (xhr3.readyState == 4 && xhr3.status == 200) {



							renderEventsTable(xhr3.responseText);

						}
					}
				});

	});

	function renderEventsTable(textobj) {
		let obj = JSON.parse(textobj)
		let size = obj.size;
		let events = obj.list

		const eventsTable = document.createElement("table");

		if (size == 0) {
			eventsTable.appendChild(createNoResultMessage());
		} else {
			eventsTable.appendChild(createResultSizeMessage(size));
			eventsTable.appendChild(createTableHeader());

			for (const event of events) {
				const eventRow = document.createElement("tr");
				// eventRow.classList.add("event-row");

				eventRow.append(
					createTextCell(event.a_type, event.a_type),
					createTextCell(event.a_name, event.a_name),
					createRegistrationPeriodCell(event.a_registration_starttime, event.a_registration_endrttime),
					createEventScheduleCell(event.a_startTime, event.a_endTime),
					createTextCell(event.a_address, event.a_address),
					createThumbnailCell(event.a_picturepath),
					createCapacityCell(event.applicants),
					createCurrentAttendanceCell(event.havesignedup),
					createActionGroup(event)
				);

				eventsTable.appendChild(eventRow);
			}
		}

		dataArea.replaceChildren(eventsTable);
	}
function createNoResultMessage() {
	return htmlToFragment('<tr><th colspan="1">查無資料</th><tr>');
}
function createResultSizeMessage(size) {
	const row = document.createElement("tr");

	const msgEl = document.createElement("th");
	msgEl.textContent = '共計' + size + '筆資料';

	row.appendChild(msgEl);

	return row;
}
function createTableHeader() {
	return htmlToFragment(
			'<tr>' +
				'<th>活動類型</th>' +
				'<th>活動名稱</th>' +
				'<th>報名時間</th>' +
				'<th>活動時間</th>' +
				'<th>活動地址</th>' +
				'<th>活動圖片</th>' +
				'<th>上限人數</th>' +
				'<th>目前人數</th>' +
			'</tr>');
}
function createTextCell(text, title = '') {
	const cell = document.createElement("td");
	cell.textContent = text;
	cell.title = title;

	return cell;
}
function createRegistrationPeriodCell(from, to) {
	const cell = document.createElement("td");
	cell.textContent = from + '至' + to;
	cell.title = from + '至' + to;

	return cell;
}
function createEventScheduleCell(from, to) {
	const cell = document.createElement("td");
	cell.textContent = from + '至' + to;
	cell.title = from + '至' + to;

	return cell;
}
function createThumbnailCell(imgSrc) {
	const cell = document.createElement("td");

	const thumbnailEl = document.createElement("img");
	thumbnailEl.src = imgSrc;
	thumbnailEl.classList.add('event-row__thumbnail');

	cell.appendChild(thumbnailEl);

	return cell;
}
function createCapacityCell(capacity) {
	const capacityCell = document.createElement("td");
	capacityCell.textContent = capacity;
	capacityCell.classList.add("event-row__capacity");

	return capacityCell;
}
function createCurrentAttendanceCell(currentAttendance) {
	const currentAttendanceCell = document.createElement("td");
	currentAttendanceCell.textContent = currentAttendance;
	currentAttendanceCell.classList.add("event-row__current-attendance");

	return currentAttendanceCell;
}
function createActionGroup(event) {
	const actionGroup = document.createDocumentFragment();

	actionGroup.appendChild(createEditCell(event.a_aid));
	actionGroup.appendChild(createDeleteCell(event.a_aid, event.a_name));
	actionGroup.appendChild(createViewRegistrationCell(event.a_aid));

	return actionGroup;
}
function createEditCell(a_aid) {
	const cell = document.createElement('td');
	cell.classList.add('event-row__edit-cell');

	const editButton = document.createElement('input');
	editButton.type = 'button';
	editButton.value = '修改';
	editButton.classList.add('event-row__action');
	editButton.onclick = e => onclickEdit(e, a_aid); // np

	cell.appendChild(editButton);

	return cell;
}
function onclickEdit(e, a_aid) {
	e.preventDefault();
	window.location.href = 'updateEvent/' + a_aid;
}
function createDeleteCell(a_aid, a_name) {
	const cell = document.createElement("td");

	const delButton = document.createElement('input');
	delButton.type = 'button';
	delButton.value = '下架';
	delButton.classList.add('event-row__action');
	delButton.onclick = e => onclickDelete(e, a_aid, a_name); // np

	cell.appendChild(delButton);

	return cell;
}
function onclickDelete(e, a_aid, a_name) {
	e.preventDefault();
	const cfm = confirm('是否確定下架('+ a_name + ')');
	if (cfm) {
		window.location.href = 'deleteEvent/' + a_aid;
	}
}
function createViewRegistrationCell(a_aid) {
	const cell = document.createElement('td');

	const viewRegistrationButton = document.createElement('input');
	viewRegistrationButton.type = 'button';
	viewRegistrationButton.value = '查詢報名';
	viewRegistrationButton.classList.add('event-row__action');
	viewRegistrationButton.onclick = e => onclickViewRegistration(e, a_aid);

	cell.appendChild(viewRegistrationButton);

	return cell;
}
function onclickViewRegistration(e, a_aid) {
	e.preventDefault();
	window.location.href = 'signupEvent/' + a_aid;
}
</script>

<body class="is-preload">
	<!-- Wrapper -->
	<div id="wrapper">
		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>
				<h2 align='center'>活動內容後台</h2>
								<div align="center">

					<font color='red'><c:out value="${successMessage}" /></font>
					<!--   修改成功的重定向帶值 -->
				<div style="text-align: center;">
					<input type="text" id="restname" style="display: inline; width: 300px; "placeholder="請輸入活動關鍵字">
					<button id="query" style="width:60px;height:50px;font-size:1px;border-radius:10px;" >搜尋</button>&nbsp<button id="queryall"  style="width:60px;height:50px;font-size:1px;border-radius:10px;" >搜尋全部</button>
					<br>
					<br>
				</div>

				</div>



				<div1 id='dataArea'>
<!-- 				插入表單位置 -->
				</div1>
<%-- 				<a href="<c:url value='/'/> ">回前頁</a> --%>
			</div>
		</div>

		<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="../universal/sidebar.jsp"%>

	</div>

	<!-- Scripts -->
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>






