FROM openjdk:17

EXPOSE 8080

ARG JAR_FILE
COPY ${JAR_FILE} /app.jar

CMD java -XX:+ExitOnOutOfMemoryError -jar /app.jar

