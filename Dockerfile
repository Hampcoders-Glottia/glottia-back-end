# Etapa 1: Construcción
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Crea un usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
# Copia el JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar
# Cambia el propietario del archivo
RUN chown spring:spring app.jar
USER spring
# Expone el puerto (ajústalo a tu configuración)
EXPOSE 8091
# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]