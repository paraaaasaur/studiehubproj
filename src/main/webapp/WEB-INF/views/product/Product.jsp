<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="#">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">
<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<title>Studie Hub</title>
<style type="text/css">
.product {
	border: 1px rgb(153, 149, 149) solid;
	padding: 30px;
	margin: 50px;
	border-radius: 50px;
	text-align: center;
	display: inline-block;
	width: 300px;
	height: 300px;
}

.image {
	text-align: center;
}
</style>

<script>
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
    if(u_id){
    	loginHref.hidden = true;
    	signupHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
    	userId.innerHTML = u_id;
		loginEvent.style.display = "block";
    	loginALLEvent.style.display = "block";
    } 


	var query = document.getElementById("query");
	var productname = document.getElementById("productname");

	query.addEventListener('click',function(){
		let pname = productname.value;
		if(!pname){
			alert('請輸入關鍵字');
			return
		}

		let xhr2 = new XMLHttpRequest();
		xhr2.open('GET',"<c:url value='/queryByProductName' />?pname="+pname);
		xhr2.send();
		xhr2.onreadystatechange = function(){
			if(xhr2.readyState == 4 && xhr2.status == 200){
				var result = JSON.parse(xhr2.responseText)
				dataArea.innerHTML = showData(result);
			}
		}
	})

	//購買商品
	$('#buyProduct').on('click',function(){
		var p_ID = "${product.p_ID}";
		var u_ID = "${loginBean.u_id}";
		var xhr = new XMLHttpRequest();
		xhr.open('GET',"<c:url value='/buyProduct?p_ID="+p_ID+"&u_ID="+u_ID+"' />",true);
		xhr.send();

		alert("課程已加入購入車");







	})
    
}

function showData(textObj) {
    let obj = JSON.parse(JSON.stringify(textObj));
    let size = obj.size;
    let products = obj.list;
	console.log(obj);
	console.log(size);
	console.log(products);
    let segment = "";
        if (size == 0) {
			segment += "<tr><th colspan='5'>查無資料</th></tr>";
		} else {
			
			for(n=0;n<products.length;n++){
				let product = products[n];
				segment += "<div class='product'>";
				segment += "<a href='"+"<c:url value = '/takeClass/"+ product.p_ID +"'/>" +"'class='image'style='height:270px'>";
				segment += "<img src='${pageContext.request.contextPath}/images/productImages/"+ product.p_Img +"' width='230px' height='120px'>";
				segment += "<br>";
				segment += "<h3>"+ product.p_Name +"</h3>"
			    segment += "</a>";
			    segment += "</div>";
			}
			
			
			
        }
       
        return segment;
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
				<div style="text-align: center; margin-top: 10px;">
					<input type="text" id="productname"
						style="display: inline; width: 500px; float: none; border-radius: 50px;"
						placeholder="請輸入課程關鍵字">
					<button id="query" style="display: inline;">搜尋</button>
					<br> <br>

				</div>
				<div id='dataArea'>

					<h2>${product.p_Name}</h2>
					<input hidden id="p_ID" value="${product.p_ID}">
					<c:choose>
						<c:when test="${product.p_Status == 1}">
							<iframe
								src="${pageContext.request.contextPath}/video/productVideo/${product.p_Video}"
								width="1200px" height="700px" frameborder="0"
								allow="accelerometer;clipboard-write; encrypted-media; gyroscope; picture-in-picture"
								allowfullscreen></iframe>

						</c:when>
						<c:otherwise>
							<img
								src='${pageContext.request.contextPath}/images/productImages/${product.p_Img}'
								width="1500px" height="700px">
						</c:otherwise>

					</c:choose>
					<hr>
					<h2>關於課程</h2>
					<div>${product.p_DESC}</div>
					<br>
					<br>
					<div style="text-align: center;"><button type="button" id="buyProduct" style="text-align: center;">購買此課程</button></div>
					<hr>
					<h2>評論</h2>

					<div align='center' style="padding: 50px;">
						<i class="fa fa-star fa-2x commentStar" data-index="0"></i> <i
							class="fa fa-star fa-2x commentStar" data-index="1"></i> <i
							class="fa fa-star fa-2x commentStar" data-index="2"></i> <i
							class="fa fa-star fa-2x commentStar" data-index="3"></i> <i
							class="fa fa-star fa-2x commentStar" data-index="4"></i>
					</div>

					<hr>
					<div id="stars"></div>
					<textarea rows="10" cols="100" style="resize: none" name="comment"
						id="comment"></textarea>
					<button id="ratingSubmit" type="button">提交</button>
					<br>
					<br>
					<br>

					<!--show comment-->
					<div id="showComment"></div>

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
					var p_ID = $('#p_ID').val();
					let xhr0 = new XMLHttpRequest();
					xhr0.open("GET","<c:url value='/findRatingById'/>?p_ID="+p_ID,true)
					xhr0.send();
					xhr0.onreadystatechange = function(){
					if(xhr0.readyState == 4 && xhr0.status == 200){
						var result = JSON.parse(xhr0.responseText);
						comment.innerHTML = showComment(result);

						
						}
					}
					});
				
					function showComment(comment){
						let obj = JSON.parse(JSON.stringify(comment));
						let rating = obj.list;
						let size = obj.size;
						let segment = "";

						if(size == 0){
							segment += "<div>尚無評論</div>"
						}else{
							for(n=0;n<rating.length;n++){
								let ratingComment = rating[n];
								let resultStars = setResultStars(ratingComment.ratedIndex);
								console.log(resultStars);
								segment += "<div>"
								
								segment += "<div>"
										+ resultStars
										+"</div>"
										+ratingComment.comment
								segment += "</div>";

								segment +="<hr>"
								
							}
						}
						
						return segment;
					}
					function setStars(max){
							for(var i=0;i<=max;i++)
								$('.commentStar:eq('+i+')').css('color','gold');
					}
					function setResultStars(ratedIndex){
						let star = ""
							for(var i=0;i<=ratedIndex;i++){
								star +=	"<i class='fa fa-star fa-x' style='color: gold;'></i>";
							}
							return star;
								
					}

					function resetStarColors(){
						$('.commentStar').css('color','gray');
					}

					$('#ratingSubmit').on('click',function(){
						console.log(ratedIndex);
						var text = $('#comment').val();
						var p_ID = $('#p_ID').val();

						var xhr = new XMLHttpRequest();
						xhr.open("POST", "<c:url value='/saveRating' />",true);
						xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
						xhr.send("p_ID="+p_ID+"&ratedIndex="+ratedIndex+"&commentString="+text);
						window.location.href="http://localhost:8080/studiehub/takeClass/"+p_ID;
						

					});

					

					</script>



				</div>
			</div>
		</div>
		<!-- Sidebar -->
		<!-- 這邊把side bar include進來 -->
		<%@include file="../universal/sidebar.jsp"%>

	</div>

	<!-- Scripts -->
	<script src="https://kit.fontawesome.com/c43b2fbf26.js"	crossorigin="anonymous"></script>
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