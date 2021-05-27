
teste="1 2 3 4 5 1000" 

sudo docker kill jadeCont 
sudo docker rm jadeCont

sudo docker run -p 8080:7778 -t -d \
    -e "HOST_IP=$(ip -4 addr show wlp7s0 | grep -Po 'inet \K[\d.]+')" \
    -e HOST_PORT='8080' \
    --name jadeCont rafalencar18/jadecontainer


command sudo docker exec jadeCont java myAgents.AgentHost \
${teste[0]} ${teste[1]} ${teste[2]} ${teste[3]} ${teste[4]} ${teste[5]} &>> output.txt
 
sleep 1m

sudo docker cp -a jadeCont:/jade/bin/results .
sudo docker kill jadeCont 
sudo docker rm jadeCont