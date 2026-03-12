<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="#">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<script src="assets/js/jquery.min.js"></script>
<title>Studie Hub</title>
<style type="text/css">
	.cantBuy {
		color : rgb(0, 132, 255) !important;
		box-shadow : inset 0 0 0 2px rgb(0, 132, 255);
	}
	.cantBuy:hover {
		background-color: rgb(230, 245, 253);
	}
	.cantBuy:active {
		background-color: rgb(200, 231, 248);
	}
</style>
<script type="application/json" id="bootstrap-data">
	{
		"p_ID": "${fn:escapeXml(product.p_ID)}",
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}"
	}
</script>
<script>
	const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);
	const { p_ID, u_id, userPicString } = bootstrapData;

	window.onload = function() {
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

		// nin's
		$(function(){
			if (!u_id) {
				const buyEl = document.getElementById('buyProduct');
				buyEl.style.display = 'none';
				buyEl.toggleAttribute('disabled', true);
				return;
			}

			let xhr3 = new XMLHttpRequest();
			xhr3.open('POST', "cart.controller/clientInitializeProductBtnFunc");
			xhr3.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr3.send("p_ID=" + p_ID + "&u_ID=" + u_id);
			xhr3.onreadystatechange = function(){
				if(xhr3.readyState == 4 && xhr3.status == 200){
					let ninResult = JSON.parse(xhr3.responseText);
					if (ninResult == 1) {
						$('#buyProduct').text('已購買本課程');
						$('#buyProduct').attr('disabled', true);
					} else if (ninResult == 2) {
						$('#buyProduct').addClass('cantBuy');
						document.getElementById('buyProduct').innerHTML = '移除出購物車';
						document.getElementById('buyProduct').dataset.state = 'false';
					} else if (ninResult == 3) {
						console.log('尚未過買過，css保持原樣');
					} else {
						console.log('#buyProduct按鈕初始化出錯');
					}
				}
			}
		})

		// nin's
		$('#buyProduct').on('click', function(){
			let state = this.dataset.state;
			let xhr = new XMLHttpRequest();
			let preQueryString = "p_ID="+p_ID+"&u_ID="+u_id+"&toDo=";
			xhr.open('POST',"cart.controller/clientAddProductToCart",true);
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			if (state == 'true') {
				xhr.send(preQueryString + "buy");
				xhr.onreadystatechange = function(){
					if(xhr.readyState == 4 && xhr.status == 200){
						if (xhr.responseText == false) {
							alert('您已有權觀看此課程，因此加入購物車失敗！');
							return;
						}
						$('#buyProduct').addClass('cantBuy');
						// document.getElementById('buyProduct').style.color = 'blue !important';
						// document.getElementById('buyProduct').style.boxShadow = 'inset 0 0 0 2px aqua';
						document.getElementById('buyProduct').innerHTML = '移除出購物車';
						document.getElementById('buyProduct').dataset.state = 'false';
						alert("課程已加入購入車！");
					}
				}
			} else if (state == 'false') {
				xhr.send(preQueryString + "remove");
				xhr.onreadystatechange = function(){
					if(xhr.readyState == 4 && xhr.status == 200){
						if (xhr.responseText == false) {
							alert('課程移除失敗！');
							return;
						}
						$('#buyProduct').removeClass('cantBuy');
						document.getElementById('buyProduct').innerHTML = '購買此課程';
						document.getElementById('buyProduct').dataset.state = 'true';
						alert("課程已自購入車移除！");
					}
				}
			}
		})
	}
</script>
</head>

