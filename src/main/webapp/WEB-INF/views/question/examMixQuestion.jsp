<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<!-- 綜合題 -->
<style type="text/css">
   span.error {
	color: red;
	display: inline-block;
	font-size: 5pt;
}

.spinner {
    width: 70px;
    height: 70px;
    background-color: #5b99de;
    margin: 50px auto 50px auto;
  }
  .spin {
    animation: RotatePlane 1.5s infinite ease-in-out;
  }
  .text {
    text-align: center;
    font-weight: bolder;
    font-size: 2rem;
    color: #5b99de;
  }
  @keyframes RotatePlane {
    0%   { transform: perspective(120px) rotateX(0deg) rotateY(0deg); }
    50%  { transform: perspective(120px) rotateX(-180.1deg) rotateY(0deg); }
    100% { transform: perspective(120px) rotateX(-180deg) rotateY(-179.9deg); }
  }
	/*   覆寫套版樣式 */
  input[type="checkbox"] + label:before{
  	border-radius: 100% !important ;
  }

	/* 卡套版待確 ---覆寫側邊目前題數 */
	#countArea{
	border:3px #cccccc solid ;
	cellpadding:'10'; 
	border:'1' ; 
	width:500px ;
	height:20% ;
	align:left ;
	display:'';
  }

</style>

<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel='stylesheet'
	href="${pageContext.request.contextPath}/assets/css/main.css">

<title>線上測驗區</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
var obj = null; 
var questions = null;			// 全部值
var size = 0;     				// 總題數
var counter = 0;   				// 目前題目
var userChoice = [];			// 存放使用者所有回答
var examResult = [];			// 考試結果


