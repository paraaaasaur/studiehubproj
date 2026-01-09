# StudieHub Renewal Project (1.0.0-SNAPSHOT)

[English](README.md) | [日本語](README-jp.md) | [繁體中文](README-zh-tw.md)

Welcome to StudieHub Renewal Project!  
StudieHub originated as a Spring Boot-based project with the goal of building an online learning platform (similar to Udemy, edX...), developed by a six-person team during a Java web development bootcamp back in 2021.  
This repository represents a **renewal and refactoring effort** on top of [that legacy codebase](https://github.com/AWildHuskyAppeard/studiehubproj), focusing on improved architecture, maintainability, and alignment with modern development practices.

## Goal

StudieHub Renewal Project serves the following goals:

1. **Portfolio showcase** of Java/Spring backend development and system design skills.
2. **Legacy system refactoring practice**, simulating real-world constraints such as:
   - REST architecture design
   - Automated testing
   - Separation of web concerns
   - Migration toward modern coding practices
   - Dependency upgrades
   - Domain model and database schema redesign
   - Versioning
   - Documentation
3. **Completion and preservation** of the former team project, as a tribute to the original contributors.

See `ROADMAP.md` and `docs/version-work-details/` for detailed work items.


## Run App
> Note: This project doesn't have a one-click demo at the moment.  
> As this is a small side project,
> reviewers are welcome to read the code and documentation without running the application.

> Note: User data and UI are all in Traditional Chinese(zh-tw), 
> while the source code and documentation are mostly in English.
> i18n is not available at the moment.

### Prerequisites
- Java 11
- SQL Server 2019 with collation = `Chinese_Taiwan_Stroke_CI_AS`
  - ⚠️ Other versions and collations are untested
  - If your SQL Server is run on Docker, collation must be assigned at image-running stage
    - See `dev/db/sqlserver/00-docker-sqlserver-setup-note.md` for details
- A terminal (CLI) environment to run command lines

### Steps
1. Clone the repository to your local environment
2. Set up database environment for SQL Server
   1. Make sure port = 1433 (default for SQL Server)
   2. Create a database named `STUDIEHUB`
   3. Create SQL Server LOGIN and USER objects using `dev/db/sqlserver/03-login-and-user-template.sql`
3. Execute the following command to run the app in the project root:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
4. When successfully run, you should see a beaver ASCII art saying "DEV MODE" as welcome banner.  
   - No need to install Maven since this project contains Maven Wrapper.


## Feature Overview

### Domain Scope
The application manages resources related to online learning, including:
- user
- course
- question
- event
- discussion thread
- order/cart

### UI & Usage Notes
- Sidebars are provided for navigation and feature testing.
- Certain features (e.g. write operations) require authentication.
  - User login: `id = test`, `password = test`
  - Admin login: `id = adming5`, `password = manager`

For detailed feature flows, see `docs/feature-flows/`

### ⚠️ Known Limitations & Cautions

#### 3rd-Party Payment Service
1. This app uses ECPay API locally, so external software ngrok is used to resolve the issue.
   1. Install Ngrok and run Ngrok service in the background
   2. Copy the forwarding url and register it to the environment variable `ngrokHttp`
   3. Rerun the app with ngrok environment variable set up.
2. Dedicated credit card credential for testing
   - Stub card number: `4311 9522 2222 2222`
   - Expire date: `12/55`
   - CVV: `222`
> Note: Payment methods other than credit card are not viable at the moment.


## Credit

- This README is based on the advice from the guru ChatGPT, plus some personal editing. Thank you as always!