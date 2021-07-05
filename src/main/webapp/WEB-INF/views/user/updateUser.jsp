<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<style>
    .tdTitle{
        width: 110px;
        text-align: center;
    }
    .tdContent{
        width: 300px;
    }
    .tdError{
        width: 200px;
        text-align: center;
        color: red;
    }
</style>
<title>會員資訊</title>
<script>

if("${successMessage}"=="修改成功"){alert('會員資料修改成功!');}

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
    var userId = document.getElementById('userId');
    var userPic = document.getElementById('userPic');
    if(u_id){
    	loginHref.hidden = true;
    	signupHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
    	userId.innerHTML = u_id;
    }
    
    //一鍵帶入
    $('#autoInput').on('click', function(){
    	$('#u_address').val("中央大學");
    	$('#u_address').val("中央大學");
    	$('#u_tel').val("0933509390");
    	$('#u_birthday').val("1997-08-18");
    })
}
</script>
</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%><!-- 帳號，密碼，確認密碼，姓，名，信箱， -->
				
				<div align='center'>
				<br>
				<form:form method="POST" action="updateUserinfo.controller" modelAttribute="userBean" enctype='multipart/form-data'>
						<table style="width: 750px;">
							<tr>
								<td class="tdTitle">帳號:</td>
								<td class="tdContent"><b>&nbsp;&nbsp;&nbsp;&nbsp;${userBean.u_id}</b></td>
								<td class="tdError">&nbsp;</td>
							</tr>
							<tr>
								<td class="tdTitle">姓氏:</td>
								<td class="tdContent"><form:input path="u_lastname"/></td>
								<td class="tdError"><form:errors path="u_lastname" cssClass="error"/></td>
							</tr>
							<tr>
								<td class="tdTitle">名字:</td>
								<td class="tdContent"><form:input path="u_firstname"/></td>
								<td class="tdError"><form:errors path="u_firstname" cssClass="error"/></td>
							</tr>
							<tr>
								<td class="tdTitle">地址:</td>
								<td class="tdContent"><form:input path="u_address"/></td>
								<td class="tdError"><form:errors path="u_address" cssClass="error"/></td>
							</tr>
							<tr>
								<td class="tdTitle">信箱:</td>
								<td class="tdContent"><form:input path="u_email"/></td>
								<td class="tdError"><form:errors path="u_email" cssClass="error"/></td>
							</tr>
							<tr>
								<td class="tdTitle">電話:</td>
								<td class="tdContent"><form:input path="u_tel" /></td>
								<td class="tdError"><form:errors path="u_tel" cssClass="error"/></td>
							</tr>
							<tr>
								<td class="tdTitle">生日:</td>
								<td class="tdContent"><form:input type="date" path="u_birthday" /></td>
								<td class="tdError"><form:errors path="u_birthday" cssClass="error"/></td>
							</tr>
							<tr>
								<td class="tdTitle">性別:</td>
								<td class="tdContent"><form:radiobuttons path="u_gender" items="${genderList}"/></td>
								<td class="tdError"><form:errors path="u_gender" cssClass="error"/></td>
							</tr>
							<tr>
								<td class="tdTitle">照片:</td>
								<td class="tdContent"><img id="output" width="30%" src="${loginBean.pictureString }"/><form:input path="uploadImage" type='file' onchange="loadFile(event)" /></td>
								
								<td class="tdError"><form:errors path="uploadImage" cssClass="error"/></td>
							</tr>
							
							<tr>
							    <td colspan='3' align='center'><button type="button" id="autoInput">一鍵</button> &nbsp;<input class="primary" type='submit' value="儲存"></td>
						   </tr>
						</table>
					</form:form>
				</div>
			</div>
		</div>
		<%@include file="../universal/sidebar.jsp"%>
	</div>
	<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/browser.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/breakpoints.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/util.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
	<script>
  var loadFile = function(event) {
    var reader = new FileReader();
    reader.onload = function(){
      var output = document.getElementById('output');
      output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
  };
</script>
</body>
</html>