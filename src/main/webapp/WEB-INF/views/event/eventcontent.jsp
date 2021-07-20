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
        <title>活動內容</title>
        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/startbootstrap/assets/favicon.ico" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="${pageContext.request.contextPath}/startbootstrap/css/styles.css" rel="stylesheet" />
        <link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">

        <script src="https://cdn.bootcss.com/limonte-sweetalert2/7.20.5/sweetalert2.all.min.js"></script>
    </head>
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

</style>
    <script>
    var u_id = "${loginBean.u_id}";
	var userPicString = "${loginBean.pictureString}";   
window.addEventListener("load", function() {

	
	
	
		var logout = document.getElementById("logout");
			logout.onclick = function() {
				var xhr1 = new XMLHttpRequest();
				xhr.open("GET", "<c:url value='/logout.controller' />", true);
				xhr.send();
				xhr.onreadystatechange = function() {
					if (xhr1.readyState == 4 && xhr1.status == 200) {
						var result = JSON.parse(xhr1.responseText);
						if (result.success) {
							alert(result.success);
							top.location = '<c:url value='/' />';
						} else if (result.fail) {
							alert(result.fail);
							top.location = '<c:url value='/' />';
						}
					}
				}
			}

			var loginHref = document.getElementById('loginHref');
		    var signupHref = document.getElementById('signupHref');
		    var logoutHref = document.getElementById('logoutHref');
		    var userId = document.getElementById('userId');
		    var userPic = document.getElementById('userPic');
		    
		    if(u_id){
		    	loginHref.hidden = true;
		    	signupHref.hidden = true;
		    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
		    	userPic.src = userPicString;	//有登入就秀大頭貼
		    	userId.innerHTML = u_id;
		    	loginEvent.style.display = "block";
		    	loginALLEvent.style.display = "block";

		    }
		
	
	

	

    	dataArea = document.getElementById("div1");
    	Signup = document.getElementById("Signup");

    	let xhr = new XMLHttpRequest();
    	xhr.open("GET", "<c:url value='/eventcontentjson/' />"+${eventcontent.a_aid}, true);

    	xhr.send();
    	xhr.onreadystatechange = function() {
    		
    		if (xhr.readyState == 4 && xhr.status === 200) {
//     			            console.log(xhr.responseText);
//     			            console.log(u_id);
    			dataArea.innerHTML = showData(xhr.responseText);
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
			xhr2.open("GET", "<c:url value='/signupclick/' />"+${eventcontent.a_aid}, true);
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
	let obj = JSON.parse(textobj)


    let segment="" ;
	
	let str = obj.comment ; 
	
	
	
    let  reg = /[?？]/g;
    let  reg1 = /[,，]/g;
    let  reg2 = /[!！]/g;
    let  reg3 = /[。。]/g;
    str1 = str.replace(reg,"?<br>");
    str2 = str1.replace(reg1,",<br>");
    str3 = str2.replace(reg2,"!<br>");
    str4 = str3.replace(reg3,"。<br>");
//     遇到標點符號就分割 暫時這樣 

    let tmp1 = "https://www.google.com/maps?q="+obj.a_address ;
    let tmp0 = "<a href='" + tmp1 + "' >" + "<img width='37' height='37' src='<c:url value='/images/enevt/地圖小圖.png' />'" + "</a>";
    let tmp2 = "<c:url value='/modifyRestaurant/'   />"+ obj.a_uid;
			    segment += "<article>";
			    segment += "<header class='mb-4'>";
			    
			    //判斷活動過期了沒有 有在H2裡加上已過期
			    if(obj.expired=="未過期"){
			    	
			    	segment += "<h2 class='fw-bolder mb-1'>"+obj.a_name+"</h1>";
			    }else{
			    	
			    	segment += "<h2 class='fw-bolder mb-1'>"+obj.a_name+"("+obj.expired+")"+ "</h1>";
			    }
			    
			    segment += "<div class='text-muted fst-italic mb-2'>發布者 :"+obj.uidname+"</div>";
				segment += "<div class='text-muted fst-italic mb-2'>建立時間 :"+obj.creationTime+"</div>";
				segment += "<div class='text-muted fst-italic mb-2'>報名人數上限 :"+obj.applicants+"</div>";
				segment += "<div class='text-muted fst-italic mb-2'>已報名人數 :"+obj.havesignedup+"</div>";
				segment += "<a class='badge bg-secondary text-decoration-none link-light' href='#!'>"+obj.a_type+"</a><br>";
// 				segment += "<a class='badge bg-secondary text-decoration-none link-light' href='"+tmp2+"'>"+"悄悄話"+"</a>";
				segment += "</header>"
				segment += "<span style = 'font-size:18px;'>報名活動時間 : "+obj.a_registration_starttime+"<span>至</span>"+obj.a_registration_endrttime+"<br/></span> "	
				segment += "<br>"
				segment += "<span style = 'font-size:18px;'>活動開始時間 : "+obj.a_startTime+"<span>至</span>"+obj.a_endTime+"<br/></span> "	
				segment += "<br>"
				segment += "<span style = 'font-size:18px;'>活動地點 : "+obj.a_address+"</span>"
				segment += "<br>"
				segment += "<br>"			   
				segment += "<figure class='mb-4'><img width='320' height='240'src=" + '<c:url value="/" />' + obj.a_picturepath+"  /></figure>"				
				segment += "<section class='mb-5'>"
				segment += "<p class='fs-5 mb-4'>"+str4+"</p>"
				segment += "</section>"
				segment += "</article>"

		
	
	

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
		src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>