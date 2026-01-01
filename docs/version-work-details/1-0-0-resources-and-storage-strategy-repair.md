# Resources & Storage handling Strategy Repair Note
- Type: Release-blocking infrastructure fix
- Affects: storage, media serving, deployment portability
- SemVer impact: must be completed before 1.0.0


## Issue
- Currently, this project uses a faulty strategy for upload storage and static resources handling:
  - User uploads CRUDed like static resources in some domains
  - Inconsistent and OS-dependent path string patterns across domains
  - Frontend hardcodes path rules it shouldn't know, instead of relying on backend-provided URLs
    - No unified URL mapping exists externally stored files
- As a result, the application cannot function correctly when packaged as a fat JAR
- Without addressing this, the project cannot be considered a valid 1.0.0 release


## Solution Plan
1. Decide a location as the root path for external storage
   - Suggested: `storage/` directory parallel to `src/`
   - Add a root path entry to `application.properties`
2. Create a storage config component
   - Access root entry
   - Centralize path resolution (in place of constants right now from `SystemUtils` class)
   - Expose helper methods to return storage paths for needs in needing domains
3. Define a clear separation between:
   - URL paths (used in frontend `src` attributes)
   - Logical storage path (OS-agnostic, backend-owned)
     - used for the external file system for now, and CDN in the future
   - Choose one as default to store in the database
4. Register URL <-> storage mappings in `WebConfig`
5. Locate & Fix any misaligned usages across:
   - Controller logic for media CRUD
   - media-related columns in DB sample data
   - frontend media references
6. Replace old constant dependencies & inline paths using the storage config class
7. Time to move the media part of sample data to new home (`storage/**`)
8. Zip the `storage/**` for backup and quick setup and upload to my Notion
9. Add an entry for user upload storage in `.gitignore`


## Specifics

### Common
- Added unified constant fields in controllers
- Added config components to resolve external storage & url paths
- Added `ResourceLocationResolver` to locate certain static resources

### Trivia
- Fixed missing dot(`.`) for filenames in storage logic across multiple controllers

### user
- `POST /updateUserinfo.controller`
  - avatar storage path
- View usages: nothing to fix; rendered by magic getters that directly convert db blob to base64 via HTTP

### product
- Edited sample data URLs
- `POST /updateProduct/{p_ID}`, `POST insertProduct`
  - image & video storage path
  - image & video url string as db string
- View usages

### event
- Edited sample data URLs
- Renamed sample upload image files for better recognition
- `POST /insertEvent`, `POST /updateEvent/{a_aid}`
  - fix storage logic
    - before: storage directory assigned to `resources/` of runtime artifact
    - after: switched to external storage outside the project tree
  - image storage path
- Added missing static resource
  - no-image: `src/main/resources/static/images/enevt/MemberImagexx.png`
- View usage: correct, nothing to fix... WOW

### question
- Moved static resource `NoQuestionImage.png` to domain-specific directory
- View usages: nothing to fix; rendered by magic getters that directly convert db blob to base64 via HTTP


## Unresolved Issues

### Common
1. `sidebar` pickups
   - ❌ relying on thumbnails from static resources
   - ❌ relying on hardcoded descriptions
   - both of the above should come from real data retrieved from an endpoint
   - Fix schedule: 1.1.0 - structural refactoring, to include `common.js`
2. Magic getters & setters
   - blurring responsibility boundary in multiple domains
   - event: `getComment`, `setComment`
     - unrelated to file storage
   - product: `getP_DESC`
     - unrelated to file storage
   - question: `getQ_pictureString` and `getQ_audioString`
     - client: all current usages in views
   - user: `getPictureString`
     - client: user avatar rendering JS scattered across multiple views
   - gets alleviated by the introduction of Lombok later (1.2.0)
   - gets resolved by refactoring controller/service logic (skip)
   - gets resolved cleanly by the introduction of DTO (2.0.0)

### user
1. Ambiguous media storage strategy
   - storing upload media to both db and files w/o usage of files
   - Fix schedule: 2.0.0

### event
1. Semantic lie for "no-image" scenario
   - A URL for no-image is saved to column of event table, which stores URLs for served user uploads
   - Meaning the database says: "this event uploaded a no-image"
   - no-image belongs to static resource, not user upload
   - It's technically convenient for now, but blurring responsibility boundaries and polluting domain semantics
   - possible solution: store `null`, and either
     - make backend replace it with no-image URL to DTO, while frontend stays dumb (server-side rendering culture), or
     - let backend serve it honestly via endpoint to frontend, and let frontend implement fallback representation logic (RESTful)
   - Fix schedule: 2.0.0

### question
1. Ambiguous media storage strategy
   - storing upload media to both db and files w/o usage of files
   - Fix schedule: 2.0.0