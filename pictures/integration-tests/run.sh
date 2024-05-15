#!/bin/bash

cd "$(dirname "$0")" || exit
cd ..
echo $PWD

go run . localhost:5500&
echo $?

sleep 1
newman run integration-tests/newman-tests.json -e integration-tests/envs.json
kill "$(lsof -t -i :5500)"