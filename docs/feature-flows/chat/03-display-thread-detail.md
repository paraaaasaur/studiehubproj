# Frontend
(setup: to thread-list page)
1. click on any thread title
   - `GET /goSelectOneChat/{c_ID}`
   - to thread-detail page
2. onload:
   - `GET /selectSingleChat/{c_ID}` to retrieve data to display thread title (= top post's title)
   - `GET /selectOneChat/{c_ID}` to retrieve data to display top post + replies content


# Backend
- `GET /goSelectOneChat/{c_ID}` -> returns view `chat/selectOneChat`
- `GET /selectSingleChat/{c_ID}` -> returns JSON: a `chat_info`
- `GET /selectOneChat/{c_ID}` -> returns JSON: list of arrays of chat_reply(at index 0s) + user_info(at index 1s)