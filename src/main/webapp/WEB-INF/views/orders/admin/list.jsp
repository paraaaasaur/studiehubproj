<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">
<title>訂單後台管理系統</title>
<style>
	.order-item__cell {
		text-align: center;
	}
</style>
<script type="application/json" id="bootstrap-data">
	{
		"adminId": "<c:out value="${adminId}" />"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { adminId } = bootstrapData;

	window.onload = function() {
		var logoutHref = document.getElementById('logoutHref');
		if(adminId) {
			logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
		}
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../../fragments/admin/header.jsp" %>


					<h1>訂單管理系統</h1>
					<ul class="actions fit">
						<li style="width: 70%;" id="searchBarHanger1"><input type="search" id="searchBar" placeholder='搜尋'></li>
						<li style="width: 35%;" id="searchBarHanger2" hidden><input class="" type='search' id='searchBar' placeholder='搜尋'></li>
						<li style="width: 20%;">
							<select class="fit" id='searchBy'>
								<option value='u_id' selected disabled hidden>選擇查詢參數...</option>
								<option value='u_id'>會員帳號</option>
								<option value='o_id'>訂單編號</option>
								<option value='ecpay_o_id'>訂單編號<font color='red'>(綠界)</font></option>
								<option value='p_id'>課程代號</option>
								<option value='p_name'>課程名稱</option>
								<option value='u_lastname'>會員姓氏</option>
								<option value='u_firstname'>會員名字</option>
								<option value='o_status'>訂單狀態</option>
								<option value='o_amt'>訂單小計</option>
								<option value='o_date'>訂單日期</option>
							</select>
						</li>
						<li style="width: 10%;" class="">
							<button type="submit" class="" id="searchBtn" disabled>查詢</button>
						</li>
					</ul>
					<h1 id='topLogo'></h1>
					<div id="pageHref" class="" style="display: flex; justify-content: center;"></div>
					<br>

					<!-- 秀出所有Order_Info -->
					<table class="alt" style="border: 2px " >
						<thead id="theadArea"></thead>
						<tbody id="tbodyArea"></tbody>
					</table>
					<h1 id='logo' style="background-color: red"></h1>
					<hr>

					<button id='toAdminIndexBtn'>回管理者首頁</button>
					<button id='toClientIndexBtn'>回使用者首頁</button>

					<br><br><br><br><br><br>


			</div>
		</div>
		<%@include file="../../fragments/admin/sidebar.jsp" %>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	<script src="assets/js/custom/TaJenUtils.js"></script>

	<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
	<script>
		const allOrderRows = [];
		let checkedIdentitySeeds = [];
		let pageNum = 0;
		let rowNum = 0;
		let rowPerPage = 10;
		let maxPageNum = 10;
		let orders = [];
		const theadContent =
				"<th style='text-align: center'>訂單編號<br></th>"
				+ "<th style='text-align: center'>訂單編號<font color='red'>(綠界)</font><br></th>"
				+ "<th style='text-align: center'>課程代號<br></th>"
				+ "<th style='text-align: center'>用戶帳號<br></th>"
				+ "<th style='text-align: center'>訂單狀態<br></th>"
				+ "<th style='text-align: center'>訂單時間<br></th>"
				+ "<th style='text-align: center'>訂單總額<br></th>";


		// 【自訂函數 1】掛頁籤函數
		let appendPegination = function() {
			pageNum = Math.ceil((allOrderRows.length)/rowPerPage);
			let temp0 = "";
			let tempPageNum = (pageNum > maxPageNum)? maxPageNum : pageNum;
			for (let i = 0; i < tempPageNum; i++) {
				temp0 += "<button class='pageBtn' data-index='" + i + "' type='button' id='btnPage" + i + "'>" + (i + 1) + "</button>&nbsp;&nbsp;&nbsp;";
			}
			$(pageHref).html(temp0);
			for (let i = 0; i < tempPageNum; i++) {
				$('#btnPage' + i).on('click', function() {
					$('.pageBtn').removeClass('primary');
					$('#btnPage' + i).addClass('primary');
				});
			}

			$('.pageBtn').on('click', function() {
				let pageIndex = $(this).attr('data-index');
				renderPageAt(pageIndex);
			});
		}

		// 【自訂函數 2】頁籤掛資料
		function renderPageAt(pageIndex) {
			const tbodyAreaEl = document.querySelector('#tbodyArea');
			tbodyAreaEl.replaceChildren();

			const from = pageIndex * rowPerPage;
			const to = (from + rowPerPage > allOrderRows.length)? allOrderRows.length : from + rowPerPage;
			for(let i = from; i < to; i++){
				tbodyAreaEl.appendChild(allOrderRows[i]);
			}
		}

		// DOM載入完成後
		$(function() {
			let topLogo = $('#topLogo');
			let tbodyArea = $('#tbodyArea');
			let theadArea = $('#theadArea');
			let pageHref = $('#pageHref');
			let searchBarHanger1 = $('#searchBarHanger1');
			let searchBarHanger2 = $('#searchBarHanger2');
			let searchBy = $('#searchBy');

			/*********************************************************************************************************/
			// 【自訂函數 3】查詢框(#searchBar)樣式隨使用者的選擇變化
			$(searchBy).on('change', function() {
				$('#searchBtn').attr('disabled', false);
					// <1> 日期範圍值
				if(this.value == 'o_date') {
					searchBarHanger1.css('width', '35%');
					searchBarHanger2.attr('hidden', false);
					$(searchBarHanger1).html("<input type='datetime-local' step='1' id='searchDateStart'>起始時間");
					$(searchBarHanger2).html("<input type='datetime-local' step='1' id='searchDateEnd'>結束時間");
					$('input[type="datetime-local"]').setNow();
					// <2> 唯一值
				} else if(this.value == 'u_id' || this.value == 'u_firstname' || this.value == 'u_lastname'
							|| this.value == 'p_name' || this.value == 'ecpay_o_id'){
					searchBarHanger1.css('width', '70%');
					searchBarHanger2.attr('hidden', true);
					$(searchBarHanger1).html("<input type='search' id='searchBar' placeholder='搜尋'>");
					// <3> 數值範圍值
				} else if(this.value == 'o_amt' || this.value == 'o_id' || this.value == 'p_id'){
					searchBarHanger1.css('width', '35%');
					searchBarHanger2.attr('hidden', false);
					$(searchBarHanger1).html("<input type='search' id='searchMin' placeholder='最小值'>");
					$(searchBarHanger2).html("<input type='search' id='searchMax' placeholder='最大值'>");
					// <4> 選擇值
				} else if(this.value == 'o_status') {
					searchBarHanger1.css('width', '70%');
					searchBarHanger2.attr('hidden', true);
					$(searchBarHanger1).html(
						"<select id='searchBar'>" +
						"<option value='完成' selected>完成</option>" +
						"<option value='處理中'>處理中</option>" +
						"<option value='失效'>失效</option>" +
						"</select>"
					);
				}
			})
			// 【自訂函數 4】重新導向頁面
			$('#toAdminIndexBtn').on('click', function() {
				top.location = "gotoAdminIndex.controller";
			});
			$('#toClientIndexBtn').on('click', function() {
				top.location = "";
			});

			// 【自訂函數 6】查詢功能
			$('#searchBtn').on('click', function() {
				let xhr = new XMLHttpRequest();
				let queryString = '';
				let forDate = (searchBy.val() == 'o_date');
				let forSingle = (searchBy.val() == 'u_id' || searchBy.val() == 'u_firstname' || searchBy.val() == 'u_lastname' ||
										searchBy.val() == 'p_name' || searchBy.val() == 'o_status' || searchBy.val() == 'ecpay_o_id');
				let forRange = (searchBy.val() == 'o_amt' || searchBy.val() == 'o_id' || searchBy.val() == 'p_id');

				// 日期範圍查詢
				if(forDate) {
					queryString = 'searchBy=' + searchBy.val() + '&searchBar=' + ($('#searchDateStart').val() + ',' + $('#searchDateEnd').val());
				// 單值查詢
				} else if(forSingle) {
					queryString = 'searchBy=' + searchBy.val() + '&searchBar=' + $('#searchBar').val();
				// 數值範圍查詢
				} else if(forRange) {
					queryString = 'searchBy=' + searchBy.val() + '&searchBar=' + ($('#searchMin').val() + ',' + $('#searchMax').val());
				}

				xhr.open('POST', "order.controller/adminSearchBar", true);
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); // ❓
				xhr.send(queryString);
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						tbodyArea.html("");
						pageHref.html("");
						// 解析&暫存回傳資料
						parseSelectedRows(xhr.responseText);
						// 掛topLogo
						topLogo.text("以下是資料庫最新" + allOrderRows.length + "筆訂單");
						// 掛資料(index = 0 即第 1 頁)
						renderPageAt(0);
						// 掛頁籤
						appendPegination();
						// 掛th
						theadArea.html(theadContent);
						if (allOrderRows.length == 0) {
							theadArea.html('');
						}
					}
				}
			})

			// 【自訂函數 7】顯示資料庫最新100筆訂單 (SELECT TOP(100)) + 掛資料 + 掛頁籤
			function showTop100() {
				let xhr = new XMLHttpRequest();
				let url = "order.controller/adminSelectTop100";
				xhr.open("GET", url, true);
				xhr.send();
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						parseSelectedRows(xhr.responseText);
						renderPageAt(0);
						appendPegination();
						topLogo.text("以下是資料庫最新" + allOrderRows.length + "筆訂單");
						if (allOrderRows.length == 0) {
							theadArea.html("");
						}
					}
				}
			}

			/** 【自訂函數 8】解析回傳資料 & 暫存進segments陣列 & 更新全域變數值
			* 重置全域變數 @cartItems @segments @rowNum @checkedIdentitySeeds
			* 重置#deleteBtn 的樣式及disabled屬性
			*/
			function parseSelectedRows(resp) {
				orders = JSON.parse(resp).list;
				checkedIdentitySeeds = [];
				rowNum = (orders)? orders.length : 0;
				allOrderRows.length = 0;
				for (const order of orders) {
					const orderRow = document.createElement('tr');
					{
						orderRow.appendChild(createTextCell(order.o_id));
						orderRow.appendChild(createTextCell(order.ecpay_o_id));
						orderRow.appendChild(createTextCell(order.p_id));
						orderRow.appendChild(createTextCell(order.u_id));
						orderRow.appendChild(createTextCell(order.o_status));
						orderRow.appendChild(createTextCell(order.o_date));
						orderRow.appendChild(createTextCell(order.o_amt));
					}

					allOrderRows.push(orderRow);
				}
			}

			function createTextCell(text) {
				const cell = document.createElement("td");
				cell.className = 'order-item__cell';

				const labelEl = document.createElement("label");
				labelEl.textContent = text;

				cell.appendChild(labelEl);

				return cell;
			}

			//【自訂函數 10】主程式函數
			function mainFunc() {
				theadArea.html(theadContent);
				// 解析&暫存回傳資料 + 掛資料(index = 0 即第 1 頁) + 掛頁籤
				showTop100();
			}
		/*********************************************************************************************************/
			// 主程式
			mainFunc();
		})
	</script>

</body>
</html>