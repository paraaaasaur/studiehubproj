<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
.tracker-header {
	border: 2px #cccccc solid;
	padding: 1px;
}
  .spin {
    animation: RotatePlane 1.5s infinite ease-in-out;
  }
   .text { */
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
	/*#countArea {*/
	/*	border:3px #cccccc solid ;*/
	/*!* 	cellpadding:'10';  *!*/
	/*!* 	border:'1' ;  *!*/
	/*	width:700px ;*/
	/*!* 	align:left ; *!*/
	/*!* 	display:''; *!*/
	/*}*/

	.question-tracker {
		table-layout: fixed;
		border: 3px #cccccc solid;
		width: 700px;
	}

	.question-tracker td {
		padding: 1px;
		text-align: center;
		height: 26px;
	}

	.group.listening { width: 40%; }
	.group.multi     { width: 30%; }
	.group.single    { width: 30%; }

	.group-border {
		border-right: 2px #cccccc solid;
	}

	.is-current {
		background-color: #cccccc;
	}

	hr.selection-underline {
		margin: 1px;
	}
</style>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">

<title>線上測驗區</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" defer></script>
	
<script type="application/json" id="bootstrap-data">
	{

	}
</script>
<script>
const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);

var obj = null;
var questions = null;			// 全部值
var size = 0;     				// 總題數
var counter = 0;   				// 目前題目
var userChoice = [];			// 存放使用者所有回答


let state = {
	status: 'loading', // loading || error || success || quizzing || result
	questions: [],
	error: null,
	index: -1,
	answers: new Array(state.questions.length).fill(new Set())
};
const APP_CONFIG = {};
const api = {
	fetchRandomMixExam: async function() {
		const response = await fetch('question.controller/sendRandomMixExam');
		if (!response.ok) {
			throw new Error('Fetch failed');
		}
		return response.json();
	}
};


const countArea = document.getElementById("countArea");
const dataArea = document.getElementById("dataArea");
const next = document.getElementById("next");
const back = document.getElementById("back");
const submit = document.getElementById("submit");
const display = document.getElementById("time");

async function loadQuestions() {
	state.status = 'loading';
	console.log('loading data...');

	try {
		const data = api.fetchRandomMixExam();
		state.questions = data.list;
		state.status = 'success';
		state.error = null;
	} catch (e) {
		state.status = "error";
		state.error = e;
	}

	dataArea.replaceChildren(showData(state));
	countArea.replaceChildren(createQuestionTracker(state));
}
const stateSetter = {
	onChoiceToggled: function (e) {
		const { id, checked, type } = e.target;
		const currentAnswerSet = state.answers[state.index];

		if (type === 'checkbox') {
			if (checked) currentAnswerSet.add(id);
			else currentAnswerSet.remove(id);
		}

		if (type === 'radio' && checked) {
			currentAnswerSet.clear();
			currentAnswerSet.add(id);
		}

		render(state);
	}
};
function getCurrentUserAnswer() {
	return state.answers[state.index];
}
function persistAnswer(answer) {
	// if (q_type is ms || ss || listening)
	const checked = [];
	for(const ans of answer) {
		checked.push(ans.value);
	}
	// persist current answer into state.answers
	state.answers[state.index].splice(state.index, 1, checked.join(""));

	// else if (q_type is free-text)
	// persist answers as string, not array of choices
}

next.addEventListener('click', onClickNext);
back.addEventListener('click', onClickBack);
submit.addEventListener('click', onClickSubmit);

