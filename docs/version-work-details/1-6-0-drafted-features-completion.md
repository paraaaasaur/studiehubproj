# [1.6.0] - Drafted Features Completion

## Goal
> DISCLAIMER: all items in this version are optional based on your mood, so 
> feel free to postpone literally everything to 2.0.0 in progress with redesign, especially when it feels
>   - need remakes anyway after redesign
>   - cleaner to implement after redesign
>   - and so on
- Finish almost-done features
- Repair moderately broken features


## Items

### Common
- Introduce String trimmer bean
- Introduce Logger at different levels in place of `System.out.println`
- Frontend common js: 
  - sortObjectsByACommonKey
  - sortTable (improve)

### User

#### Wishlist
- User dashboard: `GET /gotoUserIndex.controller` + `user/userIndex`

#### Halfway
- functionalities: rememberMe, #gotoDeleteUser, #deleteUser, #showSingleUser
- Adds logout logic to all missing views

### Product

#### Missing
- "Check detail" functionality for pending courses

#### Repair
- `product/Product`: Whiteboard 404 when no preview is found
  - make media upload product insertion required
  - redesign as optional `block` when in 2.0.0
- Rating system JS used in `product/Product`, `product/showProductToUser`

### Question
- Remove `01-display-all-quiz-overviews` feature, because I believe raw questions are inappropriate user products. Expose only quizzes, the meaningful design & combination of questions.
  - usage: 
    - frontend UI item 所有試題 in `sidebar`
    - view file `guestQueryQuestion`
    - backend endpoint `GET /question.controller/guestQueryQuestion`
    - integration test
- Move insert-question feature to admin side
  - usage:
    - frontend UI item from sidebar to adminSidebar
    - backend endpoints `GET /question.controller/insertQuestion` and `POST /question.controller/insertQuestion`
    - integration tests
- Apply new session rules 
  - requires admin: 
    - `GET /question.controller/findAllQuestions`
    - `GET /question.controller/queryByName`
    - `GET /question.controller/insertQuestion`
    - `POST /question.controller/insertQuestion`
- Repair countdown js for `question/examMixQuestion`
  - [ ] countdown invisibly goes on after submission
  - [ ] invisible time-up after submission breaks remarks with non-existing question numbers and answers
- Finish features:
  - improve exam-index, the intro page available for
    - [ ] jp multi-select exam
    - [ ] jp single-select exam
    - [ ] jp listening exam
    - [x] jp mixed type exam
    - [ ] en mixed type exam
  - bring any remaining sidebar item to this page
    - idea: choose a type of quiz and a subject you want to practice

### Chat
- Make a chat-index to show:
  - weekly trendy categories
  - weekly/daily trendy topics 
    - we don't have view counts, but just make the UI for now
      - add `view_count` and/or `emoji` column in 2.0.0 schema redesign
  - random suggestion
    - for now: literally random suggestion for display purpose
    - later: suggested by most emoted post with a random type of stamp
      - e.g., thumb-up, heart, happy...

- Add choices to dotdotdot image to have further choice for same-page update/delete
- Add js to allow the use of `ckeditor` on same-page insert/update, to replace new page editing style in 1.0.0 era that got removed in 1.0.2 fix
  - update: toggle between display message & update message
  - insert
- Grant user power to delete their own posts 
  - post: 404
    - Add a special 404 page that indicates the post has been deleted in case someone accesses from url or bookmark
  - reply: placeholder like that on dcard
- Ignore or repair anti-pattern of the workflow in `chat` domain (the whole `chat_info` & `chat_reply` retrieval, insertion, update, and representation strategy)
  - right now: 
    - when `chat_info` is persisted via current insertion endpoint, it is also persisted in `chat_reply` like a duplicate there other than the main one in `chat_info`
    - when loading a thread, 
      - `chat_info` is queried, but only used to represent the title
      - related `chat_reply`s are queried, and the main post of the thread is filled with the duplicate in the `chat_reply`, not the original one in `chat_info`
    - while I feel that counterintuitive, "repairing" doesn't seem worth the time.
      - Plus, it's destined to get redesigned in 2.0.0

### Event

#### Wishlist
- A themed event index page instead of raw event threads
- `article` or `blog` domain which can be posted by an `instructor` and have `comment`s below
  - https://startbootstrap.github.io/startbootstrap-blog-post/
  - looks like no relevance to the `event` domain; an independent, separated one
  - hashtag feature: navigate through other articles of the same category
- `sendmessage`: ...???? Looks like the reply domain to an `event`?
- Google Maps API instead of `<a href="">'`

#### Missing Features
- "Cancel" functionality in event detail page
- A page for applicant to 
  - view all registered events
  - cancel registration