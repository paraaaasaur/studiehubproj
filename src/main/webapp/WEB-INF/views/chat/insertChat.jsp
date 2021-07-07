<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>發表文章</title>
<script src="https://cdn.ckeditor.com/ckeditor5/28.0.0/classic/ckeditor.js"></script>
<script>
var hasError = false;
window.onload = function(){
	var sendData = document.getElementById("sendData");
	sendData.onclick = function(){
		//抓欄位資料
		var c_Date = null;
		var c_Class = document.getElementById("c_Class").value;
		var c_Title = document.getElementById("c_Title").value;
		var c_Conts = document.getElementById("c_Conts").value;
		var u_ID = null;
		var span1 = document.getElementById('result1c');
		var span2 = document.getElementById('result2c');
		
		if(!c_Title){
			setErrorFor(span1, "請輸入標題");
		} else{
			span1.innerHTML = "";
		}
		if(!c_Conts){
			setErrorFor(span2, "請輸入內容");
		} else{
			span2.innerHTML = "";
		}
		
		if (hasError){
			return false;
		}
		var xhr1 = new XMLHttpRequest();
		xhr1.open("POST", "<c:url value='/insertChat' />");
		var jsonInsertData = {
			"c_Date" : c_Date,
			"c_Class" : c_Class,
			"c_Title" : c_Title,
			"c_Conts" : c_Conts,
			"u_ID" : u_ID
		}
		xhr1.setRequestHeader("Content-Type", "application/json");
		xhr1.send(JSON.stringify(jsonInsertData));
		xhr1.onreadystatechange = function() {
			if (xhr1.readyState == 4 && xhr1.status == 200){
				result = JSON.parse(xhr1.responseText);
				//判斷回傳
				if(result.fail){
					span1.innerHTML = "<font color='red' >" + result.fail + "</font>";
				}else if(result.success){
					alert(result.success + "! 為您導回上一頁...");
					top.location='<c:url value='/goSelectAllChat' />';
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
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>
				
				<div align='center'>
					<div class="container">
					<br>
						<form>
							<table style="width: 750px;">
								<tr>
									<td style="width:60px;">標題:</td>
									<td style="width:650px;"><input type="text" name="c_Title" id="c_Title" style="width:650px;" placeholder="請輸入文章標題..."><span id="result1c">&nbsp;</span></td>
								</tr>
								<tr>
									<td style="width:60px;">類別:</td>
									<td style="width:650px;"><select name="c_Class" id="c_Class" style="width:650px;">
										<option value="英文">英文</option>
										<option value="西文">西文</option>
										<option value="法文">法文</option>
										<option value="德文">德文</option>
										<option value="俄文">俄文</option>
										<option value="日文">日文</option>
										<option value="韓文">韓文</option>
										<option value="其他">其他</option>
									</select></td>
								</tr>
								<tr>
									<td style="width:60px;">內容:</td>
									<td style="width:650px;"><input type="text" name="c_Conts" id="c_Conts" style="width:650px;"><span id="result2c">&nbsp;</span></td>
									
								</tr>
								<tr>
									<td style="width:60px;">內容:</td>
									<td style="width:650px;"><textarea name="c_Contss" id="c_Contss" placeholder="請輸入文章內容..."></textarea>
									</td>
								</tr>
								<tr>
									<td colspan="4" align="center" style="table-layout: fixed">
										<button type="button" class="primary" id="sendData">送出</button> &nbsp;
										<input type="reset" value="清除">
									</td>
								</tr>
							</table>
						</form>
					</div>
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
	ClassicEditor.create( document.querySelector( '#c_Contss' ))
		.then( editor => {
			console.log( editor );
		 })
		 .catch( error => {
			 console.error( error );
		 });
	</script>
</body>
</html>