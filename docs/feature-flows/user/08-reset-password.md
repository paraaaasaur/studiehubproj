## Frontend
(Setup: login)
1. click on the 會員資訊 > 更改密碼`<a>` on the sidebar
   - `GET /gotoChangePassword.controller`
   - to change-password page
2. fill in the old/new/new x2-passwords and click on 儲存變更
   - `POST /changePassword.controller`
3. redirected to the index


## Backend
- `GET /gotoChangePassword.controller` -> returns view `user/changePassword`
- `POST /changePassword.controller`   
  -> returns JSON `{ successMessageOfChangingPassword: "" }`  
  -> redirected to `/`
- `GET /`