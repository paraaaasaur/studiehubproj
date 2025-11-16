## Frontend
(Setup: login)
1. click on 登出`<a#logout>` in the top-right corner of the header
   - `GET /logout.controller`
   - show the success alert message "已成功登出!"
   - redirected back to index


## Backend
- `GET /logout.controller` [1] -> returns JSON `{ success: "已成功登出!" }` and lose session