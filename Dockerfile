# Imagem base utilizada
FROM openjdk:latest

# Copia as biliotecas do jade para dentro do container
COPY ./lib /jade/lib

# Copia os arquivos dos agentes para dentro do container
COPY ./src /jade/src

# Diretório padrão onde serão executados os comandos
WORKDIR /jade/src

# Expõe porta do container
# EXPOSE 8080

ENV CLASSPATH=/jade/lib/jade.jar:$CLASSPATH
# ENV CLASSPATH=/jade/lib/jadeExamples.jar:$CLASSPATH
# ENV CLASSPATH=/jade/lib/commons-codec/commons-codec-1.3.jar:$CLASSPATH

# Comando executado durante o processo de build da imagem
RUN ["javac", "HelloAgent.java"]
RUN ["javac", "ReceiverAgent.java"]
RUN ["javac", "SenderAgent.java"]
# Comando executado ao terminar de subir o container
# ENTRYPOINT ["java", "jade.Boot", \\
#             "-container", "-host", "172.17.0.1", "-port", "1099",\\
#             "-agents", "'Hello:HelloAgent'"]