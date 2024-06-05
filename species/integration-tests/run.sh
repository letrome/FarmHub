#!/bin/bash

cd "$(dirname "$0")/.." || exit

## Launch species
node server.js 3300&
echo "waiting for species service being launched ..."
while ! nc -z localhost 3300; do
  sleep 1
done
echo "species service launched"

# Launch the integration tests
newman run integration-tests/newman-tests.json -e integration-tests/envs.json

# Teardown
kill "$(lsof -t -i :3300)"