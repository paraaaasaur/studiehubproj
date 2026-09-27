# Domain Semantics

## Temporary Domain Model (Valid Through 1.1.0 → 2.0.0)
Chat domain has a usable DB schema, but backend implemented unconventionally, and frontend API calls depend on such endpoints. As a result, fix on contract-breaking behaviors is not available until the next MAJOR version (2.0.0).

Currently, in chat domain, user intents and technical schema truths are highly misaligned. Use the temporary domain vocabulary shown below to help navigate through code.
- Period: 1.1.0-refactoring ~ 2.0.0-domain-redesign
- Chat domain method naming strategy:
  - default (controller, service...): reflect user intent
  - dao: reflects the raw schema truth; can be verbose when needed

### User Facing Vocabulary
1. top post, top-post, topPost: The main article at the top of the thread
2. reply: A post written under a top post.
3. post: refers to either a top-post or a reply.
4. poster: refers to the content creator of a post.
5. thread: the full relation, the whole discussion that consists of
   - a top post
   - all replies under the top post

### Schema Vocabulary
- `Chat_Info` and `Chat_Reply`
  - For now, they refer to schema truth only, and they should not be used to derive user intent. (Were originally designed to mean top-post and reply directly)

---

## Intended Model (Ideal)
- `Chat_Info`
  - the table for top-posts
- `Chat_Reply`:
  - the table for all replies under a top-post
- Structurally identical, except that `Chat_Info` is the parent entity(1), and `Chat_Reply` is the child entity(M)

### JPA Setup
- Unidirectional
- Cascade: DELETE only (deleting a top-post also deletes associated replies)

### CRUD

#### Retrieve
When read,
- top-post is loaded from `Chat_Info` table
- replies are loaded from `Chat_Reply` table into the reply area below

#### Create
- Top-post is stored into `Chat_Info` table
- Replies are stored into `Chat_Reply`. A new reply must reference an existing `Chat_Info` record.

#### Update
- When updated, both are updated solely and have no cascading effect on each other

#### Delete
- Deleting a top-post record in `Chat_Info` will delete all associated reply records in `Chat_Reply` as well
- Deleting a reply record in `Chat_Reply` has no cascading effect on `Chat_Info`

---

## Implemented Model (Reality)
- ❓ `Chat_Info`: the hook a top-post record in `Chat_Reply` must reference
- ❓ `Chat_Reply`: refers to both the top-post redundancy and all replies under a top-post.
- ✅ Structurally identical, except that `Chat_Info` is the parent entity(1), and `Chat_Reply` is the child entity(M)

### ✅ JPA Setup
- Unidirectional; no cascading operation

#### ❓ Retrieve
When read,
- `Chat_Info` is loaded for its `title` only; nothing else is used.
- `Chat_Reply`s are loaded into both the main article and the reply area below.

#### Create
- ❓ main article is stored into both `Chat_Info` and `Chat_Reply`
  - `Chat_Reply` needs to reference the hook in `Chat_Info` due to table constraint in `Chat_Reply`
- ✅ replies are stored into `Chat_Reply` and need to reference `Chat_Info`

#### Update
- When updated,
  - ❓ content of the main article gets updated properly in `Chat_Reply`, but stays the same in `Chat_Info`
  - 👷 replies have no UI and endpoints meant to perform update for them

#### Delete
- When deleted,
  - ✅ deletion on `Chat_Info` will delete all associated `Chat_Reply`s as well
  - 👷 deletion on `Chat_Reply` alone is not implemented currently

## Summary of Defects (Legacy Behavior)
1. Top-posts are duplicated into `Chat_Reply` as redundancy outside `Chat_Info`, BUT updates on top-posts modify `Chat_Reply` but leave `Chat_Info` stale(???)
2. Queries on top-posts only come from their redundancies in `Chat_Reply`, not `Chat_Info`.
3. `Chat_Reply` acts as the table for both the top-post redundancy and replies
4. `Chat_Info` has no semantic authority
  - Technical authority: it holds PKs that `Chat_Reply` records have to reference, no matter it's a top-post redundancy or a reply.

---

## Overall Problems
1. Multiple misleading endpoint namings
2. Frontend coupling with backend's weird implementation on DB schema
3. Users cannot perform DELETE for either top-post, thread or reply
4. Low request validation coverage rate
5. Validation not covering ID, the single most important field