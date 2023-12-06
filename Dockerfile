FROM eclipse-temurin:11-jdk-jammy AS builder
WORKDIR /home/proj/backend
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:resolve-plugins dependency:resolve
COPY ./src ./src
RUN ./mvnw install
RUN ./mvnw test

FROM eclipse-temurin:11-jre-jammy
WORKDIR /home/proj/backend
EXPOSE 8080
COPY --from=builder ./home/proj/backend/target/*.jar ./app.jar
CMD ["java","-jar","app.jar"]