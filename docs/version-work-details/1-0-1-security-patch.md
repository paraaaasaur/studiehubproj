# [1.0.1] - Security Patch

## Vulnerability List
1. Authentication
   - inline session checks that leak out privileged server-side operations
   - incomplete auth impls that only exist in few endpoints
   - decorative frontend session checks for falsy confidence
2. XSS risk brought up by the API `CKEditor` used in `chat` domains for lack of HTML sanitization
3. Resources leaking endpoints across domains
4. Related bugs (if any is found)


## 1. Implement A Centralized Security Layer
A new feature to replace scattered, inline session checks.

### Mechanism
1. Categorize auth requirements in `HandlerInterceptor` based on custom annotations, 
   e.g., `@RequiresAdmin` marked on the handler methods
2. Further decide whether it's a REST or page-rendering endpoint
   - If REST -> respond with 401/403 so that clients(ajax) know they need to redirect
   - If page-rendering -> server implements redirect logic for clients(browser)


### Technical Details
1. Add custom auth annotations
2. Add a `HandlerInterceptor` using auth annotations to deny bad accesses
   - rejects a user while user is logged in
   - requires a user login while it's a guest
   - rejects an admin while admin is logged in
   - requires an admin login while without that
   - separate by REST/page-rendering
3. Add a `WebMvcConfigurer` to register the `HandlerInterceptor`
4. Put annotation to handler methods that need it
   - follow the Notion table `endpoint` to check out which are in need


### Related Changes
- Remove duplicate inline auth logic, release params in handlers
- Remove `#checkIfAdminLoggedIn` and `#checkIfLogin`
  - login check gets replaced with the new interceptor + auth annotations style
- Remove frontend auth for no need & consistency, notably:
  - question view files
- Update client(ajax) usages
  - add redirect logic for REST endpoint usages when 401/403
- Add missing tests for the cases of failed auth


### Specific Items
- `ss#setComplete` -> `httpSession#invalidate` for logout & adminLogout


## 2. Add HTML Sanitization
Use the dependency `OWASP Java HTML sanitizer` to perform server-side HTML sanitization
- [tutorial](https://www.baeldung.com/java-sanitize-html-prevent-xss-attacks)
- target: update and insert logic where `CKEditor` is used


## 3. Unauthorized Resources Leaking Endpoints across Domains

### product
1. `GET /queryByProductName`: query missing pending status check


### event
1. `GET /EventfindAll`: authorization enforced only at the presentation layer
   - shared by both user page(`event/eventindex`) and admin page(`event/adminAllEvent`)
   - retrieves both `verification = 'Y'` or `N` and sends to both views
   - user page shouldn't see unreviewed data
   - solution:
     1. Create a new endpoint for user page
        - only retrieves data which `verification = 'Y'`
     2. Replace endpoint usages in user page
     3. Admin endpoint remains unchanged for minimum code impact
2. `GET /eventcontentjson/{a_aid}`
   - issue: `findById` bypasses verification domain logic
   - solution: create a new service/dao method to switch to which filters `verification` field
3. `GET /queryEventByName`: almighty retrieval endpoint misused by users outside admin uses
   - reserve it for admin usages in `event/managerAllEvent`, `event/adminAllEvent`
   - remove feature in `event/userAllEvent`
     - like, do you really have tons of events as one user that you really need a partial query?
   - no need to add an endpoint; only misuse removal


## Notes
- Unresolved issue: resource ownership that needs clear domain redesign after 2.0.0


## Footnotes
1. In Spring Security, the standard approach to draw the boundary between 
   REST/page-rendering is url patterns, like `/api/**`.  
   However, to respect versioning discipline, we decide endpoint url editing as a 
   constraint outside MAJOR change, and choose to use annotation property to draw 
   the boundary.