window.addEventListener('load', function(){
	
	var countArea = document.getElementById("countArea");
	var dataArea = document.getElementById("dataArea");
	var next = document.getElementById("next");
	var back = document.getElementById("back");
	var submit =document.getElementById("submit");
	
	
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "<c:url value='/question.controller/sendRandomMixExam' />", true);
	xhr.onreadystatechange = function(){
		if (xhr.readyState == 4 && xhr.status == 200 ){
			console.log(xhr.responseText);
			dataArea.innerHTML = showData(xhr.responseText);
			countArea.innerHTML = showCountData(xhr.responseText);
		}
	};
	xhr.send();
	
	

	next.addEventListener('click', function(){
			var userAnswers = document.getElementsByName("userAnswer"); 
			var userAnswerChecked  = [];	// 存取使用者單題回答
			
			for(var i = 0; i < userAnswers.length; i ++){    //抓取陣列中,被使用者所選取的項目
				if(userAnswers[i].checked){ 
					userAnswerChecked.push(userAnswers[i].value);
					}
				}
	 if(userAnswerChecked != ""){  	//使用者有選擇才進判斷

		if(counter < size-2){
			back.style.display = '';
			userChoice.splice(counter, 1, userAnswerChecked.join(""));
			counter += 1;
			
// 			alert("userChoice=" + userChoice );
			
			var userLastAnswer = userChoice[counter];
			dataArea.innerHTML = showData(xhr.responseText,userLastAnswer);
			countArea.innerHTML = showCountData(xhr.responseText);


		}else if(counter == size-2){
			next.style.display = 'none';
			submit.style.display = '';

			userChoice.splice(counter, 1, userAnswerChecked.join(""));
			
// 			alert("userAnswerChecked=" + userAnswerChecked );
// 			alert("userChoice=" + userChoice );
			counter += 1;
			var userLastAnswer = userChoice[counter];
			dataArea.innerHTML = showData(xhr.responseText,userLastAnswer);
			countArea.innerHTML = showCountData(xhr.responseText);

			}
	}else{
		alert("請先作答！");
	}
		});
		
	
	
	
	back.addEventListener('click', function(){

		var userAnswers = document.getElementsByName("userAnswer"); 
		var userAnswerChecked  = [];	// 存取使用者單題回答
		
		for(var i = 0; i < userAnswers.length; i ++){    //抓取陣列中,被使用者所選取的項目
			if(userAnswers[i].checked){ 
// 				alert(userAnswers[i].value);
				userAnswerChecked.push(userAnswers[i].value);
				}
			}
// 				alert("userAnswerChecked="+ userAnswerChecked);
		
		if(counter == size-1){
			next.style.display = '';
			submit.style.display = 'none';

			userChoice.splice(counter, 1, userAnswerChecked.join(""));
			counter -= 1;
			var userLastAnswer = userChoice[counter];
			dataArea.innerHTML = showData(xhr.responseText,userLastAnswer);
			countArea.innerHTML = showCountData(xhr.responseText);
			
		 }else if(counter > 1 && counter < size-1){

			
				userChoice.splice(counter, 1, userAnswerChecked.join(""));
				
				counter -= 1;
				var userLastAnswer = userChoice[counter];
				dataArea.innerHTML = showData(xhr.responseText,userLastAnswer);
				countArea.innerHTML = showCountData(xhr.responseText);
			
			
		}else if(counter == 1){
			back.style.display = 'none';
				
			userChoice.splice(counter, 1, userAnswerChecked.join(""));

			counter -= 1;
			var userLastAnswer = userChoice[counter];
			dataArea.innerHTML = showData(xhr.responseText,userLastAnswer);
			countArea.innerHTML = showCountData(xhr.responseText);
		
			
		}else{
			back.style.display = 'none';
			userChoice.splice(counter, 1, userAnswerChecked.join(""));
			dataArea.innerHTML = showData(xhr.responseTex);
			countArea.innerHTML = showCountData(xhr.responseText);
		
			}
		});
	
	
//點擊提交後判斷試題
		submit.addEventListener('click', function(){
			
			if(counter == size-1){
	
				var userAnswers = document.getElementsByName("userAnswer"); 
				// 建立用來判斷是否有選擇資料
				var userAnswer0 = userAnswers;
				//抓取陣列中,被使用者所選取的項目
				for(var i = 0; i < userAnswers.length; i ++){ 
					if(userAnswers[i].checked){ 
						var userAnswer = userAnswers[i].value;
						userAnswers = 'userAnswer is Checked'; 
						}
					}
				userChoice.splice(counter, 1, userAnswer);
			}
			

			for(var i=0 ; i<size ; i++){
			 	if(userChoice[i] == questions[i].q_answer.replaceAll(",","") ){
					examResult.push("O");
			 	}
			 	else if(userChoice[i] != questions[i].q_answer.replaceAll(",","")){
			 		examResult.push("X");
						}
				 }
		
				dataArea.innerHTML = showResult(xhr.responseText,examResult);
			
			
		});
		
////倒數計時
// 	    var fiveMinutes = 60 * 2,
	    var fiveMinutes = 90,
        display = document.querySelector('#time');
   		 startTimer(fiveMinutes, display);		
	
});


