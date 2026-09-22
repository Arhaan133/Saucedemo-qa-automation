[![QA Automation Suite](https://github.com/Arhaan133/Saucedemo-qa-automation/actions/workflows/ci.yml/badge.svg)](https://github.com/Arhaan133/Saucedemo-qa-automation/actions)
# SauceDemo QA Automation Framework

An end-to-end QA project covering the full testing lifecycle for a sample
e-commerce web app — manual test design, UI automation, API testing, defect
tracking, and CI integration. Built to demonstrate practical QA skills, not
just tool familiarity.

**Application under test (UI):** [saucedemo.com](https://www.saucedemo.com/)
**Application under test (API):** [fakestoreapi.com](https://fakestoreapi.com/)

---

## What this project demonstrates

| Skill | Where |
|---|---|
| Manual test case design (SDLC/STLC) | [`test-cases/Test_Case_Documentation.xlsx`](test-cases/Test_Case_Documentation.xlsx) — 25 test cases across login, inventory, cart, checkout |
| UI test automation (Selenium + Java) | [`src/test/java/com/qaproject`](src/test/java/com/qaproject) — Page Object Model, 3 test classes, 18 automated tests |
| API testing | [`postman/FakeStoreAPI_Tests.postman_collection.json`](postman/FakeStoreAPI_Tests.postman_collection.json) — 8 requests with assertions |
| Defect tracking / bug reports | [`bug-reports/`](bug-reports/) — real bug report format with severity/priority |
| CI/CD for tests | [`.github/workflows/ci.yml`](.github/workflows/ci.yml) — runs the suite headlessly on every push |
| Reporting | TestNG + Surefire HTML reports, uploaded as CI artifacts |

## Project structure

```
qa-automation-framework/
├── src/test/java/com/qaproject/
│   ├── pages/        # Page Object Model (LoginPage, InventoryPage, CartPage, CheckoutPage)
│   ├── tests/        # LoginTests, InventoryTests, CheckoutTests
│   └── utils/         # BaseTest (WebDriver setup/teardown, screenshot-on-failure)
├── src/test/resources/testng.xml
├── test-cases/Test_Case_Documentation.xlsx   # 25 manual test cases + summary sheet
├── postman/FakeStoreAPI_Tests.postman_collection.json
├── bug-reports/       # BUG-001, BUG-002, and a reusable template
├── .github/workflows/ci.yml
└── pom.xml
```

## Test coverage

**Login (6 cases):** valid login, invalid password, locked-out user, empty
username/password, both fields empty.

**Inventory (8 cases):** product count, single/multiple add-to-cart, price
sort (asc/desc), cart navigation, UI element checks.

**Cart (4 cases):** item count, item removal, continue shopping, empty cart.

**Checkout (7 cases):** full happy-path order, missing first name, missing
postal code, order summary math, tax calculation, cancel flow, empty-cart
checkout.

18 of these 25 are automated in the Selenium suite; the remaining 7 are
marked `Manual` in the spreadsheet where automating them added little value
relative to a quick manual check (e.g., pure visual/UI checks).

## Running the automated suite locally

**Prerequisites:** Java 17+, Maven, Google Chrome.

```bash
git clone <your-repo-url>
cd qa-automation-framework
mvn clean test
```

This runs all 18 tests via TestNG. Chrome launches visibly by default; it
runs headless automatically inside CI. HTML/XML reports land in
`target/surefire-reports/`. Screenshots for any failing test are saved to
`screenshots/`.

## Running the API tests

1. Import `postman/FakeStoreAPI_Tests.postman_collection.json` into Postman.
2. Run the collection (or use the Collection Runner / Newman for CI).
3. Each request has built-in assertions on status code, response schema, and
   response time.

```bash
# Optional: run headlessly via Newman
npm install -g newman
newman run postman/FakeStoreAPI_Tests.postman_collection.json
```

## Continuous Integration

Every push to `main` triggers `.github/workflows/ci.yml`, which:
1. Sets up Java + Chrome on a clean Ubuntu runner.
2. Runs the full Selenium/TestNG suite headlessly.
3. Uploads the TestNG report and any failure screenshots as build artifacts.

## Defect tracking

Two sample bugs (`BUG-001`, `BUG-002`) were logged during test execution
using a standard severity/priority/repro-steps format, plus a reusable
[template](bug-reports/BUG_REPORT_TEMPLATE.md) for logging new ones. This
mirrors how defects would be tracked in JIRA in a real team setting.

## Why this project

Built to go beyond "I know Selenium/JIRA/Postman" as resume bullet points —
this repo is the actual artifact: a test plan, working automation, API
checks, logged defects, and a CI pipeline that proves the tests really run.

## Tech stack

Java 17 · Selenium WebDriver 4 · TestNG · Maven · WebDriverManager ·
Postman · GitHub Actions · openpyxl (test case doc)
