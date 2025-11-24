## Frontend
(Setup: go to the 所有課程 page from the sidebar)  
1. click on any course -> `GET /takeClass/{p_id}`   
   - display-course page  
   - sees
     - preview video
     - course description via `GET /findRatingById?p_ID={p_ID}` on `document.ready`
     - rating stars
     - comments


## Backend
- `GET/ takeClass/{p_id}` -> returns
  - view `product/Product`
  - model attr `product` mapped to found product
- `GET /findRatingById` -> returns JSON `{  
 "size" : <size>,  
 "list" : [rating list]
}`