FROM gradle:jdk17-alpine
WORKDIR /human-command-service

COPY src /human-command-service/src/
COPY build.gradle .
COPY lombok.config .

RUN gradle clean build -x test

ENTRYPOINT ["java", "-jar", "./build/libs/human-command-service-1.0.0.jar"]