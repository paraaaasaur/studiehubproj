# [1.0.1] — Security Patch

## Vulnerability List
1. Authentication
   - inline session checks that leak out privileged server-side operations
   - incomplete auth impls that only exist in few endpoints
   - decorative frontend session checks for falsy confidence
2. XSS risk brought up by the API `CKEditor` used in `chat` domains for lack of HTML sanitization
3. Resources leaking endpoints across domains

---

## 1. Implement A Centralized Security Layer
A new feature to replace scattered, inline auth checks via session.

### Overall Mechanism
1. Categorize auth requirements in `HandlerInterceptor` based on custom annotations
   - e.g., `@RequiresAdmin` marked on the handler methods
2. Further decide whether it's a REST or page-rendering endpoint
   - If REST -> respond with 401/403 so that clients(ajax) know they need to redirect
   - If page-rendering -> server implements redirect logic for clients(browser)


### Technical Details
1. Add custom auth annotations
2. Add a `HandlerInterceptor` using auth annotations to deny illegal accesses:
   - rejects a user while user is logged in
   - requires a user login while it's a guest
   - rejects an admin while admin is logged in
   - requires an admin login while without that
   - separate by REST/page-rendering
3. Add a `WebMvcConfigurer` to register the `HandlerInterceptor`
4. Put annotation to handler methods that need it
   - follow the Notion table `endpoint` to check out which are in need


### Related Targets
- Remove duplicate inline auth logic, release params in handlers
- Remove `#checkIfAdminLoggedIn` and `#checkIfLogin`
  - login check gets replaced with the new interceptor + auth annotations style
- Add auth tests
- Update client(ajax) usages
  - add redirect logic for REST endpoint usages when 401/403

### Leftover
- Resource ownership: requires clear domain redesign after 2.0.0

---

## 2. XSS Vulnerability Fix
- Apply several security policies (See security section in `docs/version-work-details/project-policy.md`)

### Target
1. [chat domain] Update/Insert logic 
   - reason: persists HTML string due to the use of CKEditor
   - use the dependency `OWASP Java HTML sanitizer` to perform server-side HTML sanitization
2. Frontend wherever cross-language boundary exists:
   - server attributes in jsp (Java -> HTML | Java -> JS | Java -> JS -> HTML)
     - added `<base>` tags
       - to centralize context path usages (server attribute)
       - to remove inline dependencies of JSTLs on `src`/`href`
     - added `<script type="application/json>`s to centralize server attributes for JS
     - escaped direct insertions of model attribute within HTML body across views
   - dynamic HTML insertion (JS -> HTML)
     - replaced HTML string concatenation where XSS can happen with DOM API
     - control output renderings
       - use `textContent` by default: automatically escaped for HTML
       - for `innerHTML` (dangerous): use `DOMPurify` API to sanitize outputs first

### Leftover
1. Certain edgy data boundaries now lose fidelity across language boundaries (because of thorough enforcement escaping policy on server attributes)
   - text data that potentially contains html-escaped characters (`'`, `"`, `<`, `>`, `&`), e.g.,
     - `loginBean.u_id`
     - `successMessageOfChangingPassword`
     - `success`
   - possible solutions:
     - short-term: apply domain validation rules to exclude illegal characters in the first place
     - long-term: ✅ HTTP payload to JS (2.0.0)
     > Data should come as data from the front door (AKA endpoints)

---

## 3. Unauthorized Resources Leaking Endpoints across Domains

### product
1. `GET /queryByProductName`: query missing pending status check
   1. usages:
      - `pendingAccess` 
        - switch to the new endpoint `GET /admin/products`
        - use query string `approved` to request unreviewed products only
        - no use of `ratedIndex` key
      - `product` -> remove usage (design choice)
      - `showProduct`
        - switch to the new endpoint `GET /admin/products` 
        - omit query string `approved` to request both results
        - no use of `ratedIndex` key
      - `showProductToUser` 
        - no switch on endpoint and usage
        - change use of service method to the new `search` service/dao methods
   2. related changes
      - deprecated `queryByName` dao/service methods
      - added core `search` dao/service methods to replace `queryByName` dao/service methods
        - cleaner semantics
        - dynamic filtering using Criteria API
        - doesn't own response shape; just pure resources
      - added endpoint `GET /admin/products` to provide an admin ver for search feature
      - added tests for `GET /admin/products`
      - refactored endpoint `GET /queryByProductName` using the new `search` service/dao methods
      - enabled test for `GET /queryByProductName`


### event
1. Fix wrong usages on `GET /EventfindAll`
   - issue: act as supreme JPA `findAll` used in improper cases
   - usage
     - guest
       - `event/eventindex`: wrong auth
         - solution: switch to the new `GET /guest/EventfindAll`
     - admin
       - `event/adminAllEvent`: np ✅
       - `event/managerAllEvent`: wrong scope; doesn't need approved events
         - solution: switch to the new `GET /admin/events` for its initial loads
2. Fix logic for `GET /eventcontentjson/{a_aid}`
   - issue: using service method `findById` that bypasses verification
   - usage: `event/eventcontent`
   - solution: switch to the new `guestFindByid` service instead
3. `GET /queryEventByName`
   - issue: supreme endpoint misused by users outside admin uses
   - usage
     - user
       - `event/userAllEvent`: wrong auth
         - solution: switch to the new `GET /me/events`
     - admin
       - `event/adminAllEvent`: np ✅
       - `event/managerAllEvent`: wrong scope; doesn't need approved events
         - solution: switch to the new `GET /admin/events` for its search feature
4. Add new endpoint `GET /admin/events`
   - uses the nwe service `adminSearch`
5. Add new endpoint `GET /me/events`
   - uses the new service `userSearch`
6. Add new endpoint `GET /guest/EventfindAll`
   - uses the new service `guestEventfindAll`
7. Add new dao and service methods
   - dao: `search`
   - service
     - `userSearch`
       - has access to name-partial-match
       - holds constraint `loginBean` (for internal uid-full-match)
     - `adminSearch`
       - has access to name-partial-match, approved, include-entryforms
     - `guestFindByid`
       - basic logic: same as the original `findByid`
       - but throws exception (to suggest 403) if the event in unreviewed
     - `guestEventfindAll`
       - basic logic: same as the original `EventfindAll` (same query as JPA `findAll`)
       - but filters only approved `verification = "Y"` events

- Related changes
  - view logic bugfix
  - tests added/fixed/enabled


---

## Footnotes
1. In Spring Security, the standard approach to draw the boundary between 
   REST/page-rendering is url patterns, like `/api/**`.  
   However, to respect versioning discipline, we decide endpoint url editing as a 
   constraint outside MAJOR change, and choose to use annotation property to draw 
   the boundary.