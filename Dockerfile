# -------------------------------------
# ETAPA 1: BUILD (Construcción del JAR)
# -------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
# Descarga todas las dependencias
RUN mvn dependency:go-offline -B


COPY src ./src
# Compila el proyecto y genera el JAR
# Usamos -B para modo batch (no interactivo).
RUN mvn clean package -DskipTests -B


# ------------------------------------------
# ETAPA 2: RUNTIME
# ------------------------------------------
FROM eclipse-temurin:21-jre-alpine
# El nombre final del JAR
ARG FINAL_JAR_NAME=TiendaAPI-0.0.1-SNAPSHOT.jar
WORKDIR /app

# Expone el puerto 8080
EXPOSE 8080

# Copia el JAR generado de la etapa 'build' a esta nueva imagen
COPY --from=build /app/target/${FINAL_JAR_NAME} app.jar

# Comando para ejecutar la aplicación.
ENTRYPOINT ["java","-jar","/app/app.jar"]