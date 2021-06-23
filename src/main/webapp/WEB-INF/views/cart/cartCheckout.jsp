<%@page import="tw.group5.controller.cart.*"%>
<%@page import="tw.group5.model.cart.*"%>
<%@page import="tw.group5.model.product.ProductInfo"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Checkout Page</title>
</head>
<body>
	<center>
	<h1>您欲購買的項目如下：</h1>
	<!-- 1. 顯示當前購物車內容表格........................................ -->
	<form> 
		<table>
			<thead>
				<tr>
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
		<h2 id='totalPrice' style='background-color: orange;'>小計</h2>
		<hr>
<!-- 2. 按鈕導向各頁................................................... -->
		<button formmethod="GET" formaction="<c:url value='/cart.controller/cartIndex' />">回上一頁</button>
		<button formmethod="POST" formaction="<c:url value='/cart.controller/pay' />">確定結帳</button>
		<button formmethod="GET" formaction="<c:url value='/' />">回首頁</button>
		<hr>
	</form>		
	</center>
	<!--****************************************** S      C      R      I      P      T ******************************************-->

	<script src="/SpringMvcWebHW/js/jquery-3.6.0.min.js"></script>
	<script>
		$(function(){
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
					let products = JSON.parse(cart);
					let segment = "";
					let totalPrice = 0;

					for (let i = 0; i < products.length; i++) {
						totalPrice += products[i].p_Price;
						segment += "<tr>"
										+ "<td>" + products[i].p_Name + "</td>"
										+ "<td>" + products[i].p_ID + "</td>"
										+ "<td>" + products[i].p_Price + "</td>"
										+ "<td>" + products[i].p_DESC + "</td>"
										+ "<td>" + products[i].u_ID + "</td>"
										+ "</tr>";
					}
					$('#totalPrice').text('小計 = ' + totalPrice);
					return segment;
			};
		})
	</script>
</body>
</html>