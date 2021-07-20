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
    
    var dataArea = document.getElementById("dataArea");
	var query = document.getElementById("query");
	var productname = document.getElementById("productname");
	var typename = document.getElementById("producttypename");
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
		let producttypename	= typename.value;
		console.log(pname)
		console.log(producttypename)
		if(!pname && !producttypename){
			alert('請輸入關鍵字');
			return
		}

		let xhr2 = new XMLHttpRequest();
		xhr2.open('GET',"<c:url value='/queryByProductName' />?pname="+pname+"&producttypename="+producttypename,true);
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
			xhr.open("GET","<c:url value='/ratingAVG'/>?p_ID="+p_ID, false);
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
		
		function showData(textObj) {
		let obj = JSON.parse(JSON.stringify(textObj));
		let size = obj.size;
		let ratedIndex = obj.ratedIndex;
		let products = obj.list;
		let segment = "<div class='posts'>";
		if (size == 0) {
			segment+="<table border='1' style = 'width:100%;text-align: center;'>";
			segment += "<tr><th colspan='8'>查無資料</th></tr></table>";
		} else {
			
			for(n=0;n<products.length;n++){
				let product = products[n];
				let resultStars = "";
				let star = ratedIndex[n];
				let showStar = "";
				if(star!=null){

					for(i=0;i<star;i++){
						showStar+="<i class='fa fa-star fa-x commentStar'style='color:gold'></i>";
					}
				}else{
					showStar+="尚未評論";
				}
				// resultStars += setResultStars(product.p_ID);
					segment += "<article>";
					segment += "<a class='image' href='<c:url value = '/takeClass/"+ product.p_ID +"'/>' alt='' />";
					segment += "<img src='${pageContext.request.contextPath}/images/productImages/"+ product.p_Img +"' width='10%' height='5%' ></a>";
					// segment += setResultStars(product.p_ID);
					segment += "<h3>"+ product.p_Name +"</h3>"
					segment += showStar;
					segment += "<p>NT"+product.p_Price+"</p>"
					segment += "<br>"
					segment += "<ul class='actions'";
					segment += "<li><a class='button' href='<c:url value = '/takeClass/"+ product.p_ID +"'/>" +"'>More</a></li>";
					segment += "</ul>";
					segment += "</article>";
					
					
				}
				
				
				
			}
			
			segment += "</div>";
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