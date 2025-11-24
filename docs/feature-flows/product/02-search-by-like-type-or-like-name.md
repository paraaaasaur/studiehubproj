## Frontend
(Setup: click on sidebar -> 所有課程)  
1. search by 課程關鍵字 = 英語 & 類別 = 日文, and click on 搜尋  
   - `GET /queryByProductName?pname=英文&producttypename=日文`
   - finds 2 + 1 result respectively (= 3 hits in total)

## Backend
- `GET /queryByProductName` -> returns JSON `
{"ratedIndex" : [ratedIndexList],
 "list" : [list of queried products],
 "size" : <result length> }
`