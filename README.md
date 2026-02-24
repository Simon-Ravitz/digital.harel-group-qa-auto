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

## Optional
Override base URL:
```bash
mvn -Dheadless=true -DbaseUrl="https://digital.harel-group.co.il/travel-policy" test
```
