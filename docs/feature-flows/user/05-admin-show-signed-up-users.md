## Frontend
(Setup: admin-login)
1. click on the 會員資訊`<a>` on the sidebar
   - `GET /gotoShowAllUser.controller`
   - to display-all-users page
2. window onload
   - `GET /showAllUser.controller`
   - show all users


## Backend
- `GET /gotoShowAllUser.controller` -> returns view `user/showAllUser`
- `GET /showAllUser.controller` -> returns JSON of all users