# Domain Semantics
- `chat` domain is implemented in a way that can feel counterintuitive.
- Read the following overview to clarify confusion if you have any.

## Intended
- `Chat_Info`
  - the table of a main article and its metadata of a thread
  - placed on the top to be discussed with `Chat_Reply`s (replies) below
- `Chat_Reply`: 
  - the table of all the replies and their metadata of a thread
  - placed under `Chat_Info` (the main article)
- Structurally identical, except that `Chat_Info` is the parent entity(1), and `Chat_Reply` is the child entity(M)

### JPA setting
- Unidirectional; no cascading operation

### CRUD

#### Retrieve
When read, 
  - main article is loaded into the frame from `Chat_Info` table
  - replies are loaded from `Chat_Reply` table into the reply area below

#### Create
- main article is stored into `Chat_Info` table
- replies are stored into `Chat_Reply` and need to reference `Chat_Info` table

#### Update
- When updated, both are updated solely and have no cascading effect on each other

#### Delete
- When deleted,
  - Deletion on `Chat_Info` will delete all associated `Chat_Reply`s as well
  - Deletion on `Chat_Reply` has no cascading effect on `Chat_Info`

## Implementation
- ❓ `Chat_Info`: the hook that the main article in `Chat_Reply` must reference
- ❓ `Chat_Reply`: refers to both the main article and all replies under the main the article.
- ✅ Structurally identical, except that `Chat_Info` is the parent entity(1), and `Chat_Reply` is the child entity(M)

### ✅ JPA setting
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
   - ✅ Deletion on `Chat_Info` will delete all associated `Chat_Reply`s as well
   - 👷 Deletion on `Chat_Reply` alone is not implemented currently

## Summary of Defects (Legacy Behavior)
1. The main article is duplicated into `Chat_Reply`.
2. The UI reads the main article only from `Chat_Reply`, not `Chat_Info`.
3. Updates modify `Chat_Reply` but leave `Chat_Info` stale.
4. The two-model structure does not reflect actual usage
   - `Chat_Reply` acts as the table for both the main article and replies

## Temporary Vocabulary (Valid Through 1.1.0 → 2.0.0)
- Given the current situation where *user intent* can contradict technical *schema truth* and vice versa, follow the internal vocabulary shown below to help you navigate through the code and distinguish potential ambiguities
  - Applies as of method renaming scheduled in 1.1.0
  - This vocabulary will be, hopefully, no longer needed and removed after the schema redesign in 2.0.0
- For this domain especially, as of the upcoming renaming in 1.1.0, by default, the method naming strategy will be:
  - controller and service: reflect *user intent*
  - dao: reflects the raw *schema truth*; can be verbose when needed
- For this domain specifically, *user intent* doesn't guarantee underlying *implementation and schema*

1. `Chat_Info` and `Chat_Reply`
   - Zero user intent. Only refer to schema truth.
2. top post, top-post, topPost: The main article at the top of the thread
3. reply: A post written under a top post.
4. post: refers to either a top-post or a reply.
5. poster: refers to the content creator of a post. 
6. thread: the full relation, the whole discussion that consists of 
   - a top post
   - all replies under the top post


# Overall Problems
1. Misleading, confusing or implicit endpoint naming
    - can't instantly recognize if it's dealing with `Chat_Info` or `Chat_Reply`
2. structural design anti-pattern
    - to be redesigned in 2.0.0 as single `Post` entity with replies
3. Storing datetime along with format