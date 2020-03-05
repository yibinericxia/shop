#!/usr/bash

app=spring-shop-customers

mvn clean build

docker build -t $app:latest .

docker run --rm -d --name $app $app:latest

