#!/bin/bash

cd "$(dirname "$0")" || exit

node ../server.js 3300&
newman run newman-tests.json -e envs.json
kill "$(lsof -t -i :3300)"