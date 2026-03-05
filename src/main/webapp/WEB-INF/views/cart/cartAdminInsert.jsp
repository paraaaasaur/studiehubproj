<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">
<title>購物車後台管理系統</title>
<style type="text/css">
	span.error {
		color: red;
		display: inline-block;
		font-size: 100%;
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
		if (adminId) {
			logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
		}
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/adminHeader.jsp" %>


					<fieldset>
						<h1 style="text-align: center;">新增購物車品項資料</h1>
						<form:form method="POST" modelAttribute="emptyCartItem" enctype='application/x-www-form-urlencoded'>
							<table>
								<tr>
									<td>(1) 品項代號：<br>&nbsp;</td>
									<td width='360'>
										<form:input path="cart_id" disabled="true" value="[由系統自動產生]" /><br>&nbsp;
									</td>
									<td>(2) 課程代號：<br>&nbsp;</td>
									<td width='360'>
										<form:input path='p_id' /><br>&nbsp;
										<form:errors path='p_id' cssClass="error" />
									</td>
								</tr>
								<tr>
									<td>(3) 課程名稱：<br>&nbsp;</td>
									<td width='360'>
										<form:input path="p_name" readonly="true" placeholder="【由系統自動代入】" /><br>&nbsp;
									</td>
									<td>(4) 課程價格：<br>&nbsp;</td>
									<td>
										<form:input path="p_price" readonly="true" placeholder="【由系統自動代入】" /><br>&nbsp;
									</td>
								</tr>
								<tr>
									<td>(5) 會員帳號：<br>&nbsp;</td>
									<td width='360'>
										<form:input path="u_id" /><br>&nbsp;
										<form:errors path='u_id' cssClass="error" />
									</td>
									<td>(6) 會員名字：<br>&nbsp;</td>
									<td width='360'>
										<form:input path='u_firstname' readonly="true" placeholder="【由系統自動代入】" /><br>&nbsp;
									</td>
								</tr>
								<tr>
									<td>(7) 會員姓氏：<br>&nbsp;</td>
									<td>
										<form:input path="u_lastname" readonly="true" placeholder="【由系統自動代入】" /><br>&nbsp;
									</td>
									<td>(8) 品項加入日期：<br>&nbsp;</td>
									<td>
										<form:input path="cart_date" readonly="true" placeholder="【由系統自動產生】" /><br>&nbsp;
									</td>
								</tr>

								<tr>
									<td colspan='4' align='center'><br>&nbsp;
										<a class="button" href="cart.controller/adminSelect" >回上一頁</a>
										<input type='submit' value='送出資料'>
									</td>
								</tr>
							</table>
						</form:form>
					</fieldset>


			</div>
		</div>
		<%@include file="../universal/adminSidebar.jsp" %>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	<script src="assets/js/custom/TaJenUtils.js"></script>

	<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
	<script>
		$(function() {
			let p_id = $('#p_id');
			let p_name = $('#p_name');
			let p_price = $('#p_price');
			let u_id = $('#u_id');
			let u_firstname = $('#u_firstname');
			let u_lastname = $('#u_lastname');

			let cheat = $('#cheat');


			function autoFillInProductStuff() {
				if (!(p_id.val())) {
					p_name.val('【由系統自動代入】');
					p_price.val('【由系統自動代入】');
					return;
				}
				let xhr = new XMLHttpRequest();
				xhr.open("POST", "cart.controller/adminSelectProduct", true);
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
				});
			}

			$(p_id).on('focusout', autoFillInProductStuff);

			function autoFillInUserStuff() {
				if (!(u_id.val())) {
					u_firstname.val('【由系統自動代入】');
					u_lastname.val('【由系統自動代入】');
					return;
				}
				let xhr = new XMLHttpRequest();
				xhr.open("POST", "cart.controller/adminSelectUser", true);
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				xhr.send("u_id=" + u_id.val());
				$(xhr).on('readystatechange', function(){
					if(xhr.readyState == 4 && xhr.status == 200){
						let json = (!xhr.responseText)? null : JSON.parse(xhr.responseText);
						if(json){
							u_firstname.val(json.u_firstname);
							u_lastname.val(json.u_lastname);
						} else {
							u_firstname.val('該會員帳號(u_id)尚未存在！');
							u_lastname.val('該會員帳號(u_id)尚未存在！');
						}
					}
				})
			}

			$(u_id).on('focusout', autoFillInUserStuff);

			$(cheat).on('click', function() {
				p_id.val(1);
				u_id.val('tajen');
				autoFillInProductStuff();
				autoFillInUserStuff();
			});
		});
	</script>

</body>
</html>