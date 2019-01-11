FROM frolvlad/alpine-oraclejdk8:slim
EXPOSE 8080
ADD build/libs/reactive-backend.jar .
ENTRYPOINT exec java -jar reactive-backend.jar
