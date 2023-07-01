FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM eclipse-temurin:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/com.example.ktor-crud-users-all.jar /app/com.example.ktor-crud-users-all.jar
ENTRYPOINT ["java","-jar","/app/com.example.ktor-crud-users-all.jar"]