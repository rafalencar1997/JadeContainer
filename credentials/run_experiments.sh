#!/bin/bash

declare -a teste=(
"1 32 1 1 1 1000"
"2 32 1 32 1 1000"
"3 32 32 32 1 1000"
"1 16 1 1 1 1000"
"1 16 1 1 1 1000"
"1 16 1 1 10 1000"
"1 16 1 1 100 1000"
"1 16 1 1 1000 1000"
"1 16 1 1 1 1"
"1 16 1 1 1 10"
"1 16 1 1 1 100"
"1 16 1 1 1 1000"
"2 16 1 16 1 1000"
"2 16 1 16 1 1000"
"2 16 1 16 10 1000"
"2 16 1 16 100 1000"
"2 16 1 16 1000 1000"
"2 16 1 16 1 1"
"2 16 1 16 1 10"
"2 16 1 16 1 100"
"2 16 1 16 1 1000"
"3 16 16 16 1 1000"
"3 16 16 16 1 1000"
"3 16 16 16 10 1000"
"3 16 16 16 100 1000"
"3 16 16 16 1000 1000"
"3 16 16 16 1 1"
"3 16 16 16 1 10"
"3 16 16 16 1 100"
"3 16 16 16 1 1000"
"1 8 1 1 1 1000"
"2 8 1 8 1 1000"
"3 8 8 8 1 1000"
"1 4 1 1 1 1000"
"2 4 1 4 1 1000"
"3 4 4 4 1 1000"
"1 2 1 1 1 1000"
"2 2 1 2 1 1000"
"3 2 2 2 1 1000"
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

sudo yum update -y

sudo amazon-linux-extras install docker -y

sudo usermod -a -G docker ec2-user

sudo service docker start

sudo docker pull rafalencar18/jadecontainer

sudo docker kill jadeCont 
sudo docker rm jadeCont

for t in "${teste[@]}";
do
    HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')

    nmchine=$((${t:2:2}+10-1))

    if [ ${HOST_IP: -2} -le $nmchine ]
    then
        echo "Experimento com parâmetros |$t| será executado"
        sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

        sudo docker exec -d jadeCont java myAgents.AgentHost "$teste"
        
        sleep 5m

        sudo docker cp -a jadeCont:/jade/bin/results .
        sudo docker kill jadeCont 
        sudo docker rm jadeCont
    else
        echo "Esta máquina será desligada"
        sudo shutdown
    fi
done