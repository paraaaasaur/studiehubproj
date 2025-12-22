## Domain Issues
- Schema defects: `a_uid` in `EventInfo` is not FK and the `u_id`s in the sample data are fake
- Missing proper JPA associate with `user` 
- Entity `Entryform` is currently simply the M-N relationship between `event` and `user` and should be implemented in standard way
  - unless more functions are to be added in the future
- Column `havesignedup`
  - manual, real-time, error-prone human work
- Magic getter `getComment` and setter `setComment`
- Error-prone, redundant field `havesignedup`
  - use the direct query on `entryform` total size, either JPA or SQL query

## Improvements
- Make `Event#type` field an enum class  