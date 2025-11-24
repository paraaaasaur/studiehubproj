## Frontend
(Setup: login)
1. click on 新增課程`<a>` on the sidebar
   - `GET /insertProduct` -> show create-new-course page
2. fill in blanks (with 一鍵) + upload image & video, and click on SUBMIT`<input>`
   - `POST /insertProduct`


## Backend
- `GET /insertProduct` -> returns view `product/insertProduct`
- `POST /insertProduct` -> redirects to `GET /queryProductForUser` -> returns view `product/showProductToUser`