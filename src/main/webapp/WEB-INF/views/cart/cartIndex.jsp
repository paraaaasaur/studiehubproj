<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cart Index Page</title>
	<style>
		table {
		    border: 2px solid black;
		    border-collapse: 
		}
		
		tbody thead {
			border: 2px solid black;
		    border-collapse: 
		}
		
		tr td {
		    border: 1.5px solid gray;
		    border-collapse: collapse;
		}
		
		tr th {
		    border: 1.5px solid gray;
		    border-collapse: collapse;
		}
	</style>
</head>
<body>
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
				    <th>課程介紹(P_DESC))</th>
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
		<button formmethod="GET" formaction="<c:url value='/cart.controller/cartAdmin' />">到購物車GM頁面</button>
	</form>
		<hr>

	<!--****************************************** S      C      R      I      P      T ******************************************-->

	<script src="<c:url value='/' />assets/js/jquery.min.js"></script>
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
					   products = JSON.parse(cart);
					   let segment = "";
					   let totalPrice = 0;
	
					   for (let i = 0; i < products.length; i++) {
						   segment += "<tr>"
										 + "<td><input type='checkbox' class='ckbox ckbox" + i + "' name='ckbox' value='" + i + "' id='ckbox'>取消</td>"
										 + "<td>" + products[i].p_Name + "</td>"
										 + "<td>" + products[i].p_ID + "</td>"
										 + "<td>" + products[i].p_Price + "</td>"
										 + "<td>" + products[i].p_DESC + "</td>"
										 + "<td>" + products[i].u_ID + "</td>"
										 + "</tr>";
							totalPrice += products[i].p_Price;
					   }
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
					counter += ($('.ckbox' + j).is(':checked'))? 1 : 0;
				}
				for(let i = 0; i < products.length; i++) {
					let checkOrNot = $('.ckbox' + i).is(':checked');
					if(checkOrNot) {
						queryString += (i == ckboxValues[counter])? i : i + ',';
					}
				}
				if(counter == -1) {
					alert('必須至少勾選一項想要刪除的項目。')
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