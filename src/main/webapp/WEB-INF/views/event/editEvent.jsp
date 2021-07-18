<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>

<style type="text/css">
span.error {
	color: red;
	display: inline-block;
	font-size: 5pt;
}
</style>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">
<title>Studie Hub</title>


<script>
let div1 = null;
var u_id = "${loginBean.u_id}";
var userPicString = "${loginBean.pictureString}";
window.addEventListener("load", function() {
	//window.addEvenListener 網頁監聽器 
	//當瀏覽器從第一行到最後一行載完畢後才執行 function() 
	var logout = document.getElementById("logout");
		logout.onclick = function() {
			var xhr1 = new XMLHttpRequest();
			xhr.open("GET", "<c:url value='/logout.controller' />", true);
			xhr.send();
			xhr.onreadystatechange = function() {
				if (xhr1.readyState == 4 && xhr1.status == 200) {
					var result = JSON.parse(xhr1.responseText);
					if (result.success) {
						alert(result.success);
						top.location = '<c:url value='/' />';
					} else if (result.fail) {
						alert(result.fail);
						top.location = '<c:url value='/' />';
					}
				}
			}
		}

		var loginHref = document.getElementById('loginHref');
	    var signupHref = document.getElementById('signupHref');
	    var logoutHref = document.getElementById('logoutHref');
	    var userId = document.getElementById('userId');
	    var userPic = document.getElementById('userPic');
	    
	    if(u_id){
	    	loginHref.hidden = true;
	    	signupHref.hidden = true;
	    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
	    	userPic.src = userPicString;	//有登入就秀大頭貼
	    	userId.innerHTML = u_id;
	    	loginEvent.style.display = "block";
	    	loginALLEvent.style.display = "block";

	    }
	
})
</script>


</head>

<body class="is-preload">
	

	<!-- Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>
                
				<h2 align='center'>請輸入修改活動</h2>
				<hr>

				<form:form method="POST" modelAttribute="EventInfo" enctype='multipart/form-data'>
					
					<table border="1">

						<c:choose>

							<c:when test='${EventInfo.a_aid == null}'>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td>活動編號：<br>&nbsp;
									</td>
									<td><form:hidden path='a_aid' /> <%-- form:hidden 這個欄位不能被修改  在修改的時候才會出現--%>
										${EventInfo.a_aid}<br>&nbsp;</td>
								</tr>
							</c:otherwise>
						</c:choose>
						<tr>
						<tr>
							<td>活動名稱:</td>
							<td><form:input path="a_name" />
                            <form:errors path="a_name"  cssClass="error"/>		      
                            </td>
						</tr>
						<tr>
							<td>活動類型：<br>&nbsp;</td>
				            <td>
				            <form:select path="a_type" >
                            <form:option label="請挑選" value="" />
                            <form:options  items="${eventtype}" />
                            </form:select>
                            <form:errors path="a_type"  cssClass="error"/>		                                 
                            </td>
						</tr>
						<tr>
							<td>活動報名開始時間:</td>
							<td><form:input type="datetime-local" path="registration_starttime" />
							<form:errors path="registration_starttime"  cssClass="error"/>	
							</td>
						</tr>
						<tr>
							<td>活動報名結束時間</td>
							<td><form:input type="datetime-local" path="registration_endrttime" />
							<form:errors path="registration_endrttime"  cssClass="error"/>	
							</td>
						</tr>
						<tr>
							<td>活動開始時間:</td>
							<td><form:input type="datetime-local" path="Transienta_startTime" />
                            <form:errors path="Transienta_startTime"  cssClass="error"/>								
							</td>
						</tr>
						<tr>
							<td>活動結束時間</td>
							<td><form:input type="datetime-local" path="Transienta_endTime" />
							<form:errors path="Transienta_endTime"  cssClass="error"/>	
							</td>
						</tr>
						
						<tr>
							<td>活動地址:</td>
							<td><form:input path="a_address" />
							<form:errors path="a_address"  cssClass="error"/>		      
							</td>
						</tr>
						<tr>
							<td>活動說明:</td>
							<td><form:textarea path="comment" cols="100" rows="10" />
							<form:errors path="comment"  cssClass="error"/>		      
							</td>		
						</tr>
						<tr>
							<td>報名人數上限</td>
							<td><form:input  path="applicants" />
							<form:errors path="applicants"  cssClass="error"/>		      
							</td>
						</tr>
						<tr>
							<td>活動圖片:</td>
							<td><form:input path="eventImage" type="file" /></td>
							
						</tr>

						<tr>
							<td><input type="submit"></td>
						</tr>
					</table>
				</form:form>

			</div>
		</div>

		<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="../universal/sidebar.jsp"%>

	</div>

	<!-- Scripts -->
	<script
		src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

</body>
</html>