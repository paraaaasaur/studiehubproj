<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="<c:out value="${pageContext.request.contextPath}/" />">
<link rel='stylesheet' href="assets/css/main.css">
<style>
	.payment-receipt__item-name {
		width: 30%
	}
	.payment-receipt__item-value {
		width: 70%
	}
</style>
<title>顧客回傳頁面</title>
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "<c:out value="${loginBean.u_id}" />",
		"userPicString": "<c:out value="${loginBean.pictureString}" />"
	}
</script>
<script>
const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
const { u_id, userPicString } = bootstrapData;

	window.onload = function(){
		var logout = document.getElementById("logout");
		logout.onclick = function(){
			var xhr = new XMLHttpRequest();
			xhr.open("GET", "logout.controller", true);
			xhr.send();
			xhr.onreadystatechange = function(){
				if(xhr.readyState == 4 && xhr.status == 200){
					var result = JSON.parse(xhr.responseText);
					if(result.success){
						alert(result.success);
						top.location = '';
					}else if(result.fail){
						alert(result.fail);                    
						top.location = '';
					}
				}
			}
		}
		
		//universal
	    //如果有登入，隱藏登入標籤
	    var loginHref = document.getElementById('loginHref');
	    var signupHref = document.getElementById('signupHref');
	    var logoutHref = document.getElementById('logoutHref');
	    var userId = document.getElementById('userId');
	    var userPic = document.getElementById('userPic');
		var loginEvent = document.getElementById('loginEvent');
		var loginEvent1 = document.getElementById('loginEvent1');
	    var loginALLEvent1 = document.getElementById('loginALLEvent1');
	    if(u_id){
	    	loginHref.hidden = true;
	    	signupHref.hidden = true;
	    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
	    	userPic.src = userPicString;	//有登入就秀大頭貼
	    	userId.textContent = u_id;
	    	loginEvent.style.display = "block";
	    	loginEvent1.style.display = "block";
	    	loginALLEvent1.style.display = "block";
	    }
		// 有登入才會顯示購物車sidebar
		let cartHref = document.querySelector('#cartHref');
		cartHref.hidden = (u_id)? false : true;
		cartHref.style.visibility = (u_id)? 'visible' : 'hidden';
	}
	//universal

		
	
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
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>
			<script src="assets/js/custom/TaJenUtils.js"></script>

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
					xhr.open('POST', 'cart.controller/getEcpayResultAttr', true);
					xhr.send();
					xhr.onreadystatechange = function() {
						if (xhr.readyState == 4 && xhr.status == 200) {
							// parseEcpayResult(xhr.responseText);
							// let htmlContent = '';
							// for (let i = 0; i < segments.length; i++) {
							// 	htmlContent += segments[i];
							// }
							document.getElementById('tbodyArea')
									.replaceChildren(parseEcpayResult(xhr.responseText));
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

					const fragment = document.createDocumentFragment();

					fragment.appendChild(createTitleHeader(ecpayResultJson.MerchantTradeNo));
					fragment.appendChild(createTextRow('支付方式', '信用卡支付'));
					fragment.appendChild(createTextRow('您的姓名', ecpayResultJson.CustomField2));
					fragment.appendChild(createTextRow('交易時間', ecpayResultJson.TradeDate));
					fragment.appendChild(createTextRow('訂單編號', ecpayResultJson.MerchantTradeNo));
					fragment.appendChild(createTextRow('交易金額', ecpayResultJson.TradeAmt));
					fragment.appendChild(createTextRow('手續費', ecpayResultJson.PaymentTypeChargeFee));
					fragment.appendChild(createTextRow('訂單狀態', ecpayResultJson.RtnMsg));

					return fragment;
				}

				function createTitleHeader(MerchantTradeNo) {
					const headerEl = document.createElement('h2');

					const textPart1 = document.createTextNode('請謹慎保留您的');

					const fontEl = document.createElement('font');
					fontEl.textContent = '訂單編號(' + MerchantTradeNo + ')';
					fontEl.color = 'red';

					const textPart2 = document.createTextNode('，以利日後客服為您查詢用。');

					headerEl.append(textPart1, fontEl, textPart2);

					return headerEl;
				}
				function createTextRow(name, value) {
					const row = document.createElement('tr');

					const cell = document.createElement('td');
					{
						const list = document.createElement('ul');
						list.classList.add('actions', 'fit');
						{
							const itemName = document.createElement('li');
							itemName.textContent = name;
							itemName.classList.add('payment-receipt__item-name');

							const itemValue = document.createElement('li');
							itemValue.textContent = value;
							itemValue.classList.add('payment-receipt__item-value');

							list.appendChild(itemName);
							list.appendChild(itemValue);
						}

						cell.appendChild(list);
					}

					row.appendChild(cell);

					return row;
				}

				////////////////////////////////////////
				
				showEcpayResult();
			})



		</script>

		</body>
</html>