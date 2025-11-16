## Frontend
(Setup: login)
1. to update-info page
   - action: click on 編輯個人資料`<a>`
   - expect: `GET /gotoUpdateUserinfo.controller`
2. upload new profile image
   - action: upload a profile image, and then click on 儲存`<input/submit>`
   - expect: `POST /updateUserinfo.controller` and get "修改成功" alert
3. redirected back to user-info page
   - trigger: status 304
   - expect: new profile image (and any other edited changes)


## Backend
- `GET /gotoUpdateUserinfo.controller` -> returns view `user/updateUser`
- `POST /updateUserinfo.controller`
  - redirected to `GET /gotoUpdateUserinfo.controller`
  - returns view `user/updateUser`