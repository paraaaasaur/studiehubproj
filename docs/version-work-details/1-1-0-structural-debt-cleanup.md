# 1.1.0 - Mass Refactoring: Structural Debt Cleanup

## Goal
First focused effort to address major maintainability issues, in preparation  
for the upcoming dependency update(@1.2.0) and project redesign(@2.x.x).

---

## Target
- Backend
  - main: framework usages, architecture, build
  - minor: semantics, code hygiene
- Frontend
  - main: naming consistency, js/css modernization, html semantics, npm (js dependency management)
  - minor: state-driven rendering migration
- Project Tree
  - main: resources

### Backend

#### 1. Architecture
+ Correctness
  - (1) fixed injection type and naming style inconsistencies in dao/service classes
  - [ ] (6) simplify/correct data shapes in web layer
    - targets: request payloads, model attributes
    - not targets: nested shapes (e.g., Object Map)
    - goal: align with intents, not just picking one random side
      - e.g., For the `Event` domain,
        - prefer: `Event` record when possible
        - avoid: generic `HashMap`
    - replace god entities with POJO DTOs
    - related
      - client code in views
      - depended on service/dao methods
    - changed endpoints
      - user domain
        - `GET /gotoUpdateUserinfo.controller`
        - `POST /login.controller`
        - `POST /userSignup`
        - `POST /changePassword.controller`
        - `POST /updateUserinfo.controller`
        - `POST /sendRandomPasswordToRegisteredEmail.controller`
      - product domain
        - `GET /takeClass/{p_ID}`
        - `GET /updateProduct/{p_ID}`
        - `GET /insertProduct`
        - `POST /updateProduct/{p_ID}`
        - `POST /insertProduct`
      - question domain
        - `GET /question.controller/insertQuestion`
        - `POST /question.controller/insertQuestion`
        - `GET /question.controller/guestOneQuestion/{q_id}`
        - `GET /question.controller/modifyQuestion/{q_id}`
        - `POST /question.controller/modifyQuestion/{q_id}`
        - `GET /question.controller/verifyOneQuestion/{q_id}`
      - event domain
        - `GET /insertEvent`
        - `POST /insertEvent`
        - `GET /updateEvent/{a_aid}`
        - `POST /updateEvent/{a_aid}`
        - `GET /Selecteventcontent/{a_aid}`
+ Compliance
  - fix failed boundary enforcement with mixed/leaked logic across layers, notably:
    - common
      - return type (e.g., a json-ready `Map` from dao layers)
      - mixed logic in wrong layers
        - move business logic in controllers and daos to services
    - local
      - `ProductController`: using infra object `EntityManager` rather than service in controller layer
      - ...
  - (6) encapsulate inline validation clutter in controllers into their own validators
  - extract storage logic to dedicated service
+ Improvement
  - controller splitting
  - mixed dao splitting
    - `ChatDao` covering both `Chat_Info` and `Chat_Reply` atm
    - `EventDao`

#### 2. Framework Usage
+ Spring
  - correctness
    - annotation misuses
      - (1) fixed misplaced `@Transactional`/`@Service`/`@Repository` on services/daos
      - (3) removed `@Component` on domain models (jpa entities)
        - fixed related misuses: `@Autowired` entities
          - `UserController`, `UserDaoImpl`: replaced autowired `user_info` fields with local variables
  - framework magic reduction
    - (6) extract niche `@ModelAttribute` methods to explicit services/helpers
      - prerequisites:
        - unambiguous payload (4/5—almost a must-have): ambiguities are just highly visually misleading to intents and technical details
        - organized responsibility boundaries (3/5): not blocking, but changing code on top of mess makes tasks harder and adds up more temporary messes that can be avoided  
      - no intended change to intents
        1. `UserController`: `userBean`
           - usages
             - `POST /changePassword.controller`
             - `POST /updateUserinfo.controller`
             - `users/change-password.jsp`
             - `users/edit-profile.jsp`
        2. `QuestionController`: all 4 `@ModelAttribute` methods
    - `@Autowired ServletContext` to `getMimeType()` => use java nio `Files.probeContentType()`
  - style consistency
    - (2) di now defaults to constructor injection
  - policy change
    - (5) enforced explicit session handling [policy](../project-policy.md)
      - switched to `HttpSession`-based mechanism to manage session attributes
      - the following old strategies should be avoided from now on:
        - set/get session attributes with `Model`
        - class-level `@SessionAttributes`
        - `SessionStatus.setComplete()` to end sessions
      - related changes
        - `UserController`: method signature changes
          - `updateLoginBean()`: param `(Model, SessionStatus)` => `(HttpSession)`
          - `@ModelAttribute getLoginUserInfos()`: param `(Model)` => `(@SessionAttribute(required = false) UserInfo loginBean)`
