## Frontend
(setup: admin login)
1. click on 題庫 > 查詢、編輯試題資料(後端)`<a>` on the sidebar
   - `GET /question.controller/queryQuestion`
   - to admin-display-quizzes page
2. window.onload
   - `GET /question.controller/findAllQuestions`
   - retrieve & display all available questions
3. click on an update icon wrapped in `<a>` under the 編輯 column
   - `GET /question.controller/modifyQuestion/{q_id}`
   - to admin-update-detail page
4. edit the question title by adding several `???????`
   - hit the `<input/submit>`
   - `POST /question.controller/modifyQuestion/{q_id}`
   - back to admin-display-quizzes page, seeing a red line of a success message


## Backend
- `GET /question.controller/queryQuestion` -> returns view `question/queryQuestion`
- `GET /question.controller/modifyQuestion/{q_id}`
  - adds model attribute `Q1`
  - returns view `question/editQuestion`
- `POST /question.controller/modifyQuestion/{q_id}`
  - gets redirected to `GET /question.controller/queryQuestion`
  - returns view `question/queryQuestion` + flash attr `successMessage`