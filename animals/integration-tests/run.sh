#!/bin/bash

cd "$(dirname "$0")/.." || exit

## Launch animals
php -S 127.0.0.1:4400 -t public&
echo "waiting for animals service being launched ..."
while ! nc -z 127.0.0.1 4400; do
  sleep 1
done
echo "animals service launched"

# Run the integration tests
newman run integration-tests/newman-tests.json -e integration-tests/envs.json

# Teardown
kill "$(lsof -t -i :4400)"