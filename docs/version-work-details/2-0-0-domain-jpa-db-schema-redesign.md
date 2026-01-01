## Domain
- What user sees and believes to be existing.
- (Can have but) no 1:1 relation to technical implementation.

### Before
- admin, instructor, student: `User_Info`
- course: `Product`
- cart: `Cart_Item`
- order: `Order_Info`
- question, quiz: `QuestionInfo`
- thread (top-post + reply): `Chat_Info` and `Chat_Reply`, but boundary is broken atm
- event: `EventInfo` + `Entryform` (for now, it's merely an M-N relationship between user)

### After
- admin, instructor, student: `User`
- course: `Course`
- cart: `Cart`, `CartItem`
- order: `Order`, `OrderInfo`, `OrderStatusHistory` + 3rd-party API table(s)
- payment api raw result
- quiz: `Question`
- thread (top-post + reply): `Post`, `Reaction`
- event: `Event`
- event_registration: `EventRegistration` that is 1-M to `event` and `user`
  - jpa embedded id / db composite id
  - registration_at(date), special_gift(boolean), queuePosition(int), status...
- article: `Post`
- article_reply: `Post`


## Ideas
- Take ownership into consideration when making columns
  - AKA who can CRUD this resource, and why?
- JPA -> schema truth; db table -> compatible & fallback net

## Schema
- Media persistence: db -> external storage (cloud, cdn, or even local storage...)
  - remove mimeType and byte columns
  - keep url string column
- Normalize every domain
  - For specific needs, create table views. Don't bake into entity tables.
- Store universal concept in English as default
- Naming strategy & watchout:
  - no prefix by default
  - only add prefix for foreign columns to disambiguate
- Make JPA one and only authoritative of datetime. Not the db, not clients.
  - db-agnostic (e.g., `CURRENT_TIMESTAMP` means differently across db vendors)
  - remove redundant layer to consider datetime discrepancy between server and db

## Database
- Migrate to MySQL@Docker
- More fitting data types
- Make pending column more clear, e.g.,
  - column name: pending_state
  - column name: status; values: PENDING, APPROVED
- Rename columns/tables, notably:

### user
- user email should be unique
- Role table & M-M role_user table
  - user role: guest, student, instructor, admin

### product
- `float` or `double` for price
- rename: `class` -> `course`

### question
- question table should reference to user/admin
  - otherwise, why would Nick ever bother requiring login in to create new question?

### chat
- chat: migrate to only `post` table that self-references itself 
  - column idea:
    - `view_count`, `emote_count`
    - fk: nullable `post_id`
- rename: `conts` -> `content`

### event
- rename: `uidname` -> `nickname`


### order & cart
- Remove 3rd-party payment api columns from app order table
  - make dedicated, isolated api tables to reference it
- order-related domains: order, order_detail, order_status_history, cart, cart_item all included
  - strict semantic difference despite many common structural similarities
    - discrepancy: discount, product price change, ...
    - order: immutable, frozen data (no deletion or update) by frontend admin
    - order_status_history: "append" status change if necessary
      - PENDING, PAID, CANCELLED_BY_USER, CANCELLED_BY_ADMIN, REFUNDED, FAILED_PAYMENT, ARCHIVED...

## JPA
- Redesign some data types:
  - Favor `java.time` API
    - `Instant`, `ZonedDateTime`, `LocalDateTime`, `LocalDate`, `LocalTime`
  - Replace `Clob`... we're not storing 2GB of text, or length = INTEGER.MAX of String
- Use strict, explicit `@ColumnDefinition`s whenever it differs from the db-owned schema
- Simplify entity class names
- Rename fields
- Remove denormalized fields
- Meaningful `toString`, `equals`, `hashCode`
- Add convenience methods if any makes sense 
- Add convenience constructors
- As lazy as possible
  - (probably a bunch of related problems)
  - `FETCH JOIN` (temporary EAGER for that query only, but is precise and solve N+1)
- Move all (de)serialization works to DTO layer
- API contracts use renamed field names

### user
- gender: unified list of enums in English