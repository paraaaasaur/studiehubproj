<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <base href="<c:out value="${pageContext.request.contextPath}/" />">
        <title>活動內容</title>
        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="startbootstrap/assets/favicon.ico" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="startbootstrap/css/styles.css" rel="stylesheet" />
        <link rel='stylesheet' href="assets/css/main.css">

        <script src="https://cdn.bootcss.com/limonte-sweetalert2/7.20.5/sweetalert2.all.min.js"></script>
        <script src="assets/js/purify.js/"></script>
    <style>
.father{
    width: 300px;
    height: 300px;
    overflow: hidden;
    /* position: relative;*/
}
.father img{
    display: block;
    width: 300px;
    /* 如果需要图片居中的话
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    */
}
.event-content__thumbnail {
    width: 320px;
    height: 240px;
}
.event-content__description {
    white-space: pre;
}
</style>
        <script type="application/json" id="bootstrap-data">
            {
                "u_id": "<c:out value="${loginBean.u_id}" />",
                "userPicString": "<c:out value="${loginBean.pictureString}" />",
                "a_aid": "<c:out value="${eventcontent.a_aid}" />"
            }
        </script>
    <script>
    const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
    const { u_id, userPicString, a_aid } = bootstrapData;
window.addEventListener("load", function() {




		var logout = document.getElementById("logout");
			logout.onclick = function() {
				var xhr1 = new XMLHttpRequest();
				xhr.open("GET", "logout.controller'", true);
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
		    	userId.innerHTML = u_id;
		    	loginEvent.style.display = "block";
		    	loginEvent1.style.display = "block";
		    	loginALLEvent1.style.display = "block";
		    }
			// 有登入才會顯示購物車sidebar
			let cartHref = document.querySelector('#cartHref');
			cartHref.hidden = (u_id)? false : true;
			cartHref.style.visibility = (u_id)? 'visible' : 'hidden';
		//universal






    	const dataArea = document.getElementById("div1");
    	const Signup = document.getElementById("Signup");

    	let xhr = new XMLHttpRequest();
    	xhr.open("GET", "eventcontentjson/"+a_aid, true);

    	xhr.send();
    	xhr.onreadystatechange = function() {

    		if (xhr.readyState == 4 && xhr.status === 200) {
//     			            console.log(xhr.responseText);
//     			            console.log(u_id);
    			dataArea.replaceChildren(showData(xhr.responseText));
    			//執行方法 將 jsoe字串  轉為 jsoe物件
    		}
    	};


    	Signup.addEventListener("click", function() {
			//當id= query 的DOM物件被按下後 執行此方法
// 			let rname = restname.value;
			//取得 id=rname 的DOM物件的值
// 			if (!rname) {
// 				alert('請輸入活動名稱,可輸入部分')
// 				return;
// 			}

			let xhr2 = new XMLHttpRequest();
			xhr2.open("GET", "signupclick/"+a_aid, true);
			xhr2.send();
			xhr2.onreadystatechange = function() {
				if (xhr2.readyState == 4 && xhr2.status == 200) {

// 					console.log(u_id);

					result = JSON.parse(xhr2.responseText);

					if(result.succes){
						console.log(result.succes);
						swal(result.succes);
					}else if(result.fail){
						swal(result.fail);
					}else if(result.Time){
						swal(result.Time);
					}else if(result.Exceed){
						swal(result.Exceed);
					}
// 					dataArea.innerHTML = showData(xhr2.responseText);

				}
			}
		});


    });




