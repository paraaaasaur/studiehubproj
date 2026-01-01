# [1.4.0] - Logic Refactoring & Redesign

## Goal
Renovate non-structural controller/service (mainly) logic

## Items

### UserController
1. `@ModelAttribute("genderList") getGenderList`
    - Make a new Gender enum and feed it inline?
2. `updateLoginBean`:
    - rename the method to `fetchLoginBean`
    - remove param `SessionStatus ss`
    - change param `Model model` to `@SessionAttribute("loginBean") User_Info loginBean`
3. Clear comments

### UserService
1. `checkUserId`
- Rename to `existsById` to return boolean
- Boil down logic to simple existence check
- Usage:
    - Detail message: move to the resource bundle
    - Controller: autowire messageSource
    - Handler method: i18n
    - Tests

### CartController
1. `/cart.controller/clientRemoveProductFromCartByCartId`
   - issue: non-atomic deletion on multiple cartItems
   - solution: create a service to perform atomic deletion


### Frontend
- Replace jQuery with raw JS
