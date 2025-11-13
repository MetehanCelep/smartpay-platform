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

# Copy and build api-gateway
WORKDIR /build
COPY api-gateway/pom.xml ./api-gateway/
COPY api-gateway/src ./api-gateway/src
WORKDIR /build/api-gateway
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/api-gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]