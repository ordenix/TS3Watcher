FROM gradle:7.4.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-oracle

EXPOSE 443

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/Watcher-0.0.1-SNAPSHOT.jar /app/Watcher-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/Watcher-0.0.1-SNAPSHOT.jar"]