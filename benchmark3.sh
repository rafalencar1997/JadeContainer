#!/bin/bash

benchmark         = "3"
numberOfSenders   = "1"
numberOfReceivers = "1"
numberOfHosts     = ["5", "10", "5", "20"]
messageSize       = ["1", "10", "100", "1000"]
numberOfMessages  = ["1", "10", "100", "1000"]

echo "Benchmark: $benchmark"
for nh in numberOfHosts; 
do
    echo "Experimento: 1"
    sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

    sudo docker exec jadeCont java myAgents.AgentHost \
    $benchmark $nh $numberOfSenders $numberOfReceivers "1" "1000"

    sleep 5m

    sudo docker cp -a jadeCont:/jade/bin/results .
    sudo docker kill jadeCont 
    sudo docker rm jadeCont
    
done

for ms in messageSize; 
do
    echo "Experimento: 2"
    sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

    sudo docker exec jadeCont java myAgents.AgentHost \
    $benchmark "1" $numberOfSenders $numberOfReceivers $ms "1000"
    
    sleep 5m

    sudo docker cp -a jadeCont:/jade/bin/results .
    sudo docker kill jadeCont 
    sudo docker rm jadeCont
    
done

for nm in numberOfMessages; 
do
    echo "Experimento: 3"
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