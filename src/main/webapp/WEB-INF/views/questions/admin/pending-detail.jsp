<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<title>Studie Hub</title>
<script type="application/json" id="bootstrap-data">
	{
		"adminId": "${fn:escapeXml(adminId)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { adminId } = bootstrapData;

	window.onload = function() {
		var logoutHref = document.getElementById('logoutHref');
		if(adminId) {
			logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
		}
	}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../../fragments/admin/header.jsp"%>


				<div align="center">
					<h2 align='center'>查看詳細試題</h2>

					<table>
						<tr>
							<td>題目編號：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_id)}<br>&nbsp;</td>
						</tr>

						<tr>
							<td>課程分類：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_class)}</td>
						</tr>

						<tr>
							<td>題目類型：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_type)}</td>
						</tr>

						<tr>
							<td>問題：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_question)}</td>
						</tr>

						<tr>
							<td>選項A：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_selectionA)}</td>
						</tr>

						<tr>
							<td>選項B：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_selectionB)}</td>
						</tr>

						<tr>
							<td>選項C：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_selectionC)}</td>
						</tr>

						<tr>
							<td>選項D：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_selectionD)}</td>
						</tr>

						<tr>
							<td>選項E：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_selectionE)}</td>
						</tr>

						<tr>
							<td>正解：<br>&nbsp;</td>
							<td>${fn:escapeXml(questionDetail.q_answer)}</td>
						</tr>

						<tr>
							<td style='vertical-align: middle;'>題目音檔：<br>&nbsp;</td>
							<td>
								<audio controls src='${fn:escapeXml(questionDetail.q_audioString)}' ></audio>
							</td>
						</tr>

						<tr>
							<td style='vertical-align: middle;'>題目照片：<br>&nbsp;</td>
							<td>
								<img width='300' height='180' src= '${fn:escapeXml(questionDetail.q_pictureString)}'>
							</td>
						</tr>
					</table>

					<br>

					<a href="question.controller/intoVerifyQuestion">
					   <button>回前頁</button>
					</a>
				</div>


			</div>
		</div>
		<%@include file="../../fragments/admin/sidebar.jsp"%>
	</div>

	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>