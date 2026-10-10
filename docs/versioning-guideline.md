# About Project Version
- This project adopts semVer rules as the backbone to track and plan changes in clearer structure for the backend, and also for the purpose of learning.
- This is a fullstack project, not a public API consumed by random people. Strict semVer rules don't fully apply to this project.
- Situations that don't happen for typical public APIs do appear here, AKA private contracts
  - e.g., An endpoint suddenly becomes dead code, because I (backend) secretly know I (frontend, the only client) just abandon a feature that depends on it
  - For such situations, it's crucial we need to define our own house rules to enforce a consistent and helpful versioning strategy.
- Pre-2-0-0: No specified API contract defined. Only frontend & backend implementations exist.
  - Therefore, "contract breaking" doesn't happen UNLESS backend endpoint changes don't cause a frontend API call or usage to become invalid or has to change to adapt.

# House Rules
Basically, changes and plans follow typical semVer, with unique cases and custom overrides listed below. 

## PATCH
1. Removal of a dead endpoint and its dependencies when frontend removes all client features that depend on it
   - special case: unlike public API, here we (backend) can affirm clients (also we lul) are all out
   - no deprecation or warning duration is required

## MINOR
1. Model attribute changes: Should be MAJOR normally, because it breaks expectations and current usages in client code, not backward compatible. Nevertheless, for the convenience of project progress, and to keep the appropriate amount of constraints for learning, they count as MINOR changes here.
   - Might reconsider after the 2.0.0 overhaul.

## MAJOR