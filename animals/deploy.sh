#!/bin/bash

pid=$(sudo lsof -t -i:4000)

if [ $? -eq 0 ]; then
  sudo kill $pid

  if [ $? -eq 0 ]; then
    echo "Previous instance killed"
  else
    echo "Error when trying to kill the instance"
  fi
else
  echo "No running instance to kill"
fi

php -S localhost:4000 -t animals/public &