function showCountData(textObj){
	let status0=["","","","","","","","","",""];  	//顯示目前題數
	for(var c = 0; c <= size ; c ++){    //抓取陣列中,被使用者所選取的項目
		if(c == counter){ 
			status0[c]="background-color:#cccccc;";
			}
		}
	
	let content =  "<tr><th colspan='4' style=' border:2px #cccccc solid;height:1px;'>聽力題</th><th colspan='3' style=' border:2px #cccccc solid;'>多選題</th><th colspan='3' style=' border:2px #cccccc solid;'>單選題</th></tr>";
	    content += "<tr><td style='" + status0[0] + ";height:1px;'>1</td> <td  style='" + status0[1] + "'>2</td> <td  style='"+status0[2]+"'>3</td> <td style='border-right:2px #cccccc solid ;"+status0[3]+"'>4</td>";
	    content += "<td style='" + status0[4] + "'>5</td><td style='" + status0[5] + "'>6</td><td style=' border-right:2px #cccccc solid ;"+status0[6]+"'>7</td>";
	    content += "<td style='" + status0[7] + "'>8</td><td style='" + status0[8] + "'>9</td><td style='"+ status0[9] +"'>10</td></tr>";
	return content;
};



 function showData(textObj,userLastAnswer){
	let status=["","","","",""];  	//先判斷使用者已勾選項
	if(userLastAnswer != undefined){
	if(userLastAnswer.search("A") != -1){
			status[0]="checked";
		}; 
	if(userLastAnswer.search("B") != -1){
			status[1]="checked";
		};
	if(userLastAnswer.search("C") != -1){
			status[2]="checked";
		}; 
	if(userLastAnswer.search("D") != -1){
			status[3]="checked";
		};
	if(userLastAnswer.search("E") != -1){
			status[4]="checked";
		};		
	};	
	
	
	obj = JSON.parse(textObj);
	size = obj.size;
	questions = obj.list;
	let segment = "";
	
	if (size == 0){
		segment += "<h4>很抱歉，目前系統無相關試題</h4>";
	} else {
// 		segment += "<h4>測驗共" + size + "題</h4><br>";
	    
		   	let question = questions[counter];
	   		let number = counter+1;
	     	
// 		   	segment += "<h4>第&ensp;" + number + "&ensp;題</h4>";
	
	   		if(question.mimeTypePic == null){
		 	   }else{
		   		segment += "<div><img width='400' height='260' src='" + question.q_pictureString + "' ></div>"; 	
		   		};  	
	  		if(question.mimeTypePic == null){
		  	   }else{
		  	 	segment += "<div><audio controls src='" + question.q_audioString + "' ></div>"; 
	   	   		}; 
			
	   	   	if(question.q_type == "聽力題" || question.q_type == "單選題"){
	   		   	segment += "<h3>問題：" + question.q_question + "</h3><br>"; 
				segment += "<div><input type='radio' value='A' name='userAnswer'  id='A'" + status[0] + " /><label for='A'>"+ "A &emsp; " + question.q_selectionA +"</label><br>";
				segment += "<input type='radio' value='B' name='userAnswer' id='B'" + status[1] + " /><label for='B'>"+ "B &emsp; " + question.q_selectionB +"</label><br>";
				segment += "<input type='radio' value='C' name='userAnswer' id='C'" + status[2] + " /><label for='C'>"+ "C &emsp; " + question.q_selectionC +"</label><br>";
				segment += "<input type='radio' value='D' name='userAnswer' id='D'" + status[3] + " /><label for='D'>"+ "D &emsp; " + question.q_selectionD +"</label></div><hr><br>";
	   	   	}
	   	   	
	   	   	if(question.q_type == "多選題"){
				segment += "<h3>問題：" + question.q_question + "</h3><br>"; 
				segment += "<div><input type='checkbox' value='A' name='userAnswer'  id='A'" + status[0] + " /><label for='A'>"+ "A &emsp; " + question.q_selectionA +"</label><br>";
				segment += "<input type='checkbox' value='B' name='userAnswer' id='B'" + status[1] + " /><label for='B'>"+ "B &emsp; " + question.q_selectionB +"</label><br>";
				segment += "<input type='checkbox' value='C' name='userAnswer' id='C'" + status[2] + " /><label for='C'>"+ "C &emsp; " + question.q_selectionC +"</label><br>";
				segment += "<input type='checkbox' value='D' name='userAnswer' id='D'" + status[3] + " /><label for='D'>"+ "D &emsp; " + question.q_selectionD +"</label><br>";
				segment += "<input type='checkbox' value='E' name='userAnswer' id='E'" + status[4] + " /><label for='E'>"+ "E &emsp; " + question.q_selectionE +"</label></div><hr><br>";

	   	   	}
	   }
			return segment;
	};

	
	 //考試結果
	 function showResult(textObj,examResult){
			back.style.display = 'none';
			next.style.display = 'none';
			submit.style.display = 'none';
			countArea.style.display = 'none';

			var correct = 0;    //答對數
			var wrong = 0;      //答對=錯數
			for(var i=0 ; i<size ; i++){
			 	if(userChoice[i] == questions[i].q_answer.replaceAll(",","") ){
// 			 		alert("答對 使用者選擇="+ userChoice[i]);
// 			 		alert("答案="+questions[i].q_answer.replaceAll(",",""));
			 		correct += 1
			 	}
			 	else if(userChoice[i] != questions[i].q_answer.replaceAll(",","")){
// 			 		alert("答錯 使用者選擇="+ userChoice[i]);
// 			 		alert("答案="+questions[i].q_answer.replaceAll(",",""));
			 	 	wrong += 1
						}
				 }

			
			let correctPercent = correct/size*100 ;
			let	segment2  = "<h3>＜測驗結果＞</h3><br>";
			    segment2 += "<div><h4 style='color:red;'>&emsp;測驗共" + size + "題</h4></div>";
				segment2 += "<div><h4 style='color:red;'>&emsp;答錯題數："+ wrong +"題，答對率：" + correctPercent + "%</h4></div><br>";
				if(correct >= 7){
					segment2 += "<div><h4 style='color:red;'>&emsp;✓測驗評語：您的日語能力遠高於目前測驗程度，建議您往更高程度進行測驗學習！</h4></div><br>";
				}else if(correct > 4 && correct < 7){
					segment2 += "<div><h4 style='color:red;'>&emsp;✓測驗評語：您的日語能力落在於目前測驗程度，建議您持續測驗學習！</h4></div><br>";
				}else{
					segment2 += "<div><h4 style='color:red;'>&emsp;✓測驗評語：您的日語能力落在於基礎至目前測驗程度，建議您調整程度，持續測驗學習！</h4></div><br>";
				}
				segment2 += "<table>";
			
				segment2 += "<tr>" ;
////待改成每五題換行				
			    for(n = 0; n < examResult.length ; n++){
				    segment2 += "<th>第"+ Number(n+1) +"題</th>"
			    }
				    segment2 += "</tr><tr>";
			    
			    for(m = 0; m < examResult.length ; m++){
					segment2 += "<td>" + examResult[m] + "</td>"; 
			    }
			 	    segment2 += "</tr></table>";
			 	
			
////帶出所有測驗過試題			 	    
			 	 for(p = 0; p < size ; p++){  	    
				   	let question = questions[p];
			   		let number = p+1;
			   		
			   		let status2=["","","","",""];  	//先判斷使用者已勾選項
			   		if(userChoice[p] != undefined){
			   		if(userChoice[p].search("A") != -1){
			   				status2[0]="checked";
			   			}; 
			   		if(userChoice[p].search("B") != -1){
			   				status2[1]="checked";
			   			};
			   		if(userChoice[p].search("C") != -1){
			   				status2[2]="checked";
			   			}; 
			   		if(userChoice[p].search("D") != -1){
			   				status2[3]="checked";
			   			};
			   		if(userChoice[p].search("E") != -1){
			   				status2[4]="checked";
			   			};		
			   		};	
			     	
		 		   	segment2 += "<h4>第&ensp;" + number + "&ensp;題</h4>";
			
			   		if(question.mimeTypePic == null){
				 	   }else{
				   		segment2 += "<div><img width='400' height='260' src='" + question.q_pictureString + "' ></div>"; 	
				   		};  	
			  		if(question.mimeTypePic == null){
				  	   }else{
				  	 	segment2 += "<div><audio controls src='" + question.q_audioString + "' ></div>"; 
			   	   		}; 
					
			   		   	segment2 += "<h3>問題：" + question.q_question + "</h3>"; 
			   		   	
			   		 if(userChoice[p] != questions[p].q_answer.replaceAll(",","")){
			   			segment2 +="<h4 style='color:red;'>正確答案："+ questions[p].q_answer +"</h4>"
			   			 }else{
				   	   	segment2 += "<br>"; 
			   		    };
			   		   	
			   		   	
			   	   	if(question.q_type == "聽力題" || question.q_type == "單選題"){   //問題:若使用radio會無法對應status2
						segment2 += "<div><input type='checkbox' value='A' name='userAnswer'  id='A'" + status2[0] + " /><label for='A'>"+ "A &emsp; " + question.q_selectionA +"</label><br>";
						segment2 += "<input type='checkbox' value='B' name='userAnswer' id='B'" + status2[1] + " /><label for='B'>"+ "B &emsp; " + question.q_selectionB +"</label><br>";
						segment2 += "<input type='checkbox' value='C' name='userAnswer' id='C'" + status2[2] + " /><label for='C'>"+ "C &emsp; " + question.q_selectionC +"</label><br>";
						segment2 += "<input type='checkbox' value='D' name='userAnswer' id='D'" + status2[3] + " /><label for='D'>"+ "D &emsp; " + question.q_selectionD +"</label></div><hr><br>";
			   	   	}
			   	   	
			   	   	if(question.q_type == "多選題"){
						segment2 += "<div><input type='checkbox' value='A' name='userAnswer'  id='A'" + status2[0] + " /><label for='A'>"+ "A &emsp; " + question.q_selectionA +"</label><br>";
						segment2 += "<input type='checkbox' value='B' name='userAnswer' id='B'" + status2[1] + " /><label for='B'>"+ "B &emsp; " + question.q_selectionB +"</label><br>";
						segment2 += "<input type='checkbox' value='C' name='userAnswer' id='C'" + status2[2] + " /><label for='C'>"+ "C &emsp; " + question.q_selectionC +"</label><br>";
						segment2 += "<input type='checkbox' value='D' name='userAnswer' id='D'" + status2[3] + " /><label for='D'>"+ "D &emsp; " + question.q_selectionD +"</label><br>";
						segment2 += "<input type='checkbox' value='E' name='userAnswer' id='E'" + status2[4] + " /><label for='E'>"+ "E &emsp; " + question.q_selectionE +"</label></div><hr><br>";

			   	   	}	
				} 	
			 	    return segment2; 
	 } 	 

