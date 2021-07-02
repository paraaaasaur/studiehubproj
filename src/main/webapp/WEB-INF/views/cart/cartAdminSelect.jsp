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
    
    //如果有登入，隱藏登入標籤
    var loginHref = document.getElementById('loginHref');
    var signupHref = document.getElementById('signupHref');
    var logoutHref = document.getElementById('logoutHref');
    var userPic = document.getElementById('userPic');
    if(u_id){
    	loginHref.hidden = true;
    	signupHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
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
								<%@include file="../universal/header.jsp" %>
								
								
								<h1>管理者頁面</h1>
								<input type='text' id='searchBar'><label for='searchBar'>模糊搜尋：</label><button type="submit" id="searchBtn">查詢</button>
								<select id='searchBy'>
									<option value='o_id' selected>以帳單編號</option>
									<option value='p_id'>以課程代號</option>
									<option value='p_name'>以課程名稱</option>
									<option value='u_id'>以使用者帳號</option>
									<option value='u_lastname'>以使用者姓氏</option>
									<option value='u_firstname'>以使用者名字</option>
									<option value='o_status'>以訂單狀態</option>
									<option value='o_date'>以訂單日期</option>
								</select>
								<hr>
								<form>
									<h1 id='topLogo'>以下顯示的是資料庫的至多200筆訂單</h1>
									<!-- 秀出所有Order_Info (希望之後能每20項分一頁) -->
									<table border="2px">
										<thead id="headArea"></thead>
										<tbody id="dataArea"></tbody>
									</table>
									<h1 id='logo' style="background-color: red"></h1>
									<hr>
<!-- 									<input name="counter" value="-1" id="counter" type="text" value="xxx" hidden> -->
									
								</form>
								<button name="todo" id="insert" value="insertAdmin">新增</button>
								<button name="todo" id="delete" value="deleteAdmin">刪除勾選資料</button>
								<button id="testxx" hidden="true">測試</button>
								<hr>
								<form>
									<button formmethod="GET" formaction="<c:url value='/' />">回首頁</button>
									<button formmethod="GET" formaction="<c:url value='/cart.controller/cartIndex' />">回購物車使用者首頁</button>
								</form>
								
								<button type="button" id="labelall" hidden="true">click me</button>
								

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

		<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
			<script>
				// go to UPDATE page
				function toUpdatePage(oid){
					// let url = "<c:url value='/cart.controller/cartAdminUpdate/' />" + oid; // ❓
					let url = "http://localhost:8080/studiehub/cart.controller/cartAdminUpdate/" + oid;
					console.log(url);
					top.location = url;
				}
				
				$(function(){
					let logo = $('#logo');
					let dataArea = $('#dataArea');
					let headArea = $('#headArea');
					let oldRowsNum = 0;
					// let dateFormat = /^(((199\d)|(20[0-1]\d)|(20(2[0-1])))\-((0\d)|(1[0-2]))\-(([0-2]\d)|(3[0-1])))( )((([0-1]\d)|(2[0-3])):[0-5]\d:[0-5]\d\.\d)$/;
					// 從1990-01-01到2021-12-31 // 沒有防大小月和２月
					

					
				/*********************************************************************************************************/
				
					$(window).on('load', function(){
						headArea.html(
								"<th>DELETE BUTTON</th>"
								+ "<th>訂單代號(o_id)<br>(READ-ONLY)</th>"
								+ "<th>課程代號<br>(p_id)</th>"
								+ "<th>用戶帳號<br>(u_id)</th>"
								+ "<th>訂單狀態<br>(o_status)</th>"
								+ "<th>訂單時間<br>(o_date)</th>"
								+ "<th>訂單總額<br>(o_amt)</th>"
								+ "<th>操作</th>"
						)
						showTop20();
					});
					
					// Search Bar 模糊搜尋
					$('#searchBtn').on('click', function(){
						let searchBy = $('#searchBy').val();
						let searchBar = $('#searchBar').val();
						let xhr = new XMLHttpRequest();
						xhr.open('GET', "<c:url value='/cart.controller/adminSearchBar' />", true);
						xhr.send('?searchBy=' + searchBy + '&searchBar=' + searchBar);
						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4 && xhr.status == 200) {
								dataArea.html(parseSelectedRows(xhr.responseText));
							}
						}
					})
						
					// [AJAX] 載入便顯示資料庫最新20筆訂單 (SELECT TOP(20))
					function showTop20() {
						let dataArea = $('#dataArea');
						let xhr = new XMLHttpRequest();
						let url = "<c:url value='/cart.controller/adminSelectTop20' />";
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
														"<td><input name='ckbox' class='ckbox" + i + "' id='ckbox" + i + "' type='checkbox' value=' + " + i + "'><label for='ckbox" + i + "'></label></td>" +
														"<td><input required name='" + i + "0' type='text' class='old" + i + "0' value='" + orders[i].o_id + "' readonly></td>" +
														"<td><input required name='" + i + "1' type='text' class='old" + i + "1' value='" + orders[i].p_id + "' ></td>" +
														"<td><input required name='" + i + "2' type='text' class='old" + i + "2' value='" + orders[i].u_id + "' ></td>" +
														"<td><input required name='" + i + "3' type='text' class='old" + i + "3' value='" + orders[i].o_status + "' ></td>" +
														"<td><input required name='" + i + "4' type='text' class='old" + i + "4' value='" + orders[i].o_date + "' ></td>" +
														"<td><input required name='" + i + "5' type='text' class='old" + i + "5' value='" + orders[i].o_amt + "' id='num'></td>" +
														"<td width='120'><a href='http://localhost:8080/studiehub/cart.controller/cartAdminUpdate/" + orders[i].o_id + "'>修改</a></td>" +
														"</tr>";
							   }
							   segment += "<div>小計：" + totalPrice + "</div>";
							   return segment;
					};	

				/*********************************************************************************************************/
					// DELETE
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
	// 					logo.text('已刪除勾選之項目！');
					})
					
				/*********************************************************************************************************/
		

					
	

	
					
	
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
				</script>		
		
		</body>
</html>