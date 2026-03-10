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

<script type="application/json" id="bootstrap-data">
	{
		"adminId": "${fn:escapeXml(adminId)}",
		"successMessage" : "${fn:escapeXml(successMessage)}"
	}
</script>

<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { successMessage } = bootstrapData;

	if (successMessage) alert(successMessage);

	const state = {
		status: "loading", // 'idle' | 'loading' | 'success' | 'error'
		adminId: bootstrapData.adminId,
		products: null,
		error: null
	};
	let logoutHref, dataArea, query, productname, typename;
	const api = {
		fetchAllProducts: async function() {
			const response = await fetch('findAllProduct');
			if (!response.ok) {
				throw new Error('Fetch failed');
			}
			return response.json();
		},
		fetchProductsByName: async function(pname, producttypename) {
			const url = 'admin/products' +
					'?pname=' + pname + '&producttypename=' + producttypename;
			const response = await fetch(url);
			if (!response.ok) {
				throw new Error('Something went wrong');
			}
			return response.json();
		}
	};

	window.onload = init;
	async function init() {
		// dom wiring
		logoutHref = document.getElementById('logoutHref');
		dataArea = document.getElementById("dataArea");
		query = document.getElementById("query");
		productname = document.getElementById("productname");
		typename = document.getElementById("producttypename");

		bindEvents();
		await loadProducts();
	}

	function bindEvents() {
		query.addEventListener('click', queryProducts);
	}
	async function loadProducts() {
		state.status = "loading";
		render();

		try {
			const data = await api.fetchAllProducts();
			state.products = data.list;
			state.status = "success";
			state.error = null;
		} catch (e) {
			state.status = "error";
			state.error = e;
		}

		render();
	}
	function render() {
		if (state.status === "loading") {
			showSpinner();
			return;
		}

		if (state.status === "error") {
			console.error('something went wrong: ', state.error);
			return;
		}

		if (state.adminId != null) {
			renderCommonAdminUI();
		}

		if (state.products != null) {
			dataArea.replaceChildren(renderProductTable(state.products));
		}
	}
	function showSpinner() {
		console.log('Spinning');
	}
	function renderCommonAdminUI() {
		logoutHref.style.visibility = "visible";
	}
	async function queryProducts() {
		const pname = productname.value;
		const producttypename = typename.value;
		if(!pname){
			alert('請輸入關鍵字');
			return;
		}

		state.status = "loading";
		render();

		try {
			const data = await api.fetchProductsByName(pname, producttypename);
			state.products = data.list;
			state.status = "success";
			state.error = null;
		} catch (error) {
			state.status = "error";
			state.error = error;
		}

		render();
	}
	function renderProductTable(products) {
		const table = document.createElement('table');
		table.border = '1';
		table.style.width = '100%';
		table.style.textAlign = 'center';

		if (products.length === 0) {
			table.appendChild(emptyResultRow());

			return table;
		}

		table.appendChild(resultNumberRow(products.length));
		table.appendChild(header());

		products.forEach(product => {
			table.appendChild(row(product));
		});

		return table;
	}
	function emptyResultRow() {
		const tr = document.createElement('tr');
		const th = document.createElement('th');
		th.textContent = '查無資料';
		th.colSpan = 5;

		tr.appendChild(th);

		return tr;
	}
	function resultNumberRow(length) {
		const tr = document.createElement('tr');
		const th = document.createElement('th');
		th.colSpan = 5;
		th.textContent = '共計"' + length + '"筆資料';

		tr.appendChild(th);

		return tr;
	}
	function header() {
		const tr = document.createElement('tr');

		const imgTh = document.createElement('th');
		imgTh.textContent = '課程圖片';
		imgTh.style.textAlign = 'center';

		const nameTh = document.createElement('th');
		nameTh.textContent = '課程名稱';
		nameTh.style.textAlign = 'center';

		const classTh = document.createElement('th');
		classTh.textContent = '課程類別';

		const priceTh = document.createElement('th');
		priceTh.textContent = '課程價格';

		const descTh = document.createElement('th');
		descTh.textContent = '課程介紹';
		descTh.style.textAlign = 'center';

		const actionTh = document.createElement('th');
		actionTh.textContent = '功能';
		actionTh.style.textAlign = 'center';
		actionTh.width = '50px';

		tr.append(imgTh, nameTh, classTh, priceTh, descTh, actionTh);

		return tr;
	}
	function row(product) {
		const tr = document.createElement('tr');

		tr.append(
				imageCell(product),
				nameCell(product),
				td(product.p_Class),
				td(product.p_Price),
				descCell(product),
				actionCell(product)
		)

		return tr;
	}
	function imageCell(product) {
		const td = document.createElement('td');

		const img = document.createElement("img");
		img.src = product.p_Img;
		img.width = "100";
		img.height = "60";

		td.appendChild(img);

		return td;
	}
	function nameCell(product) {
		const name = document.createElement("td");
		name.textContent = product.p_Name;
		name.style.textAlign = 'center';

		return name;
	}
	function td(text) {
		const td = document.createElement('td');
		td.textContent = text;
		td.style.width = '100px';

		return td;
	}
	function descCell(product) {
		const desc = document.createElement('td');
		desc.textContent = product.p_DESC;

		return desc;
	}
	function actionCell(product) {
		const td = document.createElement('td');

		const upd = document.createElement('input');
		upd.type = 'button';
		upd.value = '更新';
		upd.style.margin = '5px';
		upd.addEventListener('click', () => {
			location.href = 'updateProduct/' + product.p_ID;
		});

		const del = document.createElement('input');
		del.type = 'button';
		del.value = '刪除';
		del.style.margin = '5px';
		del.addEventListener('click', () => {
			location.href = 'deleteProduct/' + product.p_ID;
		});

		td.append(upd, del);

		return td;
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/adminHeader.jsp"%>


				<h2 align='center'>課程資訊</h2>
				<hr>
				<div style="text-align: center;">
					<select id="producttypename" style="width: 150px;display: inline;float: none;border-radius: 50px;">
						<option label="類別" value="-1" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="英文" value="英文" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="日文" value="日文" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="西語" value="西語" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="葡萄牙語" value="葡萄牙語" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="拉丁語" value="拉丁語" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="韓文" value="韓文" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
					</select>
					<input type="text" id="productname" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入課程關鍵字">
					<button id="query" style="display: inline;">搜尋</button>
					<br>
					<br>
				</div>
				
				<div id='dataArea'></div>
			</div>


		</div>
		<%@include file="../universal/adminSidebar.jsp"%>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>