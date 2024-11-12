# Usamos una imagen base de Maven con JDK 11 para compilar la aplicación
FROM maven:3.8.4-openjdk-11 AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos todo el proyecto a la imagen para construirlo
COPY . .

# Compilamos y empaquetamos todos los módulos
RUN mvn clean install -DskipTests

# Segunda etapa: imagen ligera para correr la aplicación
FROM amazoncorretto:11-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /PERSONAPP-HEXA-SPRING-BOOT

# Copiamos el archivo .jar específico del REST desde el builder
COPY --from=builder /app/rest-input-adapter/target/*.jar app.jar

# Exponer el puerto 8080 para el servicio REST
EXPOSE 3000

# Comando para ejecutar la aplicación REST
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=live"]
