FROM eclipse-temurin:11-jdk-jammy AS builder
WORKDIR /backend
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:11-jre-jammy
WORKDIR /backend
EXPOSE 8080
COPY --from=builder ./backend/target/*.jar ./app.jar
CMD ["java","-jar","app.jar"]
