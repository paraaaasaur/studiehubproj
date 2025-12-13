# Frontend
(setup: login + to thread-list page)
1. click on the pencil icon on the bottom right
   - `GET /selectAllChat`
   - go to create-top-post page
2. fill in the form or click on 一鍵, and then click on `<input#sendData>` to submit
   - `POST /insertChat/{c_ID}`
   - alert success message
   - get redirected back to thread-list page


# Backend
- `GET /selectAllChat` -> returns view `chat/selectAllChat`
- `POST /insertChat/{c_ID}` -> returns json `success` or `fail`