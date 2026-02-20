<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">

<title>Studie Hub</title>
<style>
.ellipsis {
overflow:hidden;
white-space: nowrap;
text-overflow: ellipsis;
}
.event-box {}
.event-box__thumbnail {
	height: 300px;
}
</style>
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "<c:out value="${loginBean.u_id}" />",
		"userPicString": "<c:out value="${loginBean.pictureString}" />"
	}
</script>
<script>
const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
const { u_id, userPicString } = bootstrapData;
let div1 = null;
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
	
	
	
	
	
	
	
	
	
	dataArea = document.getElementById("div1");
// 	restname = document.getElementById("restname");
// 	query = document.getElementById("query");
	//抓到 Id 叫 dataArea 能對這個地方做修改 或 對他做監聽事件
	let xhr = new XMLHttpRequest();
	xhr.open("GET", "guest/EventfindAll", true);
	//他會送出請求去/findAll 然後 controller 去接收 /findAll 執行方法
	//說明請求的內容 fales 就是同步 true 就是非同步 
	xhr.send();
	//真正送出請求
	xhr.onreadystatechange = function() {
		//當屬性發生變化的時候執行方法	
		if (xhr.readyState == 4 && xhr.status === 200) {
			            console.log(xhr.responseText);

			dataArea.replaceChildren(showData(xhr.responseText));
			//執行方法 將 jsoe字串  轉為 jsoe物件 
		}
	};

});


function showData(textobj) {
	const obj = JSON.parse(textobj);
	const events = obj.list;

	const eventsWrapper = document.createDocumentFragment();

		for (const event of events) {
			const eventBox = document.createElement("article");
			eventBox.classList.add('container');
			// eventBox.classList.add('event-box');

			eventBox.append(
				createLinkingThumbnail(event.a_aid, event.a_picturepath),
				createLinkingTitle(event.a_aid, event.a_name, event.expired),
				createRegistrationPeriod(event.a_registration_starttime, event.a_registration_endrttime),
				createEventSchedule(event.a_startTime, event.a_endTime),
				createLocation(event.a_address),
				createActionList(event)
			);

			eventsWrapper.appendChild(eventBox);
	}
	

	return eventsWrapper;
}
const DICT = {
	registrationPeriod: '報名時間',
	eventSchedule: '活動時間',
	moreInfo: '詳細資訊',
	locationDetail: '詳細地址'
};
function createLinkingThumbnail(a_aid, imgSrc) {
	const url = "Selecteventcontent/" + a_aid;

	const link = document.createElement("a");
	link.href = url;
	link.classList.add('image');

	const thumbnailEl = document.createElement("img");
	thumbnailEl.src = imgSrc;
	thumbnailEl.alt = '';
	thumbnailEl.classList.add('event-box__thumbnail');

	link.appendChild(thumbnailEl);

	return link;
}
function createLinkingTitle(a_aid, a_name, expired) {
	const url = "Selecteventcontent" + a_aid;

	const titleEl = document.createElement("h3");
	titleEl.classList.add('ellipsis');
	{
		const link = document.createElement("a");
		link.href = url;
		link.textContent = a_name;

		const expireText = (expired == '未過期')
				? document.createTextNode('')
				: document.createTextNode('(' + expired + ')');

		titleEl.append(link, expireText);
	}

	return titleEl;
}
function createRegistrationPeriod(from, to) {
	const registrationPeriodEl = document.createElement("p");
	registrationPeriodEl.classList.add('ellipsis');
	{
		const labelText = DICT.registrationPeriod + ":"

		const fromText = document.createTextNode(from);

		const toEl = document.createElement('span');
		toEl.textContent = '~' + to;

		registrationPeriodEl.append(labelText, fromText, toEl);
	}

	return registrationPeriodEl;
}
function createEventSchedule(from, to) {
	const eventScheduleEl = document.createElement("p");
	eventScheduleEl.classList.add('ellipsis');
	{
		const labelText = DICT.eventSchedule + ":"

		const fromText = document.createTextNode(from);

		const toEl = document.createElement('span');
		toEl.textContent = '~' + to;

		eventScheduleEl.append(labelText, fromText, toEl);
	}

	return eventScheduleEl;
}
function createLocation(location) {
	const locationEl = document.createElement("p");
	locationEl.classList.add('ellipsis');
	locationEl.textContent = location;

	return locationEl;
}
function createActionList(event) {
	const actionList = document.createElement("ul");
	actionList.classList.add('actions');

	actionList.append(
		createMoreInfoLink(event.a_aid),
		createLocationLink(event.a_address)
	);

	return actionList;
}
function createMoreInfoLink(a_aid) {
	const url = "Selecteventcontent/" + a_aid;

	const item = document.createElement("li");

	const link = document.createElement("a");
	link.textContent = DICT.moreInfo;
	link.href = url;
	link.classList.add('button');

	item.append(link);

	return item;
}
function createLocationLink(location) {
	const url = 'https://www.google.com/maps?q=' + location;

	const item = document.createElement("li");

	const locationLinkEl = document.createElement("a");
	locationLinkEl.classList.add('button');
	locationLinkEl.href = url;
	locationLinkEl.textContent = DICT.locationDetail;

	item.append(locationLinkEl);

	return item;
}
</script>

</head>

<body class="is-preload">

		<!-- Wrapper -->
			<div id="wrapper">

				<!-- Main -->
					<div id="main">
						<div class="inner">

							<!-- Header -->
							<!-- 這邊把header include進來 -->
								<%@include file="../universal/header.jsp" %>  

							<!-- Banner -->
<!-- 								以刪 -->

							<!-- Section -->
<!-- 								以刪 -->

							<!-- Section -->
								<section>
									<header class="major">
										<h2>活動頁面</h2>
									</header>
									<div class="posts" id="div1">

									
										
										
									</div>
								</section>

						</div>
					</div>

				<!-- Sidebar -->
				<!-- 這邊把side bar include進來 -->
				<%@include file="../universal/sidebar.jsp" %>  

			</div>

		<!-- Scripts -->
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>

	</body>
</html>