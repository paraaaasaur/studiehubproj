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
	    	userId.innerHTML = u_id;
	    	loginEvent.style.display = "block";
	    	loginEvent1.style.display = "block";
	    	loginALLEvent1.style.display = "block";
	    }
		// 有登入才會顯示購物車sidebar
		let cartHref = document.querySelector('#cartHref');
		cartHref.hidden = (u_id)? false : true;
		cartHref.style.visibility = (u_id)? 'visible' : 'hidden';
	//universal
	
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
							<td>報名開始時間:</td>
							<td><form:input type="datetime-local" path="registration_starttime" />
							<form:errors path="registration_starttime"  cssClass="error"/>	
							</td>
						</tr>
						<tr>
							<td>報名結束時間</td>
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
						
						
						<td>
						<input class='primary'
						style="border: none;  color: white; border-radius: 5px;"
						type="button" onclick="inport()" value="一鍵輸入">
						</td>
						</tr>
					</table>
					
					            <script>
// 									$(document).ready(function() {
						
// 									});
									function inport() {

										
										var aa = "新創雖如雨後春筍般的興盛但大部分都處於經營困難或甚至失敗、因此現在有一群人、專門協助新創公司、希望可以幫這些好的產品/服務、推廣給適合的使用者、以及改善企業內部的營運問題，也就是新創孵化器，究竟孵化器在做甚麼呢？是管理顧問嗎？這次Skyline邀請到Rainmaking Innovation台灣營運長Leo來跟我們分享、他在各國擔任創業顧問的經驗及他是如和走上新創顧問這條路的！歡迎對 #職涯規劃 #跨國工作 #新創 #創業 有興去的朋友來聽聽！"
										       + "這次邀請到英商Rainmaking Innovation台灣營運長-Leo,（一）講者：Leo【簡介】曾旅居北京6年，完成社會學博士學位，並設立跨國顧問公司，客戶包含任天堂、KDDI與不下40個中、日、韓新創科技企業，及韓國政府計畫。博班資格考前夕，一邊做口試投影片，一邊前往南京提案，獲得200多萬人民幣合約，順勢開啟創業之路賺學費；並藉學術活動，前往波士頓、里斯本、巴塞隆納、金邊、胡志明市、雪梨、東京、上海和深圳等地與新創平台、創投進行商情研究。現於歐洲第一大創新與加速器Rainmaking，推動台灣業務落地、新創國際拓展與投資評估，尤其是日本與東南亞市場。興趣是當背包客，前往20幾個國家，包含北韓與前蘇聯地區。最近開始學俄語。【現職】英商Rainmaking Innovation台灣營運長在這場講座，你可以聽到,1.興趣跟職業到底能不能結合？碩士班研究孵化小雞，到博士班研究孵化公司？2.我不是斜槓，只是充滿好奇與行動：創業、合夥與遷徙,3.我喜歡什麼？醒來不知到在哪個城市的顧問生活（兼談中日韓台的經歷）,4.我從不相信work life balance：怎麼可以去日本玩，還順便接到任天堂的委託？5.職涯探索，就跟當背包客旅行一樣：東南歐、東南亞、南半球，再回到台灣。"
										document.getElementById("a_name").value = "三10而立,10年,10城,10個職涯決定";
										document.getElementById("a_type").value = "分享會";
										document.getElementById("registration_starttime").value = "2021-06-07T15:00";
										document.getElementById("registration_endrttime").value = "2021-10-12T17:00";
										document.getElementById("Transienta_startTime").value = "2021-10-12T08:50";
										document.getElementById("Transienta_endTime").value = "2021-10-12T17:10";
										document.getElementById("a_address").value = "桃園市中壢區中大路300號";
										document.getElementById("comment").value = aa
										document.getElementById("applicants").value = "1";

									};
                                    
								</script>
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