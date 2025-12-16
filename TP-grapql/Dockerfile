#ETAPE 1
FROM maven:4.0.0-rc-5-amazoncorretto-25-al2023 AS build

WORKDIR /usr/src/app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

#ETAPE 2
FROM amazoncorretto:17

WORKDIR /usr/src/app

COPY --from=build /usr/src/app/target/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]

# docker build -t tp-grapql-multistage .
# docker run -p 9090:8081 -d tp-grapql-multistage