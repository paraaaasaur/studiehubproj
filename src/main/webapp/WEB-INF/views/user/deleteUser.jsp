<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>刪除使用者</title>
<script>
var u_id = "${u_id}"; //showAllUsers點刪除，會把id放在url後面，再去controller放到attribute上

window.onload = function(){
	var divResult = document.getElementById('resultMsg');
	var id = document.getElementById("u_id");
	var psw = document.getElementById("u_psw");
	var lastname = document.getElementById("u_lastname");
	var firstname = document.getElementById("u_firstname");
	var birthday = document.getElementById("u_birthday");
	var email = document.getElementById("u_email");
	var tel = document.getElementById("u_tel");
	var gender = document.getElementById("u_gender");
	var address = document.getElementById("u_address");
	var img = document.getElementById("u_img");
	var xhr = new XMLHttpRequest();
	
	xhr.open("GET", "<c:url value='/showSingleUser.controller/" + u_id + "' />", true);
	xhr.send();
	var message = "";
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var userBean = JSON.parse(xhr.responseText);
			id.value = userBean.u_id;
			psw.value = userBean.u_psw;
			lastname.value = userBean.u_lastname;
			firstname.value = userBean.u_firstname;
			birthday.value = userBean.u_birthday;
			email.value = userBean.u_email;
			tel.value = userBean.u_tel;
			gender.value = userBean.u_gender;
			address.value = userBean.u_address;
			img.value = userBean.u_img;
			
		}
	}
	var deleteData = document.getElementById("deleteData");
	deleteData.onclick = function(){
		var result = confirm("確定要刪除會員: " + id.value + " 嗎?");
		if(result){
			var xhr1 = new XMLHttpRequest();
			xhr1.open("DELETE", "<c:url value='/user.controller/" + u_id + "' />", true);
			xhr1.send();
			xhr1.onreadystatechange = function() {
				if(xhr1.readyState == 4 && (xhr1.status == 200 || xhr1.status == 204)){
					result = JSON.parse(xhr1.responseText);
					if(result.fail){
						divResult.innerHTML = "<font color='red' >" + result.fail + "</font>";
					} else if (result.success){
						alert("刪除成功! 點擊確認將為您導回上一頁...");
						top.location= '<c:url value='/gotoShowAllUser.controller' />';
					}
				}
			}
		}
	}
	

}


</script>
</head>

<body>
<div align='center'>
<h3>刪除使用者資料</h3>
<div id='resultMsg' style="height:18px; font-weight: bold;"></div>
<hr>
<br>
<table style="line-height:30px;">
<!-- <table border='1'> -->
	<tr>
		<td align='left'>帳號: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_id"/></td>
	</tr>
	
	<tr>
		<td align='left'>密碼: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_psw"/></td>
	</tr>
	
	<tr>
		<td align='left'>姓氏: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_lastname"/></td>
	</tr>
	
	<tr>
		<td align='left'>名字: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_firstname"/></td>
	</tr>
	
	<tr>
		<td align='left'>生日: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_birthday"/></td>
	</tr>
	
	<tr>
		<td align='left'>電子郵件: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_email"/></td>
	</tr>
	
	<tr>
		<td align='left'>電話: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_tel"/></td>
	</tr>
	
	<tr>
		<td align='left'>性別: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_gender"/></td>
	</tr>
	
	<tr>
		<td align='left'>地址: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_address"/></td>
	</tr>
	
	<tr>
		<td align='left'>圖片: </td>
		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_img"/></td>
	</tr>
	<tr>
		<td colspan='2' align='center'><button id='deleteData'>刪除</button></td>
	</tr>
</table>

<br>
<hr>

<a href="<c:url value='/gotoShowAllUser.controller' />">上一頁</a>
</div>
</body>
</html>