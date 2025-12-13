# Frontend
(setup: admin-login + to admin-top-post-list)
1. choose a top-post and hit the delete icon
   - `GET /goDeleteChatAdmin/{c_ID}`
   - to admin-top-post-detail page
2. onload `GET /selectSingleChat/{c_ID}`
   - retrieve the top-post and display it
3. hit 刪除`<button#deleteData>`
   - hit ok on the pop-up confirm box
   - `DELETE /deleteChatAdmin/{c_ID}`
   - see a success message and get navigated back to admin-top-post-list


# Backend
- `GET /goDeleteChatAdmin/{c_ID}` -> returns view `chat/deleteChatAdmin`
- `GET /selectSingleChat/{c_ID}` -> returns JSON: an object of `chat_info`
- `DELETE /deleteChatAdmin/{c_ID}` -> returns JSON: a map of `success` key