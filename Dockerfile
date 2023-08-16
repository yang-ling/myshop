FROM gradle:8.2.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle
WORKDIR /home/gradle
# ENV JAVA_OPTS="-Xms2g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"
ENV JAVA_OPTS="-XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"
RUN gradle --no-daemon --no-build-cache bootJar

FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="Ling Yang<yanglingit@outlook.com>"

RUN mkdir /home/nobody
WORKDIR /home/nobody

COPY --from=build /home/gradle/build/libs/*.jar app.jar

EXPOSE 8080
COPY --chown=65534:65534 startup.sh ./

USER 65534

ENTRYPOINT ["sh", "./startup.sh"]
