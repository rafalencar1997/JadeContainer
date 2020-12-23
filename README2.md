## Script para rodar os experimentos:

1. Subir as máquinas

2. Adicionar ips privados das máquinas

3. Puxar imagem do docker
```
docker pull rafalencar18/jadecontainer
```

4. Alterar lista de receivers
```
vi receivers.txt
```

5. Subir container
```
 docker run -p 8080:7778 -t -d --name jadeCont rafalencar18/jadecontainer
```

6. Copiar lista de receiver para dentro do container
```
docker cp receivers.txt jadeCont:/jade/bin/receivers.txt
```

7. Rodar comando do AgentHost no container
```
docker exec -it java myAgents.AgentHost IP PORT BENCHMARK AGENTTYPE NUMBEROFAGENTS MESSAGESIZE NUMBEROFMESSAGES 
```

8. Copiar arquivos .csv do container para o host
```
docker cp jadeCont:/jade/bin/receivers.txt bin/receivers.txt
```

9. Copiar arquivos .csv do host aws para máquina local
```
scp -r ec2-user@52.14.73.156:results resultsX
```
```
java myAgents.AgentHost IP PORT BENCHMARK AGENTTYPE NUMBEROFAGENTS MESSAGESIZE NUMBEROFMESSAGES 
```
## Benchmark 1
```
# Rodar em N hosts 
java myAgents.AgentHost "10.0.1.N" "8080" "1" "0" "1" "1" "1000" 
```
## Benchmark 2
```
# Rodar em um host
java myAgents.AgentHost "10.0.1.10" "8080" "2" "1" "1" "1" "1000" 

# Rodar em N hosts 
java myAgents.AgentHost "10.0.1.N" "8080" "2" "2" "1" "1" "1000" 
```
## Benchmark 3
```
# Rodar em um host
java myAgents.AgentHost "10.0.1.10" "8080" "3" "1" "N" "1" "1000" 

# Rodar em N hosts 
java myAgents.AgentHost "10.0.1.N" "8080" "3" "2" "1" "1" "1000" 
```
## Benchmark 4
Para o primeiro experimento:
```
# Rodar em um host 
java myAgents.AgentHost "10.0.1.N" "8080" "4" "0" "N" "1" "1000"
```
Para o segundo experimento:
```
# Rodar em um host
java myAgents.AgentHost "10.0.1.10" "8080" "4" "1" "N" "1" "1000" 

# Rodar em outro host 
java myAgents.AgentHost "10.0.1.11" "8080" "4" "2" "N" "1" "1000" 
```
