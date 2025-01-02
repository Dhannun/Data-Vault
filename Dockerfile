FROM gcr.io/distroless/java17-debian12
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 80
ENV PORT=80
ENTRYPOINT ["java", "-jar", "/app/app.jar"]