<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">
<script src="assets/js/jquery.min.js"></script>
<title>Studie Hub</title>
<style type="text/css">

.product{
    border: 1px rgb(153, 149, 149) solid;
    padding: 30px;
    margin: 50px;
    border-radius: 50px;
    text-align: center;
    display: inline-block;
    width:300px;
    height:300px;
}
.image{
	text-align: center;
}

</style>
<script type="application/json" id="bootstrap-data">
	{
		"u_id": "${fn:escapeXml(loginBean.u_id)}",
		"userPicString": "${fn:escapeXml(loginBean.pictureString)}"
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

 // 有登入才會顯示購物車sidebar
	let cartHref = document.querySelector('#cartHref');
	cartHref.hidden = (u_id)? false : true;
	cartHref.style.visibility = (u_id)? 'visible' : 'hidden';

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

    var dataArea = document.getElementById("dataArea");
	var query = document.getElementById("query");
	var productname = document.getElementById("productname");
	var typename = document.getElementById("producttypename");
    let xhr = new XMLHttpRequest();
    xhr.open("GET","findAllProduct",true);
    xhr.send();
    xhr.onreadystatechange = function(){

        if(xhr.readyState == 4 && xhr.status == 200){
            var result = JSON.parse(xhr.responseText);
			dataArea.replaceChildren(renderProductPreviews(result));
        }
    }

	query.addEventListener('click',function(){
		let pname = productname.value;
		let producttypename	= typename.value;
		console.log(pname)
		console.log(producttypename)
		if(!pname && !producttypename){
			alert('請輸入關鍵字');
			return
		}

		let xhr2 = new XMLHttpRequest();
		xhr2.open('GET',"queryByProductName?pname="+pname+"&producttypename="+producttypename,true);
		xhr2.send();
		xhr2.onreadystatechange = function(){
			if(xhr2.readyState == 4 && xhr2.status == 200){
				var result = JSON.parse(xhr2.responseText)
				dataArea.replaceChildren(renderProductPreviews(result));
			}
		}
	})
}

		function setResultStars(p_ID){
			let star = "";

			let xhr = new XMLHttpRequest();
			xhr.open("GET","ratingAVG?p_ID="+p_ID, false);
			xhr.send();

				if(xhr.status ==200){

					let result = xhr.responseText;


					if(!result){
						star += "<span>尚無評論</span>";
					}else{

						for(n=0;n<=result;n++){
							star += "<i class='fa fa-star fa-x' style='color: gold;'></i>";
						}
						console.log(star);
					}
				}
				return star;


		}

		function renderProductPreviews(resObj) {
			const { list: products, ratedIndex: ratedIndices} = resObj;

			console.log(ratedIndices);

			const container = document.createElement('div');
			container.classList.add('posts');
			container.replaceChildren();

			if (products.length === 0) {
				container.innerHTML =
						"<table border='1' style = 'width:100%;text-align: center;'>" +
							"<tr>" +
								"<th colspan='8'>查無資料</th>" +
							"</tr>" +
						"</table>";
			} else {
				for (let i = 0; i < products.length; i++) {
					const product = products[i];
					const ratedIndex = ratedIndices[i];

					const productPreview = document.createElement('article');

					const a = document.createElement('a');
					a.className = 'image';
					a.href = "takeClass/" + product.p_ID;
					a.alt = '';
					{
						const img = document.createElement('img');
						img.src = product.p_Img;
						img.style.height = '5%';
						a.appendChild(img);
					}

					const productName = document.createElement('h3');
					productName.textContent = product.p_Name;

					const starBlock = createStarBlock(ratedIndex);

					const productPrice = document.createElement('p');
					productPrice.textContent = product.p_Price;

					const btnBlock = document.createElement('div');
					btnBlock.className = 'actions';
					{
						const a = document.createElement('a');
						a.className = 'button';
						a.href = "takeClass/" + product.p_ID;
						a.textContent = 'MORE';

						btnBlock.appendChild(a);
					}

					productPreview.append(
							a, productName, starBlock, productPrice,
							document.createElement('br'), btnBlock
					);

					container.appendChild(productPreview);
				}
			}

			return container;
		}

		function createStarBlock(ratedIndex) {
			const starBlock = document.createElement('div');
			if (ratedIndex == null) {
				const noRating = document.createElement('div');
				noRating.textContent = "尚無評分";

				starBlock.appendChild(noRating);
			} else {
				for (let i = 0; i < ratedIndex; i++) {
					const star = document.createElement('i');
					star.classList.add('fa', 'fa-star', 'fa-x', 'commentStar');
					star.style.color = 'gold';

					starBlock.appendChild(star);
				}
			}

			return starBlock;
		}


</script>

</head>

<body class="is-preload">
	<!-- Wrapper -->
	<div id="wrapper">
		<!-- Main -->
		<div id="main">
			<div class="inner">
				<%@include file="../universal/header.jsp"%>
				<h2 align='center'>課程資訊</h2>
				<hr>
				<div style="text-align: center;">
					<select id="producttypename" style="width: 150px;display: inline;float: none;border-radius: 50px;">
						<option label="類別" value="-1" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="英文" value="英文" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="日文" value="日文" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="西語" value="西語" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="葡萄牙語" value="葡萄牙語" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="拉丁語" value="拉丁語" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
						<option label="韓文" value="韓文" style="width: 10px;display: inline;float: none;border-radius: 50px;">英文</option>
					</select>
					<input type="text" id="productname" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入課程關鍵字">
					<button id="query">搜尋</button>
					<br>

				<br>

				</div>
				<div id='dataArea'></div>
			</div>
		</div>
		<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="../universal/sidebar.jsp"%>

	</div>

	<!--Rating JS-->
	<script>
		var ratedIndex =-1;
		var stars = document.getElementById("stars");
		var comment = document.getElementById("showComment");


		$(document).ready(function(){
			resetStarColors();

			if(localStorage.getItem('ratedIndex') != null)
			setStars(parseInt(localStorage.getItem('ratedIndex')));

			$('.commentStar').on('click',function(){
				ratedIndex = parseInt($(this).data('index'));
				localStorage.setItem('ratedIndex',ratedIndex);
			});

			$('.commentStar').mouseover(function(){
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
		// var p_ID = $('#p_ID').val();
		// let xhr0 = new XMLHttpRequest();
		// xhr0.open("GET","<c:url value='/findRatingById'/>?p_ID="+p_ID,true)
		// xhr0.send();
		// xhr0.onreadystatechange = function(){
		// if(xhr0.readyState == 4 && xhr0.status == 200){
		// 	var result = JSON.parse(xhr0.responseText);
		// 	comment.innerHTML = showComment(result);


		// 	}
		// }
		});


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
			console.log(text);
			console.log(p_ID);

			var xhr = new XMLHttpRequest();
			xhr.open("POST", "saveRating",true);
			xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			xhr.send("p_ID="+p_ID+"&ratedIndex="+ratedIndex+"&commentString="+text);
			window.location.href="takeClass/"+p_ID;


		});



		</script>

	<!-- Scripts -->
	<script src="https://kit.fontawesome.com/c43b2fbf26.js"	crossorigin="anonymous"></script>
	<script
		src="assets/js/jquery.min.js"></script>
	<script
		src="assets/js/browser.min.js"></script>
	<script
		src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>

</body>
</html>