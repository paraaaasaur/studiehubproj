## Idea
- `block` table
  - to store any type of content in desired order
  - media, html-paragraph...
  - column: id, page
  - usage: 1-M references `page` table
    - `page` M-N references `course`, `event`, ... any domain that needs to present creator content, which doesn't depend on domain data and logic

- `post` + `reply` combo
  - column `type` in `post`: can be discussion, event detail, instructor's announcement, article...
  - or reddit nested `post` for everything
    - `post`: id, op id, post type, dates, content, reaction counts
    - `post_reaction`: id, user id, post id, emoji unicode
    - and then when a user clicks into a post
      - load `post` for general content
      - load `post_reaction` for user to react or cancel a reaction
        - when user reacts, insert -> `post_reaction` & update reaction counts in `post` 

- Make it visible if a post/reply has been edited at least once

## How to implement reaction?
- a reaction by a user to a post


## User
- `reset-password` feature
  - stop revealing email existence to respect privacy
  - treat existence and non-existence both as successful requests (status 200)
  - the JSON message should say, "An email is sent to the address if it exists" or similar

## Product
- Rating: 
  - from: optional rating
  - to: use Apple App Store style, which requires rating first to enable comment.