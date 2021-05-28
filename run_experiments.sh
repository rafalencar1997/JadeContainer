#!/bin/bash

declare -a teste=(
"1 50 1 1 1 1000"
"2 50 1 50 1 1000"
"3 50 50 50 1 1000"
"1 40 1 1 1 1000"
"2 40 1 40 1 1000"
"3 40 40 40 1 1000"
"1 30 1 1 1 1000"
"2 30 1 30 1 1000"
"3 30 30 30 1 1000"
"1 20 1 1 1 1000"
"2 20 1 20 1 1000"
"3 20 20 20 1 1000"
"1 10 1 1 1 1000"
"1 10 1 1 1 1000"
"1 10 1 1 10 1000"
"1 10 1 1 100 1000"
"1 10 1 1 1000 1000"
"1 10 1 1 1 1"
"1 10 1 1 1 10"
"1 10 1 1 1 100"
"1 10 1 1 1 1000"
"2 10 1 10 1 1000"
"2 10 1 10 1 1000"
"2 10 1 10 10 1000"
"2 10 1 10 100 1000"
"2 10 1 10 1000 1000"
"2 10 1 10 1 1"
"2 10 1 10 1 10"
"2 10 1 10 1 100"
"2 10 1 10 1 1000"
"3 10 10 10 1 1000"
"3 10 10 10 1 1000"
"3 10 10 10 10 1000"
"3 10 10 10 100 1000"
"3 10 10 10 1000 1000"
"3 10 10 10 1 1"
"3 10 10 10 1 10"
"3 10 10 10 1 100"
"3 10 10 10 1 1000"
"4 2 1 1 1 1000"
"4 2 10 10 1 1000"
"4 2 100 100 1 1000"
"4 2 1000 1000 1 1000"
"4 2 10000 10000 1 1000"
"4 2 100 100 1 1000"
"4 2 100 100 10 1000"
"4 2 100 100 100 1000"
"4 2 100 100 1000 1000"
"4 2 100 100 1 1"
"4 2 100 100 1 10"
"4 2 100 100 1 100"
"4 2 100 100 1 1000"
"5 1 1 1 1 1000"
"5 1 10 10 1 1000"
"5 1 100 100 1 1000"
"5 1 1000 1000 1 1000"
"5 1 10000 10000 1 1000"
"5 1 100 100 1 1000"
"5 1 100 100 10 1000"
"5 1 100 100 100 1000"
"5 1 100 100 1000 1000"
"5 1 100 100 1 1"
"5 1 100 100 1 10"
"5 1 100 100 1 100"
"5 1 100 100 1 1000"
)

sudo su

sudo docker pull rafalencar18/jadecontainer

sudo docker kill jadeCont 
sudo docker rm jadeCont

for t in "${teste[@]}";
do
    HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')

    nmchine=$((${t:2:2}+10))

    if [ ${HOST_IP: -2} -le $nmchine ]
    then
        echo "Experimento com parâmetros |$t| será executado"
        sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

        command sudo docker exec jadeCont java myAgents.AgentHost \
        "$teste" &>> output.txt
        
        sleep 5m

        sudo docker cp -a jadeCont:/jade/bin/results .
        sudo docker kill jadeCont 
        sudo docker rm jadeCont
    else
        echo "Esta máquina será desligada"
        shutdown
    fi
done