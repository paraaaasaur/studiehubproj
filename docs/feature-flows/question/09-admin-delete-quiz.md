## Frontend
(setup: admin login + to admin-display-quizzes page)
1. click on a trash bin icon wrapped in `<a>` under the 編輯 column
  - see a confirmation box, click yes to confirm deletion
  - `GET /question.controller/deleteQuestion/{q_id}`
  - to the same page + see a line of red success message


## Backend
- `GET /question.controller/deleteQuestion/{q_id}`
  - gets redirected to `GET /question.controller/queryQuestion`
  - returns view `question/queryQuestion` + flash attr `successMessage`