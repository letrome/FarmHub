#!/bin/bash

cd "$(dirname "$0")/.." || exit

# Launch the stack
## Launch species
cd species || exit
node server.js 3300&
sleep 5
echo "species service launched"
cd .. || exit

## Launch animals
cd animals || exit
php -S 127.0.0.1:4400 -t public&
sleep 5
echo "animals service launched"
cd .. || exit

## Launch pictures
cd pictures || exit
go run . 127.0.0.1:5500&
sleep 5
echo "pictures service launched"
cd .. || exit

## Launch farmers
cd farmers || exit
python main.py --port=6600&
sleep 5
echo "farmers service launched"
cd .. || exit

## Launch farm
cd farm || exit
export SPRING_PROFILES_ACTIVE=intg
mvn spring-boot:run&
sleep 10
echo "farm service launched"
cd .. || exit

sleep 5

# Launch the integration tests
cd integration-tests || exit
newman run newman-tests.json -e envs.json

# Teardown

for ((i=3; i<=7; i++)); do
    kill "$(lsof -t -i :$((i * 1100)))"
done