function onClickNext() {
	const answer = document.getElementsByName("userAnswer");

	if(answer.length === 0) {
		alert("請先作答！");
		return;
	}

	persistAnswer(answer);
	state.index++;
	toggleQuizNavigationButtons();

	dataArea.replaceChildren(showData(state));
	countArea.replaceChildren(createQuestionTracker(state));
}
function onClickBack() {
	const answer = document.getElementsByName("userAnswer");

	persistAnswer(answer);
	state.index--;
	toggleQuizNavigationButtons();
	dataArea.replaceChildren(showData(state));
	countArea.replaceChildren(createQuestionTracker(state));
}
function onClickSubmit() {
	const answer = document.getElementsByName("userAnswer");

	if(answer.length === 0) {
		alert("請先作答！");
		return;
	}

	if(!isLast()) {
		alert('not the last question, submission failed :)');
		return;
	}

	persistAnswer(answer);
	const examResult = scoreAll(state.answers, getCorrectAnswers(state.questions));

	dataArea.innerHTML = showResult(xhr.responseText,examResult);
}
function toggleQuizNavigationButtons() {
	if (isFirst()) {
		back.style.display = 'none';
		next.style.display = '';
		submit.style.display = 'none';
	}

	else if (isInBetween()) {
		back.style.display = '';
		next.style.display = '';
		submit.style.display = 'none';
	}

	else if (isLast()) {
		back.style.display = '';
		next.style.display = 'none';
		submit.style.display = '';
	}

	// loading, after submission
	else {
		back.style.display = 'none';
		next.style.display = 'none';
		submit.style.display = 'none';
	}
}
function isFirst() {
	return state.index === 0;
}
function isInBetween() {
	return state.index > 0 && state.index < state.questions.length - 1;
}
function isLast() {
	return state.index === state.questions.length - 1;
}
function scoreAll(answers, correctAnswers) {
	const examResult = [];

	for(let i = 0; i < correctAnswers.length; i++) {
		const isCorrect = state.answers[i] === correctAnswers[i];
		examResult.push(isCorrect? 'O' : 'X');
	}

	return examResult;
}
function getCorrectAnswers(questions) {
	return state.questions.map(q => q.q_answer.replaceAll(",", ""));
}
function createQuestionTracker(state) {
	// head
	const head = document.createElement("tr");
	{
		const th1 = document.createElement("th");
		th1.textContent = ' 聽力題'
		th1.colSpan = 4;
		th1.classList.add('group listening');

		const th2 = document.createElement("th");
		th2.textContent = ' 多選題';
		th2.colSpan = 3;
		th2.classList.add('group multi');

		const th3 = document.createElement("th");
		th3.textContent = ' 單選題';
		th3.colSpan = 3;
		th3.classList.add('group single');

		head.append(th1, th2, th3);
	}

	// body
	const body = document.createElement("tr");
	for (let i = 0; i < 10; i++) {
		const td = document.createElement("td");
		td.textContent = String(i + 1);

		if (i === 2 || i === 5) {
			td.classList.add('group-border');
		}

		if (i === state.index) {
			td.classList.add('is-current');
		}

		body.appendChild(td);
	}

	const fragment = document.createDocumentFragment();
	fragment.append(head, body);

	return fragment;
}
/**
 * returns a boolean array to indicate whether a choice
 * is checked or not for the current question.
 * e.g., 'ABD' => [true, true, false, false, true]
 * */
function mapAnswerToIsCheckedArr(answer) {
	const checked = [];
	for (const val of 'ABCDE') {
		const isChecked = answer.search(val) !== -1;
		checked.push(isChecked);
	}

	return checked;
}
startTimer(90);




function showData(state) {
	if (state.questions.length === 0) {
		const h4 = document.createElement("h4");
		h4.textContent = '很抱歉，目前系統無相關試題';
		return h4;
	}

	const fragment = document.createDocumentFragment();

	const question = state.questions[state.index];
	const img = imgDiv(question.q_pictureString);

	const audio = audioDiv(question.q_audioString);

	const title = questionTitle(question.q_question)

	const br = document.createElement('br');

	const choicesContainer = document.createElement("div");
	choicesContainer.append(createQuestionChoices(state));

	const hr = document.createElement("hr");
	hr.classList.add('question-underline');

	fragment.append(img, audio, title, br , choicesContainer, hr);

	return fragment;
}

