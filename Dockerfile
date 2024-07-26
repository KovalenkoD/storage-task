#
# Build stage
#
FROM maven:3.6.0-jdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true

#
# Package stage
#
FROM eclipse-temurin:17-jre-focal
USER root
COPY --from=build /home/app/target/bookstore-api-0.0.1-SNAPSHOT.jar /usr/local/lib/bookstore-api.jar
COPY /src/main/resources/config/liquibase/v1 /usr/local/resources/config/liquibase/v1
EXPOSE 8080
ENTRYPOINT [ \
"java", "-jar", "/usr/local/lib/bookstore-api.jar"
]
