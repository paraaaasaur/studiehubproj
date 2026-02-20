# Project Policy
This document defines certain policies across concerns for this app.

---

## Security

### Frontend
1. Server attributes injected in JSPs:
   - for JS -> 
     1. centralize in a `<script type="application/json id="bootstrap-data">` tag
     2. escape the attributes
   - for HTML -> escape where they're injected directly
2. Server data are hostile by default (whether attributes or endpoint data)
   - sanitize or escape with that in mind

### Backend
1. HTML string persistence (like `c_Conts` in chat domain):
   - sanitize with `OWASP` dependency ruthlessly


## Frontend Structure
- Use `<base href="<c:out value="${pageContext.request.contextPath}/" />">` tag universally