+ JPA
  - correctness
    - sql injection => `setParameter()`
    - unnecessary native queries => typed ones
  - later - wait until 1.2.0 dependency upgrade or 2.0.0 schema redesign
    - associations
    - convenience method (if needed)

#### 3. Code Hygiene
- Method/Class/Variable names overhaul
  - easy-to-forget cases
    - `ChatValidator` => `ChatReplyValidator` for clarity
- Review classes to enforce good practices, notably:
  - extract reusable logic
  - raw uses in generic
  - tighten modifiers
  - (and more)
- Compose/decompose service/util methods into cohesive/discrete operations if any spotted

#### 4. Semantics
- Correctness
  - object contract violation: unexpected/unpredictable `toString()` overrides in Question/Product entities
    - related fix
      - `ProductDaoImpl.queryByName()`: implicit/unexpected `p_ID` from `String.valueOf(list.get(i))` => `String.valueOf(list.get(i).getP_ID)`
      - ...
  - api type contract mismatch: lying return type `List<Chat_Reply>` in `ChatController.findOneChat()`
    - should have been something like `<Chat_ReplyAndUser_Info>`

#### 5. Build
- Change ecpay `AllInOne.java` source code to stop depending on and remove the local lib `log4j.jar`


### Project Tree
- resources => move to under `WEB-INF/`, otherwise all resources become free to request without permission


### Frontend

#### 1. Consistency
- (4) View naming standardization
  - added view naming policy (see `docs/project-policy.md`)
  - renamed all view files
  - reorganized view directory hierarchy
  - related changes:
    - tests (view name assertions)
    - ssr endpoints (return values)
    - jsp include tags (path references)
- View titles overhaul

#### 2. Code Hygiene
- JS modernization
  - language
    - `var` => `const`, `let`
    - for-i loops => for-of, for-in, `forEach()`, `map()`
    - type coercions
  - platform
    - dom api
    - `fetch` for async js
    - decouple app js from jquery?

#### 3. Architecture
- new baseline rules
  1. move inline js/css to discrete files for each view
  2. extract common js, e.g.,
     - auth.js
     - unify and formalize navigation js
       - typical navigation: use `window.location.href = url;`
       - backward prevention (e.g., after login): use `window.location.replace(url);`
  3. event handling cleanup
  4. move inline styling in js/html to classes
- improvement
  - migrate to es module

#### 4. HTML Semantics Improvement
- introduce semantic tags full-on (thought: better aggressive than defensive for learning good practices)
- replace visual styling `<br>` and `<hr>` using css styling

#### 5. Build
- introduce npm to manage dependencies

#### 6. Too-Expensive
- State-driven rendering for all views

---

## Add

### Question
- add a new endpoint to retrieve only pending data for the pending page's search bar

---

## Fix

### User Domain
- [ ] `UserController`
  - add missing feature rememberMe
    - ```java
      boolean rememberMe = "null".equals(loginRequest.getRememberMe());
      session.setMaxInactiveInterval(rememberMe? 1800 : 10);
      ```
  - add missing old password check in change password feature

### Chat Domain
- [ ] `ChatServiceImpl#updateChatReply`: the hook in `Chat_Info` is not updated