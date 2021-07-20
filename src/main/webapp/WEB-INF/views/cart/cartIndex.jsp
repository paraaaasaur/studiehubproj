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
						<table class="alt" style="border: 2px;">
							<thead id='theadArea'></thead>
							<tbody id='tbodyArea'></tbody>
						</table>
						<span id='totalPrice' style="background-color: yellow; font-size: 250%;"></span>
		
						<!-- 按鈕導向各頁 -->
						<div id="btnAppender" class="fit">
							<hr>
							<button id="deleteBtn" hidden='true' disabled>刪除勾選課程</button>
							<button id="checkoutBtn" onclick="checkoutViaEcpay()" hidden='true'>我要結帳</button>
							<button id="toIndexBtn" hidden='true'>返回首頁</button>
							<hr>
						</div>
		
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
		<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
		<script src="${pageContext.request.contextPath}/assets/js/custom/TaJenUtils.js" async></script>

		<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
		<script>
			let totalPrice = 0;
			let products;
			let cartSize = 0;
			let checkedCartIds = [];
			let head = "<tr>"
						  + "<th>移除</th>"
						  + "<th>課程名稱</th>"
						  + "<th>課程編號</th>"
						  + "<th>課程價格</th>"
						  + "<th>課程介紹</th>"
						  + "<th>課程老師</th>"
						  + "</tr>";

			// 【function 1】checkout
			function checkoutViaEcpay(){
				let confirmArticle = '※您即將購買以下內容';
				for (let i = 0; i < products.length; i++) {
					let product = products[i];
					confirmArticle += '\n- 課程名稱：' + product.p_name; 
					confirmArticle += '\n【價格：' + product.p_price + '；授課老師：' + product.p_teacher + '】';
				}
				confirmArticle += '\n本次結帳共計：' + totalPrice + '元';
				let confirmAns = confirm(confirmArticle);
				if (confirmAns) {
					console.log('ok!');
					let queryString = '';
					queryString = 'u_id=' + u_id;
					queryString += '&p_ids=';
					let p_ids = [];
					for (let i = 0; i < products.length; i++) {
						queryString += products[i].p_id;
						queryString += (i + 1 == products.length)? '' : ',';
						p_ids.push(products[i].p_id);
					}
					console.log(queryString);
					// 用TaJenUtils.js的自訂函數送出隱藏版post表單
					post('<c:url value="/cart.controller/checkout" />', {'u_id': u_id, 'p_ids': [p_ids]});
					
				
				} else {
					console.log('nope!');
				}
			}

			/** 【自訂函數 0】每次按下checkbox時會記錄下來哪些是有勾的、並把cartid存進checkedCartIds陣列裡，等到要刪除時存取之送出 */
			var memorize = function(checkboxObj){
				let cartid = checkboxObj.value;
				let idx = checkedCartIds.indexOf(cartid);
				if(idx > -1) { 
					checkedCartIds.splice(idx, 1);
				} else {
					checkedCartIds.push(cartid);
				}
				console.log('checkedCartIds = ' + checkedCartIds);
				// 改變#deleteBtn外觀和disabled值
				document.querySelector('#deleteBtn').disabled = (checkedCartIds.length == 0)? true : false;
				document.querySelector('#deleteBtn').innerHTML = (checkedCartIds.length != 0)?
								'刪除<font color="cornflowerblue"> ' + checkedCartIds.length + ' </font>筆項目':
								'刪除勾選課程';
				return;
			}

			$(function(){
				let deleteBtn = $('#deleteBtn');
				let tbodyArea = $('#tbodyArea');
				let theadArea = $('#theadArea');

				// 【function 2】主程式
				$(window).on('load', function(){
					if (!u_id) {
						$('#welcomeMessage').text('')
						$('#btnAppender').html('');
						theadArea.html("");
						tbodyArea.html("<h1>必須先登入才會顯示資料！</h1>"); // ❗
					} else {
						$('#welcomeMessage').text(u_id + '，您的購物車清單如下：');
						
						let xhr = new XMLHttpRequest();
						let url = "<c:url value='/cart.controller/clientShowCart' />";
						let queryString = "u_id=" + u_id;
						xhr.open("POST", url, true);
						xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
						xhr.send(queryString);
						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4 && xhr.status == 200) {
								let tbodyContent = parseCart(xhr.responseText);
								if (cartSize == 0) {
									$('#welcomeMessage').text(u_id + '，您的購物車內還沒有任何東西！');
									$('#btnAppender').html('');
									return;
								}
								$('#totalPrice').html('小計：' + totalPrice);
								theadArea.html(head);
								tbodyArea.html(tbodyContent);
							}
						}
					}
					
				});
				
				// 【function 3】parseCart()
				/** 更新全域變數 @products @cartSize */
				function parseCart(cart) {
					products = JSON.parse(cart);
					let segment = "";
					totalPrice = 0;
					cartSize = products.length;
					
					if(cartSize){
						segment += "您的購物車內還沒有任何課程喔😉";
					}
					for (let i = 0; i < cartSize; i++) {
						let temphref1 = '<c:url value="/takeClass/" />';
						let product = products[i];
						segment += "<tr>"
										+ "<td><input onclick='memorize(this)' type='checkbox' id='ckbox" + product.cart_id + "' value='" + product.cart_id + "'>"
										+ "<label for='ckbox" + product.cart_id + "'></label></td>"
										+ "<td><a href='" + temphref1 + product.p_id + "' >" + product.p_name + "</a></td>"
										+ "<td>" + product.p_id + "</td>"
										+ "<td>" + product.p_price + "</td>"
										+ "<td>" + product.p_desc + "</td>"
										+ "<td>" + product.p_teacher + "</td>"
										+ "</tr>";
						totalPrice += product.p_price;
					}
					return segment;
				};
			
				// 【function 4】DELETE
				// 送出cartid + uid 的查詢字串到server
				// 清空checkids[]
				// 善後
				$("#deleteBtn").click(function(){
					// <1> 拼出queryString
					let queryString = 'cart_ids=';
					for(let i = 0; i < checkedCartIds.length; i++) {
						queryString += checkedCartIds[i];
						queryString += ((i + 1) == checkedCartIds.length)? '' : ',';
					}
					queryString +='&u_id=' + u_id;	
					// <2> 送出請求
					let xhr = new XMLHttpRequest();
					let url = "<c:url value='/cart.controller/clientRemoveProductFromCartByCartId' />";
					xhr.open("POST", url, true);
					xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					console.log('即將送出的查詢字串 = ' + queryString);
					xhr.send(queryString);
					xhr.onreadystatechange = function() {
						if (xhr.readyState == 4 && xhr.status == 200) {
							// <3> 善後
							checkedCartIds = [];
							let tbodyContent = parseCart(xhr.responseText);
							document.querySelector('#deleteBtn').innerHTML = '刪除勾選課程';
							document.querySelector('#deleteBtn').disabled = true;
							if (cartSize == 0) {
								console.log('hi，現在cartSize = ' + cartSize);
								$('#welcomeMessage').text(u_id + '，您的購物車內還沒有任何課程喔😉');
								$('#theadArea').html('');
								$('#tbodyArea').html('');
								$('#btnAppender').html('');
								return;
							} else {
								console.log('掛上了tbody');
								$('#totalPrice').html('小計：' + totalPrice);
								tbodyArea.html(tbodyContent);
							}
						}
					}
				});
	
				// 【自訂函數 3】回首頁
				$('#toIndexBtn').on('click', function(){
					top.location = "<c:url value='/' />";
				})


			})

				
		</script>
</body>
</html>