# About Project Version
- This project adopts semVer rules as the backbone to track and plan changes in clearer structure for the backend, and also for the purpose of learning.
- This is a fullstack project, not a public API consumed by random people. Strict semVer rules don't fully apply to this project.
- Situations that don't happen for typical public APIs do appear here, AKA private contracts
  - e.g., An endpoint suddenly becomes dead code, because I (backend) secretly know I (frontend, the only client) just abandon a feature that depends on it
  - For such situations, it's crucial we need to define our own house rules to enforce a consistent and helpful versioning strategy.


# House Rules
Basically, changes and plans follow typical semVer, with unique cases and custom overrides listed below. 

## PATCH
1. Removal of a dead endpoint and its dependencies resulted when frontend removes all client features that depend on it
   - special case: unlike public API, here we (backend) can affirm clients (also we lul) are all out
   - no deprecation or warning duration is required

## MINOR

## MAJOR