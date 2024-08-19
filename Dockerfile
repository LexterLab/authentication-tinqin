FROM amazoncorretto:21-alpine

WORKDIR /auth

COPY rest/target/rest-0.0.1-SNAPSHOT.jar /auth/auth-docker.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/auth/auth-docker.jar"]