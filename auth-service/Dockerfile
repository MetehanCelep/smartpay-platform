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

# Copy and build auth-service
WORKDIR /build
COPY auth-service/pom.xml ./auth-service/
COPY auth-service/src ./auth-service/src
WORKDIR /build/auth-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/auth-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]