<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>會員資訊</title>
<script>
var uBean = "${loginBean}"; //取得login bean
var u_id = "${loginBean.u_id}"; //取得login id
var hasError = false;

window.onload = function(){
	var divResult = document.getElementById('resultMsg');
	var id = document.getElementById("u_id");
// 	var psw = document.getElementById("u_psw");
// 	var cfmpsw = document.getElementById("cfm_psw");
	var lastname = document.getElementById("u_lastname");
	var firstname = document.getElementById("u_firstname");
	var birthday = document.getElementById("u_birthday");
	var email = document.getElementById("u_email");
	var tel = document.getElementById("u_tel");
	// var gender = document.getElementById("u_gender");
	var gender = document.getElementsByName("u_gender");
	var address = document.getElementById("u_address");
// 	var img = document.getElementById("u_img");
	var updateData = document.getElementById("updateData");
	var xhr = new XMLHttpRequest();

	xhr.open("GET", "<c:url value='/showSingleUser.controller/" + u_id + "' />", true);
	xhr.send();
	var message = "";
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var userBean = JSON.parse(xhr.responseText);
			id.innerHTML = userBean.u_id;
// 			psw.value = userBean.u_psw;
			lastname.value = userBean.u_lastname;
			firstname.value = userBean.u_firstname;
			birthday.value = userBean.u_birthday;
			email.value = userBean.u_email;
			tel.value = userBean.u_tel;
			// gender.value = userBean.u_gender;
            var genderLength = gender.length;
            for(var i=0 ; i<genderLength ; i++){
                if(userBean.u_gender == gender[i].value){
                    gender[i].checked = 'checked';
                }
            }
			address.value = userBean.u_address;
// 			img.value = userBean.u_img;
			
		}
	}

	updateData.onclick = function(){
		var divResult = document.getElementById('resultMsg');
		var idV = document.getElementById("u_id").innerHTML;
// 		var pswV = document.getElementById("u_psw").value;
// 		var cfmpswV = document.getElementById("cfm_psw").value;
		var lastnameV = document.getElementById("u_lastname").value; //必填
		var firstnameV = document.getElementById("u_firstname").value; //必填
		var birthdayV = document.getElementById("u_birthday").value;
		var emailV = document.getElementById("u_email").value; //必填
		var telV = document.getElementById("u_tel").value;
		// var genderV = document.getElementById("u_gender").value;
		var genderRadio = document.getElementsByName("u_gender");
        var genderRadioLength = genderRadio.length;
        for (var i=0 ; i<genderRadioLength ; i++){
            if(genderRadio[i].checked){
                var genderV = genderRadio[i].value;
            }
        }
		var addressV = document.getElementById("u_address").value;
//	 	var imgV = document.getElementById("u_img").value;

		var thisPsw = "${loginBean.u_psw}"; //從sessionAttribute抓u_psw的值
		var thisImg = "${loginBean.u_img}"; //從sessionAttribute抓u_img的值

		var span0 = document.getElementById("result0c");
		var span1 = document.getElementById("result1c");
		var span2 = document.getElementById("result2c");

		if(!lastnameV){
			setErrorFor(span0, "姓氏為必填欄位!");
		} else{
			span0.innerHTML = "";
		}
		if(!firstnameV){
			setErrorFor(span1, "名字為必填欄位!");
		} else{
			span1.innerHTML = "";
		}
		if(!emailV){
			setErrorFor(span2, "電子郵件為必填欄位!");
		} else{
// 			hasError = false;
			span2.innerHTML = "";
		}
		if(hasError){
			return false;
		}
		var xhr1 = new XMLHttpRequest();
		xhr1.open("PUT", "<c:url value='/updateUserinfo.controller/' />" + u_id, true);
		var jsonUpdateData = {
			"u_id" : u_id,
			"u_lastname" : lastnameV,
			"u_firstname" : firstnameV,
			"u_birthday" : birthdayV,
			"u_email" : emailV,
			"u_tel" : telV,
			"u_gender" : genderV,
			"u_address" : addressV,
			"u_psw": thisPsw
// 			"u_img" : null
		}
		xhr1.setRequestHeader("Content-Type", "application/json");
   		xhr1.send(JSON.stringify(jsonUpdateData));

   		xhr1.onreadystatechange = function() {
   			if (xhr1.readyState == 4 && (xhr1.status == 200 || xhr1.status == 201) ) {
   				result = JSON.parse(xhr1.responseText);
   				if (result.fail) {
   			 		divResult.innerHTML = "<font color='red' >" + result.fail + "</font>";
   				} else if (result.success) {
   					divResult.innerHTML = "<font color='GREEN'>"
					   					+ result.success + "</font>";
					   					  span0.innerHTML = "";	
					   					  span1.innerHTML = "";
					   					  span2.innerHTML = "";
   	   			} 
   	   		}
   	   	}
		
	}


	function setErrorFor(input, message){
		input.innerHTML = "<font color='red' size='-2'>" + message + "</font>";
		hasError = true;
	}


	
}
</script>
</head>

