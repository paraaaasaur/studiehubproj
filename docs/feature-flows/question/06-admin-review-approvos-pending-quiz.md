## Frontend
(setup: login + create a pending dummy question + admin login)
1. click on 題庫 > 試題審核區(後端)`<a>` on the sidebar
   - `GET /question.controller/intoVerifyQuestion`
   - to pending-questions-review page
2. onload-triggers `GET /question.controller/sendVerifyQuestion`
   - display retrieved pending results
3. click on 查看內容`<button>`
   - `GET /question.controller/verifyOneQuestion/{q_id}`
   - to the detail page
4. go back to the previous page
5. click on the approval icon in `<a>`
   - `GET /question.controller/verifyPassQuestion/{q_id}`
   - approves the pending question
   - to the same page + see a line of red success message

## Backend
- `GET /question.controller/intoVerifyQuestion` -> returns view `question/verifyQuestion`
- `GET /question.controller/sendVerifyQuestion` -> returns JSON `{  
 "size" : <result size>,  
 "list" : <result list>  
}`, where results are record sets of `WHERE verification = 'N'`
- `GET /question.controller/verifyOneQuestion/{q_id}` -> returns view `question/verifyOneQuestion`
- `GET /question.controller/verifyPassQuestion/{q_id}`
  - gets redirected to `GET /question.controller/intoVerifyQuestion`
  - returns view `question/verifyQuestion`