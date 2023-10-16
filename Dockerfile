FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /secodeverse.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/secodeverse.jar"]
