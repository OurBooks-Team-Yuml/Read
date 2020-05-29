FROM node:12

RUN apt update && apt install -y openjdk-8-jdk wget; \
    wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein; \
    chmod +x lein; \
    mv lein /usr/local/bin

EXPOSE 8890
