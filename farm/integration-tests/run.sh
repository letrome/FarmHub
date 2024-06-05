#!/bin/bash

cd "$(dirname "$0")/../.." || exit

# Launch the stack
## Launch species
cd species || exit
node server.js 3300&

echo "waiting for species service being launched ..."
while ! nc -z localhost 3300; do
  sleep 1
done

echo "species service launched"
sleep 2
cd .. || exit

## Launch animals
cd animals || exit
php -S 127.0.0.1:4400 -t public&
echo "waiting for animals service being launched ..."
while ! nc -z 127.0.0.1 4400; do
  sleep 1
done

echo "animals service launched"
sleep 2
cd .. || exit

## Launch pictures
cd pictures || exit
go run . 127.0.0.1:5500&

echo "waiting for pictures service being launched ..."
while ! nc -z localhost 5500; do
  sleep 1
done

echo "pictures service launched"
sleep 2
cd .. || exit

## Launch farmers
cd farmers || exit
python main.py --port=6600&

echo "waiting for farmers service being launched ..."
while ! nc -z localhost 6600; do
  sleep 1
done

echo "farmers service launched"
sleep 2

cd .. || exit

## Launch farm
cd farm || exit
export SPRING_PROFILES_ACTIVE=intg
mvn spring-boot:run&

echo "waiting for farm service being launched ..."
while ! nc -z localhost 7700; do
  sleep 1
done

echo "farm service launched"
sleep 5

cd .. || exit

sleep 5

# Launch the integration tests
cd farm || exit
newman run integration-tests/newman-tests.json -e integration-tests/envs.json

# Teardown
for ((i=3; i<=7; i++)); do
    kill "$(lsof -t -i :$((i * 1100)))"
done