#!/bin/bash

cd "$(dirname "$0")" || exit
cd ..

python main.py --port=6600&

newman run integration-tests/newman-tests.json -e integration-tests/envs.json
kill "$(lsof -t -i :6600)"