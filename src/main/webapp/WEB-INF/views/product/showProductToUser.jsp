<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">
	<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
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

<script>
var u_id = "${loginBean.u_id}";

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
    var userId = document.getElementById('userId');
    var userPic = document.getElementById('userPic');
    if(u_id){
    	loginHref.hidden = true;
    	signupHref.hidden = true;
    	logoutHref.style.visibility = "visible";	//有登入才會show登出標籤(預設為hidden)
    	userPic.src = userPicString;	//有登入就秀大頭貼
    	userId.innerHTML = u_id;
    } 
    
    var dataArea = document.getElementById("dataArea");
	var query = document.getElementById("query");
	var productname = document.getElementById("productname");
    let xhr = new XMLHttpRequest();
    xhr.open("GET","<c:url value='/findAllProduct' />",true);
    xhr.send();
    xhr.onreadystatechange = function(){

        if(xhr.readyState == 4 && xhr.status == 200){
            var result = JSON.parse(xhr.responseText);
			dataArea.innerHTML = showData(result);
        }
    }

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
}

function setResultStars(p_ID){
			let star = "";

			let xhr = new XMLHttpRequest();
			xhr.open("GET","<c:url value='/findRatingById'/>?p_ID="+p_ID,true);
			xhr.send();
			xhr.onreadystatechange = function(){

				if(xhr.readyState == 4 && xhr.status ==200){
					
					let result = JSON.parse(xhr.responseText);
					let rating = result.list;
					let size = result.size;

					if(size == 0){
						star += "<span>尚無評論</span>";
					}else{
						let ratingIndex = rating[0];
						let ratedIndex = ratingIndex.ratedIndex;
						for(n=0;n<ratedIndex;n++){
							star += "<i class='fa fa-star fa-x' style='color: gold;'></i>";
						}
						return 0;
						console.log(star);
					}
				}
			}
					
		}
		
		function showData(textObj) {
		let obj = JSON.parse(JSON.stringify(textObj));
		let size = obj.size;
		let products = obj.list;
		let segment = "";
		if (size == 0) {
			segment += "<tr><th colspan='5'>查無資料</th></tr>";
		} else {
			
			for(n=0;n<products.length;n++){
				let product = products[n];
				let resultStars = setResultStars(product.p_ID);
				console.log(product.p_ID);
				console.log(resultStars);
					segment += "<div class='product'>";
					segment += "<a href='"+ "<c:url value = '/takeClass/"+ product.p_ID +"'/>" +"'class='image'style='height:270px'>";
					segment += "<img src='${pageContext.request.contextPath}/images/productImages/"+ product.p_Img +"' width='230px' height='120px'>";
					segment += "<br>";
					segment += "<h3>"+ product.p_Name +"</h3>"
					segment += "<span>NT"+product.p_Price+"</span>"
					segment += "<br>"
					segment += setResultStars(product.p_ID);
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
				<h2 align='center'>課程資訊</h2>
				<hr>
				<div style="text-align: center;">
					<input type="text" id="productname" style="display: inline; width: 500px; float: none;border-radius: 50px;" placeholder="請輸入課程關鍵字">
					<button id="query" style="display: inline;">搜尋</button>
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
			xhr.open("POST", "<c:url value='/saveRating' />",true);
			xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			xhr.send("p_ID="+p_ID+"&ratedIndex="+ratedIndex+"&commentString="+text);
			window.location.href="http://localhost:8080/studiehub/takeClass/"+p_ID;
			

		});

		

		</script>

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