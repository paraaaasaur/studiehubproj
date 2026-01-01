# Roadmap

## [1.0.0]

### Goal
- Make the legacy project fully reproducible, platform-independent, and testable

## [1.0.1] - Security Patch: Required Session on Certain Endpoints

### Goal
1. Resolve the security vulnerability of current incomplete, inline session auth implementation that leaks out privileged server-side operations
2. Resolve XSS risk brought up by the API `CKEditor` used around `chat` domains for lack of HTML sanitization
3. Fix related or critical bugs


## [1.0.2] - Big Cleanup & Bugfix
(For endpoint and view specific/centered items, see Notion tables)

### Goal
- Profile the frontend (HTML, CSS, but mainly JS) mess
- Clean the frontend mess, notably:
  - Unused named/unnamed functions/variables/files
  - duplicates
- Make room for the upcoming 1.1.0 structural refactoring
- Fix known issues recorded in 1.0.0

- Target: known unused classes/methods/files, duplicate utilities + quick bugfixes
- feature bug quick fix


## [1.1.0] - Structural Debt Cleanup

### Goal
- Fix tangled, poorly bounded logic across controller/service/dao layers
- Dismantle panic try-catch spams in controllers
- Fix mis-/poor/outdated uses of the frameworks (Spring especially)

### Purpose
- Avoid unnecessary funny issues from upcoming dependency version bumps
- Prepare for 2.0.0 DTO mapping
- Prepare for 2.0.0 contract redesign


## [1.2.0] - The Nightmare Dependency Version Bumps

### Goal
- Update major dependencies
  - mainly Spring Boot (2.5 → 3.7) and JDK (11 → 17)
  - lombok, lombok, and lombok
- Fix every compiler error :)


## [1.3.0] - Pattern Upgrade Persistent Layers
A bit like leftover from 1.1.x that are better done here after dependency bumps
- Replace DAOs/Services with either one of the following: 
  - JpaRepositories + public repo field in service 
  - Maintained interface and an abstract basic impl of service + public repo field in service
- Rename Service/Dao interface/impl to Dao/Service and DaoImpl/ServiceImpl for consistency


## [1.4.0] - Logic Refactoring & Redesign
- Target: mainly controllers and services


## [1.5.0] - Conventional MVC Exception Handling Establishment
Introduce proper exception handling mechanisms

### Missing Pieces
- `@InitBinder`
- Convenience method in `Validator`s


## [1.6.0] - Drafted Features Completion
- Implement unfinished features, including all halfway and starting ones the folks seemed to try
  - User: rememberMe, #gotoDeleteUser, #deleteUser, #showSingleUser, #gotoUserIndex(what the fish... we had a user index!?)
- Adds logout logic to all missing views
- Repair broken features
  - Product: JS of Rating system on `product/Product`, `product/showProductToUser` 

- Introduce String trimmer bean
- Introduce Logger at different levels in place of `System.out.println`


## [2.0.0]

### Common
- Add: DTO records, Mapping framework (e.g., MapStruct)
- Switch validation framework: Spring Validator -> Jakarta bean validation
  - standardized 
  - fits to DTO
  - declarative, straightforward
  - web framework-agnostic (Jakarta EE)
  - learning purpose
- Add dependency: JOOQ
- Fix: Unify all JSON messages for success/fail to `success: <successMsg>` and `fail: <failMsg>` for consistency
- Go as restful as possible

### Contract Fix

#### Endpoint Renaming
- Non-restful: Clear and simple as possible to user
- Restful: follow a restful naming strategy consistently
- Identical (or at least aligned) handler methods
- `application/json; charset=UTF-8` -> `application/json` to align with modern browsers' default behavior

#### Logic


### Changed
- Fix contracts
  - endpoint renaming (and aligned method name as well)
- Mark specific endpoints with accurate response status (201 created, 204 no content, 401 unauthorized...), notably,
  - Endpoints which could respond with 401 later with Spring Security 
- Finish `Article`, `Comment` and `Sendmessage` for `EventInfo`
- Meaningful #toString, #equals, #hashCode (and #clones) and maybe implement Comparable<T> for domain models/DTOs
- Redesign db schema and JPA entities and all API contracts that read those fields 


### Common
- Inconsistent naming in endpoints/methods across layers: 
  - find/query/select

### User_Info
- Implement all CRUDs on admin page other than select-all 
- The way update-password form locks submit when wrong current password onmouseover? is detected
- Admin index is not meaningful to admin
  - make it like a dashboard or whatever different from the user index
- "Admin login" is by far the only route to get to admin index, unofficially (unhappy path) 
- Stop using a whole user entity as session token (bloated info; security)
- Fix hardcoded admin cred in admin controller
- reset password -> reject old psw == new psw
  - method bodies
  - tests
- Repair endpoint `GET /gotoAdminLogin.controller` and `GET /gotoAdminIndex.controller` to match semantics
  - Currently, the latter does both jobs, while the former does nothing in effect & not exposed

### ProductInfo
- Should `product` ever be changed to `course` to be more specific, if there's no other type of `product`?
- Consider requiring `Rating` reference an existing user

#### API Contract
- Rename `GET /queryProductForUser` (+ method name + view name) because it's accessible to everyone

### Question_Info
- duplicate media storage in fs and db right now & files in fs are not used anywhere after save

### OrderInfo
- Drop ECPay API
- Ship Stripe, Shop, PayPay, Line Pay...


## [2.1.0]
- Introduce Thymeleaf to replace JSP


## [2.2.0]
- Introduce Spring Security
- Revamp security around user domain: login/logout, access level on certain page, 401 unauthorized, role...
  - e.g., login
    ```java
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          boolean loggedIn = auth != null && auth.isAuthenticated()
                                          && !(auth instanceof AnonymousAuthenticationToken);
    ```
- Encrypt user password using Spring security
- Replace the existing login session mechanism 



## Unplanned Future

### Backend
- i18n + enum flags, e.g.,
  - enum class
    ```java
      public enum UserIdCheckResult {
        AVAILABLE,
        EXISTS,
        INVALID_FORMAT,
        SERVER_ERROR
    }
    ```
  - messages_zh.properties
    ```properties
    UserIdCheckResult.EXISTS=帳號已存在
    UserIdCheckResult.AVAILABLE=
    UserIdCheckResult.ERROR=發生錯誤，請再試一次
    ```
  - and replace the following with the resource bundle?
    ```java
    switch (result) {
    case AVAILABLE -> "";
    case EXISTS -> "帳號已存在";
    case ERROR -> "發生錯誤，請再試一次";
    }
    ```
- Rename `EmailSenderService` to `MailService`
- Pressing enter should send form (login, update password...) 
- LOGGER output so that which admin approved/disapproved which course is traceable
- Auth API to allow guests to leave comments with nickname + quick/i-am-not-robot stuff 


### Frontend
- Move user functionalities from the sidebar to a top-right icon drag list 
- fix/remove my (probably) redundant or outdated custom js util?
- Name every js function to make them traceable
- ocean of js `var` 
- discrete js instead of inline js
- fix DRY logout (or any other common) functionality on multiple views
  - could be ideally on `header.jsp` only or like a discrete script, like, `static/js/common.js` on every page?