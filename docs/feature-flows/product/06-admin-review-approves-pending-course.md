## Frontend
(Setup: admin-login + have pending course)
1. go to 課程資訊 > 待審核課程`<a>` on the sidebar
   - `GET /findAllProductPending`
   - show admin-pending-courses-review page
2. window.onload triggers `GET /findAllProductPendingAccess`
   - retrieve & display all pending courses
3. click on 通過`<button>`
   - `GET /accessResult/{p_ID}` to approve the new course
   - shows the current page
4. go to 所有課程(user side) and see new course is now on shelf


## Backend
- `GET /findAllProductPending` -> returns view `product/pendingAccess`
- `GET /findAllProductPendingAccess` -> returns JSON `{  
 "size" : <list length>,  
 "list" : [list of pending products]  
}`
- `GET /accessResult/{p_ID}` -> returns view `product/pendingAccess`
