FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} prodotto-service.jar
ENTRYPOINT ["java", "-jar", "/prodotto-service.jar"]
EXPOSE 9003
