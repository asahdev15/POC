FROM openjdk:8-jdk-alpine
ENV CACHE_HOST=127.0.0.1
ENV CACHE_PORT=8083
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
