# procura vpc, se não houver, cria uma

N_HOSTS=3

aws ec2 run-instances \
    --count $N_HOSTS \
    --image-id ami-09558250a3419e7d0 \
    --instance-type t2.micro \
    --key-name myKeyForJade \
    --security-group-ids sg-076a1f81ded51b4ff \
    --subnet-id subnet-00427ef6698ac4a18 
   
echo "Instâncias Lançadas"


# for i in {0..$N_HOSTS};
# do
#     sudo yum update -y
#     sudo amazon-linux-extras install docker
#     sudo usermod -a -G docker ec2-user
#     logout
#     service docker start
#     docker pull rafalencar18/jadecontainer
#     docker run -t -d
#         -p 8080:7778  
#         -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" 
#         --name container rafalencar18/jadecontainer
# done

# for i in {0..$N_HOSTS};
# do
#     docker exec container 
#         java jade.Boot -agents 'S:SenderAgent;R:ReceiverAgent' -platform-id Platform   
# done

# for i in {0..$N_HOSTS};
# do
#     derruba instâncias  
# done
