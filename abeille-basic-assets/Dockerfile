# Start with a base image containing Java runtime
FROM openjdk:11-jdk-alpine

# Add Maintainer Info
LABEL maintainer="little3201@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Tag
TAG

# Make port 8764 available to the world outside this container
#EXPOSE 8764

# The application's jar file
ARG JAR_FILE=target/abeille-basic-assets-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} abeille-basic-assets.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/abeille-basic-assets.jar"]