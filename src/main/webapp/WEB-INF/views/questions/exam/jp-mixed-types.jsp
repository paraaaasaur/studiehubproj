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
<style>
	.spinner {
		display: block;
    	margin: 50px auto;
    }

	.spin {
		animation: RotatePlane 1.5s infinite ease-in-out;
	}

	@keyframes RotatePlane {
		0%   { transform: perspective(120px) rotateX(0deg) rotateY(0deg); }
		50%  { transform: perspective(120px) rotateX(-180.1deg) rotateY(0deg); }
		100% { transform: perspective(120px) rotateX(-180deg) rotateY(-179.9deg); }
	}

	/*   覆寫套版樣式 */
	input[type="checkbox"] + label:before {
		border-radius: 100% !important;
	}

	.question-tracker {
		table-layout: fixed;
		border: 3px #cccccc solid;
		width: 700px;
	}

	.question-tracker__header--listening {
		width: 40%;
		border: 2px #cccccc solid;
		padding: 1px;
	}
	.question-tracker__header--multi     {
		width: 30%;
		border: 2px #cccccc solid;
		padding: 1px;
	}
	.question-tracker__header--single    {
		width: 30%;
		border: 2px #cccccc solid;
		padding: 1px;
	}

	.question-tracker__number {
		padding: 1px;
		text-align: center;
		height: 26px;
	}

    .question-tracker__number--current {
	    background-color: #cccccc;
	    padding: 1px;
	    text-align: center;
	    height: 26px;
    }

    .question-tracker__number--group-border {
	    border-right: 2px #cccccc solid;
	    padding: 1px;
	    text-align: center;
	    height: 26px;
    }

	.question-tracker__number--current--group-border {
		background-color: #cccccc;
		border-right: 2px #cccccc solid;
		padding: 1px;
		text-align: center;
		height: 26px;
	}

    .current-question {
	    text-align: left;
    }

	.current-question__image {
		width: 20%;
	}

	.current-question__bottom-separator {
		margin: 1px;
	}

	.quiz-review__correction {
		color: red;
	}
</style>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
<base href="${fn:escapeXml(pageContext.request.contextPath)}/">
<link rel='stylesheet' href="assets/css/main.css">

<title>線上測驗區</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" defer></script>
<script src="assets/js/utility/dom.js"></script>
<script src="assets/js/utility/mini-react.js"></script>
<script src="assets/js/domain/question.js"></script>

<script type="application/json" id="bootstrap-data">
	{

	}
</script>



