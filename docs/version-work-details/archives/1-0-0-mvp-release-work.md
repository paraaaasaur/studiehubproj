# [1.0.0] - Minimum Viable Product Release 

## Goal
- Make the project reproducible, platform-independent, and testable at minimum


## Main Quests
- Created integration tests for every domain
  - happy paths 
  - unhappy paths 
  - dge cases 
  - ± whatever I like to keep/discard
- Created summary docs for features
- Restored the MVP application
    1. Resolved dependency tree
       - Added `/lib/log4j.jar` local jar
         - deprecated by apache
         - not available on maven
    2. Restored & integrated database setup
       - Database config
         > Details: `dev/db/sqlserver/**`
         1. config: environment setup for MSSQL2019@Docker
         2. schema: based on the jpa auto-ddl, adjusted some data types
         3. sample data:
            - found a legacy DML copy
            - omitted some unnecessary N-prefixed data for `VARCHAR` types
            - edited expired data of `EventInfo` to match the date 
         4. security script template: MSSQL login/user creation and authorities assignment 
       - Added database startup config components `DevDataSourceInitializer` & `TestDataSourceInitializer`
    3. Resolved faulty strategy for resource/storage handling
       > Details: `docs/version-work-details/1-0-0-resources-and-storage-strategy-repair.md`
       - Assigned dedicated storage dirs 
         - `storage-dev/`, `storage-test/` and `storage-prod/`
         - all dirs are parallel to `src/`
       - fixed originally Windows-dependent paths to be platform-agnostic
         - e.g., `"c:\\images\\place"`
       - merged `SystemUtilsNickUse` into `SystemUtils`
         - remove extension-extracting/-striping methods (replaced by Spring's `StringUtils`)
         - clean up path constants
    4. Fixed dev-time sample data (media)
       - easily reproducible
       - trim media size
       - resolve potential copyright issues
    5. Installed ngrok for exposing endpoints from localhost to public ip, which is required for ECPay payment API
- Create tracker for current tech stack and dependencies, and label replaced/new/removed/upgraded ones

## Trivia
- Add `spring.datasource.name=studiehub_datasource` because why not, like what the fish is HikariPool-1
- Add cute banners because why not