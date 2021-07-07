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
<title>Studie Hub</title>
	<style type="text/css">
	   span.error {
		color: red;
		display: inline-block;
		font-size: 5pt;
	}
	</style>
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

								<fieldset>
									<legend >新增訂單資料</legend> 
									<form:form method="POST" modelAttribute="emptyOrderInfo" enctype='multipart/form-data'>
										<Table>
										   <tr>  
										      <td>(1) 訂單代號(o_id)：<br>&nbsp;</td>
										        <td  width='360'>
										        	<form:input path="o_id" readonly="true" /><br>&nbsp;
<%-- 										        	<form:errors path='o_id'/> --%>
											  </td>
										      <td>(2) 課程代號(p_id)：<br>&nbsp;</td>
											  <td  width='360'>
											  		<form:input path='p_id' id="p_id" /><br>&nbsp;
											  		<form:errors path='p_id'  cssClass="error" />
											  </td>
										   </tr>
<!-- 										   <tr> -->
<!-- 										      <td>(3) 課程名稱(p_name)：<br>&nbsp;</td> -->
<!-- 										      <td  width='360'> -->
<%-- 										      	<form:input path="p_name" readonly="true" /><br>&nbsp;	 --%>
<!-- 											  </td> -->
<!-- 											   <td>(4) 課程價格(p_price)：<br>&nbsp;</td> -->
<!-- 										   	  <td> -->
<%-- 										      	<form:input path="p_price" readonly="true" /><br>&nbsp;	 --%>
<!-- 											  </td> -->
<!-- 										   </tr> -->
										   <tr>
										      <td>(5) 會員帳號(u_id)：<br>&nbsp;</td>
										        <td  width='360'>
										        	<form:input path="u_id" id="u_id" /><br>&nbsp;
										        	<form:errors path='u_id'  cssClass="error" />
											  </td>
<!-- 										      <td>(6) 會員名字(u_firstname)：<br>&nbsp;</td> -->
<%-- 											  <td  width='360'><form:input path='u_firstname' readonly="true" /><br>&nbsp;	 --%>
<!-- 											  </td> -->
										   </tr>
										 
<!-- 										   <tr> -->
<!-- 										      <td>(7) 會員姓氏(u_lastname)：<br>&nbsp;</td> -->
<!-- 										   	  <td> -->
<%-- 										   	  	 <form:input path="u_lastname" readonly="true" /><br>&nbsp; --%>
<!-- 										   	  </td> -->
<!-- 										   	  <td>(8) 會員信箱(u_email)：<br>&nbsp;</td> -->
<!-- 										   	  <td> -->
<%-- 										   	  	 <form:input path="u_email" readonly="true" /><br>&nbsp; --%>
<!-- 										   	  </td> -->
<!-- 										   </tr> -->
										   
										   <tr>
										      <td>(9) 訂單狀態(o_status)：<br>&nbsp;</td>
										   	  <td>
										   	  		<form:input path="o_status" id="o_status" /><br>&nbsp;
										   	  		<form:errors path="o_status"  cssClass="error" />
										   	  </td>
										   	  <td>(10) 訂單日期(o_date)：<br>&nbsp;</td>
										   	  <td>
										   	  	 <form:input path="o_date" readonly="true" /><br>&nbsp;
<%-- 										   	  	 <form:errors path="placeImage"  cssClass="error" /> --%>
										   	  </td>
										   </tr>
										   
										   <tr>
										       <td>(11) 訂單小計(o_amt)：<br>&nbsp;</td>
										   	   <td>
											   	  	 <form:input path="o_amt" id="o_amt" /><br>&nbsp;
										   	  		 <form:errors path="o_amt"  cssClass="error" />
										   	   </td>
										       <td colspan='4' align='center'><br>&nbsp;
											       <input type='submit' value='送出資料'>
												   <input type='button' id='cheat' value='一鍵生成'>
									           </td>
										   </tr>
										</Table>
										 
									</form:form>
									
								</fieldset>
								
								<a href="http:\/\/localhost:8080/studiehub/cart.controller/cartAdminSelect" >回上一頁</a>
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
				$(function(){
					let p_id = $('#p_id');
					let u_id = $('#u_id');
					let o_status = $('#o_status');
					let o_amt = $('#o_amt');
					
					$('#cheat').on('click', function(){
						p_id.val(1);
						u_id.val('fbk001');
						o_status.val('DONE');
						o_amt.val(99999);
					})
/* 
					$(p_id).on('focusout', function(){
						let xhr = new XMLHttpRequest();
						xhr.open("POST", "<c:url value='/cart.controller/selectProduct' />", true);
						xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
						xhr.send("p_id=" + p_id.val());
						xhr.on('readystatechange', function(){
							if(xhr.readyState == 4 && xhr.status == 200){


							}
						})

					})
 */

				})
			</script>

		</body>
</html>