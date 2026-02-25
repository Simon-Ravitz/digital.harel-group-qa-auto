# Harel Travel Policy QA (Selenium + TestNG)

[![CI](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/ci.yml/badge.svg)](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/ci.yml)
[![Report](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/report.yml/badge.svg)](https://github.com/Simon-Ravitz/digital.harel-group-qa-auto/actions/workflows/report.yml)

Latest report: https://digital-harel-group-qa-auto.onrender.com

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
export REMOTE_URL="https://USERNAME:ACCESS_KEY@hub-cloud.browserstack.com/wd/hub"
mvn -Dheadless=true test
```

## Optional
Override base URL:
```bash
mvn -Dheadless=true -DbaseUrl="https://digital.harel-group.co.il/travel-policy" test
```

## Render (Docker)
This repo can run tests and serve the Extent report as a web service.

If using Render:
- Service type: Web Service
- Environment: Docker
- Port: 8080
- Start command: default (uses entrypoint.sh)

The service will run tests on startup and then serve the report at `/`.
