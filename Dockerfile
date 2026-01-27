# ETAPA 1: BUILD (compilar)}
#Creamos una iamgen osea un entorno aislado con las dependencias necesarias
FROM maven:3.9-eclipse-temurin-21 AS build
#creamos el directorio de trabajo
WORKDIR /app
#copiamos el pom.xml y src
COPY pom.xml .
COPY src ./src
#ejecuta maven, descarega dependecias y no ejecuta los test y crea un archivo .jar
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación
#ahora creamos una imagen mas ligera con linux y solo java JRE
FROM eclipse-temurin:21-jre
#definimos el /app como directorio de trbaajo si no existe lo crea
WORKDIR /app
#copia arcdhivos desde la llamada build, toma el jar generado por maven
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
#define el comando que se ejecuta al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
