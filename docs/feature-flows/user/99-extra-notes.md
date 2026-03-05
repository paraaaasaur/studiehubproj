## Domain Issues

### Technology
1. Media storing strategy
   - and column `mimeType` is useless outside the current strategy to build base64
2. Magic getter `getPictureString`
3. Unbound information exposure: full instance of user entity (`loginBean`)
   - minimize required information case by case
   - `AuthContext` for general authentication needs

### Semantics


### Architecture