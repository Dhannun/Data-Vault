# Stage 1: Build the application
FROM maven:3-amazoncorretto-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM gcr.io/distroless/java17-debian12
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 80
# Set default PORT value and allow override
ENV PORT=80
ENTRYPOINT ["java", "-jar", "/app/app.jar"]