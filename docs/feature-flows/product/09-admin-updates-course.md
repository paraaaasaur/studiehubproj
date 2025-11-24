## Frontend
(Setup: admin-login)
1. click on 所有課程`<a>` on the sidebar
   - `GET /queryProduct` -> to admin-products page
2. click on 更新`<button>` of any product
   - `GET /updateProduct/{p_ID}`
   - to update-product page
3. edit title, and click on SUBMIT`<input/submit>`
   - `POST /updateProduct/{p_ID}` to send: {p_Name, p_Class, p_Price, descString, imgFile, videoFile}
   - submit and get returned to admin-products page
4. go to the pending page and click on 通過`<button>`
   - `GET /accessResult/{p_ID}`


## Backend
- `GET /queryProduct` -> returns view `product/showProduct`
- `GET /updateProduct` -> 
  - brings populated product in the model
  - returns view `product/editProduct`
- `POST /updateProduct` -> redirected to url `/queryProduct` -> returns view `product/showProduct`
- `GET /accessResult/{p_ID}`