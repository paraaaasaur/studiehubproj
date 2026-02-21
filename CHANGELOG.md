# CHANGELOG

## [1.0.1] — Security Fix, 2026-01-26 ~ 2026-02-28
> The effective date would be 2026-02-21, as the author had been refactoring 
> a single view file `examMixQuestion.jsp` for an irrelevant topic since then😅

Bugfix on authentication, XSS vulnerability and endpoint resource leakage

### Added
- Centralized, thin, pre-Spring Security auth layer with annotations
- XSS Policies and practices
- New endpoints, service/DAO methods for bug features that need a replacement
- Tests

### Removed
- Scattered authentication logic

### Changed
- Web Policy: base url for all views to address the context path using `<base>` tag
- Centralized scattered server attributes in a JSON `<script>` for all views
- HTTP Status on denied access (401, 403) added to REST endpoints
- Tests

### Fixed
- Frontend endpoint usages in bug features
- Backend endpoint logic 
- Tests

---

## [1.0.0] — (2025-10-21 ~) 2026-01-25
First fully reproducible version

### Added
- Integration tests for every domain
- Dedicated configs for `dev` and `test` profiles
- Docs: Version-based roadmap and schemas
- Docs: Feature flows
- READMEs

### Changed
- Database: MSSQL2019 for Windows -> Docker MSSQL2019
- Organized & sanitized dev-time sample data

### Fixed
- Compatibility bugs from OS-dependent paths
- Deployment bugs from the mix of project resources/external storages

---

## [1.0.0-snapshot] — 2021-07-21

- Bootcamp presentation snapshot
- Missing local dependency and minimum database layer info to be fully reproducible