<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../fragments/header.jsp"%>


				<div id='dataArea'>
					<h2>${fn:escapeXml(product.p_Name)}</h2>
					<input hidden id="p_ID" value="${fn:escapeXml(product.p_ID)}">
					<c:choose>
						<c:when test="${product.p_Status == 1}">
							<iframe
								src="${fn:escapeXml(product.p_Video)}"
								width="1000px" height="700px" frameborder="0"
								allow="accelerometer;clipboard-write; encrypted-media; gyroscope; picture-in-picture"
								allowfullscreen></iframe>
						</c:when>
						<c:otherwise>
							<img src='${fn:escapeXml(product.p_Img)}' width="1500px" height="700px">
						</c:otherwise>
					</c:choose>

					<hr>
					<h2>關於課程</h2>
					<div>${fn:escapeXml(product.p_DESC)}</div>
					<br>
					<br>
					<div style="text-align: center;">
						<button type="button" id="buyProduct" data-state='true' style="text-align: center;">購買此課程</button>
					</div>
					<hr>
					<h2>評論</h2>

					<div align='center' style="padding: 50px;">
						<i class="fa fa-star fa-2x commentStar" data-index="0"></i>
						<i class="fa fa-star fa-2x commentStar" data-index="1"></i>
						<i class="fa fa-star fa-2x commentStar" data-index="2"></i>
						<i class="fa fa-star fa-2x commentStar" data-index="3"></i>
						<i class="fa fa-star fa-2x commentStar" data-index="4"></i>
					</div>

					<hr>
					<div id="stars"></div>
					<textarea rows="10" cols="100" style="resize: none" name="comment" id="comment"></textarea>
					<button id="ratingSubmit" type="button">提交</button>
					<br>
					<br>
					<br>

					<!--show comment-->
					<div id="showComment"></div>


				</div>
			</div>
		</div>
		<%@include file="../fragments/sidebar.jsp"%>
	</div>

	<!--Rating JS-->
	<script>
		var ratedIndex = -1;
		var stars = document.getElementById("stars");


		$(document).ready(function(){
			resetStarColors();

			if(localStorage.getItem('ratedIndex') != null)
				setStars(parseInt(localStorage.getItem('ratedIndex')));

			$('.commentStar').on('click',function() {
				ratedIndex = parseInt($(this).data('index'));
				localStorage.setItem('ratedIndex', ratedIndex);
			});

			$('.commentStar').mouseover(function() {
				resetStarColors();

				var currentIndex = parseInt($(this).data('index'));
				setStars(currentIndex);
			});

			$('.commentStar').mouseleave(function(){
				resetStarColors();

				if(ratedIndex !=-1)
					setStars(ratedIndex);
			});
			resetStarColors();

			//show rating result
			var p_ID = $('#p_ID').val();
			let xhr0 = new XMLHttpRequest();
			xhr0.open("GET","findRatingById?p_ID="+p_ID,true)
			xhr0.send();
			xhr0.onreadystatechange = function() {
				if(xhr0.readyState == 4 && xhr0.status == 200){
					var result = JSON.parse(xhr0.responseText);
					renderComments(result);
				}
			}
		});

		function renderComments(commentData){
			const container = document.getElementById('showComment');
			container.replaceChildren();
			const { list: ratings } = commentData;

			if(ratings.length === 0) {
				const emptyMsg = document.createElement('div');
				emptyMsg.textContent = '尚無評論';

				container.appendChild(emptyMsg);
				return;
			}

			for (const rating of ratings) {
				const ratingItem = document.createElement('div');
				ratingItem.classList.add('rating-item');

				const starBlock = createStarBlock(rating.ratedIndex);

				const commentBlock = document.createElement('div');
				commentBlock.textContent = rating.comment;

				ratingItem.append(starBlock, commentBlock);
				container.append(ratingItem, document.createElement('hr'));
			}
		}

		function createStarBlock(ratedIndex) {
			const starBlock = document.createElement('div');
			for (let i = 0; i <= ratedIndex; i++) {
				const star = document.createElement('i');
				star.classList.add('fa', 'fa-star', 'fa-x');
				star.style.color = 'gold';

				starBlock.appendChild(star);
			}
			return starBlock;
		}

		function setStars(max){
			for(var i=0;i<=max;i++)
				$('.commentStar:eq('+i+')').css('color','gold');
		}

		function resetStarColors(){
			$('.commentStar').css('color','gray');
		}

		$('#ratingSubmit').on('click',function(){
			console.log(ratedIndex);
			var text = $('#comment').val();
			var p_ID = $('#p_ID').val();

			const form = document.createElement('form');
			form.method = 'POST';
			form.action = window.location.href = 'saveRating';

			const params = {
				p_ID: p_ID,
				ratedIndex: ratedIndex,
				commentString: text
			};

			for (const key of Object.keys(params)) {
				const input = document.createElement('input');
				input.type = 'hidden';
				input.name = key;
				input.value = params[key];

				form.appendChild(input);
			}

			document.body.appendChild(form);
			form.submit();
		});
	</script>
	<script src="https://kit.fontawesome.com/c43b2fbf26.js"	crossorigin="anonymous"></script>
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>