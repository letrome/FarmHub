#!/bin/bash

cd "$(dirname "$0")/.." || exit

## Launch farmers
python main.py --port=6600&

echo "waiting for farmers service being launched ..."
while ! nc -z localhost 6600; do
  sleep 1
done
echo "farmers service launched"

# Launch the integration tests
newman run integration-tests/newman-tests.json -e integration-tests/envs.json

# Teardown
kill "$(lsof -t -i :6600)"