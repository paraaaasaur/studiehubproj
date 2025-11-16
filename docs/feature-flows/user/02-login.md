## Frontend
1. to login view
   - action: click on the 登入`<a>` on the sidebar
   - expect: `GET /gotologin.controller` → to login page 
2. login
   - action: fill in id/psw, and then click on 登入`<button#logic>`
   - expect: `POST /login.controller`
3. to index page
   - trigger: status 200
   - expect: login success 
     - `GET /` (index page)


## Backend
- `GET /gotologin.controller` -> returns view `user/login`
- `POST /login.controller` -> returns 
  - `$[0].success : "登入成功"`
  - `$[0].loginBean : <user_info>`
- `GET /` -> returns view `index`