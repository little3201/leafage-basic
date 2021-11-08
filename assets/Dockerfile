# Start with a base image containing Java runtime
FROM openjdk:11.0.13-jre-slim

# Add Maintainer Info
LABEL maintainer="little3201@gmail.com"

# Add param to use in anywhere
ARG JAR_NAME=assets

# Add env
ENV VERSION=0.1.0 ACTIVE=dev

# Add a volume pointing to /tmp
WORKDIR /usr/app

# Make port 8764 available to the world outside this container
EXPOSE 8764

# Add the application's jar to the container
ADD target/${JAR_NAME}-${VERSION} ${JAR_NAME}.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/${JAR_FILE}.jar --spring.profiles.active=${ACTIVE}"]