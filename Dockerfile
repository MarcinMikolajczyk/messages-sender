FROM openjdk:13-alpine
RUN addgroup -S acaisoft && adduser -S acaisoft -G acaisoft
USER acaisoft:acaisoft
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]