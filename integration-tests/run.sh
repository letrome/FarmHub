#!/bin/bash

cd "$(dirname "$0")/.." || exit

# Launch the stack
## Launch species
cd species || exit
echo "SPECIES: $PWD"
node server.js 3300&
cd .. || exit

## Launch animals
cd animals || exit
echo "ANIMALS: $PWD"
php -S 127.0.0.1:4400 -t public&
cd .. || exit

## Launch pictures
cd pictures || exit
echo "PICTURES: $PWD"
go run . 127.0.0.1:5500&
cd .. || exit

## Launch farmers
cd farmers || exit
echo "FARMERS: $PWD"
python main.py --port=6600&
cd .. || exit

## Launch farm
cd farm || exit
echo "FARM: $PWD"
export SPRING_PROFILES_ACTIVE=intg
mvn spring-boot:run&
cd .. || exit

sleep 5

# Launch the integration tests
cd integration-tests || exit
echo "INTG-TESTS: $PWD"
newman run newman-tests.json -e envs.json

# Teardown

for ((i=3; i<=7; i++)); do
    kill "$(lsof -t -i :$((i * 1100)))"
done