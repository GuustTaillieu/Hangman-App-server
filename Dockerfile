FROM eclipse-temurin:17-alpine

# Install Maven
RUN apk --no-cache add maven

VOLUME /tmp
COPY target/*.jar app.jar

# Run Maven build
RUN mvn clean package -Pprod -DskipTests

ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080