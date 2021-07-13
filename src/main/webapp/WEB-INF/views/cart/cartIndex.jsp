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
		
						<h1 id='welcomeMessage'></h1>
						<!-- 顯示當前購物車內容表格 -->
						<table>
							<thead id='theadArea'></thead>
							<tbody id='tbodyArea'></tbody>
						</table>
		
						<hr>
						<!-- 按鈕導向各頁 -->
						<button id="remove">移除</button>
						<button id="toCheckoutPageBtn">去結帳</button>
						<button id="toIndexBtn">回首頁</button>
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
		<script src="${pageContext.request.contextPath}/assets/js/custom/TaJenUtils.js" async></script>

		<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
		<script>
			let products;
			let cartSize = 0;
			let checkboxes = [];
			let head = "<tr>"
						  + "<th>移除</th>"
						  + "<th>課程名稱(P_Name)</th>"
						  + "<th>課程編號(P_ID)</th>"
						  + "<th>課程價格(P_Price)</th>"
						  + "<th>課程介紹(P_DESC)</th>"
						  + "<th>課程老師(U_ID)</th>"
						  + "</tr>";

				$(function(){
					let welcomeMessage = $('#welcomeMessage');
					let tbodyArea = $('#tbodyArea');
					let theadArea = $('#theadArea');
						// [AJAX] showCart ✔
						$(window).on('load', function(){
							let x = (!u_id)? '' : u_id + '，您的購物車清單如下：';
							welcomeMessage.text(x);

							if(!u_id){
								theadArea.html("");
								tbodyArea.html("<h1>必須先登入才會顯示資料！</h1>"); // ❗
								return;
							}
							
							let xhr = new XMLHttpRequest();
							let url = "<c:url value='/cart.controller/clientShowCart' />";
							let queryString = "u_id=" + u_id;
							console.log('queryString = ' + queryString);
							xhr.open("POST", url, true);
							xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
							// xhr.send("u_id=miosya");
							xhr.send(queryString);
							xhr.onreadystatechange = function() {
								if (xhr.readyState == 4 && xhr.status == 200) {
									theadArea.html(head);
									tbodyArea.html(parseCart(xhr.responseText));
									for(let i = 0; i < cartSize; i++){
										checkboxes.push($('#ckbox' + i));
										// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 測試用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
										// console.log($('#ckbox' + i).attr('data-test01'));
										// console.log(checkboxes[i].attr('data-test01'));
										// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 測試用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
										}
								}
							}
						});
						
						// #parseCart() ✔
						function parseCart(cart) {
							products = JSON.parse(cart);
							let segment = "";
							let totalPrice = 0;
							cartSize = products.length;
							
							if(cartSize){
								segment += "您的購物車內尚未有任何內容！";
							}
							for (let i = 0; i < cartSize; i++) {
								segment += "<tr>"
												+ "<td><input type='checkbox' id='ckbox" + i + "' data-test01='" + i*10 + "'>"
												+ "<label for='ckbox" + i + "'>取消</label></td>"
												+ "<td>" + products[i].p_name + "</td>"
												+ "<td>" + products[i].p_id + "</td>"
												+ "<td>" + products[i].p_price + "</td>"
												+ "<td>" + products[i].p_desc + "</td>"
												+ "<td>" + products[i].p_teacher + "</td>"
												+ "</tr>";
								totalPrice += products[i].p_price;
								
							}
							return segment;
						};
					
						// 2 Remove
						$("#remove").click(function(){
							let xhr = new XMLHttpRequest();
							let url = "<c:url value='/cart.controller/clientRemoveProductFromCart' />";
							let ckboxValues = [];
							let queryString = 'p_ids=';
							// 不勾選任何checkbox時 == -1，每多勾選一個checkbox都會 +1。用於下述防呆機制。
							let counter = -1; 
							for(let i = 0; i < products.length; i++){
								if(checkboxes[i].is(':checked')){
									ckboxValues.push(i);
								} 
							}
							// 利用counter計數，來確保使用者至少要勾一件東西才能送出
							for(let i = 0; i < products.length; i++) {
								counter += (checkboxes[i].is(':checked'))? 1 : 0;
							}
							if(counter == -1) {
								alert('必須至少勾選一項想要刪除的項目。');
								return;
							}
							// 
							for(let i = 0; i < products.length; i++) {
								let checkOrNot = checkboxes[i].is(':checked');
								if(checkOrNot) {
									queryString += products[i].p_id;
									queryString += (i == ckboxValues[counter])? '' : ',';
								}
							}
							queryString +='&u_id=' + u_id;	


							xhr.open("POST", url, true);
							xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
							xhr.send(queryString);
							xhr.onreadystatechange = function() {
								if (xhr.readyState == 4 && xhr.status == 200) {
									tbodyArea.html(parseCart(xhr.responseText));
								}
							}
						});
			
						// 【自訂函數 3】回首頁
						$('#toIndexBtn').on('click', function(){
							top.location = "<c:url value='/' />";
						})



						// 【自訂函數 4】去結帳頁
						$('#toCheckoutPageBtn').on('click', function(){
							post('<c:url value="/cart.controller/cartCheckout" />', {});
						})

							// DELETE功能防呆
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