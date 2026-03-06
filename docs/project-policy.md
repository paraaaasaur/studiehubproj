# Project Policy
This document defines certain policies across concerns for this app.

---

## Security

### Frontend

#### 1. JSP Injection
- For JS -> 
  1. centralize in a `<script type="application/json id="bootstrap-data">` tag
  2. escape the attributes
- For HTML -> escape where they're injected directly

#### 2. Server data are hostile by default (whether attributes or endpoint data)
- Sanitize or escape with that in mind


### Backend
1. HTML string persistence (like `c_Conts` in chat domain):
   - sanitize with `OWASP` dependency ruthlessly

---

## Session Handling Policy
For explicitness and semantic clarity, 
- use `HttpSession`-based mechanism to access session attributes:
  - store with `HttpSession.setAttribute()`
  - access with `@SessionAttribute` param, or optionally `HttpSession.getAttribute()`
  - end a session with `HttpSession.invalidate()`
- avoid `Model` `@SessionAttributes` `SessionStatus` based mechanism
  - `Model` is from now on reserved for request-level attributes only.

---

## Frontend Structure
- Use `<base href="<c:out value="${pageContext.request.contextPath}/" />">` tag universally

---

## View Naming Policy

### 1. Trivial CRUD Operation Glossary
- Insert operation: use `add`
- Select operation: use `detail`/`list` for singular/plural
- Update operation: use `edit`
- Delete operation: use `delete`

### 2. Word Glue: Use Kebab Case
- e.g., `changePassword` => `change-password`
- e.g., `cartItems/myList` => `cart-items/my-list`

### 3. Path Hierarchy Convention
1. Resources: use `[group]/<resource-plural>/[role/]<action>|detail|list`
   - `users/admin/list`
   - `products/add`
   - `chat/threads/admin/delete`

2. Non-resources
   - layout/fragments: use `<layouts|fragments>/[role/]<piece>`
     - `fragments/header`
     - `fragments/admin/sidebar`
   - general pages: use `common/[role/]<action>`
   - features: use `<feature>/[role/]/<action>`
     - e.g., authentication => `auth/[role/]/<action>`