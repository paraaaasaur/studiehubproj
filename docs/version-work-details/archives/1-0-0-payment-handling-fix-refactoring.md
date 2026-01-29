# Compatibility & Integration Fix Note (Runtime & Environment) for ECPay Services
1. Fix multiple compatibility bugs on 3rd-party payment services
   - ngrok
   - API resource path error
   - hardcoded urls
2. Single out payment API (ECPay) controller method to a dedicated class
3. Local enhancement with custom DTO to accept results using payment API


## Issue: Hardcoded external callback URL
- External service (ECPay) callback URL was hardcoded to an inline, specific ngrok address as demo shortcut
- Causes immediate breakage when:
  - ngrok tunnel changes
  - running on another machine
  - deploying to non-local environments

### Decision
- Move external callback base URL to runtime environment configuration
- Read value from environment variable `ngrokHttp`

### Result
- Callback URL becomes environment-agnostic
- No code change required when URL changes


## Issue: Resource path inconsistency causing NPE
- Payment API attempted to load a config file from classpath
- File existed in source code package & cannot be correctly located, but happened to work previously
- Resulted in NullPointerException at runtime

### Root Cause
- Resource file was not located under standard `resources/` directory
- Code relied on implicit filesystem layout

### Decision
- Move resource file into proper classpath location
- Load resource via standard classpath
- Avoid filesystem-relative paths

## Result
- Consistent behavior across machines
- Eliminates machine-dependent runtime failure


## Issue: Webhook endpoint mixed with user-facing controllers
- Payment result endpoint is not consumed by frontend
- Client is an external payment provider (server-to-server)
- Previous placement, along with `void` type, caused confusion about its purpose

### Decision
- Move webhook endpoint into a dedicated controller for ECPay 
- Explicitly separate:
  - user-facing endpoints
  - third-party callback endpoints
- Rename to `/payment/ecpay/callback` to disambiguate from usual app endpoints

### Result
- Clear ownership and intent
- Prevents accidental misuse or frontend coupling


## Trivia
- Introduced DTO for ECPay callback endpoint