# Frontend
(setup: admin-login)
1. click on 討論區 on the admin sidebar
   - `GET /goSelectAllChatAdmin`
   - go to admin-top-post-list page
2. onload `GET /selectAllChatAdmin`
   - display the top-post list from the server


# Backend
- `GET /goSelectAllChatAdmin` -> returns view `chat/selectAllChatAdmin`
- `GET /selectAllChatAdmin` -> returns JSON: an array of `chat_info`