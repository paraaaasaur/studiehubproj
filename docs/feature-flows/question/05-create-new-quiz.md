## Frontend
(setup: login)
1. click on the 題庫 > 申請新增試題 on the sidebar
   - `GET /question.controller/insertQuestion`
   - to create-new-question page
2. fill-in with 一鍵 [1], choose answers and then click on 送出資料`<input/submit>`
   - `POST /question.controller/insertQuestion`
   - to display-all-quizzes page, and see a red line of a success message


## Backend
- `GET /question.controller/insertQuestion` -> returns view `question/insertQuestion` 
- `POST /question.controller/insertQuestion`
  - gets redirected to `GET /question.controller/guestQueryQuestion`
  - returns view `question/guestQueryQuestion`