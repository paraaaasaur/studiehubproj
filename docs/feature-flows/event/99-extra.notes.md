## Domain Issues
- Schema defects: `a_uid` in `EventInfo` is not FK and the `u_id`s in the sample data are fake
- <fixme@2.0.0>: Missing proper JPA associate with `user` 
- Entity `Entryform` is currently simply the M-N relationship between `event` and `user` and should be implemented in standard way
  - unless more functions are to be added in the future
- <fixme@2.0.0>: remove computed column `havesignedup`
- <fixme@2.0.0>: magic getter `getComment` and setter `setComment`
- What the fish is `modifyRestaurant`?
- <fixme@2.0.0>: redesign: type (event category) should be plural (collection)

## Improvements
- Make `Event#type` field an enum class

## Drafted Ideas

### Domain Model
1. `article`
   - fields: id, title, content (`String`)
   - association: 1-M with `comment` (see below)
   - operations: basic CRUD (save, update, find-by-id, delete)
   - also
     - yuz seems to be wanting to add JPA convenience methods
2. `comment`
   - fields: id, content (`String`)
   - association: M-1 with `article`
   - operations: basic CRUD
   - also:
     - yuz seems to be wanting to add JPA convenience methods
3. `sendmessage`
   - fields: id, title, content
   - associations:
     - M-1 with `event`
     - 1-1 with `user`
   - operations: n/a found
   - my opinion: cannot fully get what yuz was trying to implement...
     1. A mailbox between event uploader and participants about an event?
     2. A nested reply section as Q&A under an event page?
   - remark
     - safe to delete
     - fine to add later if intended