#!/bin/bash

declare -a teste=(
"1 3 1 1 1 1000 TRUE"
"1 3 1 1 1 1000 FALSE"
"2 3 1 1 1 1000 TRUE"
"2 3 1 5 1 1000 TRUE"
"2 3 1 10 1 1000 TRUE"
"2 3 1 1 1 1000 FALSE"
"2 3 1 5 1 1000 FALSE"
"2 3 1 10 1 1000 FALSE"
"3 3 1 1 1 1000 TRUE"
"3 3 3 1 1 1000 TRUE"
"3 3 1 1 1 1000 FALSE"
"3 3 3 1 1 1000 FALSE"
"4 2 20 20 1 1000 TRUE"
"4 2 40 40 1 1000 TRUE"
"4 2 60 60 1 1000 TRUE"
"5 1 20 20 1 1000 TRUE"
"5 1 40 40 1 1000 TRUE"
"5 1 60 60 1 1000 TRUE"
"4 2 20 20 1 1000 FALSE"
"4 2 40 40 1 1000 FALSE"
"4 2 60 60 1 1000 FALSE"
"5 1 20 20 1 1000 FALSE"
"5 1 40 40 1 1000 FALSE"
"5 1 60 60 1 1000 FALSE"
)

sudo yum update -y

sudo amazon-linux-extras install docker -y

sudo usermod -a -G docker ec2-user

sudo service docker start

sudo docker pull rafalencar18/jadecontainer

sudo docker kill jadeCont 
sudo docker rm jadeCont

HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')

for t in "${teste[@]}";
do

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