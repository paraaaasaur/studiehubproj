<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>

</head>

<body class="is-preload">

		<!-- Wrapper -->
			<div id="wrapper">

				<!-- Main -->
					<div id="main">
						<div class="inner">

							<!-- Header -->
								
								
								<h1 id='topLogo'>以下是資料庫最新${selectedRowNum}筆訂單</h1>
								<hr id="pageHref">
								<form>
									<!-- 秀出所有Order_Info (希望之後能每10項分一頁) -->
									<table>
										<thead id="headArea"></thead>
										<tbody id="dataArea"></tbody>
									</table>

						</div>
					</div>
			</div>

		<!-- Scripts -->
			<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

		<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
			<script>
				// 不用等DOM就可以先宣告的變數們

				// 把Server送回來的資料轉換成要掛上的html格式的資料陣列。
				let segments = []; 
				let counter = 0;
				let pageNum = 0;
				let rowPerPage = 15; // 每頁想要多少筆資料行
				
				$(function(){
					let dataArea = $('#dataArea');
					let headArea = $('#headArea');
					let pageHref = $('#pageHref');
					/*********************************************************************************************************/

					// 【自訂函數 1】顯示資料庫最新100筆訂單 (SELECT TOP(100)) + 掛資料
					function showTop100() {
						let xhr = new XMLHttpRequest();
						let url = "<c:url value='/cart.controller/adminSelectTop100' />";
						xhr.open("GET", url, true);
						xhr.send();
						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4 && xhr.status == 200) {
								parseSelectedRows(xhr.responseText);
								let htmlStuff = "";
								let tempCounter0 = (counter + rowPerPage > segments.length)? segments.length : counter + rowPerPage;
								for(let i = counter; i < tempCounter0; i++){
									htmlStuff += segments[i]
								}
								dataArea.html(htmlStuff);
							}
						}
					} 					

					// 【自訂函數 2】解析資料 & 暫存進segments陣列
					function parseSelectedRows(map) {
						parsedMap = JSON.parse(map);
						let orders = parsedMap.list;
							segments = []; // 清空segments陣列
							for (let i = 0; i < orders.length; i++) {
								let temp0 =	 "<tr>" + 
													"<td><label data-val='" + orders[i].o_id + "' class='old" + i + "0' >" + orders[i].o_id + "</label></td>" +
													"<td><label data-val='" + orders[i].p_id + "' class='old" + i + "1' >" + orders[i].p_id + "</label></td>" +
													"<td><label data-val='" + orders[i].u_id + "' class='old" + i + "2' >" + orders[i].u_id + "</label></td>" +
													"<td><label data-val='" + orders[i].o_status + "' class='old" + i + "3' >" + orders[i].o_status + "</label></td>" +
													"<td><label data-val='" + orders[i].o_date + "' class='old" + i + "4' >" + orders[i].o_date + "</label></td>" +
													"<td><label data-val='" + orders[i].o_amt + "' class='old" + i + "5' id='num' >" + orders[i].o_amt + "</label></td>" +
													"</tr>";
								segments.push(temp0); // push()可以把參數送進陣列最後一個位置
							}
					};
					
					// 【自訂函數 3】分頁掛資料
					
					function switchPage(pageIndex){
						let htmlStuff = "";
						counter = pageIndex * rowPerPage;
						let tempCounter0 = (counter + rowPerPage > segments.length)? segments.length : counter + rowPerPage;
						for(let i = counter; i < tempCounter0; i++){
							htmlStuff += segments[i];
						}
						dataArea.html(htmlStuff);
					}

					//【自訂函數 4】主程式函數
					function mainFunc(){
						headArea.html(
							"<th>訂單代號(o_id)<br>(READ-ONLY)</th>"
							+ "<th>課程代號<br>(p_id)</th>"
							+ "<th>用戶帳號<br>(u_id)</th>"
							+ "<th>訂單狀態<br>(o_status)</th>"
							+ "<th>訂單時間<br>(o_date)</th>"
							+ "<th>訂單總額<br>(o_amt)</th>"
						)

						let xhr0 = new XMLHttpRequest();
						let url = "<c:url value='/cart.controller/adminSelectTop100' />";
						xhr0.open("GET", url, true);
						xhr0.send();
						xhr0.onreadystatechange = function() {
							if (xhr0.readyState == 4 && xhr0.status == 200) {
								// 解析&暫存回傳資料
								parseSelectedRows(xhr0.responseText);
								// 掛資料
								let htmlStuff = "";
								let tempCounter0 = (counter + rowPerPage > segments.length)? segments.length : counter + rowPerPage;
								for(let i = counter; i < tempCounter0; i++){
									htmlStuff += segments[i]
								}
								dataArea.html(htmlStuff);
								// 掛分頁按鈕
									// 不知道為什麼寫成function呼叫之前一直出錯，照理來說應該寫成function比較好
								pageNum = Math.ceil((segments.length)/rowPerPage);
								let temp0 = "";
								for(let i = 0; i < pageNum; i++){
									temp0 += "<button class='pageBtn' data-index='" + i + "' type='button' id='btnPage'>" + (i + 1) + "</button>&nbsp;&nbsp;&nbsp;";
								}
								pageHref.html(temp0);
								$('.pageBtn').on('click', function(){
									let pageIndex = $(this).attr('data-index');
									switchPage(pageIndex);
								})
							}
						}
					}
					
				/*********************************************************************************************************/
					// 主程式
					mainFunc();

				})
				</script>		
		
		</body>
</html>