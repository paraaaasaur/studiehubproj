<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>
<style>
	.deleteBtn {
		color : rgb(0, 132, 255) !important;
		box-shadow : inset 0 0 0 2px rgb(0, 132, 255);
	}
	.deleteBtn:hover {
		background-color: rgb(230, 245, 253);
	}
	.deleteBtn:active {
		background-color: rgb(200, 231, 248);
	}

	.cart-item__cell {
		text-align: center;
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

	window.onload = function() {
		var logout = document.getElementById("logout");
		logout.onclick = function() {
			var xhr = new XMLHttpRequest();
			xhr.open("GET", "logout.controller", true);
			xhr.send();
			xhr.onreadystatechange = function() {
				if(xhr.readyState == 4 && xhr.status == 200){
					var result = JSON.parse(xhr.responseText);
					if(result.success){
						alert(result.success);
						top.location = '';
					}else if(result.fail){
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
		document.getElementById('user-info-actions').toggleAttribute('hidden', false);
		var logoutHref = document.getElementById('logoutHref');
		var userId = document.getElementById('userId');
		var userPic = document.getElementById('userPic');
		var loginEvent = document.getElementById('loginEvent');
		var loginEvent1 = document.getElementById('loginEvent1');
		var loginALLEvent1 = document.getElementById('loginALLEvent1');

		if(u_id) {
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
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
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
		<%@include file="../universal/sidebar.jsp" %>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script src="assets/js/custom/TaJenUtils.js" async></script>

	<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
	<script>
		let totalPrice = 0;
		let products;
		let cartSize = 0;
		let checkedCartIds = [];
		const head = "<tr>"
					  + "<th style='text-align: center'>移除</th>"
					  + "<th style='text-align: center'>課程名稱</th>"
					  + "<th style='text-align: center'>課程編號</th>"
					  + "<th style='text-align: center'>課程價格</th>"
					  + "<th style='text-align: center'>課程老師</th>"
					  + "</tr>";

		// 【function 1】checkout
		function checkoutViaEcpay() {
			let confirmArticle = '※您即將購買以下內容';
			for (let i = 0; i < products.length; i++) {
				let product = products[i];
				confirmArticle += '\n- 課程名稱：' + product.p_name;
				confirmArticle += '\n【價格：' + product.p_price + '；授課老師：' + product.p_teacher + '】';
			}
			confirmArticle += '\n本次結帳共計：' + totalPrice + '元';
			let confirmAns = confirm(confirmArticle);
			if (confirmAns) {
				let queryString = '';
				queryString = 'u_id=' + u_id;
				queryString += '&p_ids=';
				let p_ids = [];
				for (let i = 0; i < products.length; i++) {
					queryString += products[i].p_id;
					queryString += (i + 1 == products.length)? '' : ',';
					p_ids.push(products[i].p_id);
				}
				post('cart.controller/checkout', {'u_id': u_id, 'p_ids': [p_ids]});


			}
		}

		/** 【自訂函數 0】每次按下checkbox時會記錄下來哪些是有勾的、並把cartid存進checkedCartIds陣列裡，等到要刪除時存取之送出 */
		var memorize = function(e){
			let cartid = e.target.value;
			let idx = checkedCartIds.indexOf(cartid);
			if(idx > -1) {
				checkedCartIds.splice(idx, 1);
			} else {
				checkedCartIds.push(cartid);
			}
			// 改變#deleteBtn外觀和disabled值
			if (checkedCartIds.length == 0) {
				deleteBtn.classList.remove('deleteBtn');
			} else {
				deleteBtn.classList.add('deleteBtn');
			}
			document.querySelector('#deleteBtn').disabled = (checkedCartIds.length == 0);
			document.querySelector('#deleteBtn').innerHTML = (checkedCartIds.length != 0)?
							'刪除<font color="cornflowerblue"> ' + checkedCartIds.length + ' </font>筆資料':  // ❗ 超過10筆資料時button會變胖
							'刪除勾選資料';
			return;
		}

		$(function(){
			let tbodyArea = $('#tbodyArea');
			let theadArea = $('#theadArea');

			// 【function 2】主程式
			$(window).on('load', function(){
				if (!u_id) {
					$('#welcomeMessage').text('')
					$('#btnAppender').html('');
					theadArea.empty();
					tbodyArea.html("<h1>必須先登入才會顯示資料！</h1>"); // ❗
				} else {
					$('#welcomeMessage').text(u_id + '，您的購物車清單如下：');

					let xhr = new XMLHttpRequest();
					let url = "cart.controller/clientShowCart";
					let queryString = "u_id=" + u_id;
					xhr.open("POST", url, true);
					xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					xhr.send(queryString);
					xhr.onreadystatechange = function() {
						if (xhr.readyState == 4 && xhr.status == 200) {
							const tbodyContent = parseCart(xhr.responseText);
							if (cartSize == 0) {
								$('#welcomeMessage').text(u_id + '，您的購物車內還沒有任何東西！');
								$('#btnAppender').html('');
								return;
							}
							$('#totalPrice').text('小計：' + totalPrice);
							theadArea.html(head);
							tbodyArea.empty();
							tbodyArea.append(tbodyContent);
						}
					}
				}

			});

			// 【function 3】parseCart()
			/** 更新全域變數 @products @cartSize */
			function parseCart(res) {
				products = JSON.parse(res);
				totalPrice = 0;
				cartSize = products.length;

				const rows = document.createDocumentFragment();

				if(cartSize === 0) {
					rows.textContent = "您的購物車內還沒有任何課程喔😉";
					return rows;
				}

				for (const product of products) {
					const cartItemRow = document.createElement("tr");

					cartItemRow.appendChild(createCheckboxCell(product.cart_id));
					cartItemRow.appendChild(createLinkedNameCell(product.p_id, product.p_name));
					cartItemRow.appendChild(createTextCell(product.p_id));
					cartItemRow.appendChild(createTextCell(product.p_price));
					cartItemRow.appendChild(createTextCell(product.p_teacher));

					rows.appendChild(cartItemRow);

					totalPrice += product.p_price;
				}

				return rows;
			}
			function createCheckboxCell(cartId) {
				const cell = document.createElement('td');
				cell.className = 'cart-item__cell';

				const checkboxEl = document.createElement('input');
				checkboxEl.type = 'checkbox';
				checkboxEl.id = 'ckbox' + cartId;
				checkboxEl.value = cartId;
				checkboxEl.onclick = e => memorize(e);

				const labelEl = document.createElement('label');
				labelEl.htmlFor = checkboxEl.id;

				cell.appendChild(checkboxEl);
				cell.appendChild(labelEl);

				return cell;
			}
			function createLinkedNameCell(productId, name) {
				const cell = document.createElement('td');
				cell.className = 'cart-item__cell';

				const link = document.createElement('a');
				link.href = 'takeClass/' + productId;
				link.textContent = name;

				cell.appendChild(link);

				return cell;
			}
			function createTextCell(text) {
				const cell = document.createElement('td');
				cell.textContent = text;
				cell.className = 'cart-item__cell';

				return cell;
			}

			// 【function 4】DELETE
			$("#deleteBtn").click(function() {
				// <1> 拼出queryString
				let queryString = 'cart_ids=';
				for(let i = 0; i < checkedCartIds.length; i++) {
					queryString += checkedCartIds[i];
					queryString += ((i + 1) == checkedCartIds.length)? '' : ',';
				}
				queryString +='&u_id=' + u_id;
				// <2> 送出請求
				let xhr = new XMLHttpRequest();
				let url = "cart.controller/clientRemoveProductFromCartByCartId";
				xhr.open("POST", url, true);
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				xhr.send(queryString);
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						// <3> 善後
						checkedCartIds = [];
						const tbodyContent = parseCart(xhr.responseText);
						document.querySelector('#deleteBtn').classList.remove('deleteBtn');
						document.querySelector('#deleteBtn').innerHTML = '刪除勾選課程';
						document.querySelector('#deleteBtn').disabled = true;
						if (cartSize == 0) {
							$('#welcomeMessage').text(u_id + '，您的購物車內還沒有任何課程喔😉');
							$('#totalPrice').text('小計：0');
							$('#theadArea').html('');
							$('#tbodyArea').html('');
							$('#btnAppender').html('');
						} else {
							$('#totalPrice').text('小計：' + totalPrice);
							tbodyArea.empty();
							tbodyArea.append(tbodyContent);
						}
					}
				}
			});

			// 【自訂函數 3】回首頁
			$('#toIndexBtn').on('click', function(){
				top.location = "";
			});
		});
	</script>
</body>
</html>