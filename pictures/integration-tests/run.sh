#!/bin/bash

cd "$(dirname "$0")/.." || exit

## Launch pictures
go run . 127.0.0.1:5500&

echo "waiting for pictures service being launched ..."
while ! nc -z 127.0.0.1 5500; do
  sleep 1
done
echo "pictures service launched"

# Launch the integration tests
newman run integration-tests/newman-tests.json -e integration-tests/envs.json

# Teardown
kill "$(lsof -t -i :5500)"