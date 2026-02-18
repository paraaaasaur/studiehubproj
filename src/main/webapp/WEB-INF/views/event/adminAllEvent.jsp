<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<style type="text/css">
 td {white-space:nowrap;overflow:hidden;text-overflow: ellipsis;}
 table{table-layout:fixed;word-wrap:break-word;}
</style>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>

</head>



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

	// 踢除非管理員
	if(!adminId){
	alert('您不具有管理者權限，請登入後再試。');
	top.location = "gotoAdminIndex.controller";
}




window.onload = function(){
// console.log(adminId);

	//如果有登入，隱藏登入標籤
	var loginHref = document.getElementById('loginHref');
	var logoutHref = document.getElementById('logoutHref');
	var userId = document.getElementById('userId');
	var userPic = document.getElementById('userPic');
	if(adminId){
		loginHref.hidden = true;
		logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
	}

}
	let dataArea = null; //變數放在外面 空值(原始狀態)  放在方法裡 別的方法要用它會找不到 不要讓他被綁住
	let restname = null;
	let query = null;


	window.addEventListener("load", function() {
		//window.addEvenListener 網頁監聽器
		//當瀏覽器從第一行到最後一行載完畢後才執行 function()


		dataArea = document.getElementById("dataArea");
		restname = document.getElementById("restname");
		query = document.getElementById("query");
		queryall = document.getElementById("queryall");

		//抓到 Id 叫 dataArea 能對這個地方做修改 或 對他做監聽事件
		let xhr = new XMLHttpRequest();
		xhr.open("GET", "EventfindAll", true);
		//他會送出請求去/findAll 然後 controller 去接收 /findAll 執行方法
		//說明請求的內容 fales 就是同步 true 就是非同步
		xhr.send();
		//真正送出請求
		xhr.onreadystatechange = function() {
			//當屬性發生變化的時候執行方法
			if (xhr.readyState == 4 && xhr.status === 200) {
                 if(successMessage){
                	 alert(successMessage);
                 }
				dataArea.replaceChildren(showData(xhr.responseText));
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

					console.log(xhr2.responseText);

					dataArea.replaceChildren(showData(xhr2.responseText));

				}
			}
		});

		queryall.addEventListener("click", function() {
			//當id= query 的DOM物件被按下後 執行此方法

			let xhr3 = new XMLHttpRequest();
			xhr3.open('GET', "EventfindAll", true);
			xhr3.send();
			xhr3.onreadystatechange = function() {
				if (xhr3.readyState == 4 && xhr3.status == 200) {



					dataArea.replaceChildren(showData(xhr3.responseText));

				}
			}
		});

	});

	function showData(textobj) {
		// 	改成map回傳 obj裡面變成 物件 有size 跟 list 陣列物件
		// 	let places = JSON.parse(textData);
		// 	將 jsoe字串  轉為 jsoe物件
		let obj = JSON.parse(textobj)
		let size = obj.size;
		//  分別把物件裡的 size 跟 list 拆開
		let events = obj.list

		const container = document.createElement("table");

		if (size == 0) {
			const noRows = htmlToFragment("<tr><th colspan='1'>'查無資料'</th><tr>");
			container.appendChild(noRows);
		} else {
			container.append(dataSizeMessage(size), header());
			for (n = 0; n < events.length; n++) {
				let event = events[n];

				const eventBox = document.createElement("tr");

				eventBox.append(
					tdText(event.a_uid, event.a_uid),
					tdText(event.a_type, event.a_type),
					tdText(event.a_name, event.a_name),
					registrationPeriodCell(event.a_registration_starttime, event.a_registration_endrttime),
					eventPeriodCell(event.a_startTime, event.a_endTime),
					tdText(event.a_address, event.a_address),
					imageCell(event.a_picturepath),
					tdText(event.expired),
					deleteLinkCell(event.a_aid, event.a_name)
				);

				container.appendChild(eventBox);
			}
		}

		return container;
	}
function dataSizeMessage(size) {
	const tr = document.createElement('tr');

	const th = document.createElement('th');
	th.colSpan = 8;
	th.textContent = "共計" + size + "筆資料";

	tr.appendChild(th);

	return tr;
}
function header() {
	return htmlToFragment(
			"<tr>" +
				"<th>會員帳號</th>" +
				"<th>活動類型</th>" +
				"<th>活動名稱</th>" +
				"<th>報名時間</th>" +
				"<th>活動時間</th>" +
				"<th>活動地址</th>" +
				"<th>活動照片</th>" +
				"<th>活動過期</th>" +
			"</tr>"
	);
}
function tdText(text, title = '') {
	const td = document.createElement("td");
	td.textContent = text;
	if (title) {
		td.title = title;
	}

	return td;
}
function registrationPeriodCell(from, to) {
	const td = document.createElement("td");
	td.textContent = from + '至' + to;
	td.title = from + '至' + to;

	return td;
}
function eventPeriodCell(from, to) {
	const td = document.createElement("td");
	td.textContent = from + '至' + to;
	td.title = from + '至' + to;

	return td;
}
function imageCell(a_picturepath) {
	const td = document.createElement("td");

	const img = document.createElement("img");
	img.style.width = "100px";
	img.style.height = "60px";
	img.src = a_picturepath;

	td.appendChild(img);

	return td;
}
function deleteLinkCell(a_aid, a_name) {
	const td = document.createElement("td");

	const input = document.createElement("input");
	input.type = 'button';
	input.value = '強制下架';
	input.onclick = e => onclickDelete(e, a_aid, a_name);
	input.style.width = '60px';
	input.style.height = '50px';
	input.style.fontSize = '1px';
	input.style.borderRadius = '10px';

	td.appendChild(input);

	return td;
}
function onclickDelete(e, a_aid, a_name) {
	e.preventDefault();

	const cfm = confirm('是否確定下架('+ a_name + ')');
	if(cfm) {
		window.location.href = 'deleteadminEvent/' + a_aid;
	}
}
</script>

<body class="is-preload">
	<!-- Wrapper -->
	<div id="wrapper">
		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/adminHeader.jsp"%>
				<h2 align='center'>管理者活動內容後台</h2>

				<div align="center">

					<font color='red'>${successMessage}</font>
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
		<%@include file="../universal/adminSidebar.jsp"%>

	</div>

	<!-- Scripts -->
	<script
		src="assets/js/jquery.min.js"></script>
	<script
		src="assets/js/browser.min.js"></script>
	<script
		src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>






