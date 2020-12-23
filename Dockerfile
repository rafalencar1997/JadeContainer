# Imagem base utilizada
FROM openjdk:latest

# Copia as biliotecas do jade para dentro do container
COPY ./lib /jade/lib

# Copia os arquivos compilados dos agentes para dentro do container
COPY ./bin /jade/bin

# Diretório padrão onde serão executados os comandos
WORKDIR /jade/bin

# Set a variável de ambiente CLASSPATH
ENV CLASSPATH=/jade/lib/jade.jar:$CLASSPATH