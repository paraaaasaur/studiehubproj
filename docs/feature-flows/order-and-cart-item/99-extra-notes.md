## Feature
- 3rd-party payment flow:
  - customer adds cart items and checks out
  - server sends request to payment API, while still keeping cart items
  - After the server receives a successful API result from webhook endpoint, it saves into order items using that API result + db cart items
  - Only after the order persisting is finished, the cart items get deleted.


## Thoughts
- Keep webhook endpoint simple and fast
  - Only save the API payment result into db and end the method
  - Save order (and drop cart) somewhere else



## Feature Quirks
- Volatile after-payment result for customers
  - Right now, payment results are presented to customers in a transient return page
    - gone forever on page refresh
  - Replace it with some other persistent methods, e.g., 
    - email
    - a view showing all purchased items
- Manual admin CRUD operations on orders other than legitimate business rules (payment)
  - order insertion/deletion and arbitrary update
  - scheduled for removal

## Domain Vocabulary
- `order_info`: the order detail table that flattens an order into each item
  - `identity_seed`: the real id column in `order_info`
  - `o_id`:
    - the common id of all items in the same order; order id
    - not unique
    - not the identity seed in `order_info` table
- order: 
  - the collections of all `order_info` of the same `o_id` atm
  - no one dedicated table to represent order atm

  
## Technical Trivia
1. in-memory static field carts holder in `CartViewController`
2. unnecessary slash(`/`) prefixed in multiple returned views
3. green horizontal bar looks really annoying :)
4. datetime is currently db-generated -> switch to server-owned (see 2.0.0 schema plan)


## Missing Features


## Redesign
- order status appending


## Unanswered Questions
1. What is everything `public static cartInfoMap` in `CartViewController` do?
   - You need to name all its tasks before getting rid of it
2. How to request the payment result multiple times before expiration?