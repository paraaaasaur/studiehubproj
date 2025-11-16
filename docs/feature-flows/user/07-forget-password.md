## Frontend
1. click on the 忘記密碼`<a>` on the 登入 page on the sidebar
   - `GET /gotoForgetPassword.controller`
   - to forget password page
2. fill in the email and click on the 送出`<button#send>`
   - `POST /sendRandomPasswordToRegisteredEmail.controller` to send email + alert message
   - redirected to the login page
3. see a success alert message, receive an email reset email, and redirected to login page 
4. use the new password to log in successfully


## Backend
- `GET /gotoForgetPassword.controller` -> returns view `user/forgetPassword`
- `POST /sendRandomPasswordToRegisteredEmail.controller` -> returns JSON `{ success: "新密碼信件已寄送至您的信箱，請盡快更新!" }`
- `GET /gotologin.controller` -> returns view `user/logic`