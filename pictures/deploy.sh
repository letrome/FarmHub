#!/bin/bash

IP_ADDRESS="${{ steps.get_ip.outputs.ip_address }}"

pid=$(sudo lsof -t -i:5000)

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


cd pictures || exit
go run . $IP_ADDRESS:5000 &