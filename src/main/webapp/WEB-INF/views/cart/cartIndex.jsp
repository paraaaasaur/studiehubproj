<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>

<script>
if("${successMessageOfChangingPassword}"=="修改成功"){alert('密碼修改成功!');}

var u_id = "${loginBean.u_id}";
var userPicString = "${loginBean.pictureString}";

window.onload = function(){
    var logout = document.getElementById("logout");
    logout.onclick = function(){
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "<c:url value='/logout.controller' />", true);
        xhr.send();
        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4 && xhr.status == 200){
                var result = JSON.parse(xhr.responseText);
                if(result.success){
                    alert(result.success);
                    top.location = '<c:url value='/' />';
                }else if(result.fail){
                    alert(result.fail);                    
                    top.location = '<c:url value='/' />';
                }
            }
        }
    }
    
    //如果有登入，隱藏登入標籤
    var loginHref = document.getElementById('loginHref');
    var signupHref = document.getElementById('signupHref');
    var logoutHref = document.getElementById('logoutHref');
    var userPic = document.getElementById('userPic');
    if(u_id){
    	loginHref.hidden = true;
    	signupHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
    }
    
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

								<h1>您的購物車內有：</h1>
								<!-- 顯示當前購物車內容表格 -->
								<form method="POST" action="#"> 
									<table>
										<thead>
											<tr>
											    <th>移除</th>
											    <th>課程名稱(P_Name)</th>
											    <th>課程編號(P_ID)</th>
											    <th>課程價格(P_Price)</th>
											    <th>課程介紹(P_DESC)</th>
											    <th>課程老師(U_ID)</th>
											</tr>
										</thead>
										<tbody id='dataArea'>
										</tbody>
									</table>
									
									<hr>
									<!-- 按鈕導向各頁 -->
								</form>
								<button id="remove">移除</button>
								<form>
									<button formmethod="POST" id="checkout" formaction="<c:url value='/cart.controller/cartCheckout' />">去結帳</button>
									<button formmethod="GET" formaction="<c:url value='/' />">回首頁</button>
									<button formmethod="GET" formaction="<c:url value='/cart.controller/cartAdminSelect' />">到購物車GM頁面</button>
								</form>
									<hr>

						</div>
					</div>

				<!-- Sidebar -->
				<!-- 這邊把side bar include進來 -->
				<%@include file="../universal/sidebar.jsp" %>  

			</div>

		<!-- Scripts -->
			<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

		<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
			<script>
				$(function(){
					let products;	
						// [AJAX] showCart ✔
						let dataArea = $('#dataArea');
						$(window).on('load', function(){
							let xhr = new XMLHttpRequest();
							let url = "<c:url value='/cart.controller/showCart' />";
							xhr.open("GET", url, true);
							xhr.send();
							xhr.onreadystatechange = function() {
								if (xhr.readyState == 4 && xhr.status == 200) {
									dataArea.html(parseCart(xhr.responseText));
								}
							}
						});
						
						// #parseCart() ✔
						function parseCart(cart) {
							console.log('parseCart方法開始');
							products = JSON.parse(cart);
							let segment = "";
							let totalPrice = 0;
			
							for (let i = 0; i < products.length; i++) {
								segment += "<tr>"
												+ "<td><input type='checkbox' class='form-check-input ckbox ckbox" + i + "' name='ckbox' value='" + i + "' id='ckbox" + i + "'>"
												+ "<label for='ckbox" + i + "'>取消</label></td>"
												+ "<td>" + products[i].p_Name + "</td>"
												+ "<td>" + products[i].p_ID + "</td>"
												+ "<td>" + products[i].p_Price + "</td>"
												+ "<td>" + products[i].p_DESC + "</td>"
												+ "<td>" + products[i].u_ID + "</td>"
												+ "</tr>";
								totalPrice += products[i].p_Price;
							}
							console.log(segment);
							console.log('parseCart方法結束');
							return segment;
						};
					
						// 2 Remove by AJAX
						$("#remove").click(function(){
							let xhr = new XMLHttpRequest();
							let url = "<c:url value='/cart.controller/remove' />";
							let ckboxValues = [];
							let queryString = 'ckboxValues=';
							let counter = -1;
							for(let z = 0; z < products.length; z++){
								if($('.ckbox' + z).is(':checked')){
									ckboxValues.push(z);
								} 
							}
				
							for(let j = 0; j < products.length; j++) {
								counter += ($('.ckbox' + j).is(':checked'))? 1 : 0; // 這機制應該有缺陷 (勾 > 取消勾應該也能騙過)
							}
							for(let i = 0; i < products.length; i++) {
								let checkOrNot = $('.ckbox' + i).is(':checked');
								if(checkOrNot) {
									queryString += (i == ckboxValues[counter])? i : i + ',';
								}
							}
							if(counter == -1) {
								alert('必須至少勾選一項想要刪除的項目。');
								return;
							}
								
							xhr.open("POST", url, true);
							xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
							xhr.send(queryString);
							xhr.onreadystatechange = function() {
								if (xhr.readyState == 4 && xhr.status == 200) {
									dataArea.html(parseCart(xhr.responseText));
								}
							}
						});
			
						// 3 DELETE功能防呆
						// let ckboxs = $('input.ckbox');
						// let ckboxsChecked = $('input.ckbox:checked')
						// $(ckboxs).on('click', function() {
						// 	alert('YO!!!');
						// 	let ckboxsChecked = $(ckboxsChecked);
						// 	$('#remove').attr('disabled', true);
						// 	if($(ckboxsChecked).length == 0 || $(ckboxsChecked).length == null || $(ckboxsChecked).length == undefined) {
						// 	} else {
						// 		$('#remove').attr('disabled', false);			
						// 	}
						// })
		
				})

				
			</script>
	</body>
</html>