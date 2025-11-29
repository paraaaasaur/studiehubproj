## Frontend
1. click on 題庫 > 所有試題`<a>` on the sidebar
   - `GET /question.controller/guestQueryQuestion`
   - to display-all-quizzes page
2. window.onload
   - `GET /question.controller/findAllQuestions`
   - retrieve & display all available questions


## Backend
- `GET /question.controller/guestQueryQuestion` -> returns view `question/guestQueryQuestion`
- `GET /question.controller/findAllQuestions` -> returns json `{  
 "size" : <result size>,  
 "list" : <result>  
}`, where results are admin-approved record sets