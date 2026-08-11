FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/hypervisor-0.0.1.jar app.jar

EXPOSE 8762

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
