<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<title>刪除文章</title>
<script type="application/json" id="bootstrap-data">
	{
		"c_ID": "${fn:escapeXml(c_ID)}"
	}
</script>
<script>
const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
const { c_ID } = bootstrapData;

window.onload = function(){
	var divResult = document.getElementById('resultMsg');
	var ID = document.getElementById("c_ID");
	var c_Date = document.getElementById("c_Date");
	var c_Class = document.getElementById("c_Class");
	var c_Title = document.getElementById("c_Title");
	var c_Conts = document.getElementById("c_Conts");
	var u_ID = document.getElementById("u_ID");
	var xhr = new XMLHttpRequest();

	xhr.open("GET", "selectSingleChat/" + c_ID, true);
	xhr.send();
	var message = "";
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			var chatBean = JSON.parse(xhr.responseText);
			ID.value = chatBean.c_ID;
			c_Date.value = chatBean.c_Date;
			c_Class.value = chatBean.c_Class;
			c_Title.value = chatBean.c_Title;
			c_Conts.value = chatBean.c_Conts;
			u_ID.value = chatBean.u_ID;
		}
	}
	var deleteData = document.getElementById("deleteData");
	deleteData.onclick = function(){
		var result = confirm("確定要刪除文章 " + ID.value + " 嗎?");
		if(result){
			var xhr1 = new XMLHttpRequest();
			xhr1.open("DELETE", "deleteChatAdmin/" + c_ID, true);
			xhr1.send();
			xhr1.onreadystatechange = function() {
				if(xhr1.readyState == 4 && (xhr1.status == 200 || xhr1.status == 204)){
					result = JSON.parse(xhr1.responseText);
					if(result.fail){
						const font = document.createElement("font");
						font.color = 'red';
						font.textContent = result.fail;

						divResult.replaceChildren(font);
					} else if (result.success){
						alert("刪除成功! 點擊確認將為您導回上一頁...");
						top.location= 'goSelectAllChatAdmin';
					}
				}
			}
		}
	}
}
</script>
</head>
<body>
<div id="wrapper">
		<div id="main">
			<div class="inner">
				<div align='center'>
				<div align='center'>
					<%@include file="../universal/adminHeader.jsp"%>
					<br>
					<table style="line-height:20px;">
					  <tr>
					    <td align='left'>文章編號: </td>
						<td align='center'>&nbsp;<input type='text' disabled="disabled" id="c_ID"/></td>
					  </tr>
					  <tr>
					    <td align='left'>日期: </td>
						<td align='center'>&nbsp;<input type='text' disabled="disabled" id="c_Date"/></td>
					  </tr>
					  <tr>
					    <td align='left'>類別: </td>
						<td align='center'>&nbsp;<input type='text' disabled="disabled" id="c_Class"/></td>
					  </tr>
					  <tr>
					    <td align='left'>標題: </td>
						<td align='center'>&nbsp;<input type='text' disabled="disabled" id="c_Title"/></td>
					  </tr>
					  <tr>
					    <td align='left'>內容: </td>
						<td align='center'>&nbsp;<input type='text' disabled="disabled" id="c_Conts"/></td>
					  </tr>
					  <tr>
					    <td align='left'>帳號: </td>
						<td align='center'>&nbsp;<input type='text' disabled="disabled" id="u_ID"/></td>
					  </tr>
					  <tr>
						<td colspan='2' align='center'><button id='deleteData'>刪除</button></td>
					  </tr>
					</table>
					<div id='resultMsg' style="height: 18px; font-weight: bold;"></div>
					<div align='center'>
						<hr>
						<a href="goSelectAllChatAdmin">上一頁</a>
					</div>
				</div>
				<p />
			</div>
		</div>
	</div>
	<script	src="assets/js/jquery.min.js"></script>
	<script	src="assets/js/browser.min.js"></script>
	<script	src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
</body>
</html>