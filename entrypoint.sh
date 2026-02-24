#!/usr/bin/env bash
set -euo pipefail

REPORT_DIR="/app/target/extent-report"
PUBLIC_DIR="/app/public"

mkdir -p "$PUBLIC_DIR"
cat > "$PUBLIC_DIR/index.html" <<'HTML'
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="refresh" content="20">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Report Generating</title>
  </head>
  <body>
    <h2>Report is being generated...</h2>
    <p>Please refresh in a couple of minutes. This page will auto-refresh.</p>
  </body>
</html>
HTML

# Start server immediately so Render detects the open port
python3 -m http.server 8080 --bind 0.0.0.0 --directory "$PUBLIC_DIR" &
SERVER_PID=$!

# Run tests to generate the report (unless SKIP_TESTS=true)
if [ "${SKIP_TESTS:-false}" != "true" ]; then
  echo "Running tests..."
  if ! mvn -Dheadless=true test; then
    echo "Tests failed. Attempting to serve the last available report."
  fi
else
  echo "SKIP_TESTS=true, skipping test execution."
fi

if [ -d "$REPORT_DIR" ]; then
  rm -rf "$PUBLIC_DIR"/*
  cp -R "$REPORT_DIR"/* "$PUBLIC_DIR"/
  if [ -f "$PUBLIC_DIR/index.html" ]; then
    echo "Report index found: $PUBLIC_DIR/index.html"
  else
    echo "Report index missing in $PUBLIC_DIR"
  fi
  ls -la "$PUBLIC_DIR"
  echo "Report published to $PUBLIC_DIR"
else
  echo "Report directory not found: $REPORT_DIR"
fi

wait "$SERVER_PID"
