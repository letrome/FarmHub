#!/bin/bash

cd "$(dirname "$0")" || exit

IP_ADDRESS=$(hostname -I | cut -d ' ' -f1)

# Launch the stack
## Launch species
cd species || exit
node server.js 3000 > ../nohup.out 2> ../nohup.err < /dev/null &

echo "waiting for species service being launched ..."
while ! nc -z localhost 3000; do
  sleep 1
done

echo "species service launched"
sleep 2
cd .. || exit

## Launch animals
cd animals || exit
php -S ${IP_ADDRESS}:4000 -t public > ../nohup.out 2> ../nohup.err < /dev/null &
echo "waiting for animals service being launched ..."
while ! nc -z ${IP_ADDRESS} 4000; do
  sleep 1
done

echo "animals service launched"
sleep 2
cd .. || exit

## Launch pictures
cd pictures || exit
./farmhub ${IP_ADDRESS}:5000 > ../nohup.out 2> ../nohup.err < /dev/null &

echo "waiting for pictures service being launched ..."
while ! nc -z ${IP_ADDRESS} 5000; do
  sleep 1
done

echo "pictures service launched"
sleep 2
cd .. || exit

## Launch farmers
cd farmers || exit
python main.py --port=6000 > ../nohup.out 2> ../nohup.err < /dev/null &

echo "waiting for farmers service being launched ..."
while ! nc -z localhost 6000; do
  sleep 1
done

echo "farmers service launched"
sleep 2

cd .. || exit

## Launch farm
cd farm || exit
mvn spring-boot:run > ../nohup.out 2> ../nohup.err < /dev/null &

echo "waiting for farm service being launched ..."
while ! nc -z localhost 7000; do
  sleep 1
done

echo "farm service launched"