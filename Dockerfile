# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY . .
RUN chmod +x mvnw \
	&& ./mvnw -B -DskipTests package \
	&& cp target/pet-adoption-system-0.0.1-SNAPSHOT.jar /app/app.jar

FROM eclipse-temurin:21-jre-jammy
RUN groupadd --system spring && useradd --system --gid spring spring
WORKDIR /app

COPY --from=build /app/app.jar /app/app.jar
RUN chown spring:spring /app/app.jar

USER spring:spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
