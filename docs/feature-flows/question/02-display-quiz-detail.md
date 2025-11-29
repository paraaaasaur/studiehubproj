## Frontend
(setup: go to display-all-quizzes page)
1. select and click on an icon (wrapped with `<a>`) under 查看試題 column
   - `GET /question.controller/guestOneQuestion/{q_id}`
   - to quiz-detail page
2. show details for the selected entry


## Backend
- `GET /question.controller/guestOneQuestion/{q_id}`
  - send model attribute `Q1 : question-entity`
  - returns view `question/guestOneQuestion`