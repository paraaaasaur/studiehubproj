# 1.1.0 - Structural Debt Cleanup

## Goal
- Fix tangled, poorly bounded logic across controller/service/dao layers
- Dismantle panic try-catch spams in controllers
- Fix mis-/poor/outdated uses of the frameworks (Spring especially)

## Principles
- I should be able to instantly spot what a controller method promises without having to dive 3+ layers to check out implementation for web contract
- No need to do unit tests for new/changed service methods extracted from inline controller logic
- Change field/setter injection to constructor injection
- Remove usages of `@autowired` entity fields & simply make new instance inline
  - Spotted in DAOs/Services/Controllers...
  - Identify all usages before removal
- ...And remember to verify everything with IT tests! That's why you spent eternity to make them!!
- Split controller by subject (user/admin) + optional sub-categorization

## Backend

### CCCs

#### Common
- Refactor view names (+ controller returned views), and handler method names
  - styles: kebab-case (dash-separated): HTML file naming conventions
  - add a role-separated layer in the layout (admin, instructor, student, guest)
  - be careful about usages:
    - tests, controller return value
    - !! local inclusions inside views
- Replace `@autowired` dao/service implementations with interfaces
- Organize validation logic 
  - move controller inline clutters to their own validators for centralization
- Create JPA convenience methods for entity associations (if any exists)
- Consolidate fragmented service methods into cohesive operations

### Specifics

#### Common
- Replace `servletContext#getMimeType` with `Files#probeContentType`
  - But okay to overlap if naturally
- Use `setParameter` instead of raw hql/jpql/sql (which allows injections) in persistent logic
- Hide 會員資訊 on the sidebar from guests


#### User
- `@ModelAttribute("userBean") getLoginUserInfos`:
  - reduce it to a service method
  - apply only where it's used, instead of getting eagerly triggered all over the controller
  - recommended name: e.g., `loadProfile(uid)`
  - update usage: `changePassword`, `updateUser`: replace `@ModelAttribute("userBean")` with `@SessionAttribute("loginBean")`
- Switch session attr handling strategy 
  - old: class annotations `@SessionAttributes` in controllers
  - new
    - remove all `@SessionAttributes`
    - use explicit writer `httpSession.setAttribute("loginBean", user_info);` instead of generic `Model`
    - use explicit reader `@SessionAttribute("loginBean") loginBean` instead of generic `Model`
  - update usage in handler methods
  - update usage in helper methods:
    - `#updateLoginBean` method signature
      - from: `(Model, SessionStatus)` (`SessionStatus` is unused btw)
      - to: `(HttpSession)`
    - `@ModelAttribute("userBean") #getLoginUserInfos` method signature
      - from: `(Model)`
      - to: `(HttpSession)`

#### Product


#### Question
- Simplify `@ModelAttribute` method for `Q1` into a service method to call in controller explicitly

#### Chat
- Rename `ChatValidator` as `ChatReplyValidator` for clarity


## Frontend
Recommended steps:
1. Make a `common.js`, and first of all, move login-state auth logic, sidebar + header logic there  

### JavaScript
- Structural cleanup & internal hygiene
- No logic changes intended
- Mind compass: Does what I'm trying to do worth even after 2.0.0?
- Make me INSTANTLY understand what this page does when `window.onload`...

1. Name functions and replace `var` → `const`/`let` to
   - tighten scopes
   - clear out ambiguities
2. Resolve type coercions where they should be strict equality
3. Move inline JS into discrete files (static/js/...)
4. Extract initial actions (bind functions to elements...) to `init()` function to run, leaving only global scope required items like global variables (counter...)
5. Replace inline XHR calls → fetch API
   - small, mechanical modernization
   - no redesign at the moment (therefore not 1.4.0)
6. Resolve duplicated JS across pages (DRY)
7. Unify navigation methods
   - typical navigation: use `window.location.href = url`
   - backward prevention (like after login): use `window.location.replace(url);`

### CSS, HTML
- Make discrete files as well if any heavy amount exists.