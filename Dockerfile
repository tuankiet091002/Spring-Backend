FROM eclipse-temurin:11-jdk-jammy AS base
WORKDIR /home/proj/backend
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY ./src ./src

FROM base as test
RUN ["./mvnw", "test"]

FROM base as build
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f ./pom.xml clean package

FROM eclipse-temurin:11-jre-jammy AS production
WORKDIR /home/proj/backend
EXPOSE 8080
COPY --from=build ./home/proj/backend/target/*.jar ./app.jar
CMD ["java","-jar","app.jar"]