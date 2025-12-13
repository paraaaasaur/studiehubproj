# Frontend
1. click on the 討論區`<a>` on the sidebar
   - `GET /goSelectAllChat`
   - go to thread-list page
2. onload `GET /selectAllChat`
   - display the thread list from the server

# Backend
- `GET /goSelectAllChat` -> returns view `chat/selectAllChat`
- `GET /selectAllChat` -> returns JSON: an array of `chat_info`