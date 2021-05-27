#!/bin/bash

benchmark="2"
numberOfHosts="10 20 30 40 50"
numberOfReceivers="1"
numberOfSenders="10 20 30 40 50"
messageSize="1 10 100 1000"
numberOfMessages="1 10 100 1000"

sudo docker kill jadeCont 
sudo docker rm jadeCont

echo "Benchmark: $benchmark"

echo "Experimento: 1"
for nh in numberOfHosts; 
do
    sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

    sudo docker exec jadeCont java myAgents.AgentHost \
    $benchmark $nh $nh $numberOfReceivers "1" "1000" &

    sleep 5m

    sudo docker cp -a jadeCont:/jade/bin/results .
    sudo docker kill jadeCont 
    sudo docker rm jadeCont
    
done

echo "Experimento: 2"
for ms in messageSize; 
do
    sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

    sudo docker exec jadeCont java myAgents.AgentHost \
    $benchmark "1" $numberOfSenders $numberOfReceivers $ms "1000" &
    
    sleep 5m

    sudo docker cp -a jadeCont:/jade/bin/results .
    sudo docker kill jadeCont 
    sudo docker rm jadeCont
    
done

echo "Experimento: 3"
for nm in numberOfMessages; 
do
    sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

    sudo docker exec jadeCont java myAgents.AgentHost \
    $benchmark "1" $numberOfSenders $numberOfReceivers "1" $nm &

    sleep 5m

    sudo docker cp -a jadeCont:/jade/bin/results .
    sudo docker kill jadeCont 
    sudo docker rm jadeCont
    
done