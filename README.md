#  JADE Container

## Instalações

### Instalando e rodando JADE


```
export CLASSPATH=~/jade/lib/jade.jar:$CLASSPATH


java jade.Boot -gui
```

### Instalando e rodando Docker


Comandos úteis:
```
sudo systemctl unmask docker
```


## Criando Imagem do Docker 

Dentro do diretório contendo o arquivo **Dockerfile**, execute o comando abaixo para gerar a imagem do container. Depois de gerar a imagem, envie ela para o seu repositório no dockerhub:
```
docker build --tag rafalencar18/jadecontainer .
docker push rafalencar18/jadecontainer
```
Criada a imagem, rodamos o container com o comando abaixo, lembrando de fazer o bind as portas do container e da máquina:
```
docker run -p port:7778 -t -d --name container rafalencar18/jadecontainer
```
E por fim, podemos entrar dentro do container com o seguinte comando:
```
docker exec -it container /bin/bash 
```



## Rodando os agentes Sender e Receiver dentro do Container

```
docker run -p 8080:7778 -t -d -e "HOST_IP=$(ip -4 addr show enp6s0 | grep -Po 'inet \K[\d.]+')" --name receiver rafalencar18/jadecontainer

docker run -p 8081:7778 -t -d -e "HOST_IP=$(ip -4 addr show enp6s0 | grep -Po 'inet \K[\d.]+')" --name sender rafalencar18/jadecontainer
```

```
docker exec -it sender java jade.Boot -agents 'S:SenderAgent' -platform-id Platform1

docker exec -it receiver java jade.Boot -agents 'R:ReceiverAgent' -platform-id Platform2
```

```

javac -cp bin *.java 
java jade.Boot -container -host 172.17.0.1 -port 1099
java jade.Boot -container -host 172.17.0.1 -port 1099 -agents 'Hello:HelloAgent'

java jade.Boot -agents 'S1:SenderAgent;S2:SenderAgent;S3:SenderAgent;S4:SenderAgent;S5:SenderAgent' -platform-id Platform1
java jade.Boot -agents 'R:ReceiverAgent' -platform-id Platform2

```

### Mtp Address
-mtp "jade.mtp.http.MessageTransportProtocol(http://MYHOST:MYPORT/acc)"

### Sniffer Agent 
-agents 'mySniffer:jade.tools.sniffer.Sniffer'

### AWS 

Instale o docker nas instâncias com o seguinte comando:
```
sudo yum update -y
sudo amazon-linux-extras install docker
sudo usermod -a -G docker ec2-user
```
De um logout da instância e conecte novamente

```
service docker start
docker pull rafalencar18/jadecontainer

```

Caso queira remover o container:
```
docker kill container 
docker rm container

```

## Lista com links úteis:
- [First programs in JADE](https://www.iro.umontreal.ca/~vaucher/Agents/Jade/primer2.html);
- [Tutorial JADE](https://jade.tilab.com/doc/tutorials/JADEAdmin/JadePlatformTutorial.html);

