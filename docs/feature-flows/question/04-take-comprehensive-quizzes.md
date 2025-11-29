## Frontend
1. click on the 題庫 > 線上測驗區`<a>` on the sidebar
   - `GET /question.controller/turnQuestionIndex`
   - to display-comprehensive-quizzes page
2. choose any (Japanese) category to challenge
   - `GET /question.controller/startRandomMixExam`
   - to take-quiz page
3. window.onload:  
   - `/question.controller/sendRandomMixExam` to retrieve comprehensive quiz set
   - interactions:
     - countdown (1:30)
     - play audio (if any)
     - choose answer(s)
     - go to previous/next question
     - submit (button appears at the last question)
     - remarks on the quiz


## Backend
- `GET /question.controller/turnQuestionIndex` -> returns view `question/intro_QuestionIndex`
- `GET /question.controller/startRandomMixExam` -> returns view `question/examMixQuestion`
- `GET /question.controller/sendRandomMixExam` -> returns JSON `{  
 "size" : <10>,
 "list" : <result list>
}`, where the result list is a mix of random selections with 聽力題 x4 + 多選題 x3 + 單選題 x3