//倒數計時器
	 function startTimer(duration, display) {
		    var timer = duration, minutes, seconds;
		    var count = setInterval(function () {
		        minutes = parseInt(timer / 60, 10);
		        seconds = parseInt(timer % 60, 10);

		        minutes = minutes < 10 ? "0" + minutes : minutes;
		        seconds = seconds < 10 ? "0" + seconds : seconds;

		        display.textContent = minutes + ":" + seconds;

		        if (--timer < 0) {
// 		            timer = duration;
// 		        	var submit =document.getElementById("submit");
					clearInterval(count);
					alert("時間到，自動提交試卷！");
					timecounter.style.display = 'none';
		        	$("#submit").click();
		        }
		    }, 1000);
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

<div align='center'>
<h2>線上測驗區</h2>

<div id='timecounter' style="display: ''">開始測驗，作答時間剩 <span id="time">01:30</span> 分鐘！</div>

<!-- <hr> -->
<%-- <font color='red'>${successMessage}</font>&nbsp; --%>
<!-- <hr> -->


<table id='countArea'>
</table>  


<div align='left'  id='dataArea'>
</div>

<div>
<button id='back' style="display: none">上一題</button>
<button id='next' style="display: ''">下一題</button>
&emsp;<button id='submit' style="display: none">提交</button>
</div><br>
<!-- <br> -->
<%-- <br><a href="<c:url value='/question.controller/turnQuestionIndex'/> " >回前頁</a> --%>
			</div>
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