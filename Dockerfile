FROM adoptopenjdk:11-jdk-hotspot

EXPOSE 8080
WORKDIR /application/weather
ENV spring_profiles_active=prod
ENTRYPOINT ["java", "-jar", "weather-api.jar"]
USER daemon

COPY target/coding-dojo-spring-boot-0.0.1-SNAPSHOT.jar weather-api.jar