<body>
<div align='center'>
<h3>會員資訊</h3>
<div id='resultMsg' style="height:18px; font-weight: bold;"></div>
<hr>
<br>
<table style="line-height:30px;">
<!-- <table border='1'> -->
	<tr>
		<td align='left'>帳號: </td>
		<td align='center'>&nbsp;<span id="u_id" style="font-weight: bold;"></span></td>
<!-- 		<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_id"/></td> -->
	</tr>
	
<!-- 	<tr> -->
<!-- 		<td align='left'>更改密碼: </td> -->
<!-- 		<td align='center'>&nbsp;<input type='text' id="u_psw"/></td> -->
<!-- 	</tr> -->
<!-- 	<tr> -->
<!-- 		<td align='left'>確認密碼: </td> -->
<!-- 		<td align='center'>&nbsp;<input type='text' id="cfm_psw"/></td> -->
<!-- 	</tr> -->
	<tr>
	
	<tr>
		<td align='left'>姓氏: </td>
		<td align='center'>&nbsp;<input type='text' id="u_lastname" required/></td>
		<td>&nbsp;<span id='result0c'></span></td>
	</tr>
	
	<tr>
		<td align='left'>名字: </td>
		<td align='center'>&nbsp;<input type='text' id="u_firstname" required/></td>
		<td>&nbsp;<span id='result1c'></span></td>
	</tr>
	
	<tr>
		<td align='left'>生日: </td>
		<td align='center'>&nbsp;<input type='date' id="u_birthday"/></td>
	</tr>
	
	<tr>
		<td align='left'>電子郵件: </td>
		<td align='center'>&nbsp;<input type='text' id="u_email" placeholder="...@email" required/></td>
		<td>&nbsp;<span id='result2c'></span></td>
	</tr>
	
	<tr>
		<td align='left'>電話: </td>
		<td align='center'>&nbsp;<input type='text' id="u_tel"/></td>
	</tr>
	
	<tr>
		<td align='left'>性別: </td>
		<td align='left'>&nbsp;<!--<input type='text' id="u_gender" placeholder="male / female"/><br> -->
        <label><input type="radio" name="u_gender" id="u_gender" value="男">男</label>
        <label><input type="radio" name="u_gender" id="u_gender" value="女">女</label>
        </td>
	</tr>
	
	<tr>
		<td align='left'>地址: </td>
		<td align='center'>&nbsp;<input type='text' id="u_address"/></td>
	</tr>
	
<!-- 	<tr> -->
<!-- 		<td align='left'>會員圖片: </td> -->
<!-- 		<td align='center'>&nbsp;<input type='file' id="u_img"/></td> -->
<!-- 	</tr> -->
	<tr>
		<td colspan='2' align='center'><button id='updateData'>儲存</button></td>
	</tr>
</table>

<br>
<hr>

<a href="<c:url value='gotoUserIndex.controller' />">上一頁</a>
</div>
</body>
</html>