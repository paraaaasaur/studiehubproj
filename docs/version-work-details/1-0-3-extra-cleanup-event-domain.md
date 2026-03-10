# 1.0.3 — Extra Cleanup & Bugfix

## Goals
- Same as previous version 1.0.2.

## Targets
- Frontend of event domain mainly
- Improper item `會員資訊` user sidebar include and related usages
- Any uncaught local bug

---

## 1. Cleanup

#### Event Domain
- Views
  - `event/insertsendmessage`
  - `event/NewFile`

---

## 2. Bugfix

### Common
- `universal/sidebar`
  - added missing utility to toggle user info actions for logged-in users
    - fixed related usages in all pages that allow/require logged-in user state

### Product Domain
- Code
  - `product/editProduct`
    - fixed incorrect role utility (usual user => admin)
    - fixed includes of incorrect role (usual user => admin)
  - `product/showProduct`
    - removed uncaught login utility removal (1.0.2) 

### Chat Domain
- Code
  - `chat/updateChatReply`
    - added missing utility for logged-in user
    - fixed includes of incorrect role (admin => usual user)

### Event Domain
- Code
  - `event/adminAllEvent`
    - fixed a uncaught unescaped jsp injection (1.0.1)
    - fixed broken UI
  - `event/managerAllEvent`
    - fixed broken UI
  - `event/userAllEvent`
    - resolved search button id conflict; changed from `query` to `event-query` (+ `queryall` to `event-queryA-all` for consistency)
    - fixed broken UI

---

## 3. Added

### Event Domain
- Code
  - `event/managerAllEvent`
    - finished search bar feature using existing query js