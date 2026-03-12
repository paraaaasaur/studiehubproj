<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<style>
	#iconPos {
		width: 20px;
		position: relative;
		bottom: 8px;
		font-size: 20px;
		color: #ADADAD;
	}

	.reply-box {} /* <todo@1.1.0>: reserved for each reply container (looping <tr>) */
	.reply-box__avatar-cell {
		text-align: center;
		width: 20%;
	}
	.reply-box__avatar {
		width: 80%;
		border-radius: 10%;
	}
	.reply-box__content-cell {
		text-align: left;
		width: 80%;
	}
	.reply-box__content {
		min-height: 180px;
	}
	.reply-box__date {
		text-align: right;
	}
	.reply-box__content-date-separator {
		margin: -20px;
	}
</style>
<title>討論區</title>
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}",
		"c_ID": "${fn:escapeXml(c_ID)}"
	}
</script>
<script src="assets/js/purify.js/"></script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { u_id, userPicString, c_ID } = bootstrapData;

	var hasError = false; // <todo@1.1.0>: reason this; why is everyone using this, what the fish??

	window.onload = function() {
		var xhr0 = new XMLHttpRequest();
		xhr0.open("GET", "selectSingleChat/" + c_ID, true);
		xhr0.send();
		xhr0.onreadystatechange = function() {
			if (xhr0.readyState == 4 && xhr0.status == 200) {
				var users = JSON.parse(xhr0.responseText);
				var content = users.c_Title;
				var selectSingle = document.getElementById("selectSingle");
				selectSingle.textContent = content;
			}
		}

		var xhr = new XMLHttpRequest();
		xhr.open("GET", "selectOneChat/" + c_ID, true);
		xhr.send();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				var users = JSON.parse(xhr.responseText);
				var content = "<table align='right'>";
				for (var i = 0; i < users.length; i++) {
					const isAuthor = users[i][0].u_ID==u_id;
					content += "<tr>"
							+ avatarCell(users[i][1].pictureString, users[i][0].u_ID).outerHTML
							+ contentCell(
									users[i][0].c_Date,
									users[i][0].c_Conts,
									isAuthor,
									users[i][0].c_ID
							).outerHTML
					content += "</tr>";
					console.log(users[i]);
				}
				content += "</table>";
				var selectAll = document.getElementById("selectAll");
				selectAll.innerHTML = content;
			}

		}

		var sendData = document.getElementById("sendData");
		sendData.onclick = function() {
			//抓欄位資料
			var today = new Date();
			var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
			if(today.getHours()<12){
			var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds() + "AM";
			}else{
				var time = today.getHours()-12 + ":" + today.getMinutes() + ":" + today.getSeconds() + "PM";
			}
			var dateTime = date+' '+time;
			var c_IDr = c_ID;
			var c_Date = dateTime;
			var c_Conts = document.getElementById("c_Conts").value;
			var span1 = document.getElementById('result1c');

			if (hasError) {
				return false;
			}

			if (u_id != "") {
				var xhr1 = new XMLHttpRequest();
				xhr1.open("POST", "insertChatReply");
				var jsonInsertData = {
					"c_IDr" : c_IDr,
					"c_Date" : c_Date,
					"c_Conts" : c_Conts,
					"u_ID" : u_id
				}
				xhr1.setRequestHeader("Content-Type", "application/json");
				xhr1.send(JSON.stringify(jsonInsertData));
				xhr1.onreadystatechange = function() {
					if (xhr1.readyState == 4 && xhr1.status == 200){
						result = JSON.parse(xhr1.responseText);
						//判斷回傳
						if(result.fail){
							const font = document.createElement("font");
							font.color = 'red';
							font.textContent = result.fail;

							span1.replaceChildren(font);
						}else if(result.success){
							alert(result.success);
							history.go(0);
						}
					}
				}
			} else {
				top.location='gotologin.controller';
			}

		};

		var logout = document.getElementById("logout");
	    logout.onclick = function(e) {
			e.preventDefault();

			var xhr = new XMLHttpRequest();
			xhr.open("GET", "logout.controller", true);
			xhr.send();
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4 && xhr.status == 200) {
					var result = JSON.parse(xhr.responseText);
					if (result.success) {
						alert(result.success);
						top.location = '';
					} else if (result.fail) {
						alert(result.fail);
						top.location = '';
					}
				}
			};
		};

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

	    if (u_id) {
	    	loginHref.hidden = true;
	    	signupHref.hidden = true;
			document.getElementById('user-info-actions').toggleAttribute('hidden', false);
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
		//universal



	    $('#autoInput').on('click', function() {
	    	$('#c_Conts').val("我也想知道，同問");
	    });
	}

	function avatarCell(pictureString, u_id) {
		const td = document.createElement('td');
		td.classList.add('reply-box__avatar-cell');

		const div = document.createElement('div');
		{
			const br1 = document.createElement('br');

			const img = document.createElement('img');
			img.src = pictureString;
			img.classList.add('reply-box__avatar');

			const br2 = document.createElement('br');

			const u_idText = document.createTextNode(u_id);

			div.append(br1, img, br2, u_idText);
		}

		td.appendChild(div);

		return td;
	}
	function contentCell(c_Date, c_Conts, isAuthor, c_ID) {
	const cell = document.createElement('td');
	cell.classList.add('reply-box__content-cell');

	const content = document.createElement('div');
	content.classList.add('reply-box__content');
	{
		const date = document.createElement('p');
		date.textContent = c_Date;
		date.classList.add('reply-box__date');
		{
			const hr = document.createElement('hr');
			hr.classList.add('reply-box__content-date-separator');

			date.appendChild(hr);
		}

		const contentFrag = document.createDocumentFragment();
		{
			const temp = document.createElement('div');
			// kills potentially xss-risky tags like <script>
			temp.innerHTML = DOMPurify.sanitize(c_Conts);
			// [...temp.childNodes].forEach((node, i) => console.log('node ' + i, node));
			contentFrag.append(...temp.childNodes);
		}

		content.append(date, contentFrag);
	}

	const updateBtn = document.createElement('span');
	if (isAuthor) {
		const link = document.createElement('a');
		link.href = 'goUpdateChat/' + c_ID;

		const icon = document.createElement('i');
		icon.id = 'iconPos'; // fixme@1.1.0: should be css class
		icon.classList.add('fas', 'fa-ellipsis-v');

		link.appendChild(icon);
		updateBtn.appendChild(link);
	}

	cell.append(content, updateBtn);

	return cell;
}
</script>

</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../../fragments/header.jsp"%>


				<div align='center'>
					<br>
					<h2><span id='selectSingle' style='display: block; text-align: left;'></span></h2>
					<div align='center' id='selectAll'></div>
					<div style='text-align: center'>
						<form>
							<table align='right' style='width: 80%;'>
								<tr>
									<td>
										<textarea id='c_Conts' style='min-height: 100px;' placeholder='請輸入回覆內容...'></textarea>
										<span id='result1c'></span>
										<span style="float:right;"><a href="goInsertChatReply">進階</a></span>
									</td>
								</tr>
								<tr>
									<td>
										<button type="button" id="autoInput">一鍵</button> &nbsp;
										<input type='submit' class='primary' id='sendData' value="送出">
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>


			</div>
		</div>
		<%@include file="../../fragments/sidebar.jsp"%>
	</div>

	<script	src="assets/js/jquery.min.js"></script>
	<script	src="assets/js/browser.min.js"></script>
	<script	src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>