# Start with a base image containing Java runtime
FROM eclipse-temurin:17-jre-alpine

# Add Maintainer Info
LABEL maintainer="little3201@gmail.com"

# Add env
ENV VERSION=0.1.0

# Make port 8763 available to the world outside this container
EXPOSE 8763

VOLUME /tmp

# Add the application's jar to the container
ARG JAR_FILE=target/hypervisor-${VERSION}.jar

# Copy file
COPY ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]