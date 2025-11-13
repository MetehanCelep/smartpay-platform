FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copy and install parent pom first
COPY pom.xml ./
RUN mvn install -N

# Copy and build common-lib
COPY common-lib/pom.xml ./common-lib/
COPY common-lib/src ./common-lib/src
WORKDIR /build/common-lib
RUN mvn clean install -DskipTests

# Copy and build analytics-service
WORKDIR /build
COPY analytics-service/pom.xml ./analytics-service/
COPY analytics-service/src ./analytics-service/src
WORKDIR /build/analytics-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/analytics-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]