function showData(textobj) {
	const obj = JSON.parse(textobj);


    const eventContainer = document.createElement("article");

    const header = document.createElement("header");
    header.classList.add('mb-4');

    header.append(
        createTitle(obj.a_name, obj.expired),
        createPublisher(obj.uidname),
        createCapacityInfo(obj.applicants),
        createCurrentAttendance(obj.havesignedup),
        createHashtag(obj.a_type),
    );

    eventContainer.append(
        header,
        createBr(),
        createRegistrationPeriod(obj.a_registration_starttime, obj.a_registration_endrttime),
        createBr(),
        createBr(),
        createEventSchedule(obj.a_startTime, obj.a_endTime),
        createBr(),
        createBr(),
        createLocation(obj.a_address),
        createBr(),
        createBr(),
        createThumbnail(obj.a_picturepath),
        createDescriptionSection(obj.comment)
    );

	return eventContainer;
}
const DICT = {
    publisher: '發布者',
    createdAt: '建立時間',
    capacity: '報名人數上限',
    currentAttendance: '已報名人數',
    registrationPeriod: '報名活動時間',
    eventSchedule: '活動開始時間',
    location: '活動地點'
};
function createTitle(title, expiredMsg) {
    // if(obj.expired=="未過期"){
    //
    //     segment += "<h2 class='fw-bolder mb-1'>"+obj.a_name+"</h1>";
    // }else{
    //
    //     segment += "<h2 class='fw-bolder mb-1'>"+obj.a_name+"("+obj.expired+")"+ "</h1>";
    // }

    const titleEl = document.createElement("h2");
    titleEl.textContent = title + (expiredMsg != '未過期'?
        '(' + expiredMsg + ')'
        : '');
    titleEl.classList.add("fw-bolder", 'mb-1');

    return titleEl;
}
function createPublisher(publisher) {
    const publisherEl = document.createElement("div");
    publisherEl.textContent = DICT.publisher + ' :' + publisher;
    publisherEl.classList.add('text-muted', 'fst-italic', 'mb-2');

    return publisherEl;
}
function createCreatedAt(createdAt) {
    const createdAtEl = document.createElement("div");
    createdAtEl.textContent = DICT.createdAt + ' :' + createdAt;
    createdAtEl.classList.add('text-muted', 'fst-italic', 'mb-2');

    return createdAtEl;
}
function createCapacityInfo(capacity) {
    const capacityInfoEl = document.createElement("div");
    capacityInfoEl.textContent = DICT.capacity + ' :' + capacity;
    capacityInfoEl.classList.add('text-muted', 'fst-italic', 'mb-2');

    return capacityInfoEl;
}
function createCurrentAttendance(currentAttendance) {
    const currentAttendanceEl = document.createElement("div");
    currentAttendanceEl.textContent = DICT.currentAttendance + ' :' + currentAttendance;
    currentAttendanceEl.classList.add('text-muted', 'fst-italic', 'mb-2');

    return currentAttendanceEl;
}
function createHashtag(category) {
    const link = document.createElement("a");
    link.href = 'javascript:void(0);';
    link.textContent = category;
    link.classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light')

    return link;
}
function createRegistrationPeriod(from, to) {
    const registrationPeriodEl = document.createElement("span");
    registrationPeriodEl.style.fontSize = '18px';
    {
        const labelText = document.createTextNode(DICT.registrationPeriod + ' :');

        const fromText = document.createTextNode(from);

        const separatorEl = document.createElement('span');
        separatorEl.textContent = ' ~ ';

        const toText = document.createTextNode(to);

        registrationPeriodEl.append(labelText, fromText, separatorEl, toText);
    }

    return registrationPeriodEl;
}
function createEventSchedule(from, to) {
    const eventScheduleEl = document.createElement("span");
    eventScheduleEl.style.fontSize = '18px';
    {
        const labelText = document.createTextNode(DICT.eventSchedule + ' :');

        const fromText = document.createTextNode(from);

        const separatorEl = document.createElement('span');
        separatorEl.textContent = ' ~ ';

        const toText = document.createTextNode(to);

        eventScheduleEl.append(labelText, fromText, separatorEl, toText);
    }

    return eventScheduleEl;
}
function createLocation(location) {
    const locationEl = document.createElement("span");
    locationEl.textContent = DICT.location + ' :' + location;
    locationEl.style.fontSize = '18px';

    return locationEl;
}
function createThumbnail(imgSrc) {
    const thumbnailEl = document.createElement("figure");
    thumbnailEl.classList.add('mb-4');

    const img = document.createElement("img");
    img.src = imgSrc;
    img.classList.add('event-content__thumbnail');

    thumbnailEl.appendChild(img);

    return thumbnailEl;
}
function createDescriptionSection(description) {
    const section = document.createElement("section");
    section.classList.add('mb-5');

    const descEl = document.createElement("p");
    descEl.classList.add('event-content__description', 'fs-5', 'mb-4');
    descEl.textContent = splitDescByBreakTokens(description);

    section.appendChild(descEl);


    return descEl;
}
function splitDescByBreakTokens(description) {
    return description
        .replace(/[?？]+/g, '$&\n')
        .replace(/[,，]+/g, '，\n')
        .replace(/[!！]+/g, '$&\n')
        .replace(/。+/g, '$&\n');
}
function createBr() {
    return document.createElement('br');
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



                <div  id = "div1"class="col-lg-8">
<!--                    插入活動內容的位置 -->
                </div>

            <button id='Signup'>我要報名</button>





			</div>
		</div>

		<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="../universal/sidebar.jsp"%>

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