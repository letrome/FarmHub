#!/bin/bash

cd "$(dirname "$0")" || exit
cd ..

node server.js 3300&

newman run integration-tests/newman-tests.json -e integration-tests/envs.json
kill "$(lsof -t -i :3300)"