</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<%@include file="../../fragments/header.jsp"%>


				<div id="root" align="center"></div>


			</div>
		</div>
		<%@include file="../../fragments/sidebar.jsp"%>
	</div>

	<script>
		// Left improvements:
		// 1. centralize state changes (+ snapshot and render) to a single setState function
		// 2. centralize timer related items (timerId, tick, onTimeout...) to an object or function
		// 3. move out inline CSS and use class instead
		const bootstrapData = JSON.parse(document.getElementById('bootstrap-data').textContent);

		window.addEventListener('DOMContentLoaded', init);

		const state = {
			// loading || error || success || quizzing || result
			status: null,
			questions: null,
			error: null,
			// -1: before quiz || 0 ~: current question index || null: after submission
			index: -1,
			answers: [new Answer()],
			remainingSeconds: 90
		};
		const prevState = {};
		/** Save a deep copy of the current state into prevState. */
		function snapshot() {
			Object.entries(state).forEach(entry => prevState[entry[0]] = structuredClone(entry[1]));
		}
		function selectViewModel() {
			return Object.freeze({
				totalQuestions: state.questions.length,
				correctAnswers: state.questions.map(q => q.correctAnswer),
				currentAnswer: state.answers[state.index],
				currentQuestion: state.questions[state.index],
				correctnessSummary: state.questions.map(q => q.correctAnswer).map((ca, i) => Answer.equals(state.answers[i], ca)),
				allSelections: state.questions.map(q => q.selections),
				position: {
					notStarted: state.questions.index === -1,
					first: state.index === 0,
					inBetween: state.index > 0 && (state.index < state.questions.length - 1),
					last: state.index === state.questions.length - 1,
					ended: state.index === null
				}
			});
		}
		const api = {
			fetchRandomMixExam: async function() {
				const response = await fetch('question.controller/sendRandomMixExam');
				if (!response.ok) {
					throw new Error('Fetch failed');
				}
				return response.json();
			}
		};
		const lifecycle = new LifecycleManager();
		const root = document.getElementById('root');
		const DEBUG = {
			compareIndices: () => console.log('previous/current index: ' + prevState.index + ' / ' + state.index)
		}
		let timerId = null;
		async function init() {
			snapshot();
			state.status = 'loading';
			render();

			try {
				const data = await api.fetchRandomMixExam();
				snapshot();
				state.questions = data.list.map(QuestionInfo.fromResObj);
				state.answers = new Array(state.questions.length).fill('dummy').map(_ => new Answer());
				state.status = 'success';
				state.error = null;
				render();
			} catch (e) {
				snapshot();
				state.status = "error";
				state.error = e;
				render();
			}
		}
		function render() {
			lifecycle.beginRender();

			if (state.status === 'loading') {
				root.replaceChildren(
						createSpinner()
				);
			}

			else if (state.status === 'error') {
				root.replaceChildren(
						document.createTextNode('error happened( ･᷄ᯅ･᷅ )')
				);
				console.error('Error: ' + state.error.stack);
			}

			else if (state.status === 'success') {
				root.replaceChildren(
					createHeading(),
					createStartInterface()
				)
			}

			else if (state.status === 'quizzing') {
				// fixme: hardcoded to 10 anyway rn
				if (state.questions.length !== 10) {
					renderNoDataMessage();
					return;
				}

				root.replaceChildren(
					lifecycle.render('heading', null, createHeading, null),
					lifecycle.render('timer', null, createTimer, syncTimer),
					lifecycle.render('question-tracker', state.index, createQuizTracker, null),
					lifecycle.render('current-question', state.index, createCurrentQuestion, null),
					lifecycle.render('nav-btn-group', state.index, createQuizNavigationButtonGroup, null)
				)
			}

			else if (state.status === 'result') {
				renderQuizReview();
			}
		}
		function handleMSSelection (e) {
			const { id, checked } = e.target;

			snapshot();
			state.answers[state.index][id] = checked;
			render();
		}
		function handleSSSelection (e) {
			const { id } = e.target;

			snapshot();
			state.answers[state.index].resetToAllFalse();
			state.answers[state.index][id] = true;
			render();
		}
		function handleGoNext() {
			const vm = selectViewModel();

			if (vm.currentAnswer.isEmpty()) {
				alert("請先作答！");
				return;
			}

			snapshot();
			state.index++;
			render();
		}
		function handleGoBack() {
			snapshot();
			state.index--;
			render();
		}
		function handleSubmission(e, timerId) {
			const vm = selectViewModel();

			if (vm.currentAnswer.isEmpty()) {
				alert("請先作答！");
				return;
			}
			clearInterval(timerId);

			submit();
		}
		function submit() {
			snapshot();
			state.index = null;
			state.status = 'result';
			render();
		}
		function handleStartQuiz() {
			snapshot();
			state.status = 'quizzing';
			state.index = 0;
			render();
		}
		function createSpinner() {
			const spinnerWrapperEl = document.createElement('figure');
			spinnerWrapperEl.classList.add('spinner');

			const spinnerEl = document.createElement('img');
			spinnerEl.src = 'images/user/loading.gif';
			spinnerEl.classList.add('spin');

			spinnerWrapperEl.appendChild(spinnerEl);

			return spinnerWrapperEl;
		}
		function createStartInterface() {
			const startQuizButton = document.createElement("button");
			startQuizButton.onclick = handleStartQuiz;
			startQuizButton.textContent = '開始測驗！';

			return startQuizButton;
		}
		function createHeading() {
			const heading = document.createElement("h2");
			heading.textContent = '線上測驗區';

			return heading;
		}
		function createTimer() {
			const timeInfoEl = document.createElement('article');
			observeTimer(timeInfoEl);

			const timeEl = document.createElement('time');
			timeEl.textContent = formatTime(state.remainingSeconds);
			timeEl.datetime = 'PT' + state.remainingSeconds + 'S';

			timeInfoEl.timeNode = timeEl;
			timeInfoEl.append(
					document.createTextNode('開始測驗，作答時間剩 '),
					timeEl,
					document.createTextNode(' ！')
			);

			return timeInfoEl;
		}
		function formatTime(totalSeconds) {
			const mins = String(Math.floor(totalSeconds / 60)).padStart(2, '0');
			const secs = String(totalSeconds % 60).padStart(2, '0');

			return mins + ':' + secs;
		}
		function syncTimer(timer) {
			timer.timeNode.textContent = formatTime(state.remainingSeconds);
			timer.timeNode.datetime = 'PT' + state.remainingSeconds + 'S';
		}
		function tick() {
			snapshot();
			state.remainingSeconds--;
			render();

			if (state.remainingSeconds === 0) {
				clearInterval(timerId);
				alert('時間到，自動提交試卷！');

				submit();
			}
		}
		function startTimer() {
			timerId = setInterval(tick, 1000);
		}
		function createQuizTracker() {
			const questionTrackerEl = document.createElement('table');
			questionTrackerEl.classList.add('question-tracker');


			const headers = document.createElement("tr");
			{
				const listeningHeaderEl = document.createElement("th");
				listeningHeaderEl.textContent = '\u2002聽力題';
				listeningHeaderEl.colSpan = 4;
				listeningHeaderEl.classList.add('question-tracker__header--listening');

				const msHeaderEl = document.createElement("th");
				msHeaderEl.textContent = '\u2002多選題';
				msHeaderEl.colSpan = 3;
				msHeaderEl.classList.add('question-tracker__header--multi');

				const ssHeaderEl = document.createElement("th");
				ssHeaderEl.textContent = '\u2002單選題';
				ssHeaderEl.colSpan = 3;
				ssHeaderEl.classList.add('question-tracker__header--single');

				headers.append(listeningHeaderEl, msHeaderEl, ssHeaderEl);
			}

			const bodies = document.createElement("tr");
			for (let i = 0; i < 10; i++) {
				const questionNumberEl = document.createElement("td");
				questionNumberEl.textContent = String(i + 1);

				if ((i === 3 || i === 6) && i === state.index) {
					questionNumberEl.classList.add('question-tracker__number--current--group-border');
				}
				else if (i === 3 || i === 6) {
					questionNumberEl.classList.add('question-tracker__number--group-border');
				}
				else if (i === state.index) {
					questionNumberEl.classList.add('question-tracker__number--current');
				}
				else {
					questionNumberEl.classList.add('question-tracker__number');
				}

				bodies.appendChild(questionNumberEl);
			}

			questionTrackerEl.append(headers, bodies);

			return questionTrackerEl;
		}
		function renderNoDataMessage() {
			const h4 = document.createElement("h4");
			h4.textContent = '很抱歉，目前系統無相關試題';

			root.replaceChildren(h4);
		}
		function createCurrentQuestion(s = state, vm = selectViewModel()) {
			const { q_pictureString, q_audioString, q_question, q_type } = vm.currentQuestion;

			const currentQuestionEl = document.createElement('article');
			currentQuestionEl.classList.add('current-question');

			currentQuestionEl.append(
					createImage(q_pictureString),
					createAudio(q_audioString),
					createPrompt(q_question),
					document.createElement('br'),
					createSelections(q_type, vm.currentQuestion.selections, vm.currentAnswer),
					createBottomSeparator()
			);

			return currentQuestionEl;
		}
		function createImage(q_pictureString) {
			const imgWrapper = document.createElement('figure');

			const imgEl = document.createElement("img");
			imgEl.src = q_pictureString;
			imgEl.className = 'current-question__image';

			imgWrapper.appendChild(imgEl);

			return imgWrapper;
		}
		function createAudio(q_audioString) {
			const audioContainer = document.createElement('div');

			const audioEl = document.createElement("audio");
			audioEl.src = q_audioString;
			audioEl.controls = true;

			audioContainer.appendChild(audioEl);

			return audioContainer;
		}
		function createPrompt(q_question) {
			const promptEl = document.createElement("h3");
			promptEl.textContent = '問題：' + q_question;

			return promptEl;
		}
		function createSelections(q_type, selections, answer) {
			const choicesEl = document.createElement("fieldset");

			if (q_type === '多選題') {
				choicesEl.appendChild(createMSSelections(selections, answer));
			}

			else if (q_type === '單選題') {
				choicesEl.appendChild(createSSSelections(selections, answer));
			}

			else if (q_type === '聽力題') {
				choicesEl.appendChild(createListeningSelections(selections, answer));
			}

			// fixme: looking for better alternative
			else {
				console.error('unknown question type');
				choicesEl.appendChild(document.createDocumentFragment());
			}

			return choicesEl;
		}
		function createMSSelections(selections, answer) {
			const fragment = document.createDocumentFragment();

			for (const label of Object.keys(selections)) {
				const selection = selections[label];
				const isChecked = answer[label];

				const selectionEl = document.createElement("input");
				selectionEl.type = "checkbox";
				selectionEl.name = "answer";
				selectionEl.id = label;
				selectionEl.onchange = handleMSSelection;
				selectionEl.toggleAttribute('checked', isChecked);

				const labelEl = document.createElement("label");
				labelEl.setAttribute('for', selectionEl.id);
				labelEl.textContent = label + ' ' + selection;

				const br = document.createElement("br");

				fragment.append(selectionEl, labelEl, br);
			}

			return fragment;
		}
		function createSSSelections(selections, answer) {
			const fragment = document.createDocumentFragment();

			for (const label of Object.keys(selections)) {
				const selection = selections[label];
				const isChecked = answer[label];

				const selectionEl = document.createElement("input");
				selectionEl.type = "radio";
				selectionEl.name = "answer";
				selectionEl.id = label;
				selectionEl.onchange = handleSSSelection;
				selectionEl.toggleAttribute('checked', isChecked);

				const labelEl = document.createElement("label");
				labelEl.setAttribute('for', selectionEl.id);
				labelEl.textContent = label + ' ' + selection;

				const br = document.createElement("br");

				fragment.append(selectionEl, labelEl, br);
			}

			return fragment;
		}
		function createListeningSelections(selections, answer) {
			return createSSSelections(selections, answer);
		}
		function createBottomSeparator() {
			const underline = document.createElement("hr");
			underline.classList.add('current-question__bottom-separator');

			return underline;
		}
		function createQuizNavigationButtonGroup() {
			const vm = selectViewModel();

			const quizNavGroup = document.createElement('fieldset');

			// conditional navigation controls
			const backEl = document.createElement('button');
			backEl.textContent = '上一題';
			backEl.addEventListener('click', handleGoBack);

			const nextEl = document.createElement('button');
			nextEl.textContent = '下一題';
			nextEl.addEventListener('click', handleGoNext);

			const submitEl = document.createElement('button');
			submitEl.textContent = '提交';
			submitEl.addEventListener('click', e => handleSubmission(e, timerId));

			if (vm.position.first) {
				quizNavGroup.append(nextEl);
			}

			else if (vm.position.inBetween) {
				quizNavGroup.append(backEl, nextEl);
			}

			else if (vm.position.last) {
				quizNavGroup.append(backEl, submitEl);
			}

			return quizNavGroup;
		}
		function renderQuizReview() {
			const vm = selectViewModel();
			const { correctnessSummary, totalQuestions } = vm;


			const wrapper = document.createElement("div");
			wrapper.style.textAlign = 'left';

			const statisticsSection = createStatisticsSection(correctnessSummary, totalQuestions);
			const reviewSection = createReviewSection();

			wrapper.append(statisticsSection, reviewSection);

			root.replaceChildren(wrapper);
		}
		function createStatisticsSection(correctnessSummary, totalQuestions) {
			// calculate counts of correct/wrong questions
			const correctCount = correctnessSummary.filter(c => c === true).length;
			const wrongCount = correctnessSummary.filter(c => c !== true).length;
			const correctPercent = correctCount / totalQuestions * 100;


			// DOMs
			const section = document.createElement('section');

			const sectionHeading = document.createElement('h3');
			sectionHeading.textContent = '＜測驗結果＞';

			const totalQuestionsInfo = document.createElement('h4');
			totalQuestionsInfo.textContent = ' 測驗共' + totalQuestions + '題';
			totalQuestionsInfo.style.color = 'red';

			const accuracyInfo = document.createElement('h4');
			accuracyInfo.textContent = ' 答錯題數：' + wrongCount + '題，答對率：' + correctPercent + '%';
			accuracyInfo.style.color = 'red';

			const br = document.createElement('br');

			const resultComment = createResultComment(correctPercent);

			const correctnessTable = document.createElement("table");
			for (let i = 0; i < totalQuestions; i+= 5) {
				const thRow = document.createElement("tr");
				const tdRow = document.createElement("td");

				for (let j = i; j < i && j < totalQuestions; j++) {
					const th = document.createElement("th");
					th.textContent = '第' + (j + 1) + '題';

					const td = document.createElement("td");
					td.textContent = correctnessSummary[i];

					thRow.appendChild(th);
					tdRow.appendChild(td);
				}

				correctnessTable.appendChild(thRow);
				correctnessTable.appendChild(tdRow);
			}

			section.append(
					sectionHeading,
					totalQuestionsInfo,
					accuracyInfo,
					br,
					resultComment,
					correctnessTable
			);

			return section;
		}
		function createResultComment(correctPercent) {
			const resultCommentEl = document.createElement('h4');
			resultCommentEl.style.color = 'red';

			if (correctPercent >= 70) {
				resultCommentEl.textContent = '✓測驗評語：您的日語能力遠高於目前測驗程度，建議您往更高程度進行測驗學習！';
			}
			else if (correctPercent > 40 && correctPercent < 70) {
				resultCommentEl.textContent = '✓測驗評語：您的日語能力落在於目前測驗程度，建議您持續測驗學習！';
			}
			else {
				resultCommentEl.textContent = '✓測驗評語：您的日語能力落在於基礎至目前測驗程度，建議您調整程度，持續測驗學習！';
			}

			return resultCommentEl;
		}
		function createReviewSection(vm = selectViewModel()) {
			const reviewSection = document.createElement("section");
			const { correctnessSummary, totalQuestions } = vm;
			const { answers, questions } = state;

			for (let i = 0; i < totalQuestions; i++) {
				const question = questions[i];
				const answer = answers[i];
				const questionNo = i + 1;
				const isCorrect = (correctnessSummary[i] === true);


				// DOMs
				const reviewContainer = document.createElement("article");

				reviewContainer.append(
						createQuestionNumberInfo(questionNo),
						createImage(question.q_pictureString),
						createAudio(question.q_audioString),
						createPrompt(question.q_question),
						isCorrect
								? document.createElement('br')
								: createCorrection(question.correctAnswer),
						createAnswer(question.q_type, question.selections, answer)
				);

				reviewSection.appendChild(reviewContainer);
			}

			return reviewSection;
		}
		function createQuestionNumberInfo(questionNo) {
			const questionNumberLabelEl = document.createElement("h4");
			questionNumberLabelEl.textContent = '第 ' + questionNo + ' 題';

			return questionNumberLabelEl;
		}
		function createCorrection(correctAnswer) {
			const correctionEl = document.createElement("h4");
			correctionEl.textContent = '正確答案:\u2002' + correctAnswer.getChosenLabels();
			correctionEl.classList.add('quiz-review__correction');

			return correctionEl;
		}
		function createAnswer(q_type, selections, answer) {
			const selectionsEl = document.createElement("div");

			if (q_type === '多選題') {
				selectionsEl.appendChild(createAnswerMS(selections, answer));
			}

			else if (q_type === '單選題') {
				selectionsEl.appendChild(createAnswerSS(selections, answer));
			}

			else if (q_type === '聽力題') {
				selectionsEl.appendChild(createAnswerListening(selections, answer));
			}

			else {
				console.error('unknown question type');
				selectionsEl.appendChild(document.createDocumentFragment());
			}

			return selectionsEl;
		}
		function createAnswerMS(selections, answer) {
			const labels = Object.keys(selections);

			const listEl = document.createElement("ul");
			listEl.style.listStyleType = 'none';

			for (let i = 0; i < labels.length; i++) {
				const label = labels[i];
				const q_selection = selections[label];
				const isChecked = answer[label];

				const itemEl = document.createElement("li");
				{
					const marker = isChecked ? createCheckedIcon() : createUncheckedIcon();
					const itemText = document.createTextNode(' ' + label + ' ' + q_selection);
					itemEl.appendChild(marker);
					itemEl.appendChild(itemText);
				}

				const br = document.createElement("br");

				listEl.append(itemEl, br);
			}

			return listEl;
		}
		function createAnswerSS(selections, answer) {
			return createAnswerMS(selections, answer);
		}
		function createAnswerListening(selections, answer) {
			return createAnswerSS(selections, answer);
		}
		function observeTimer(timerEl) {
			const observer = new MutationObserver((mutationsList, observer) => {
				for (const mutation of mutationsList) {
					if (mutation.type === 'childList') {
						if (timerEl) {
							startTimer(state.remainingSeconds);
							observer.disconnect();
						}
					}
				}
			});
			observer.observe(document.body, { childList: true, subtree: true });
		}
	</script>
	<script src="assets/js/jquery.min.js"></script>
	<script src="assets/js/browser.min.js"></script>
	<script src="assets/js/breakpoints.min.js"></script>
	<script src="assets/js/util.js"></script>
	<script src="assets/js/main.js"></script>
	
</body>
</html>