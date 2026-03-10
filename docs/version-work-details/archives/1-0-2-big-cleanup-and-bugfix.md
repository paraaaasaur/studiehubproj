# 1.0.2 - Big Cleanup & Bugfix

## Goals
- Provide a less messy foundation for the upcoming 1.1.0 big refactoring
- Remove obvious trash and fix quick bugs

## Target
1. Zero-impact
   - unused/duplicate/trash/toy files or code
   - safe for removal
2. Impactful
   - draft features, design choice, improper features...
   - unsafe for removal UNTIL intents are preserved, decisions are documented elsewhere 
3. Comments that don't carry visible intents, design or funniness 
   - e.g., to-dos/fixmes, tutorials, quick notes, in-case-of code, artifact code

## Recommended Process
1. Endpoints/Controllers + related usages/dependencies
2. Frontend (Views): thoroughly examine HTML/JS/CSS/JSP of each one by one
3. All the others == trivial
   - service & dao layers
   - trash or toy files
   - configs
   - libraries
   - data
   - resources
   - ...
> Reference Notion tables for endpoint and frontend(view) items.

---

## 1. Cleanup
> Note: when a removed item is unused, duplicate, trash or toy, there won't be
> further explanation added, otherwise see the following glossary:
> - `draft`: optimistic to reintroduce in the future
> - `design[: <reason>]`: removed for the given design choice
> - `improper[: <reason>]`: not a proper feature for the given reason

### Backend

#### Common
- Dependency 
  - `spring-boot-devtools`: multiple known runtime issues (e.g., Broken session & cookies lifecycle)
    - free to remove; no impact on app's intended runtime behavior
- Files
  - classes
    - `HandlerInterceptorDemoController`
    - `com.group5.springboot.annotation.dev.DeprecatedDetail`
    - `StudiehubprojApplicationTests`
  - trivial
    - `dev/db/sqlserver/02-data-v1.0.0-snapshot.sql`
    - `src/main/java/com/group5/springboot/utils/api/佔位用.txt`
    - `src/main/resources/static/api/佔位用.txt`
  - resource
    - `src/main/resources/static/video/productVideo/【从零开始】第一期：学英语，从入门到摔门！_1.mp4`
    - `src/main/resources/static/images/`: `pic01.jpg` ~ `pic11.jpg`
- Code
  - `System.out.println` everywhere
  - `SystemUtils#pathToClob`

#### User Domain
- Code
  - endpoints
    - `GET /gotoUserIndex.controller` (draft)
    - `GET /gotoDeleteUser.controller/{u_id}` (draft)
    - `GET /showSingleUser.controller/{u_id}` (draft)
    - `DELETE /user.controller/{u_id}`
  - unused session attribute `adminBean`
  - service/dao methods
    - `#isUserExist`
    - `#deleteUserById`

#### Product Domain
- Files
  - domain model artifact `OldProductInfo`
- Code
  - endpoints
    - `GET /buyProduct`
    - `GET /ratingAVG` + related dependencies:
      - `RatingService#ratingAVG`
      - `RatingDao#ratingAVG`
  - controllers
    - `ProductController`: unused fields `context`, `cartItemService`
    - `ProductResultController`: unused fields `context`, `ratingService`
    - `RatingController`: unused fields: `context`, `ratingService`
  - service/dao methods
    - `RatingServiceImpl`: `#findAllRating`
    - `RatingDao`: `#findAllRating`
    - `ProductServiceImpl`, `ProductDao`, `ProductDaoImpl`
      - `queryByName` (replace by `search`s)
      - `isProductExist`
      - `stars`

#### Question Domain
- Code
  - endpoints
    - `GET /question.controller/startRandomExam`
    - `GET /question.controller/sendRandomExam`
    - `GET /question.controller/tstartRandomExam`
    - `POST /goInsertChatReply`
    - `DELETE /deleteChat/{c_ID}`
  - service/dao methods
    - `#sendRandomExam`
  - `QuestionDaoImpl`
    - unused field `@Autowired Question_Info question_Info;`

#### Chat Domain
- Code
  - endpoints
    - `GET /chatIndex`
    - `GET /goInsertChatReply` (design)
    - `GET /goDeleteChat/{c_ID}` (design)
    - `GET /selectSingleChatReply/{c_ID}`
  - `ChatController`
    - unused session attribute `adminBean`
    - unused fields `@Autowired Chat_Info chat_Info`, `@Autowired UserController uc`
  - service/dao methods
    - `#updateChat`

#### Event Domain
- Files
  - domain models `Article`, `Comment` and `Sendmessage` (draft)
    - related usages: CRUDs methods in event service/dao
- Code
  - endpoints
    - `GET /NewFile`
    - `GET /Eventindex`
    - `GET /success`
  - `EventController`
    - unused field `@Autowired ServletContext context` after the following usage cleanup
    - `#insertEvent`: unused param `Model model`
    - `#insertSaveEvent`:
      - unused param `Model m`
      - unused local variable `mimeType` outside of console display
    - `#updateSaveEvent`: 
      - unused local variable `mimeType` outside of console display
    - `#deleteEditPage`: unused param `Model model`
    - `#deleteadminEvent`: unused param `Model model`
    - `#verification`: unused param `Model model`
    - `#deleteverification`: unused param `Model model`
    - `#signupclick`: unused param `Model model`
    - `@ModelAttribute getSendmessage`

