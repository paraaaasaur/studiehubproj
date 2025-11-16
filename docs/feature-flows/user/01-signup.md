## Frontend
1. to signup view
   - action: click on the 註冊`<a>` on the sidebar
   - expect: `GET /gotosignup.controller` → to signup page
2. autofill
   - action: click on 一鍵`<button#autoInput>`
   - expect: autofill all columns
3. verify user id existence
   - action: click on 檢查帳號`<a#accountCheck>`
   - expect: restful `POST /checkUserId`
4. hint message after the server response
   - expect: message "帳號可用!" shows up next to 檢查帳號`<a>`
5. submit
   - action: click on 送出`<button#sendData>`
   - expect: 
     - show loading.gif
     - restful `POST /userSignup`
     - alert msg "successful, go check mailbox for welcome msg"
6. check mailbox
   - expect: a welcome email


## Backend
- `GET /gotosignup.controller` -> returns view `user/signup`
- `POST /checkUserId` -> returns JSON `{ u_id: ""}`
- `POST /userSignup` -> returns JSON `{ success: "註冊成功"}`