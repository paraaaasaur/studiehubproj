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
                 
				<h2 align='center'>請輸入新增活動</h2>
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
							<td>報名結束時間:</td>
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
							<td>活動結束時間:</td>
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
							<td><form:textarea path="transientcomment" cols="100" rows="10" />
						    <form:errors path="transientcomment"  cssClass="error"/>		      
							</td>		
						</tr>
						<tr>
							<td>報名人數上限</td>
							<td><form:input  path="applicants"  placeholder="請填寫數字"/>
							<form:errors path="applicants"  cssClass="error"/>	
								      
							</td>
						</tr>
						<tr>
							<td>活動圖片:</td>
							<td><form:input path="eventImage" type="file" /></td>
						</tr>

						<tr>
							<td><input type="submit"></td>
<!-- 						</tr> -->
<!-- 						<tr> -->
						<td>
						<input class='primary'
						style="border: none;  border-radius: 6px;"
						type="button" onclick="inport2()" value="一鍵輸入時間相近">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input class='primary'
						style="border: none; border-radius: 6px;"
						type="button" onclick="inport()" value="一鍵輸入"></td>
						

						</tr>
						
					</table>
					
					             <script>
// 									$(document).ready(function() {
						
// 									});
									function inport() {

										
										var aa = "在使用Google Analytics你是否曾出現過以下疑問 這些數據，真的能監控網站與分析轉換率，改變訂單成交數量嗎？我要怎麼靠數據了解顧客？哪些數據可以讓我瞭解客戶的行為與需求？數據裡的名詞代表意義是什麼？"
										       + "這些問題讓創新未來學校來幫你,創新未來學校設計的課程將Google Analytics分析的應用know-how，歸納整理一套完整的操作流程與即學即用的實務方法，協助解決提高網站流量與購買轉換率的核心難題！"
										       +"七大學習重點,Google廣告類型,1.關鍵字廣告運用的範圍,2.廣告競價機制模式,3.競業關鍵字與自有品牌關鍵字操作,4.廣告操作介面說明,5.廣告活動架構與廣告建立,6.廣告報表分析及廣告優化。"
										
										document.getElementById("a_name").value = "GoogleAnalytics數據分析";
										document.getElementById("a_type").value = "線下課程";
										document.getElementById("registration_starttime").value = "2021-06-07T15:00";
										document.getElementById("registration_endrttime").value = "2021-10-01T17:00";
										document.getElementById("Transienta_startTime").value = "2021-10-02T08:50";
										document.getElementById("Transienta_endTime").value = "2021-10-12T17:10";
										document.getElementById("a_address").value = "桃園市桃園區埔新路12號";
										document.getElementById("transientcomment").value = aa
										document.getElementById("applicants").value = "1";

									};
                                        function inport2() {

                                        var dt = (+new Date());
                                        var  aa= "企業對於數位行銷的常見八大迷思,"+"- 數位商機龐大、我一定可以在其中分杯羹？"+"- 有使用數位工具、就是在做數位行銷？"+"- 看現在流行用什麼數位推廣方式、跟著用就對了？"+"- 數位行銷要做得好、就得要靠小編？"+"- 有投放廣告、就一定有流量？"
                                        	+ "- 拍Youtube影片、做直播、找網紅/KOL、一定有效果？"+"- 延攬GA高手、就可以提升業績？"+"- 把數位行銷交給專業的行銷顧問公司、就是業績的保證？"
                                        	+"你需要的是RACE數位行銷規劃術RACE是由英國數位行銷大師DaveChaffey&FionaEllis-Chadwick針對數電子商務業者提出的的數位行銷策略企劃流程四大流程,"
                                        	+"1. Reach接觸顧客,2. Act 相互影響,3. Convert銷售轉換,4 Engage顧客口碑,藉由結合數位消費行為，訂定每個流程的策略、管理目標與KPI標準，協助企業系統化規劃完善的數位行銷與電商策略與績效評估標準。能協助您改善以下問題,   - 銷售不佳卻不知道從何改善問題,- 不知如何建立策略目標與合理的KPI標準,- 憑經驗制定行銷計畫 上次成功這次卻失敗,課程大綱 。 - 數位行銷企劃概述／SOSTAC + RACE 數位行銷企劃程序,- Traction集客力戰術設計,- RACE 數位整合行銷計畫,- 數位行銷一頁企劃書。 適合進修對象 。  - 數位行銷工作者,- 欲轉職電子商務者- 企業行銷主管,- 電商創業主,"
										document.getElementById("a_name").value = "RACE數位行銷規劃術";
										document.getElementById("a_type").value = "線下課程";
										document.getElementById("registration_starttime").value = "2021-06-07T15:00";
										document.getElementById("registration_endrttime").value = "2021-07-18T17:00";
										document.getElementById("Transienta_startTime").value = "2021-07-19T08:50";
										document.getElementById("Transienta_endTime").value = "2021-07-21T17:10";
										document.getElementById("a_address").value = "桃園市桃園區復興路180號";
										document.getElementById("transientcomment").value = aa
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