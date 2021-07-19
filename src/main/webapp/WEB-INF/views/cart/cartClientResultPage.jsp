<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet' href="${pageContext.request.contextPath}/assets/css/main.css">
<title>顧客回傳頁面</title>
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
							
							<h1 style="text-align: center;">顧客回傳頁面</h1>
							
							<table >
								<thead id='theadArea'></thead>
								<tbody id="tbodyArea" ></tbody>
							</table>
								

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
			<script src="${pageContext.request.contextPath}/assets/js/custom/TaJenUtils.js"></script>

		<!--********************************** M      Y      S      C      R      I      P      T ******************************************-->
	
		<script>

			// let segment = '';
			// segment += '<h2>請謹慎保留您的<font color="red">交易編號(' + 'ecpayResultJson.MerchantTradeNo' + ')</font>，以利日後客服為您查詢用。</h2>';
			// segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '支付方式' + '</li><li style="width: 70%;">' + '信用卡支付' + '</li></ul></td></tr>';
			// segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '您的姓名' + '</li><li style="width: 70%;">' + 'ecpayResultJson.CustomField2' + '</li></ul></td></tr>';
			// segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '交易時間' + '</li><li style="width: 70%;">' + 'ecpayResultJson.TradeDate' + '</li></ul></td></tr>';
			// segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '交易編號' + '</li><li style="width: 70%;">' + 'ecpayResultJson.MerchantTradeNo' + '</li></ul></td></tr>';
			// segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '交易金額' + '</li><li style="width: 70%;">' + 'ecpayResultJson.TradeAmt' + '</li></ul></td></tr>';
			// segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '手續費' + '</li><li style="width: 70%;">' + 'ecpayResultJson.PaymentTypeChargeFee' + '</li></ul></td></tr>';
			// segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '訂單狀態' + '</li><li style="width: 70%;">' + 'ecpayResultJson.RtnMsg' + '</li></ul></td></tr>';
			

			// $('#tbodyArea').html(segment);


			var ecpayResultJson = [];
			var segments = [];
			$(function(){

				function showEcpayResult(){

					let xhr = new XMLHttpRequest();
					xhr.open('POST', '<c:url value="/cart.controller/getEcpayResultAttr" />', true);
					xhr.send();
					xhr.onreadystatechange = function() {
						if (xhr.readyState == 4 && xhr.status == 200) {
							// parseEcpayResult(xhr.responseText);
							// let htmlContent = '';
							// for (let i = 0; i < segments.length; i++) {
							// 	htmlContent += segments[i];
							// }
							let htmlContent = parseEcpayResult(xhr.responseText);
							$('#tbodyArea').html(htmlContent);
						}
					}
				}

				function parseFullEcpayResult(unparsedEcpayResultMap){
					ecpayResultJson = JSON.parse(unparsedEcpayResultMap);
					console.log(ecpayResultJson);
					let keys = Object.keys(ecpayResultJson); // An array of keynames.
					for (let i = 0; i < keys.length; i++) {
						let segment = '';
						segment += '<tr>';
						segment += '<td><ul class="actions fit"><li>' + keys[i] + '</li>'; 
						segment += '<li>' + ecpayResultJson[keys[i]] + '</li></ul></td>'; 
						segment += '</tr>';
						segments.push(segment);
					}
					console.log(segments);
				}

				// ※訂單相關編號有三種
				// (1) MerchantTradeNo = 顧客要記住的特店交易編號，可以用來反查綠界交易編號和order_item的內建o_id (資料庫訂單編號)
				// (2) TradeNo = 綠界交易編號(唯一)
				// (3) order_info.o_id 資料庫訂單編號

				// ❗ 只能應付信用卡支付的垃圾函數
				function parseEcpayResult(unparsedEcpayResultMap){
					ecpayResultJson = JSON.parse(unparsedEcpayResultMap);
					console.log(ecpayResultJson);
					
					let segment = '';
					segment += '<h2>請謹慎保留您的<font color="red">訂單編號(' + ecpayResultJson.MerchantTradeNo + ')</font>，以利日後客服為您查詢用。</h2>';
					segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '支付方式' + '</li><li style="width: 70%;">' + '信用卡支付' + '</li></ul></td></tr>';
					segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '您的姓名' + '</li><li style="width: 70%;">' + ecpayResultJson.CustomField2 + '</li></ul></td></tr>';
					segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '交易時間' + '</li><li style="width: 70%;">' + ecpayResultJson.TradeDate + '</li></ul></td></tr>';
					segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '訂單編號' + '</li><li style="width: 70%;">' + ecpayResultJson.MerchantTradeNo + '</li></ul></td></tr>';
					segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '交易金額' + '</li><li style="width: 70%;">' + ecpayResultJson.TradeAmt + '</li></ul></td></tr>';
					segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '手續費' + '</li><li style="width: 70%;">' + ecpayResultJson.PaymentTypeChargeFee + '</li></ul></td></tr>';
					segment += '<tr><td><ul class="actions fit"><li style="width: 30%;">' + '訂單狀態' + '</li><li style="width: 70%;">' + ecpayResultJson.RtnMsg + '</li></ul></td></tr>';
					
					


					console.log(segments);
					return segment;
				}

				////////////////////////////////////////
				
				showEcpayResult();
			})



		</script>

		</body>
</html>