## Frontend
(setup: go to display-all-quizzes page)
1. on the search bar `<input#questionName>`, type keyword for quiz question (e.g., あ) and hit 搜尋`<button#query>`  
  -> `GET /question.controller/queryByName`  
  -> displays matched results on the same page


## Backend
- `GET /question.controller/queryByName` -> returns JSON `{  
 "size" : <result size>,
 "list" : <result list>
}`