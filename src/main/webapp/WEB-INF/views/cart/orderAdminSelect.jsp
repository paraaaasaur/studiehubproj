<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>訂單後台管理</title>

<script>

	if("${success}"=="管理員登入成功"){alert('${"管理員登入成功!"}')}
	
	var adminId = "${adminId}";
	window.onload = function(){
	// console.log(adminId);
		
		//如果有登入，隱藏登入標籤
		var loginHref = document.getElementById('loginHref');
		var logoutHref = document.getElementById('logoutHref');
		var userId = document.getElementById('userId');
		var userPic = document.getElementById('userPic');
		if(adminId){
			loginHref.hidden = true;
			logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
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
								<%@include file="../universal/adminHeader.jsp" %>
								
								<h1>訂單管理</h1>
								
								<label id='searchBarLabel'><input type='search' id='searchBar'>搜尋</label>
								<button type="submit" id="searchBtn">查詢</button>
								<select id='searchBy'>
									<option value='u_id'selected>以使用者帳號(u_id)</option>
									<option value='o_id'>以帳單編號(o_id)</option>
									<option value='p_id'>以課程代號(p_id)</option>
									<option value='p_name'>以課程名稱(p_name)</option>
									<option value='u_lastname'>以使用者姓氏(u_lastname)</option>
									<option value='u_firstname'>以使用者名字(u_firstname)</option>
									<option value='o_status'>以訂單狀態(o_status)</option>
									<option value='o_amt'>以訂單小計(o_amt)</option>
									<option value='o_date'>以訂單日期(o_date)</option>
								</select>
								<h1 id='topLogo'></h1>
								<hr id="pageHref">
								<form>
									<!-- 秀出所有Order_Info (希望之後能每20項分一頁) -->
									<table border="2px">
										<thead id="theadArea"></thead>
										<tbody id="tbodyArea"></tbody>
									</table>
									<h1 id='logo' style="background-color: red"></h1>
									<hr>
									
								</form>
								<button name="todo" id="insert" value="insertAdmin" 
								onclick="location.href='http:\/\/localhost:8080/studiehub/order.controller/adminInsert'">新增</button>
								<button name="todo" id="delete" value="deleteAdmin">刪除勾選資料</button>
								<button id='toAdminIndexBtn'>回管理者首頁</button>
								<button id='toClientIndexBtn'>回使用者首頁</button>
								

								

						</div>
					</div>

				<!-- Sidebar -->
				<!-- 這邊把side bar include進來 -->
				<%@include file="../universal/adminSidebar.jsp" %>  

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
				let segments = [];
				let counter = 0;
				let pageNum = 0;
				let rowNum = 0;
				let rowPerPage = 10;
				let maxPageNum = 10;
				// let dateFormat = /^(((199\d)|(20[0-1]\d)|(20(2[0-1])))\-((0\d)|(1[0-2]))\-(([0-2]\d)|(3[0-1])))( )((([0-1]\d)|(2[0-3])):[0-5]\d:[0-5]\d\.\d)$/;
				// 從1990-01-01到2021-12-31 // 沒有防大小月和２月
				
				$(function(){
					let topLogo = $('#topLogo');
					let logo = $('#logo');
					let tbodyArea = $('#tbodyArea');
					let theadArea = $('#theadArea');
					let pageHref = $('#pageHref');
					let searchBarLabel = $('#searchBarLabel');
					let searchBy = $('#searchBy');
					let searchBar = $('#searchBar');
					/*********************************************************************************************************/
					// 【自訂函數 #】searchBar樣式隨使用者的選擇變化
					$(searchBy).on('change', function(){
						if(this.value == 'o_date'){
							$(searchBarLabel).html(
								"<input type='datetime-local' step='1' id='searchDateStart'>起始時間" + 
								"<input type='datetime-local' step='1' id='searchDateEnd'>結束時間<br>搜尋"
							);
						} else if(this.value == 'u_id' || this.value == 'u_firstname' || this.value == 'u_lastname'){
							$(searchBarLabel).html("<input type='search' id='searchBar'>搜尋");
						} else if(this.value == 'o_amt' || this.value == 'o_id' || this.value == 'p_id'){
							$(searchBarLabel).html(
								"<input type='search' id='searchMin'>最小值" +
								"<input type='search' id='searchMax'>最大值<br>搜尋"  
							);
						} else if(this.value == 'o_status'){
							$(searchBarLabel).html(
								"<select id='searchBar'>" +
								"<option value='完成' selected>完成</option>" +
								"<option value='處理中'>處理中</option>" +
								"<option value='失效'>失效</option>" +
								"</select>"
							);
						}
					})
					// 重新導向
					$('#toAdminIndexBtn').on('click', function(){
						top.location = "<c:url value='/gotoAdminIndex.controller' />";
					})
					$('#toClientIndexBtn').on('click', function(){
						top.location = "<c:url value='/' />";
					})


					// 【自訂函數 1】go to UPDATE page
					function toUpdatePage(oid){
						// let url = "<c:url value='/cart.controller/cartAdminUpdate/' />" + oid; // ❓
						let url = "http://localhost:8080/studiehub/order.controller/adminUpdate/" + oid;
						console.log(url);
						top.location = url;
					}
					
					// 【自訂函數 2】分頁掛資料
					
					function switchPage(pageIndex){
						let htmlStuff = "";
						counter = pageIndex * rowPerPage;
						let tempCounter0 = (counter + rowPerPage > segments.length)? segments.length : counter + rowPerPage;
						for(let i = counter; i < tempCounter0; i++){
							htmlStuff += segments[i];
						}
						tbodyArea.html(htmlStuff);
					}
									
					// 【自訂函數 3】查詢功能
					$('#searchBtn').on('click', function(){
						let xhr = new XMLHttpRequest();
						let queryString = "";

						let forDate = (searchBy.val() == 'o_date');
						let forSingle = (searchBy.val() == 'u_id' || searchBy.val() == 'u_firstname' || searchBy.val() == 'u_lastname' ||
												searchBy.val() == 'p_name' || searchBy.val() == 'o_status');
						let forRange = (searchBy.val() == 'o_amt' || searchBy.val() == 'o_id' || searchBy.val() == 'p_id');

						if(forDate) {// 日期範圍查詢
							queryString = 'searchBy=' + searchBy.val() + '&searchBar=' + ($('#searchDateStart').val() + ',' + $('#searchDateEnd').val());
						} else if(forSingle) {// 單值查詢
							queryString = 'searchBy=' + searchBy.val() + '&searchBar=' + $('#searchBar').val();
						} else if(forRange) {// 數值範圍查詢
							queryString = 'searchBy=' + searchBy.val() + '&searchBar=' + ($('#searchMin').val() + ',' + $('#searchMax').val());
						}
						console.log(queryString);
						xhr.open('POST', "<c:url value='/order.controller/adminSearchBar' />", true);
						xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); // ❓
						xhr.send(queryString);
						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4 && xhr.status == 200) {
								tbodyArea.html("");
								pageHref.html("");
								// 解析&暫存回傳資料
								parseSelectedRows(xhr.responseText);
								// 掛topLogo
								topLogo.text("以下是資料庫最新" + segments.length + "筆訂單");
								// 掛資料
								let htmlStuff = "";
								let tempCounter0 = (counter + rowPerPage > segments.length)? segments.length : counter + rowPerPage;
								for(let i = counter; i < tempCounter0; i++){
									htmlStuff += segments[i]
								}
								tbodyArea.html(htmlStuff);
								// 掛頁籤
								pageNum = Math.ceil((segments.length)/rowPerPage); // 57 / 10 => 6 個頁籤
								let temp0 = "";
								let tempPageNum = (pageNum > maxPageNum)? maxPageNum : pageNum; // 資料再多也只會顯示10個頁籤
								for(let i = 0; i < tempPageNum; i++){
									temp0 += "<button class='pageBtn' data-index='" + i + "' type='button' id='btnPage" + i + "'>" + (i + 1) + "</button>&nbsp;&nbsp;&nbsp;";
								}
								pageHref.html(temp0);
								for(let i = 0; i < tempPageNum; i++){
									$('#btnPage' + i).on('click', function(){
										$('.pageBtn').removeClass('primary');
										$('#btnPage' + i).addClass('primary');
									})
								}
								$('#btnPage0').addClass('primary');
								$('.pageBtn').on('click', function(){
									let pageIndex = $(this).attr('data-index');
									switchPage(pageIndex);
								})
							}

						}
					})
						
					// 【自訂函數 4】顯示資料庫最新100筆訂單 (SELECT TOP(100)) + 掛資料
					function showTop100() {
						let xhr = new XMLHttpRequest();
						let url = "<c:url value='/order.controller/adminSelectTop100' />";
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
								tbodyArea.html(htmlStuff);
							}
						}
					} 

					// 【自訂函數 5】解析回傳資料 & 暫存進segments陣列 & 更新全域變數值
					function parseSelectedRows(map) {
						parsedMap = JSON.parse(map);
						let orders = parsedMap.list;
							let totalPrice = 0;
							rowNum = (orders)? orders.length : 0;
							segments = [];
							for (let i = 0; i < orders.length; i++) {
								totalPrice += orders[i].p_price;
								let temp0 =	 "<tr>" + 
													"<td><input name='ckbox' class='ckbox" + i + "' id='ckbox" + i + "' type='checkbox' value=' + " + i + "'><label for='ckbox" + i + "'></label></td>" +
													"<td><label data-val='" + orders[i].o_id + "' class='old" + i + "0' >" + orders[i].o_id + "</label></td>" +
													"<td><label data-val='" + orders[i].p_id + "' class='old" + i + "1' >" + orders[i].p_id + "</label></td>" +
													"<td><label data-val='" + orders[i].u_id + "' class='old" + i + "2' >" + orders[i].u_id + "</label></td>" +
													"<td><label data-val='" + orders[i].o_status + "' class='old" + i + "3' >" + orders[i].o_status + "</label></td>" +
													"<td><label data-val='" + orders[i].o_date + "' class='old" + i + "4' >" + orders[i].o_date + "</label></td>" +
													"<td><label data-val='" + orders[i].o_amt + "' class='old" + i + "5' id='num' >" + orders[i].o_amt + "</label></td>" +
													"<td width='120'><a href='http://localhost:8080/studiehub/order.controller/adminUpdate/" + orders[i].o_id + "'>修改</a></td>" +
													"</tr>";
								segments.push(temp0);
							}
					};

					// 【自訂函數 6】DELETE
					$('#delete').on('click', function(){
						let tempCounter1 = 0;
						let result = null;
						for(let i = 0; i < rowNum; i++) {
							let ckboxIsChecked = $('.ckbox' + i).is(':checked');
							tempCounter1 ++;
							
							if(ckboxIsChecked) { 
								let ckboxValue = $('.ckbox' + i).val();
								let o_id = $('.old' + i + '0').attr('data-val');
								
								let xhr = new XMLHttpRequest();
								xhr.open("POST", "<c:url value='/order.controller/deleteAdmin' />", true);
								xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); // ❓
								xhr.send('o_id=' + o_id);
								xhr.onreadystatechange = function() {
									if (xhr.readyState == 4 && xhr.status == 200) {
										result = JSON.parse(xhr.responseText);
										console.log(result.state);
										// showTop100();
										if(tempCounter1 == rowNum){ // ❗
											pageHref.html("");
											tbodyArea.html("");
											mainFunc();
											
											// if(result.status == "true"){
											// 	logo.text('已刪除勾選之項目！'); // ❗
											// } else {
											// 	logo.text('刪除未成功！');
											// }
										}
									}
								}
							}
						}
					})

					//【自訂函數 7】主程式函數
					function mainFunc(){
						// console.log('Start of mainFunc()');
						theadArea.html(
								"<th>DELETE BUTTON</th>"
								+ "<th>訂單代號(o_id)<br>(READ-ONLY)</th>"
								+ "<th>課程代號<br>(p_id)</th>"
								+ "<th>用戶帳號<br>(u_id)</th>"
								+ "<th>訂單狀態<br>(o_status)</th>"
								+ "<th>訂單時間<br>(o_date)</th>"
								+ "<th>訂單總額<br>(o_amt)</th>"
								+ "<th>操作</th>"
						);

						let xhr0 = new XMLHttpRequest();
						let url = "<c:url value='/order.controller/adminSelectTop100' />";
						xhr0.open("GET", url, true);
						xhr0.send();
						xhr0.onreadystatechange = function() {
							if (xhr0.readyState == 4 && xhr0.status == 200) {
								// 解析&暫存回傳資料
								parseSelectedRows(xhr0.responseText);
								// 掛topLogo
								topLogo.text("以下是資料庫最新" + segments.length + "筆訂單");
								// 掛資料
								let htmlStuff = "";
								let tempCounter0 = (counter + rowPerPage > segments.length)? segments.length : counter + rowPerPage;
								for(let i = counter; i < tempCounter0; i++){
									htmlStuff += segments[i]
								}
								tbodyArea.html(htmlStuff);
								// 掛頁籤
								pageNum = Math.ceil((segments.length)/rowPerPage);
								let temp0 = "";
								let tempPageNum = (pageNum > maxPageNum)? maxPageNum : pageNum;
								for(let i = 0; i < tempPageNum; i++){
									temp0 += "<button class='pageBtn' data-index='" + i + "' type='button' id='btnPage" + i + "'>" + (i + 1) + "</button>&nbsp;&nbsp;&nbsp;";
								}
								pageHref.html(temp0);
								for(let i = 0; i < tempPageNum; i++){
									$('#btnPage' + i).on('click', function(){
										$('.pageBtn').removeClass('primary');
										$('#btnPage' + i).addClass('primary');
									})
								}
								$('#btnPage0').addClass('primary');
								
								$('.pageBtn').on('click', function(){
									let pageIndex = $(this).attr('data-index');
									switchPage(pageIndex);
								})
							}
						}
						// console.log('End of mainFunc()');
					}
					
				/*********************************************************************************************************/
					// 主程式
					mainFunc();
					
 					

/* 
					$('input#num').on('focusout', function(){
						if(!isNaN($(this).val())){
							console.log('if')
							return;
						} else {
							console.log('else')
							logo.text('Only numbers are allowed.')
							$(this).val('')
						}
					})
	
	
					// func.04 
					
					$('i#gcIcon', 'button#gcBtn').on('click', function(event){
						event.preventDefault();
					})
	
					// func.05 刪除功能防呆 ❌施工中
					$('input#ckbox').on('click', function(){
						let ckboxes = $('input#ckbox:checked');
						$('#delete').attr('disabled', true);
							if($(ckboxes).length == 0 || $(ckboxes).length == null) {
								console.log('(if)' + $(ckboxes).length);
							} else {
								$('#delete').attr('disabled', false);
								console.log('(else)' + $(ckboxes).length);		
							}
					})
	 */
				})
				</script>		
		
		</body>
</html>