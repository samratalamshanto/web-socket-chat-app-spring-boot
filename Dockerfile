FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

COPY . .

# fix permissions + line endings
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:25-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
#EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
