# 1.0.2 - Big Cleanup & Bugfix
(For endpoint and view specific/centered items, see Notion tables)

## Goal
- Profile the frontend (HTML, CSS, but mainly JS) mess
- Clean the frontend mess, notably:
  - Unused named/unnamed functions/variables/files
  - duplicates
- Make room for the upcoming 1.1.0 structural refactoring
- Fix known issues recorded in 1.0.0

- Target: known unused classes/methods/files, duplicate utilities + quick bugfixes
- feature bug quick fix

## Backend

### Common
- Remove Maven dependency `spring-boot-devtools` for multiple known runtime issues
  - Broken session & cookies lifecycle
  - No effect on real app logic
- Fix wrongly annotated `@Transactional`, `@Repository` and `@Service` in DAO and service interfaces and impls
- Media CRUDing bug:
    - target: save(product, question), update(user, product, question)
    - bug details:
        1. wrong saved filename: missing a dot before the extension name
        2. saves extension name `null` when no media file is uploaded

### user
- Remove unused session attribute `adminBean`

### products
- Remove unused class
  - `OldProductInfo`
- Remove unused fields in all 3 controllers
- Fix raw use of `EntityManager` in `ProductController` and use user service instead
- Removed unused method: `RatingService#findAllRating` & `RatingDao#findAllRating`

### question
- Missing pending check in the query: `sendRandomMixExam`, `sendRandomExam`
- Service `findById` used in controller `guestOneQuestion` should have been something like `queryValidQuestionById`

### order & cart
- Remove stupid, useless dao/service methods
  - `CartItemDao`: `#test05`
- Remove hidden messy packages of test classes

### chats
- Remove unused session attribute `adminBean`
- Fix the generic type in controller method `findOneChat` and service/dao methods `findAllChatReply` because it's not true
  - change from `<Chat_Reply>`to `<Chat_ReplyAndUser_Info>`
- `#sendData` onclick event: add `event` param and `event.preventDefault()` in the first line to fix bugs
  - known bugs:
    - no login + send reply ⇒ not correctly navigated to login page (see the last else clause), but simply a question mark(?) addition on the url, which is probably the default behavior for an `<input:submit>` inside a not configured `<form>`
    - login + send reply: no reload for the first time but fine after the second one
- Missing logic in `serviceImpl#updateChatReply`, where the hook in `Chat_Info` is not updated

### events
- Try fixing missing FK on the column `a_uid` in the `EventInfo` table & the fake seed data
- unreliable 


## Frontend
- Reference: `view_specific_issues` and `view_common_issues` on Notion
- Frontend profiling: Inspect frontend view files one by one, and inspect JS, CSS and HTML junks around each of them
- Resolve known quick issues recorded by accident in 1.0.0

### Common


### Question_Info
- Fix 一鍵: js should check selection B but currently fails to do so

### Chat_Info
- Add difference to tell between a chat and its replies, otherwise it's annoying for dev purpose
  - e,g., out-of-the-box class from bootstrap