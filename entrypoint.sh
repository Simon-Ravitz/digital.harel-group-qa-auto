#!/usr/bin/env bash
set -euo pipefail

# Run tests to generate the report (unless SKIP_TESTS=true)
if [ "${SKIP_TESTS:-false}" != "true" ]; then
  echo "Running tests..."
  if ! mvn -Dheadless=true test; then
    echo "Tests failed. Attempting to serve the last available report."
  fi
else
  echo "SKIP_TESTS=true, skipping test execution."
fi

# Serve the Extent report
REPORT_DIR="/app/target/extent-report"
if [ ! -d "$REPORT_DIR" ]; then
  echo "Report directory not found: $REPORT_DIR" >&2
  exit 1
fi

cd "$REPORT_DIR"
python3 -m http.server 8080
