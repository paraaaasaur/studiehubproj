## Frontend
(setup: login + create a pending dummy question + admin login + to pending-questions-review page)
1. onload-triggers `GET /question.controller/sendVerifyQuestion`
   - display retrieved pending results
2. click on the delete icon in `<a>`
   - see a confirmation box, click yes to confirm deletion
   - `GET /question.controller/verifyDeleteQuestion/{q_id}`
   - disapproves the pending question
   - to the same page + see a line of red success message

## Backend
- `GET /question.controller/sendVerifyQuestion` -> returns JSON `{  
 "size" : <result size>,  
 "list" : <result list>  
}`, where results are record sets of `WHERE verification = 'N'`
- `GET /question.controller/verifyDeleteQuestion/{q_id}`
  - gets redirected to `GET /question.controller/intoVerifyQuestion`
  - returns view `question/verifyQuestion`