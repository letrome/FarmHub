#!/bin/bash

cd "$(dirname "$0")" || exit
cd ..

php -S localhost:4400 -t public&

newman run integration-tests/newman-tests.json -e integration-tests/envs.json
kill "$(lsof -t -i :4400)"