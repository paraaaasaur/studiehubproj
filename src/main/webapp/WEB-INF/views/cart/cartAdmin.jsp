<%@page import="tw.group5.controller.cart.*"%>
<%@page import="javax.naming.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="javax.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",-1);
%>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Cart Administrator Page</title>
		<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
		
	</head>
	<body style="width: 100%;">
		<h1>管理者頁面</h1>
		<button id="newRow">添加空白訂單列</button>
		<button id="cheat">添加訂單列(懶人用)</button>
		<form> 
			<h1>以下顯示的是資料庫至多20筆的最新訂單</h1>
			<!-- 秀出所有Order_Info (希望之後能每20項分一頁) -->
			<table border="2px">
				<thead>
					<th>DELETE BUTTON</th>
					<th style="background: aquamarine;" >訂單代號(o_id)<br>(READ-ONLY)</th>
					<th>課程代號<br>(p_id)</th>
					<th>課程名稱<br>(p_name)</th>
					<th>課程售價<br>(p_price)</th>
					<th>用戶帳號<br>(u_id)</th>
					<th>用戶名字<br>(u_firstname)</th>
					<th>用戶姓氏<br>(u_lastname)</th>
					<th>用戶郵箱<br>(u_email)</th>
					<th>訂單狀態<br>(o_status)</th>
					<th>訂單時間<br>(o_date)</th>
					<th>訂單總額<br>(o_amt)</th>
				</thead>
				<tbody id="dataArea">
				</tbody>
			</table>
			<h1 id='logo' style="background-color: red"></h1>
			<hr>
			<input name="counter" value="-1" id="counter" type="text" value="xxx" hidden>
			
		</form>
		<button name="todo" id="insert" value="insertAdmin" disabled>新增 (一次新增一筆)</button>
		<button name="todo" id="update" value="updateAdmin">修改 (可大幅修改)</button>
		<button name="todo" id="delete" value="deleteAdmin">刪除 (勾選者皆可刪除)</button>
		<button id="testxx" hidden>測試</button>
		<hr>
		<form>
			<button formmethod="GET" formaction="<c:url value='/' />">回首頁</button>
			<button formmethod="GET" formaction="<c:url value='/cart.controller/cartIndex' />">回購物車使用者首頁</button>
		</form>
		
		<button type="button" id="labelall" hidden>click me</button>
		
		<!--****************************************** S      C      R      I      P      T ******************************************-->
		
		<script src="/SpringMvcWebHW/js/jquery-3.6.0.min.js"></script>
		<script>
			$(function(){
				let logo = $('#logo');
				let dataArea = $('#dataArea');
				let oldRowsNum = 0;
				let dateFormat = /^(((199\d)|(20[0-1]\d)|(20(2[0-1])))\-((0\d)|(1[0-2]))\-(([0-2]\d)|(3[0-1])))( )((([0-1]\d)|(2[0-3])):[0-5]\d:[0-5]\d\.\d)$/;
				// 從1990-01-01到2021-12-31 // 沒有防大小月和２月

			/************************************************************************************/

				$('#testxx').on('click', function(){
					for(let i = 0; i< oldRowsNum; i++) {
						let ckboxValue = $('.ckbox' + i).val();
						let ckboxIsChecked = $('.ckbox' + i).is(':checked');
						console.log('ckboxValue' + i + ' = ' + ckboxValue);
						console.log('ckboxIsChecked? = ' + ckboxIsChecked);
					}
				})

				// [AJAX] admin massive delete
				$('#delete').on('click', function(){
					
					for(let i = 0; i < oldRowsNum; i++) {

						let ckboxIsChecked = $('.ckbox' + i).is(':checked');
						console.log('ckboxIsChecked? = ' + ckboxIsChecked);
						
						if(ckboxIsChecked) { // 不勾選 == 不存在 == 不會進此迴圈 == 檢查下一個checkbox值
							let ckboxValue = $('.ckbox' + i).val();
							console.log('ckboxValue' + i + ' = ' + ckboxValue);

							let o_id = $('.old' + i + '0').val()

							
							let queryString = 'o_id=' + o_id 
							console.log(queryString)
							
							let xhr = new XMLHttpRequest();
							let url = "<c:url value='/cart.controller/deleteAdmin' />";
							xhr.open("POST", url, true);
							xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); // 用處？？？？？？？？？
							xhr.send(queryString);
							xhr.onreadystatechange = function() {
								if (xhr.readyState == 4 && xhr.status == 200) {
									let result = JSON.parse(xhr.responseText);
									console.log(result.state);
								}
							}
						}
					}
					showTop20();
					logo.text('已刪除勾選之項目！');

				})

				// [AJAX] admin massive update
				$('#update').on('click', function(){
					for(let i = 0; i < oldRowsNum; i++) {
						let s0 = '.old' + i + '0'; let o_id = $(s0).val()
						let s1 = '.old' + i + '1'; let p_id = $(s1).val()
						let s2 = '.old' + i + '2'; let p_name = $(s2).val()
						let s3 = '.old' + i + '3'; let p_price = $(s3).val()
						let s4 = '.old' + i + '4'; let u_id = $(s4).val()
						let s5 = '.old' + i + '5'; let u_firstname = $(s5).val()
						let s6 = '.old' + i + '6'; let u_lastname = $(s6).val()
						let s7 = '.old' + i + '7'; let u_email = $(s7).val()
						let s8 = '.old' + i + '8'; let o_status = $(s8).val()
						let s9 = '.old' + i + '9'; let o_date = $(s9).val()
						let s10 = '.old' + i + '10'; let o_amt = $(s10).val()
						// 空值 & 類型防呆
						let nullCheck = (!o_id || !p_id || !p_name || !p_price || !u_id || !u_firstname || !u_lastname || !u_email || !o_status || !o_date || !o_amt);
						if(nullCheck) {
							logo.text('不得送出空值！');
							return;
						} else if(isNaN(p_id)) {
							logo.text('課程代號須為純數字！');
							return;
						} else if(isNaN(p_price)) {
							logo.text('課程售價須為純數字！');
							return;
						} else if(isNaN(o_amt)) {
							logo.text('訂單總額須為純數字！');
							return;
						} else if(false) {
							logo.text('訂單日期格式錯誤，請參照上方！');
							return;
						}
					console.log('LULLLLLLLLLLLLL');
					}
					for(let i = 0; i < oldRowsNum; i++) {
						let json = {
								'o_id' : parseInt($('.old' + i + '0').val()),
								'p_id' : parseInt( $('.old' + i + '1').val()),
								'p_name' : $('.old' + i + '2').val(),
								'p_price' : parseInt($('.old' + i + '3').val()),
								'u_id' : $('.old' + i + '4').val(),
								'u_firstname' : $('.old' + i + '5').val(),
								'u_lastname' : $('.old' + i + '6').val(),
								'u_email' :  $('.old' + i + '7').val(),
								'o_status' : $('.old' + i + '8').val(),
								'o_date' : $('.old' + i + '9').val(),
								'o_amt' : parseInt($('.old' + i + '10').val())
						}
						
						let xhr = new XMLHttpRequest();
						let url = "<c:url value='/cart.controller/updateAdmin' />";
						xhr.open("POST", url, true);
						xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
						xhr.send(JSON.stringify(json));
						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4 && xhr.status == 200) {
								let result = JSON.parse(xhr.responseText);
								console.log(result.state);
							}
						}
					}
					showTop20(); // 為什麼會不起作用？？？？非得讓我F5不可
					logo.text('更新完成！');
				})

				// [AJAX] admin single insert
				// 有時間的話改試form:form表單
				$('#insert').on('click', function(){
					// (a) 讀取到所有要insert進資料庫的欄位資料，建立查詢字串

					let o_id = $('.new0').val()
					let p_id = $('.new1').val()
					let p_name = $('.new2').val()
					let p_price = $('.new3').val()
					let u_id = $('.new4').val()
					let u_firstname = $('.new5').val()
					let u_lastname = $('.new6').val()
					let u_email = $('.new7').val()
					let o_status = $('.new8').val()
					let o_date = $('.new9').val()
					let o_amt = $('.new10').val()

					let json = {
								'o_id' : parseInt(o_id),
								'p_id' : parseInt(p_id),
								'p_name' : p_name,
								'p_price' : parseInt(p_price),
								'u_id' : u_id,
								'u_firstname' : u_firstname,
								'u_lastname' : u_lastname,
								'u_email' :  u_email,
								'o_status' : o_status,
								'o_date' : o_date,
								'o_amt' : parseInt(o_amt)
						}

					let nullCheck = (!o_id || !p_id || !p_name || !p_price || !u_id || !u_firstname || !u_lastname || !u_email || !o_status || !o_date || !o_amt);
						if(nullCheck) {
							logo.text('不得送出空值！');
							return;
						} else if(isNaN(p_id)) {
							logo.text('課程代號須為純數字！');
							return;
						} else if(isNaN(p_price)) {
							logo.text('課程售價須為純數字！');
							return;
						} else if(isNaN(o_amt)) {
							logo.text('訂單總額須為純數字！');
							return;
						} else if(false) { // 待修正
							logo.text('訂單日期格式錯誤，請參照上方！');
							return;
						}

					// let queryString = 'o_id=' + o_id + '&p_id=' + p_id + '&p_name=' + p_name + '&p_price=' + p_price
					// 						 + '&u_id=' + u_id + '&u_firstname=' + u_firstname + '&u_lastname=' + u_lastname
					// 						 + '&u_email=' + u_email + '&o_status=' + o_status + '&o_date=' + o_date + '&o_amt=' + o_amt;

					// (b) 送進Ajax處理

					let xhr = new XMLHttpRequest();
					let url = "<c:url value='/cart.controller/insertAdmin' />";
					xhr.open("POST", url, true);
					// xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); // 用處？？？？？？？？？
					xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8"); // 用處？？？？？？？？？
					// xhr.send(queryString);
					xhr.send(JSON.stringify(json));
					xhr.onreadystatechange = function() {
						if (xhr.readyState == 4 && xhr.status == 200) {
							let result = JSON.parse(xhr.responseText);
							logo.text(result.state);
							showTop20();
							$('#newRow').attr('disabled', false);
							$('#cheat').attr('disabled', false);
							$('#insert').attr('disabled', true);
						}
					}


				})
				

			$(window).on('load', function(){
				showTop20();
			});
				
			// [AJAX] 載入便顯示資料庫最新20筆訂單 (SELECT TOP(20))
			function  showTop20() {
				let dataArea = $('#dataArea');
				let xhr = new XMLHttpRequest();
				let url = "<c:url value='/cart.controller/initAdminPageData' />";
				xhr.open("GET", url, true);
				xhr.send();
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						dataArea.html(parseSelectedRows(xhr.responseText));
					}
				}
			} 
			function parseSelectedRows(orderList) {
				let orders = JSON.parse(orderList);
				let segment = "";
					   let totalPrice = 0;
					   oldRowsNum = orders.length;
					   for (let i = 0; i < orders.length; i++) {
						   totalPrice += orders[i].p_price;
							segment +=	 "<tr>" + 
												"<td><input name='ckbox' class='ckbox" + i + "' id='ckbox' type='checkbox' value=' + " + i + "'></td>" +
												"<td style='background: aquamarine;'>" + 
													  "<input required name='" + i + "0' type='text' class='old" + i + "0' value='" + orders[i].o_id + "' readonly></td>" +
												"<td><input required name='" + i + "1'  type='text' class='old" + i + "1' value='" + orders[i].p_id + "' ></td>" +
												"<td><input required name='" + i + "2'  type='text' class='old" + i + "2' value='" + orders[i].p_name + "' ></td>" +
												"<td><input required name='" + i + "3'  type='text' class='old" + i + "3' value='" + orders[i].p_price + "' id='num'></td> <!--price-->" +
												"<td><input required name='" + i + "4'  type='text' class='old" + i + "4' value='" + orders[i].u_id + "' ></td>" +
												"<td><input required name='" + i + "5'  type='text' class='old" + i + "5' value='" + orders[i].u_firstname + "' ></td>" +
												"<td><input required name='" + i + "6'  type='text' class='old" + i + "6' value='" + orders[i].u_lastname + "' ></td>" +
												"<td><input required name='" + i + "7'  type='text' class='old" + i + "7' value='" + orders[i].u_email + "' ></td>" +
												"<td><input required name='" + i + "8'  type='text' class='old" + i + "8' value='" + orders[i].o_status + "' ></td>" +
												"<td><input required name='" + i + "9'  type='text' class='old" + i + "9' value='" + orders[i].o_date + "' ></td>" +
												"<td><input required name='" + i + "10' type='text' class='old" + i + "10' value='" + orders[i].o_amt + "' id='num'></td>" +
												"</tr>";
					   }
					   segment += "<div>小計：" + totalPrice + "</div>";
					   return segment;
			};

				
				// 加入空白列
				let counter = -1;
				$('#newRow').on('click', function(){
					counter++;
					$('#counter').attr('value', counter + 1)
					let content = `
					<tr style="background-color: yellow;">
						<td></td>
							<td><input required type='text' class='new0' name='new0' id='num' value='由系統自動產生' readonly ></td>
							<td><input required type='text' class='new1' name='new1'  id='num'  ></td>
							<td><input required type='text' class='new2' name='new2'    ></td>
							<td><input required type='text' class='new3' name='new3' id='num'   ></td>
							<td><input required type='text' class='new4' name='new4'    ></td>
							<td><input required type='text' class='new5' name='new5'    ></td>
							<td><input required type='text' class='new6' name='new6'    ></td>
							<td><input required type='text' class='new7' name='new7'    ></td>
							<td><input required type='text' class='new8' name='new8'    ></td>
							<td><input required type='text' class='new9' name='new9'     value=` /* + fs (有bug，無法正確新增)*/ + `></td>
							<td><input required type='text' class='new10' name='new10' id='num'   ></td>
							</tr>
							`;
					$('#dataArea').append(content);
					$(this).attr('disabled', true);
					$('#cheat').attr('disabled', true);
					$('#insert').attr('disabled', false);
				})

				// 一鍵產生資料
				$('#cheat').on('click', function(){
					counter++;
					$('#counter').attr('value', counter + 1)
					let content = `
					<tr style="background-color: yellow;">
						<td></td>
							<td><input required type='text' class='new0' value='由系統自動產生' readonly name='new` + counter + `0'    ></td>
							<td><input required type='text' class='new1' value='555' name='new` + counter + `1'    ></td>
							<td><input required type='text' class='new2' value='CS_Conversation' name='new` + counter + `2'    ></td>
							<td><input required type='text' class='new3' value='777' name='new` + counter + `3'  id='num'  ></td>
							<td><input required type='text' class='new4' value='randomAlien' name='new` + counter + `4'    ></td>
							<td><input required type='text' class='new5' value='aaa' name='new` + counter + `5'    ></td>
							<td><input required type='text' class='new6' value='bbb' name='new` + counter + `6'    ></td>
							<td><input required type='text' class='new7' value='c@e.f' name='new` + counter + `7'    ></td>
							<td><input required type='text' class='new8' value='done' name='new` + counter + `8'    ></td>
							<td><input required type='text' class='new9' value='1907-01-23 00:11:22.0' name='new` + counter + `9'     value=` /* + fs (有bug，無法正確新增)*/ + `></td>
							<td><input required type='text' class='new10' value='777' name='new` + counter + `10' id='num'   value='1' readonly></td>
							</tr>
							`;
					$('#dataArea').append(content);
					$(this).attr('disabled', true);
					$('#newRow').attr('disabled', true);
					$('#insert').attr('disabled', false);
				})
				// func.03 不完整的數字檢查
				// 不知道為什麼抓不到新增列id=num的格子，明明F12可以看到他有ID

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

			})
			// 以下只是自訂日期(為了smalldatetime)
			/*let d = new Date();
			let year = d.getFullYear(); // 
			console.log("year = " + year);
			let date = d.getDate(); // 
			let monthIndex = d.getMonth();
			let months = {
					0: 　'01',
					1: 　'02',
					2: 　'03',
					3: 　'04',
					4: 　'05',
					5: 　'06',
					6: 　'07',
					7: 　'08',
					8: 　'09',
					9: 　'10',
					10:　'11',
					11:　'12'
			};
			let month = months[monthIndex];
			let hour = d.getHours();
			let minute = d.getMinutes();
			let second = d.getSeconds().toString();
			if(second < 10){
				second = '0' + second
			};
			if(minute < 10) {
				minute = '0' + minute
			};
			if(hour < 10) {
				hour = '0' + hour
			}
			
			let formatted = year + '-' + month + '-' + 
			date + '&nbsp;' + hour + ':' + 
			minute + ':' + second + '.0';
			let fs = formatted.toString(); */
			// -----------------------------------------------------------------
			</script>
</body>
</html>