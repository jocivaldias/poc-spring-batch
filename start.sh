#!/bin/sh

docker-compose -f docker-compose-infra.yml up -d
sleep 5
docker-compose up -d --build --scale worker=10