FROM openjdk:17.0.2

RUN mkdir /logs
RUN chown 1000:1000 /logs
RUN mkdir /app
RUN chown 1000:1000 /app

USER 1000:1000
WORKDIR /app
COPY  --chown=1000:1000 target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]