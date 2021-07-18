<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>訂單後台管理系統</title>
	<style type="text/css">
	   span.error {
		color: red;
		display: inline-block;
		font-size: 100%;
	}
	</style>

<script>

	if("${success}"=="管理員登入成功"){alert('${"管理員登入成功!"}')}
	
	var adminId = "${adminId}";
	// 踢除非管理員
	if(!adminId){
	alert('您不具有管理者權限，請登入後再試。');
	top.location = "<c:url value='/gotoAdminIndex.controller' />";
}

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

								<fieldset>
									<h1 style="text-align: center;">維護訂單資料</h1>
									<form:form method="POST" modelAttribute="orderInfo" enctype='multipart/form-data'>
										<Table>
											<tr>
												<td>(1) 訂單代號：<br>&nbsp;</td>
												<td width='360'>
													<form:input path="o_id" id="o_id" placeholder="【選填 / 由系統自動產生】" /><br>&nbsp;
												</td>
												<td>(2) 課程代號：<br>&nbsp;</td>
												<td width='360'>
													<form:input path='p_id' id="p_id" /><br>&nbsp;
													<form:errors path='p_id' cssClass="error" id='p_idError' />
												</td>
											</tr>
											<tr>
												<td>(3) 課程名稱：<br>&nbsp;</td>
												<td width='360'>
													<form:input path="p_name" readonly="true" id="p_name" placeholder="【自動代入】" /><br>&nbsp;
												</td>
												<td>(4) 課程價格：<br>&nbsp;</td>
												<td>
													<form:input path="p_price" readonly="true" id="p_price" placeholder="【自動代入】" /><br>&nbsp;
												</td>
											</tr>
											<tr>
												<td>(5) 會員帳號：<br>&nbsp;</td>
												<td width='360'>
													<form:input path="u_id" id="u_id" /><br>&nbsp;
													<form:errors path='u_id' cssClass="error" />
												</td>
												<td>(6) 會員名字：<br>&nbsp;</td>
												<td width='360'>
													<form:input path='u_firstname' readonly="true" id="u_firstname" placeholder="【自動代入】" /><br>&nbsp;
												</td>
											</tr>
									
											<tr>
												<td>(7) 會員姓氏：<br>&nbsp;</td>
												<td>
													<form:input path="u_lastname" readonly="true" id="u_lastname" placeholder="【自動代入】" /><br>&nbsp;
												</td>
												<td>(8) 會員信箱：<br>&nbsp;</td>
												<td>
													<form:input path="u_email" readonly="true" id="u_email" inputmode="email" placeholder="【自動代入】" />
													<br>&nbsp;
												</td>
											</tr>
									
											<tr>
												<td>(9) 訂單狀態：<br>&nbsp;</td>
												<td>
													<form:select path="o_status" id="o_status">
														<form:option value="完成" label="完成" />
														<form:option value="處理中" label="處理中" />
														<form:option value="失效" label="失效" />
													</form:select><br>&nbsp;
													<form:errors path="o_status" cssClass="error" />
												</td>
												<td>(10) 訂單日期：<br>&nbsp;</td>
												<td>
													<form:input path="o_date" readonly="true" id="o_date" placeholder="【自動產生】" /><br>&nbsp;
													<!-- ❗ -->
												</td>
											</tr>
									
											<tr>
												<td>(11) 訂單小計：<br>&nbsp;</td>
												<td>
													<form:input path="o_amt" id="o_amt" /><br>&nbsp;
													<form:errors path="o_amt" cssClass="error" />
												</td>
												<td colspan='4' align='center'><br>&nbsp;
													<a class='button' href="<c:url value='/order.controller/adminSelect' />" >回上一頁</a>
													<input type='submit' value='送出資料'>
													<!-- <input type='button' id='cheat' value='一鍵生成'> -->
												</td>
											</tr>
										</Table>
									
									</form:form>
									
								</fieldset>
								
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
			<script src="${pageContext.request.contextPath}/assets/js/custom/TaJenUtils.js"></script>

		<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
		<script>
			$(function(){
				let o_id = $('#o_id');
				let p_id = $('#p_id');
				let p_name = $('#p_name');
				let p_price = $('#p_price');
				let u_id = $('#u_id');
				let u_firstname = $('#u_firstname');
				let u_lastname = $('#u_lastname');
				let u_email = $('#u_email');
				let o_status = $('#o_status');
				let o_date = $('#o_date');
				let o_amt = $('#o_amt');

				$(p_id).on('focusout', function(){
					if (!(p_id.val())) {
							p_name.val('【自動代入】');
							p_price.val('【自動代入】');
							return;
					}
					let xhr = new XMLHttpRequest();
					xhr.open("POST", "<c:url value='/order.controller/adminSelectProduct' />", true);
					xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					xhr.send("p_id=" + p_id.val());
					$(xhr).on('readystatechange', function(){
						if(xhr.readyState == 4 && xhr.status == 200){
							let json = (!xhr.responseText)? null : JSON.parse(xhr.responseText);
							if(json){
								p_name.val(json.p_Name);
								p_price.val(json.p_Price);
							} else {
								p_name.val('該課程編號(p_id)尚未存在！');
								p_price.val('該課程編號(p_id)尚未存在！');
							}
						}
					})
				});
				
				$(u_id).on('focusout', function(){
					if (!(u_id.val())) {
							u_firstname.val('【自動代入】');
							u_lastname.val('【自動代入】');
							u_email.val('【自動代入】');
							return;
					}
					let xhr = new XMLHttpRequest();
					xhr.open("POST", "<c:url value='/order.controller/adminSelectUser' />", true);
					xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					xhr.send("u_id=" + u_id.val());
					$(xhr).on('readystatechange', function(){
						if(xhr.readyState == 4 && xhr.status == 200){
							let json = (!xhr.responseText)? null : JSON.parse(xhr.responseText);
							if(json){
								u_firstname.val(json.u_firstname);
								u_lastname.val(json.u_lastname);
								u_email.val(json.u_email);
							} else {
								u_firstname.val('該會員帳號(u_id)尚未存在！');
								u_lastname.val('該會員帳號(u_id)尚未存在！');
								u_email.val('該會員帳號(u_id)尚未存在！');
							}
						}
					})
					return;
				});


			})
		</script>
		</body>
</html>