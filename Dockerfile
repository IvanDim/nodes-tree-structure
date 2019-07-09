FROM openjdk:8
ADD target/nodes-server.jar nodes-server.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "nodes-server.jar"]