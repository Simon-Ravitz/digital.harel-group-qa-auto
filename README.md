# Harel Travel Policy QA (Selenium + TestNG)

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