#### Order & Cart Domains
- Files
  - toy classes
    - `com.group5.springboot.controller.cart.FooController`
    - `com.group5.springboot.controller.cart.Tttt`
    - `com.group5.springboot.controller.cart.test.*`
    - `com.group5.springboot.service.cart.testing.*`
    - `com.group5.springboot.service.cart.FooService`
    - `com.group5.springboot.model.cart.testing.*`
  - trash
    - `src/main/java/com/group5/springboot/model/cart/帶關聯的SQL語法 (測試中).sql`
    
- Code
  1. service/dao methods
     - cart
       - `#test05`
       - `#selectByUserId` (service)
     - order
       - `#selectOrderInfoByOPUJoin`
       - `#selectCheckOrderExistence`
       - `#selectAll`
       - `#update`
       - `#delete`
       - `#selectCustom` (dao)
  2. endpoints
     - `POST /cart.controller/clientRemoveProductFromCart`
     - `POST /cart.controller/insertAdmin`
     - `GET /order.controller/adminInsert` (improper: forbidden resource operation)
     - `POST /order.controller/adminInsert` (improper: forbidden resource operation)
     - `GET /order.controller/adminUpdate/{identitySeed}` (improper: forbidden resource operation)
     - `POST /order.controller/adminUpdate/{identitySeed}` (improper: forbidden resource operation)
     - `GET /order.controller/test/selectOrderInfoByOPUJoin`
     - `GET /order.controller/adminSelectAll`
     - `POST /order.controller/adminSelectProduct`
     - `POST /order.controller/adminSelectUser`
     - `POST /order.controller/insertAdmin` (improper: forbidden resource operation)
     - `POST /order.controller/deleteAdmin` (improper: forbidden resource operation)
  3. trivial
     - `OrderViewController`: unused fields `orderService`, `orderValidator` after the following usage cleanup
     - `OrderController`: unused fields `productService`, `userService` after the following usage cleanup


### Frontend

#### Main Targets
- Session checks
  - they are decorative without the backend anyway
- User/Admin logic existing in wrong pages
- Unused/Duplicate/Broken/Commented code (JS, HTML, CSS, and JSP)

#### JSP Includes
- `universal/adminSidebar`: 
  - sidebar item _admin-login_ (improper)
  - sidebar item _credit-page_ (improper)
  - also remove relevant logic in other views which include and `universal/adminSidebar`

#### User Domain
- View `user/deleteUser` (draft)

#### Question Domain
- Views
  - `question/examMultipleQuestion`
  - `question/examQuestion`
  - `question/questionIndex`

#### Chat Domain
- Views
  - `chat/chatIndex` (draft)
  - `chat/insertChatReply` (design)

#### Cart & Order Domains
- Views
  - `cart/orderAdminInsert` (improper: forbidden resource operation)
  - `cart/orderAdminUpdate` (improper: forbidden resource operation)
  - `qwiojeoiw.html`

---

## 2. Bugfix

### Backend

#### Product Domain
- Code
  - endpoints
    - `GET insertProduct`, `POST insertProduct`
      - fixed names; added missing leading slash
    - `POST /saveRating`
      - fixed return value from `product/Product` to `"redirect:/takeClass/" + p_ID`
        - related changes
          - enabled test `#saveRatingResult`
          - `product/Product#ratingSubmit` onclick event: replace AJAX with simple synchronous JS

#### Question Domain
- Code
  - endpoints
    - `GET /question.controller/sendRandomMixExam`
      - fixed dependency: missing status check `QuestionDaoImpl#sendRandomMixExam`
      - fixed its integration test which didn't perform `N -> Y` approval
    - `GET /question.controller/guestOneQuestion/{q_id}`
      - replaced `findById` with new service/dao methods `findApprovedById` (sees below)
  - service/dao
    - added methods `findApprovedById` to retrieve only approved question by id

#### Event Domain
- Data
  - fix the dev data for `EventInfo` table: `a_uid` is not FK and filled with fake users
- Code
  - endpoints
    - `POST /updateEvent/{a_aid}`: enabled domain validation
    - `GET /signupclick/{a_aid}`
      - fixed subtle boolean condition for registration capacity excess
      - enabled test `#signupclick_whenApplicantLimitExceeded_thenRequestIsRejected`

#### Cart & Order Domains
- Code
  - endpoints
    - redundant, inconsistent leading slashes in returned view names + fix related tests
  - service/dao methods
    - added missing abstract method declarations

### Frontend

#### Main Focus
- Fix invalid layout includes in `universal/` folder
- Fix minor bugs
- Format code

#### Product Domain
- Code
  - `product/Product`
    - hid and disabled buy-product button from guests
  - `product/showProduct`
    - fixed hardcoded query param `producttypename`
    - added js to alert unused model attribute `successMessage`
  - `product/showProductToUser`
    - resolved search button id conflict; changed from `query` to `product-query`

#### Question Domain
- Code
  - `question/insertQuestion`
    - fixed one-click demo js for failing to check the assigned answer
  - `question/queryQuestion`
    - resolved search button id conflict; changed from `query` to `question-query`

#### Chat Domain
- Code
  - `chat/selectOneChat`
    - added prevent default for `sendData` to resolve minor bugs

#### Cart & Order Domains
- Code
  - `cart/cartAdminInsert`
    - fixed form enctype to `x-www-urlencoded` from `multipart/form-data`
    - removed redundant `id`s in jsp `form:input`s, because `path` already covers `id` and `name` 
---

## 3. Added/Changed

### Frontend

#### Question Domain
- Code
  - `question/examMixQuestion`
    - finished spinner component to loading phase using existing spinner classes
  - `question/verifyQuestion`
    - suspended search bar feature because the endpoint is missing pending status check