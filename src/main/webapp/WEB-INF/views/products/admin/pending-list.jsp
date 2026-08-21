<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>

<script src="assets/js/utility/dom.js"></script>
<script type="application/json" id="bootstrap-data">
	{
		"success": "${fn:escapeXml(success)}",
		"adminId": "${fn:escapeXml(adminId)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const {success, adminId} = bootstrapData;

	if (success) {
		alert(success);
	}

	window.onload = function() {
		var logoutHref = document.getElementById('logoutHref');
		if(adminId){
			logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
		}



		var dataArea = document.getElementById("dataArea");
		var query = document.getElementById("query");
		var productname = document.getElementById("productname");
		let xhr = new XMLHttpRequest();
		xhr.open("GET","findAllProductPendingAccess",true);
		xhr.send();
		xhr.onreadystatechange = function(){

			if(xhr.readyState == 4 && xhr.status == 200){
				var result = JSON.parse(xhr.responseText);
				dataArea.replaceChildren(showData(result));
			}
		}

		query.addEventListener('click',function(){
			let pname = productname.value;
			if(!pname){
				alert('請輸入關鍵字');
				return
			}

			let xhr2 = new XMLHttpRequest();
			xhr2.open('GET',"admin/products?pname="+pname+"&approved=false");
			xhr2.send();
			xhr2.onreadystatechange = function(){
				if(xhr2.readyState == 4 && xhr2.status == 200){
					var result = JSON.parse(xhr2.responseText)
					dataArea.replaceChildren(showData(result));
				}
			}
		})

	}

	function showData(textObj) {
		let obj = JSON.parse(JSON.stringify(textObj));
		let size = obj.size;
		let products = obj.list;
		console.log(obj);
		console.log(size);
		console.log(products);
		const container = document.createElement("table");
		container.border = '1';
		container.style.width = '100%';
		container.style.textAlign = 'center';

			if (size == 0) {
				const noRows = htmlToFragment("<tr><th colspan='8'>查無資料</th></tr>");
				container.appendChild(noRows);
				return container;
			} else {
				const dataSizeMessage = dataSizeRow(size);
				const header = htmlToFragment("<tr><th style='text-align: center;'>課程圖片</th><th style='text-align: center;'>課程名稱</th><th>課程類別</th><th>課程價格</th><th style='text-align: center;'>課程介紹</th><th width:50px; style='text-align: center;'>審核結果</th></tr>");

				container.append(dataSizeMessage, header);

				for (n = 0; n < products.length; n++) {
					const product = products[n];

					const productBox = document.createElement("tr");

					productBox.append(
						imageCell(product.p_Img),
						nameCell(product.p_Name),
						tdText(product.p_Class),
						tdText(product.p_Price),
						descCell(product.p_DESC),
						actionLinksCell(product.p_ID)
					);
					container.appendChild(productBox);
				}
			}

			return container;
	}
	function dataSizeRow(dataSize) {
		const tr = document.createElement("tr");

		const th = document.createElement("th");
		th.textContent = "共計" + dataSize + "筆資料";
		th.colSpan = 8;

		tr.appendChild(th);

		return tr;
	}
	function nameCell(p_Name) {
		const td = document.createElement('td');
		td.textContent = p_Name;

		return td;
	}
	function imageCell(p_Img) {
		const td = document.createElement('td');

		const img = document.createElement('img');
		img.src = p_Img;
		img.style.width = '100px';
		img.style.height = '60px';

		td.appendChild(img);

		return td;
	}
	function tdText(text) {
		const td = document.createElement('td');
		td.textContent = text;
		td.style.width = '100px';

		return td;
	}
	function descCell(p_DESC) {
		const td = document.createElement('td');
		td.textContent = p_DESC;

		return td;
	}
	/** contains approve and delete functionalities */
	function actionLinksCell(p_ID) {
		const td = document.createElement('td');

		const approve = document.createElement('input');
		approve.type = 'button';
		approve.value = '通過';
		approve.style.margin = '5px';
		approve.onclick = e => onclickApprove(e, p_ID);

		const del = document.createElement('input');
		del.type = 'button';
		del.value = '刪除';
		del.style.margin = '5px';
		del.onclick = e => onclickDelete(e, p_ID);

		td.append(approve, del);

		return td;
	}
	function onclickApprove(e, p_ID) {
		e.preventDefault();
		window.location.href = 'accessResult/' + p_ID;
	}
	function onclickDelete(e, p_ID) {
		e.preventDefault();
		window.location.href = 'deleteProduct/' + p_ID;
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../../fragments/admin/header.jsp"%>


				<h2 align='center'>課程資訊</h2>
				<hr>
				<div style="text-align: center;">
					<input type="text" id="productname" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入課程關鍵字">
					<button id="query" style="display: inline;">搜尋</button>
					<br>
					<br>
				</div>
				
				<div id='dataArea'></div>


			</div>
		</div>
		<%@include file="../../fragments/admin/sidebar.jsp"%>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>