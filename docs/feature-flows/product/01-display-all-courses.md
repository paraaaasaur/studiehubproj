## Frontend
1. click on 所有課程`<a>` on the sidebar  
   - `GET /queryProductForUser`
   - show all-products page
2. (auto-triggered by window.onload)
   -`GET /findAllProduct`
   - Each product result should have
     - p_img
     - average rating shown in stars, or 尚未評論 when n/a
     - p_price


## Backend
- `GET /queryProductForUser` -> returns view `product/showProductToUser`
- `GET /findAllProduct` -> returns JSON `
{"ratedIndex" : [ratedIndexList],
"list" : [list of queried products],
"size" : <result length>}
`