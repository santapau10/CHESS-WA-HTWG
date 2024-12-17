# Usa la imagen base de sbtscala con OpenJDK y sbt
FROM sbtscala/scala-sbt:eclipse-temurin-alpine-21.0.5_11_1.10.6_3.6.2

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el código fuente del proyecto dentro del contenedor
COPY . /app

# Instala las dependencias de sbt (esto descargará las dependencias del proyecto)
RUN sbt update

# Genera el archivo .zip usando sbt dist
RUN sbt dist

# El contenedor generará el archivo .zip en target/universal/
# Exponemos el puerto que Play Framework usará
EXPOSE 9000

# CMD no es necesario si solo estás generando el zip, pero podrías usarlo para ejecutar la aplicación si es necesario.
