FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

COPY . .
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:25-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