function createQuestionChoices(state) {
	const type = state.questions[state.index].q_type;

	if (type === '多選題') {
		return createMSChoices(state);
	}

	else if (type === '單選題') {
		return createSSChoices(state);
	}

	else if (type === '聽力題') {
		return createListeningChoices(state);
	}

	else {
		console.error('unknown question type');
		return document.createDocumentFragment();
	}
}
function createMSChoices(state) {
	const checked = mapAnswerToIsCheckedArr(state.answers[state.index]);
	const currentQuestion = state.questions[state.index];
	// selection array from A to E
	const q_selections = [currentQuestion.q_selectionA, currentQuestion.q_selectionB, currentQuestion.q_selectionC, currentQuestion.q_selectionD, currentQuestion.q_selectionE];

	const fragment = document.createDocumentFragment();
	for (const [i, symbol] of [...'ABCDE'].entries()) {
		const choice = document.createElement("checkbox");
		choice.type = "checkbox";
		choice.id = symbol;
		choice.name = 'userAnswer';
		choice.value = symbol;
		choice.onchange = stateSetter.onChoiceToggled;
		choice.toggleAttribute('checked', checked[i]);

		const label = document.createElement("label");
		label.for = symbol;
		label.textContent = ' ' + q_selections[i];

		const br = document.createElement("br");

		fragment.append(choice, label, br);
	}

	return fragment;
}
function createSSChoices(state) {
	const checked = mapAnswerToIsCheckedArr(state);
	const currentQuestion = state.questions[state.index];
	// selection array from A to D
	const q_selections = [currentQuestion.q_selectionA, currentQuestion.q_selectionB, currentQuestion.q_selectionC, currentQuestion.q_selectionD];

	const fragment = document.createDocumentFragment();
	for (const [i, symbol] of [...'ABCD'].entries()) {
		const choice = document.createElement("input");
		choice.type = "radio";
		choice.id = symbol;
		choice.name = 'userAnswer';
		choice.value = symbol;
		choice.onchange = stateSetter.onChoiceToggled;
		choice.toggleAttribute('checked', checked[i]);

		const label = document.createElement("label");
		label.textContent = ' ' + q_selections[i];
		label.for = symbol;

		const br = document.createElement("br");

		fragment.append(choice, label, br);
	}

	return fragment;
}
function createListeningChoices(state) {
	return createSSChoices(state);
}

	 //考試結果
	 function showResult(textObj,examResult){
			toggleQuizNavigationButtons()
			countArea.style.display = 'none';
			timecounter.style.display = 'none';

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

function startTimer(duration) {
	var timer = duration, minutes, seconds;
	var count = setInterval(function () {
		minutes = parseInt(timer / 60, 10);
		seconds = parseInt(timer % 60, 10);

		minutes = minutes < 10 ? "0" + minutes : minutes;
		seconds = seconds < 10 ? "0" + seconds : seconds;

		display.textContent = minutes + ":" + seconds;
// 		        if (--timer == 0) {  //問題點 前端頁面顯示會在2的時候跳判斷
		if (--timer < 0) {  //問題點 倒數計時可能會發生沒關掉情況，重新啟動倒數
// 		            timer = duration;
// 		        	var submit =document.getElementById("submit");
			clearInterval(count);
			alert("時間到，自動提交試卷！");

			$("#submit").click();
		}
	}, 1000);
}
function imgDiv(q_pictureString) {
	 const div = document.createElement('div');

	 const img = document.createElement("img");
	 img.src = q_pictureString;
	 img.style.width = '400';
	 img.style.height = '260';

	 div.appendChild(img);

	 return div;
}
function audioDiv(q_audioString) {
	const div = document.createElement('div');

	const audio = document.createElement("audio");
	audio.src = q_audioString;
	audio.controls = true;

	div.appendChild(audio);

	return div;
}
function questionTitle(q_question) {
	const h3 = document.createElement("h3");
	h3.textContent = '問題：' + q_question;

	return h3;
}
function buildQuestionByType(question) {
	const h3 = document.createElement("h3");
	h3.textContent = '問題：' + question.q_question;

	if(question.q_type == "聽力題" || question.q_type == "單選題") {
		segment += questionTitle(question.q_question) + "<br>";
		segment += "<div><input type='radio' value='A' name='userAnswer'  id='A'" + status[0] + " /><label for='A'>"+ "A &emsp; " + question.q_selectionA +"</label><br>";
		segment += "<input type='radio' value='B' name='userAnswer' id='B'" + status[1] + " /><label for='B'>"+ "B &emsp; " + question.q_selectionB +"</label><br>";
		segment += "<input type='radio' value='C' name='userAnswer' id='C'" + status[2] + " /><label for='C'>"+ "C &emsp; " + question.q_selectionC +"</label><br>";
		segment += "<input type='radio' value='D' name='userAnswer' id='D'" + status[3] + " /><label for='D'>"+ "D &emsp; " + question.q_selectionD +"</label></div><hr style='margin:1px'><br>";
	}

	if(question.q_type == "多選題") {
		segment += "<h3>問題：" + question.q_question + "</h3><br>";
		segment += "<div><input type='checkbox' value='A' name='userAnswer'  id='A'" + status[0] + " /><label for='A'>"+ "A &emsp; " + question.q_selectionA +"</label><br>";
		segment += "<input type='checkbox' value='B' name='userAnswer' id='B'" + status[1] + " /><label for='B'>"+ "B &emsp; " + question.q_selectionB +"</label><br>";
		segment += "<input type='checkbox' value='C' name='userAnswer' id='C'" + status[2] + " /><label for='C'>"+ "C &emsp; " + question.q_selectionC +"</label><br>";
		segment += "<input type='checkbox' value='D' name='userAnswer' id='D'" + status[3] + " /><label for='D'>"+ "D &emsp; " + question.q_selectionD +"</label><br>";
		segment += "<input type='checkbox' value='E' name='userAnswer' id='E'" + status[4] + " /><label for='E'>"+ "E &emsp; " + question.q_selectionE +"</label></div><hr style='margin:1px'><br>";
	}
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


<table id='countArea' class="question-tracker">
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
		src="assets/js/jquery.min.js"></script>
	<script
		src="assets/js/browser.min.js"></script>
	<script
		src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	
</body>
</html>