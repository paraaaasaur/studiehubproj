## Frontend
(Setup: admin-login + have pending course + go to 待審核課程 page)
1. click on 刪除`<button>`
   - `GET /deleteProduct/{p_ID}`


## Backend
- `GET /deleteProduct/{p_ID}`
   - redirects to `GET /queryProduct`
   - returns view `product/showProduct`