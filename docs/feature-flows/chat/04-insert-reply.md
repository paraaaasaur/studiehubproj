# Frontend
(setup: login + to thread-detail page)
1. fill in reply textarea, and then hit 送出`<input#sendData:submit>`
   - `POST /insertChatReply`
   - alert `新增成功` and then reload the page


# Backend
- `POST /insertChatReply` -> returns JSON `success` or `fail` message