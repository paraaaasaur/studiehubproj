# Frontend
(setup: login + to a thread-detail where you are the OP)
1. click on the vertical (...) icon
   - `GET /goUpdateChat/{c_ID}`
   - to update-top-post page
2. edit the top post with advanced editor that offers HTML tag effects
   - or you can use 一鍵 
3. hit 編輯`<input:submit>` to submit edited content
   - `POST /goUpdateChat/{c_ID}`
   - get redirected(302) back to the thread-detail page


# Backend
- `GET /goUpdateChat/{c_ID}` -> returns view `chat/updateChatReply`
- `POST /goUpdateChat/{c_ID}`
  - send flash attr `successMessage`
  - redirect: `GET /goSelectOneChat/{c_ID}`
  - to the view `chat/selectOneChat`