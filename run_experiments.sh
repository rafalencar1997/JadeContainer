echo "Batata"

sudo docker run -p 8080:7778 -t -d \
        -e "HOST_IP=$(ip -4 addr show wlp7s0: | grep -Po 'inet \K[\d.]+')" \
        -e HOST_PORT='8080' \
        --name jadeCont rafalencar18/jadecontainer

sudo docker exec jadeCont java myAgents.AgentHost \
    "1" "1" "1" "1" "1" "1000" > out_docker.txt

sleep 1m

sudo docker kill jadeCont 
sudo docker rm jadeCont

echo "1 minuto se passou"