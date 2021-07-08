#  JADE Container

## Instalações

Para esse projeto é necessário ter instalado JAVA e a biblioteca JADE

Após instalação do JADE, é preciso indicar seu path na variável de ambiente CLASSPATH
```
export CLASSPATH=~/jade/lib/jade.jar:$CLASSPATH
```
Para rodar o jade bastar rodar o comando a seguir:
```
java jade.Boot -gui
```

Outra ferramenta necessária é o docker. Abaixo temos um comando que pode ser útil após sua instalação:
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

### Agent Host

Para faciliar a execução dos experimentos, foi criado um programa AgentHost que irá receber certos parâmetros do experimentos e inicializar os agentes sender e receiver necessários para a execução.

O comando java abaixo cria os agentes nas máquinas de acordo com os parâmetros listados:

```
java myAgents.AgentHost BENCHMARK NUMBEROFHOSTS NUMBEROFRECEIVERAGENTS NUMBEROFSENDERAGENTS MESSAGESIZE NUMBEROFMESSAGES
```

Vale lembrar que antes disso, duas variáveis de ambientes precisam ser setadas: HOST_IP e HOST_PORT

Essas variáveis são respectivamente o IP e Porta que servirão de endereço para os agentes em diferentes hosts se comunicarem.

## AWS 

### Instâncias EC2 e VPC
Iremos criar instâncias EC2 na AWS, que devem estar na mesma Subrede dentro de uma VPC. 

Para isso, criamos uma VPC, um SubNet com IPv4 CIDR 10.0.1.0/24. Depois associamos uma RouteTable para essa SubNet para que as máquinas tenham acesso a internet via ssh, e para que elas possam se comunicar entre si por qualquer porta.

Seguimos esse [tutorial](https://www.youtube.com/watch?v=Qw7NWssyl8Y) no youtube para criar essa subrede.

Para a comunicação entre os agentes funcionar e para que eles possam se encontrar mesmo em hosts distintos, iremos utilizar o IP interno no endereço dos agentes. Para isso, eles devem todos possuir o IP do tipo 10.0.1.X, onde X inicia em 10.

Esse setup do endereço IP é importante, pois dependendo do IP, teremos configurações diferentes dos agentes instânciados as máquinas.

Exemplo: no benchmark 2, apenas um dos hosts terá um agente do tipo **receiver**, en quanto os outros terão agentes do tipo **sender**. Neste caso, o host com IP 10.0.1.10 terá o receiver, enquanto aquelas com IP 10.0.1.X, com X>10 terão os agentes sender.

### Setup das instâncias
Instale o docker nas instâncias com o seguinte comando:
```
sudo yum update -y
sudo amazon-linux-extras install docker

```
De um logout da instância e conecte novamente

```
sudo usermod -a -G docker ec2-user
sudo service docker start
alias cls="clear"
sudo docker pull rafalencar18/jadecontainer
cls
```

## Script para rodar os experimentos:

1. Subir as máquinas

2. Adicionar ips privados das máquinas

3. Puxar imagem do docker

4. Subir container

5. Rodar comando do AgentHost no container

6. Copiar arquivos .csv do container para o host
```
docker cp jadeCont:/jade/bin/results results
```

7. Copiar arquivos .csv do host aws para máquina local
```
scp -r -i myKeyForJade.pem ec2-user@18.216.22.193:results results
```

8. Voltar para o passo 4 e iniciar um novo experimento


O loop dos passos 4 a 8 se encontra abaixo:
```
sudo docker cp -a jadeCont:/jade/bin/results .
sudo docker kill jadeCont 
sudo docker rm jadeCont
cls
sudo docker run -p 8080:7778 -t -d \
    -e "HOST_IP=$(ip -4 addr show eth0 | grep -Po 'inet \K[\d.]+')" \
    -e HOST_PORT='8080' \
    --name jadeCont rafalencar18/jadecontainer
sudo docker exec -it jadeCont java myAgents.AgentHost "1" "32" "1" "1" "1" "1000"

```

## Benchmark 1
```
# Rodar em N hosts 
java myAgents.AgentHost "1" "NH" "1" "1" "MS" "NM" 
```
## Benchmark 2
```
# Rodar em N hosts (senders)
java myAgents.AgentHost  "2" "NH" "1" "NSA" "MS" "NM"  
```

## Benchmark 3
```
# Rodar em N hosts (senders) 
java myAgents.AgentHost "3" "NH" "NRA" "NSA" "MS" "NM"  
```

## Benchmark 4
Para conversa interplataforma:
```
java myAgents.AgentHost "4" "2" "NSA" "MS" "NM"
```
Para conversa intraplataforma:
```
java myAgents.AgentHost "5" "1" "NRA" "NSA" "MS" "NM"
```

## Lista com links úteis:
- [First programs in JADE](https://www.iro.umontreal.ca/~vaucher/Agents/Jade/primer2.html);
- [Tutorial JADE](https://jade.tilab.com/doc/tutorials/JADEAdmin/JadePlatformTutorial.html);

