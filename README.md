# Harel Travel Policy QA (Selenium + TestNG)

[![CI](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/ci.yml/badge.svg)](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/ci.yml)
[![Report](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/report.yml/badge.svg)](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/report.yml)

Latest report: https://digital-harel-group-qa-auto-1.onrender.com

## Requirements
- Java 17+
- Maven 3.8+
- Google Chrome installed

## Run (UI / headed)
```bash
mvn -Dheadless=false test
```

## Run (headless)
```bash
mvn -Dheadless=true test
```

## Report
After execution, open:
- `target/extent-report/index.html`

## Remote Selenium Grid
You can run the tests against a remote Selenium Grid by setting `REMOTE_URL`.
If `REMOTE_URL` is not set, the tests use local Chrome.

Example:
```bash
export REMOTE_URL="https://USERNAME:ACCESS_KEY@hub-cloud.browserstack.com/wd/hub" || REMOTE_URL="http://localhost:4444"
mvn -Dheadless=true test
```

Remove the REMOTE_URL to enable locally the non head-less mode:
```bash
unset REMOTE_URL
```

## Local Selenium Grid (Docker)
Start a local Selenium Grid with Chrome + Firefox:
```bash
docker compose -f docker-compose.selenium-grid.yml up -d
```

Grid UI:
```
http://localhost:4444/ui
```

Stop the grid:
```bash
docker compose -f docker-compose.selenium-grid.yml down
```

## Optional
Override base URL:
```bash
mvn -Dheadless=true -DbaseUrl="https://digital.harel-group.co.il/travel-policy" test
```

## Render
This repo serve the Extent report as a static file.


## GitHub Actions Flow
1. On push to `main` (and every 2 hours), the workflow runs `mvn -Dheadless=true test`.
2. The Extent report is generated in `target/extent-report`.
3. The workflow copies the report into the `report` branch and pushes it.
4. Render Static Site watches the `report` branch and serves the latest report.
