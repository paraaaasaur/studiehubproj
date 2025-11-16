## Frontend
1. click on 管理員頁面`<a>` on the sidebar
   - `GET /gotoAdminIndex.controller`
   - shows admin-login page
2. fill in admin credentials and click on 登入`<input#login>`
   - `POST /AdminLogin.controller`
   - pop up the message 管理員登入成功, shows redirected page admin-index
3. click on the 管理者登出`<a#logout>` on the top-right of the header
   - `GET /adminLogout.controller`
   - to the index


## Backend
- `GET /gotoAdminIndex.controller` -> returns view `user/adminLogin`
- `POST /AdminLogin.controller`
  - returns view `redirect:/gotoAdminIndex.controller` -> redirected to `adminIndex`
  - gets session attribute `adminId`
  - gets flash attribute `{ success: "管理員登入成功" }`
- `GET /adminLogout.controller` -> returns view `/` and finishes session